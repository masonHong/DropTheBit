package com.dropthebit.dropthebit.ui.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.dropthebit.dropthebit.R;
import com.dropthebit.dropthebit.model.CurrencyData;
import com.dropthebit.dropthebit.model.CurrencyType;

import java.text.NumberFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mason-hong on 2017. 12. 29..
 */
public class CurrencyViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.text_coin_name)
    TextView textCoinName;

    @BindView(R.id.text_current_price_number)
    TextView textCurrentPrice;

    private OnCurrencyClickListener onCurrencyClickListener;

    public interface OnCurrencyClickListener {
        void onCurrencyClick(CurrencyType type);
    }

    private CurrencyType type;

    public CurrencyViewHolder(View itemView, OnCurrencyClickListener onCurrencyClickListener) {
        super(itemView);
        this.onCurrencyClickListener = onCurrencyClickListener;

        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(v -> {
            if (this.onCurrencyClickListener != null) {
                this.onCurrencyClickListener.onCurrencyClick(type);
            }
        });
    }

    public void bind(CurrencyData data) {
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
