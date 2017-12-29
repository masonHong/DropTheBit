package com.dropthebit.dropthebit.ui.main;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.dropthebit.dropthebit.R;
import com.dropthebit.dropthebit.base.TabFragment;
import com.dropthebit.dropthebit.common.Constants;
import com.dropthebit.dropthebit.ui.adapter.viewholder.CurrencyViewHolder;
import com.dropthebit.dropthebit.ui.adapter.MainCurrencyListAdapter;
import com.dropthebit.dropthebit.viewmodel.CurrencyViewModel;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by mason-hong on 2017. 12. 16..
 */
public class TotalTabFragment extends TabFragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private MainCurrencyListAdapter adapter;
    private CurrencyViewHolder.OnCurrencyClickListener onCurrencyClickListener;

    public static TotalTabFragment newInstance(String tabTitle) {
        TotalTabFragment fragment = new TotalTabFragment();
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
            this.onCurrencyClickListener = (CurrencyViewHolder.OnCurrencyClickListener) context;
        }
    }

    @Override
    public void initView(View view) {
        // 리사이클러뷰 설정
        adapter = new MainCurrencyListAdapter(getContext(), onCurrencyClickListener);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        // 실시간 코인 시세 뷰 모델
        CurrencyViewModel currencyViewModel = ViewModelProviders.of(getActivity()).get(CurrencyViewModel.class);
        // 업데이트 될 때 마다 어뎁터에 적용
        currencyViewModel.getCurrencyList().observe(this, map -> {
            if (map != null) {
                adapter.setList(new ArrayList<>(map.values()));
            }
        });
    }
}
