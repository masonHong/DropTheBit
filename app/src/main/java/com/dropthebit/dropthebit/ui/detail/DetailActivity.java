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
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.chart)
    LineChart lineChart;

    private CurrencyType type;
    private PriceHistory[] historyList;
    private PriceHistoryDao priceHistoryDao;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        type = (CurrencyType) getIntent().getSerializableExtra(Constants.ARGUMENT_TYPE);
        priceHistoryDao = RoomProvider.getInstance(this)
                .getDatabase()
                .priceHistoryDao();

        lineChart.setMaxVisibleValueCount(100);

        Disposable disposable = DTBProvider.getInstance()
                .getHistory(type, 0)
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
        compositeDisposable.add(disposable);
    }

    private void updateLocalDatabase(List<DTBHistoryDTO> list) {
        for (DTBHistoryDTO dto : list) {
            priceHistoryDao.insertPriceHistories(new PriceHistory(dto.getName(), dto.getTime(), dto.getPrice()));
        }
    }

    private void updateChart() {
        Disposable disposable = Observable.create((ObservableOnSubscribe<PriceHistory[]>) e -> e.onNext(priceHistoryDao.loadAllHistories(type.key)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                    if (list.length > 0) {
                        historyList = list;
                        final int minute = 1000 * 60;
                        List<Entry> entries = new ArrayList<>();
                        for (PriceHistory history : historyList) {
                            entries.add(new Entry(history.time / (10 * minute), history.price));
                        }
                        LineDataSet dataSet = new LineDataSet(entries, type.key);
                        LineData lineData = new LineData(dataSet);
                        lineChart.setData(lineData);
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
