package com.dropthebit.dropthebit.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.dropthebit.dropthebit.R;
import com.dropthebit.dropthebit.base.BaseViewHolder;
import com.dropthebit.dropthebit.base.HasViewType;
import com.dropthebit.dropthebit.model.CurrencyData;
import com.dropthebit.dropthebit.ui.adapter.viewholder.ChartViewHolder;
import com.dropthebit.dropthebit.ui.adapter.viewholder.CurrencyViewHolder;
import com.dropthebit.dropthebit.ui.adapter.viewholder.HistoryViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DetailRecyclerAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    public static final int TYPE_COIN = 0;
    public static final int TYPE_CHART = 1;
    public static final int TYPE_HISTORY = 2;

    private ArrayList<HasViewType> list;

    public DetailRecyclerAdapter() {
        list = new ArrayList<>();
        list.add(new HasViewType(TYPE_COIN));
        list.add(new HasViewType(TYPE_CHART));
        list.add(new HasViewType(TYPE_HISTORY));
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        switch (viewType) {
            case TYPE_COIN:
                return new CurrencyViewHolder(LayoutInflater.from(context).inflate(R.layout.viewholder_interest_item, parent, false), null);
            case TYPE_CHART:
                return new ChartViewHolder(LayoutInflater.from(context).inflate(R.layout.viewholder_chart, parent, false));
            case TYPE_HISTORY:
                return new HistoryViewHolder(LayoutInflater.from(context).inflate(R.layout.viewholder_history, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getViewType();
    }

    public void setCoinData(CurrencyData data) {
        data.setViewType(TYPE_COIN);
        list.remove(0);
        list.add(0, data);
        notifyItemChanged(0);
    }

    public void setChartData(HasViewType data) {
        data.setViewType(TYPE_CHART);
        list.remove(1);
        list.add(1, data);
        notifyItemChanged(1);
    }

    public void setHistoryData(HasViewType data) {
        data.setViewType(TYPE_HISTORY);
        list.remove(2);
        list.add(2, data);
        notifyItemChanged(2);
    }
}
