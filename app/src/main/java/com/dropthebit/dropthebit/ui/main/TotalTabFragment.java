package com.dropthebit.dropthebit.ui.main;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dropthebit.dropthebit.R;
import com.dropthebit.dropthebit.api.BithumbProvider;
import com.dropthebit.dropthebit.base.BaseFragment;
import com.dropthebit.dropthebit.dto.BithumbCurrencyDTO;
import com.dropthebit.dropthebit.model.CurrencyData;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by mason-hong on 2017. 12. 16..
 */
public class TotalTabFragment extends BaseFragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private TotalListAdapter adapter = new TotalListAdapter();
    private int interval = 2000;
    private Disposable disposableTotal = null;

    public static TotalTabFragment newInstance() {
        TotalTabFragment fragment = new TotalTabFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_tab;
    }

    @Override
    public void initView(View view) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        interval = 3000;
        // 프래그먼트가 종료되었을 때 메모리 해제를 위해 저장하며
        // interval을 사용해서 주기적으로 호출 할 수 있도록 한다
        disposableTotal = Observable.interval(0, interval, TimeUnit.MILLISECONDS)
                .flatMap(aLong -> {
                    // 호출할 데이터는 Bithumb API
                    return BithumbProvider.getInstance().getAllPrices();
                })
                .map(dto -> {
                    List<CurrencyData> ret = new ArrayList<>();
                    BithumbCurrencyDTO target = dto.getData().getBTC();
                    ret.add(new CurrencyData("비트코인", target.getClosing_price(), target.getMax_price(), target.getMin_price()));
                    target = dto.getData().getBCH();
                    ret.add(new CurrencyData("비트코인캐시", target.getClosing_price(), target.getMax_price(), target.getMin_price()));
                    target = dto.getData().getBTG();
                    ret.add(new CurrencyData("비트코인골드", target.getClosing_price(), target.getMax_price(), target.getMin_price()));
                    target = dto.getData().getETH();
                    ret.add(new CurrencyData("이더리움", target.getClosing_price(), target.getMax_price(), target.getMin_price()));
                    target = dto.getData().getETC();
                    ret.add(new CurrencyData("이더리움클래식", target.getClosing_price(), target.getMax_price(), target.getMin_price()));
                    target = dto.getData().getXRP();
                    ret.add(new CurrencyData("리플", target.getClosing_price(), target.getMax_price(), target.getMin_price()));
                    target = dto.getData().getQTUM();
                    ret.add(new CurrencyData("퀀텀", target.getClosing_price(), target.getMax_price(), target.getMin_price()));
                    target = dto.getData().getLTC();
                    ret.add(new CurrencyData("라이트코인", target.getClosing_price(), target.getMax_price(), target.getMin_price()));
                    target = dto.getData().getDASH();
                    ret.add(new CurrencyData("대쉬", target.getClosing_price(), target.getMax_price(), target.getMin_price()));
                    return ret;
                })
                // io Scheduler에서 관리
                .subscribeOn(Schedulers.io())
                // 결과는 mainThread에서
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> adapter.setList(list));
    }

    @Override
    public void onStop() {
        super.onStop();
        if (disposableTotal != null) {
            disposableTotal.dispose();
        }
    }

    class TotalViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_coin_name)
        TextView textCoinName;

        @BindView(R.id.text_current_price_number)
        TextView textCurrentPrice;

        TotalViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(CurrencyData data) {
            textCoinName.setText(data.getName());
            String price = data.getPrice();
            if (price.contains(".")) {
                price = price.substring(0, price.indexOf("."));
            }
            price = NumberFormat.getInstance().format(Long.parseLong(price));
            textCurrentPrice.setText(price);
        }
    }

    private class TotalListAdapter extends RecyclerView.Adapter<TotalViewHolder> {
        private List<CurrencyData> list = null;

        @Override
        public TotalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new TotalViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.viewholder_total_item, parent, false));
        }

        @Override
        public void onBindViewHolder(TotalViewHolder holder, int position) {
            holder.bind(list.get(position));
        }

        public void setList(List<CurrencyData> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return list == null ? 0 : list.size();
        }
    }
}
