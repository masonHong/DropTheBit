package com.dropthebit.dropthebit.ui.detail;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dropthebit.dropthebit.R;
import com.dropthebit.dropthebit.api.DTBProvider;
import com.dropthebit.dropthebit.common.Constants;
import com.dropthebit.dropthebit.dto.DTBCoinDTO;
import com.dropthebit.dropthebit.dto.DTBHistoryDTO;
import com.dropthebit.dropthebit.model.CurrencyType;
import com.dropthebit.dropthebit.room.PriceHistory;
import com.dropthebit.dropthebit.room.PriceHistoryDao;
import com.dropthebit.dropthebit.room.RoomProvider;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
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
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        // 자세히 보여줄 타입
        type = (CurrencyType) getIntent().getSerializableExtra(Constants.ARGUMENT_TYPE);

        // SQLite 접근 객체
        priceHistoryDao = RoomProvider.getInstance(this)
                .getDatabase()
                .priceHistoryDao();

        updateLocalDbAsTime();

    }



    /**
     * 목록 업데이트
     * @param list 업데이트할 데이터
     */
    private void updateLocalDatabase(List<DTBHistoryDTO> list) {
        for (DTBHistoryDTO dto : list) {
            priceHistoryDao.insertPriceHistories(new PriceHistory(dto.getName(), dto.getTime(), dto.getPrice()));
        }
    }

    private void updateLocalDbAsTime()
    {
        Disposable disposable = Observable.create((ObservableOnSubscribe<Long>) e ->
        {
            PriceHistory item =priceHistoryDao.loadRecentHistory(type.key);
            if(item == null)
                e.onNext(0L);
            else
                e.onNext(item.time);
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(time ->
                {
                    if(time!=0)
                        time+=1; // 현재 시간 이후로 요청해야 하므로 1 더해준다.
                    DTBProvider.getInstance()
                            .getHistory(type, time)
                            .subscribeOn(Schedulers.io())
                            .map(DTBCoinDTO::getData)
                            .subscribe(list -> {
                                        updateLocalDatabase(list);
                                        updateChart();
                                    },
                                    throwable -> {
                                        throwable.printStackTrace();
                                        finish();
                                    });
                });

        compositeDisposable.add(disposable);
    }

    private void updateChart() {
        // 로컬에 있는 데이터 기반으로 차트 업데이트 요청
        Disposable disposable = Observable.create((ObservableOnSubscribe<PriceHistory[]>) e -> e.onNext(priceHistoryDao.loadAllHistories(type.key)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                    if (list.length > 0) {
                        historyList = list;
                        // 1분
                        final int minute = 1000 * 60;
                        List<Entry> entries = new ArrayList<>();
                        for (PriceHistory history : historyList) {
                            // 10분 단위로 x 좌표 찍기, y 좌표는 price
                            entries.add(new Entry(history.time / (10 * minute), history.price));
                        }
                        LineDataSet dataSet = new LineDataSet(entries, type.key);
                        LineData lineData = new LineData(dataSet);
                        lineData.setValueFormatter((value, entry, dataSetIndex, viewPortHandler) -> "");
                        lineChart.setData(lineData);
                        lineChart.setVisibleXRangeMaximum(50);
                        lineChart.invalidate();
                    }
                }, Throwable::printStackTrace);
        compositeDisposable.add(disposable);
    }

    @Override
    protected void onStop() {
        super.onStop();
        compositeDisposable.dispose();
    }
}
