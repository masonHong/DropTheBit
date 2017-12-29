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
import com.dropthebit.dropthebit.ui.detail.DetailActivity;
import com.dropthebit.dropthebit.ui.viewholder.CurrencyViewHolder;
import com.dropthebit.dropthebit.viewmodel.CurrencyViewModel;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mason-hong on 2017. 12. 16..
 */
public class TotalTabFragment extends TabFragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private TotalListAdapter adapter = new TotalListAdapter();
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

    private class TotalListAdapter extends RecyclerView.Adapter<CurrencyViewHolder> {
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

        void setList(List<CurrencyData> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return list == null ? 0 : list.size();
        }
    }
}
