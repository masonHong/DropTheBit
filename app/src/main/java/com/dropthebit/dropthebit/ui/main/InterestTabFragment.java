package com.dropthebit.dropthebit.ui.main;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dropthebit.dropthebit.R;
import com.dropthebit.dropthebit.base.TabFragment;
import com.dropthebit.dropthebit.common.Constants;
import com.dropthebit.dropthebit.model.CurrencyData;
import com.dropthebit.dropthebit.model.CurrencyType;
import com.dropthebit.dropthebit.room.InterestCoinDao;
import com.dropthebit.dropthebit.room.RoomProvider;
import com.dropthebit.dropthebit.ui.detail.DetailActivity;
import com.dropthebit.dropthebit.viewmodel.CurrencyViewModel;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by mason-hong on 2017. 12. 16..
 */
public class InterestTabFragment extends TabFragment {

    private InterestCoinDao interestCoinDao;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private Disposable disposable;

    private InterestListAdapter adapter = new InterestListAdapter();
    private List<String> interestCoins = Collections.emptyList();

    public static InterestTabFragment newInstance(String tabTitle) {
        InterestTabFragment fragment = new InterestTabFragment();
        Bundle args = new Bundle();
        args.putString(Constants.ARGUMENT_TAB_TITLE, tabTitle);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_tab;
    }

    @Override
    public String getTabTitle() {
        return getArguments().getString(Constants.ARGUMENT_TAB_TITLE);
    }


    @Override
    public void initView(View view) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        interestCoinDao = RoomProvider.getInstance(getContext()).getDatabase().intersetCoinDao();

        disposable = interestCoinDao.loadAllInterestCoins()
                .subscribeOn(Schedulers.io())
                .flatMap(Flowable::fromArray)
                .map(coin -> coin.name)
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> this.interestCoins = list);

        // 실시간 코인 시세 뷰 모델
        CurrencyViewModel currencyViewModel = ViewModelProviders.of(getActivity()).get(CurrencyViewModel.class);
        // 업데이트 될 때 마다 어뎁터에 적용 후 관심코인만 리스트에 넣어서 set
        currencyViewModel.getCurrencyList().observe(this, list ->
                adapter.setList(filterUnInterest(list)));

        /* DB에 넣는거 테스트
        Observable.just(0)
                .observeOn(Schedulers.io())
                .subscribe(integer ->
                {
                   interestCoinDao.insertIntersetCoin(new InterestCoin("BitCoin"));
                   interestCoinDao.insertIntersetCoin(new InterestCoin("BitCoinCache"));
                });
                */
    }

    // 디비에 있는 관심목록이랑 일치하는 것만 리스트에 넣음.
    public List<CurrencyData> filterUnInterest(List<CurrencyData> list) {
        List<CurrencyData> result = new ArrayList<>();
        for (CurrencyData data : list) {
            if (interestCoins.contains(data.getName())) {
                result.add(data);
            }
        }
        return result;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (disposable != null) {
            disposable.dispose();
        }
    }

    class InterestListAdapter extends RecyclerView.Adapter<InterestListAdapter.InterestViewHolder> {

        private List<CurrencyData> list = null;

        @Override
        public InterestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new InterestViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.viewholder_total_item, parent, false));
        }

        @Override
        public void onBindViewHolder(InterestViewHolder holder, int position) {
            holder.bind(list.get(position));
        }

        @Override
        public int getItemCount() {
            return list == null ? 0 : list.size();
        }

        void setList(List<CurrencyData> list) {
            this.list = list;
            notifyDataSetChanged();
        }


        class InterestViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.text_coin_name)
            TextView textCoinName;

            @BindView(R.id.text_current_price_number)
            TextView textCurrentPrice;

            private CurrencyType type;

            public InterestViewHolder(View itemView) {
                super(itemView);

                ButterKnife.bind(this, itemView);
                itemView.setOnClickListener(v -> {
                    Intent intent = new Intent(getContext(), DetailActivity.class);
                    intent.putExtra(Constants.ARGUMENT_TYPE, type);
                    startActivity(intent);
                });
            }

            void bind(CurrencyData data) {
                this.type = data.getType();
                textCoinName.setText(data.getName());
                String price = data.getPrice();
                if (price.contains(".")) {
                    price = price.substring(0, price.indexOf("."));
                }
                // 숫자에 콤마 붙이기
                price = NumberFormat.getInstance().format(Long.parseLong(price));
                textCurrentPrice.setText(price);
            }
        }
    }
}
