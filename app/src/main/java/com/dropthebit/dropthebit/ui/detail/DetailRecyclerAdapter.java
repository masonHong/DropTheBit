package com.dropthebit.dropthebit.ui.detail;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.dropthebit.dropthebit.base.BaseViewHolder;
import com.dropthebit.dropthebit.base.HasViewType;

import java.util.Collections;
import java.util.List;

public class DetailRecyclerAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private List<HasViewType> list = Collections.emptyList();

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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

    public void setList(List<HasViewType> list) {
        this.list = list;
        notifyDataSetChanged();
    }
}
