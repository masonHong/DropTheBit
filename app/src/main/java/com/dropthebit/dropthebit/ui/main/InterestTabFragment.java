package com.dropthebit.dropthebit.ui.main;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
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
import com.dropthebit.dropthebit.provider.room.InterestCoinDao;
import com.dropthebit.dropthebit.provider.room.RoomProvider;
import com.dropthebit.dropthebit.ui.detail.DetailActivity;
import com.dropthebit.dropthebit.ui.viewholder.CurrencyViewHolder;
import com.dropthebit.dropthebit.viewmodel.CurrencyViewModel;

import java.text.NumberFormat;
import java.util.ArrayList;
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
    private List<String> interestCoins = new ArrayList<>();
    private CurrencyViewHolder.OnCurrencyClickListener onCurrencyClickListener;

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
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CurrencyViewHolder.OnCurrencyClickListener) {
            onCurrencyClickListener = (CurrencyViewHolder.OnCurrencyClickListener) context;
        }
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
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(name -> interestCoins.add(name), Throwable::printStackTrace);

        // 실시간 코인 시세 뷰 모델
        CurrencyViewModel currencyViewModel = ViewModelProviders.of(getActivity()).get(CurrencyViewModel.class);
        // 업데이트 될 때 마다 어뎁터에 적용 후 관심코인만 리스트에 넣어서 set
        currencyViewModel.getCurrencyList().observe(this, map -> {
            List<CurrencyData> list = new ArrayList<>();
            for (String name : interestCoins) {
                if (map != null && map.containsKey(name)) {
                    list.add(map.get(name));
                }
            }
            adapter.setList(list);
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        if (disposable != null) {
            disposable.dispose();
        }
    }

    class InterestListAdapter extends RecyclerView.Adapter<CurrencyViewHolder> {

        private List<CurrencyData> list = null;

        @Override
        public CurrencyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CurrencyViewHolder(
                    LayoutInflater.from(getContext()).inflate(R.layout.viewholder_total_item, parent, false),
                    onCurrencyClickListener);
        }

        @Override
        public void onBindViewHolder(CurrencyViewHolder holder, int position) {
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
    }
}
