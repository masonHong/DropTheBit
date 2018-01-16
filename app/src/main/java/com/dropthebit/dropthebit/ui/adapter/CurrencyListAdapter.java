package com.dropthebit.dropthebit.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.dropthebit.dropthebit.R;
import com.dropthebit.dropthebit.model.CurrencyData;
import com.dropthebit.dropthebit.ui.adapter.viewholder.CurrencyViewHolder;
import com.dropthebit.dropthebit.util.CurrencyUtils;

import java.util.List;

/**
 * Created by mason-hong on 2017. 12. 29..
 */
public class CurrencyListAdapter extends RecyclerView.Adapter<CurrencyViewHolder> {
    public static final int TYPE_TOTAL = 0;
    public static final int TYPE_INTEREST = 1;

    private Context context;
    private List<CurrencyData> list = null;
    private CurrencyViewHolder.OnCurrencyClickListener onCurrencyClickListener;

    private int type = TYPE_TOTAL;

    public CurrencyListAdapter(Context context, CurrencyViewHolder.OnCurrencyClickListener onCurrencyClickListener) {
        this(context, onCurrencyClickListener, TYPE_TOTAL);
    }

    public CurrencyListAdapter(Context context, CurrencyViewHolder.OnCurrencyClickListener onCurrencyClickListener, int type) {
        this.context = context;
        this.onCurrencyClickListener = onCurrencyClickListener;
        this.type = type;
    }

    @Override
    public CurrencyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CurrencyViewHolder(
                LayoutInflater.from(context).inflate(
                        type == TYPE_TOTAL
                                ? R.layout.viewholder_total_item
                                : R.layout.viewholder_interest_item
                        , parent
                        , false),
                onCurrencyClickListener);
    }

    @Override
    public void onBindViewHolder(CurrencyViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    public void setList(List<CurrencyData> newList) {
        if (list == null) {
            list = newList;
            notifyDataSetChanged();
        } else {
            for (CurrencyData data : newList) {
                if (list.contains(data)) {
                    int index = list.indexOf(data);
                    long oldPrice = CurrencyUtils.getSafetyPrice(list.get(index));
                    long newPrice = CurrencyUtils.getSafetyPrice(data);
                    if (oldPrice != newPrice) {
                        list.get(index).setPrice(Long.toString(newPrice));
                        notifyItemChanged(index);
                    }
                } else {
                    list.add(data);
                    notifyItemInserted(list.size() - 1);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }
}
