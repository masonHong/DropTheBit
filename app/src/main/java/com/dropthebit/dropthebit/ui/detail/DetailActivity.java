package com.dropthebit.dropthebit.ui.detail;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dropthebit.dropthebit.R;
import com.dropthebit.dropthebit.api.DTBProvider;
import com.dropthebit.dropthebit.common.Constants;
import com.dropthebit.dropthebit.dto.DTBCoinDTO;
import com.dropthebit.dropthebit.dto.DTBHistoryDTO;
import com.dropthebit.dropthebit.model.CurrencyType;
import com.dropthebit.dropthebit.provider.room.PriceHistory;
import com.dropthebit.dropthebit.provider.room.PriceHistoryDao;
import com.dropthebit.dropthebit.provider.room.RoomProvider;
import com.dropthebit.dropthebit.ui.transaction.TransactionDialog;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.chart)
    LineChart lineChart;

    // 코인 타입
    private CurrencyType type;
    // 차트 데이터
    private PriceHistory[] historyList;
    // 로컬 데이터 접근 권한
    private PriceHistoryDao priceHistoryDao;
    // Observable 취소 용도
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        // 뷰 바인딩
        ButterKnife.bind(this);

        // 자세히 보여줄 타입
        type = (CurrencyType) getIntent().getSerializableExtra(Constants.ARGUMENT_TYPE);

        // SQLite 접근 객체
        priceHistoryDao = RoomProvider.getInstance(this)
                .getDatabase()
                .priceHistoryDao();
        // 서버로부터 데이터 최신화
        updateLocalFromRemote();
    }

    @Override
    protected void onStop() {
        super.onStop();
        compositeDisposable.dispose();
    }

    @OnClick(R.id.button_buy)
    void onClickBuy() {
        TransactionDialog dialog = TransactionDialog.newInstance(TransactionDialog.TYPE_BUY);
        dialog.show(getSupportFragmentManager(), Constants.TAG_TRANSACTION);
    }

    @OnClick(R.id.button_sell)
    void onClickSell() {
        TransactionDialog dialog = TransactionDialog.newInstance(TransactionDialog.TYPE_SELL);
        dialog.show(getSupportFragmentManager(), Constants.TAG_TRANSACTION);
    }

    /**
     * 서버로부터 데이터를 최신화 하는 함수
     */
    private void updateLocalFromRemote() {
        Disposable disposable =
                // 가장 최근 데이터 로드
                priceHistoryDao.loadRecentHistory(type.key)
                // 없을 경우 시간 0, 있을 경우 해당 시간 + 1 (해당 시간 이후를 검색하기 위해
                .map(priceHistory ->
                        priceHistory == null ? 0L : priceHistory.time + 1)
                // 불러온 시간으로 서버에 데이터 요청
                .flatMap(time ->
                        DTBProvider.getInstance().getHistory(type, time))
                // data만 필요함
                .map(DTBCoinDTO::getData)
                // 시작 스케줄러 io
                .subscribeOn(Schedulers.io())
                .subscribe(list ->
                {
                    // 불러온 목록으로 데이터베이스 업데이트
                    updateLocalDatabase(list);
                    // 차트 업데이트
                    updateChart();

                }, throwable -> {
                    throwable.printStackTrace();
                    finish();
                });

        compositeDisposable.add(disposable);
    }

    /**
     * 데이터베이스 목록 업데이트
     * @param list 업데이트할 데이터
     */
    private void updateLocalDatabase(List<DTBHistoryDTO> list) {
        for (DTBHistoryDTO dto : list) {
            priceHistoryDao.insertPriceHistories(new PriceHistory(dto.getName(), dto.getTime(), dto.getPrice()));
        }
    }

    /**
     * 차트에 데이터를 보여주는 함수
     */
    private void updateChart() {
        // 로컬에 있는 데이터 기반으로 차트 업데이트 요청
        Disposable disposable = priceHistoryDao.loadAllHistories(type.key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                    if (list.length > 0) {
                        historyList = list;
                        final int minute = 1000 * 60;   // 1분
                        List<Entry> entries = new ArrayList<>();
                        for (PriceHistory history : historyList) {
                            // 10분 단위로 x 좌표 찍기, y 좌표는 price
                            entries.add(new Entry(history.time / (10 * minute), history.price));
                        }
                        // 데이터 설정
                        LineDataSet dataSet = new LineDataSet(entries, type.key);
                        LineData lineData = new LineData(dataSet);
                        // 데이터 표시 제거
                        lineData.setValueFormatter((value, entry, dataSetIndex, viewPortHandler) -> "");
                        lineChart.setData(lineData);
                        // 보이는 데이터 최대 50개
                        lineChart.setVisibleXRangeMaximum(50);
                        // 차트 맨 끝 으로 이동
                        lineChart.moveViewToX(entries.get(entries.size() - 1).getX());
                        // 그리기
                        lineChart.invalidate();
                    }
                }, Throwable::printStackTrace);
        compositeDisposable.add(disposable);
    }

}
