package com.dropthebit.dropthebit.ui.main;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.dropthebit.dropthebit.R;
import com.dropthebit.dropthebit.base.BaseFragment;
import com.dropthebit.dropthebit.common.Constants;
import com.dropthebit.dropthebit.model.CurrencyData;
import com.dropthebit.dropthebit.model.CurrencyType;
import com.dropthebit.dropthebit.provider.room.InterestCoin;
import com.dropthebit.dropthebit.ui.adapter.MarginItemDecoration;
import com.dropthebit.dropthebit.ui.adapter.viewholder.CurrencyViewHolder;
import com.dropthebit.dropthebit.ui.adapter.MainCurrencyListAdapter;
import com.dropthebit.dropthebit.util.CurrencyUtils;
import com.dropthebit.dropthebit.viewmodel.CurrencyViewModel;
import com.dropthebit.dropthebit.viewmodel.InterestViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindDimen;
import butterknife.BindView;

/**
 * Created by mason-hong on 2017. 12. 16..
 */
public class InterestFragment extends BaseFragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindDimen(R.dimen.margin_coin_viewholder)
    int margin;

    private MainCurrencyListAdapter adapter;
    private List<CurrencyType> interestCoins = new ArrayList<>();
    private CurrencyViewHolder.OnCurrencyClickListener onCurrencyClickListener;

    public static InterestFragment newInstance() {
        InterestFragment fragment = new InterestFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_interest_tab;
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
        adapter = new MainCurrencyListAdapter(getContext(), onCurrencyClickListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new MarginItemDecoration(margin));

        // 실시간 코인 시세 뷰 모델
        CurrencyViewModel currencyViewModel = ViewModelProviders.of(getActivity()).get(CurrencyViewModel.class);
        // 업데이트 될 때 마다 어뎁터에 적용 후 관심코인만 리스트에 넣어서 set
        currencyViewModel.getCurrencyList().observe(this, map -> {
            List<CurrencyData> list = new ArrayList<>();
            for (CurrencyType currencyType : interestCoins) {
                if (map != null && map.containsKey(currencyType)) {
                    list.add(map.get(currencyType));
                }
            }
            adapter.setList(list);
        });

        InterestViewModel interestViewModel = ViewModelProviders.of(getActivity()).get(InterestViewModel.class);
        interestViewModel.getInterestCoins().observe(this, list -> {
            interestCoins.clear();
            if (list != null) {
                for (InterestCoin interestCoin : list) {
                    CurrencyType type = CurrencyUtils.findByName(interestCoin.name);
                    if (type != null) {
                        interestCoins.add(type);
                    }
                }
            }
        });
    }
}
