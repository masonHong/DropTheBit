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
import com.dropthebit.dropthebit.ui.detail.DetailActivity;
import com.dropthebit.dropthebit.viewmodel.CurrencyViewModel;

import java.text.NumberFormat;
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
    public void initView(View view) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        // 실시간 코인 시세 뷰 모델
        CurrencyViewModel currencyViewModel = ViewModelProviders.of(getActivity()).get(CurrencyViewModel.class);
        // 업데이트 될 때 마다 어뎁터에 적용
        currencyViewModel.getCurrencyList().observe(this, list -> adapter.setList(list));
    }

    class TotalViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_coin_name)
        TextView textCoinName;

        @BindView(R.id.text_current_price_number)
        TextView textCurrentPrice;

        private CurrencyType type;

        TotalViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            // 목록 클릭 시 자세히 보기 화면으로 이동
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
