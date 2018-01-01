package com.dropthebit.dropthebit.ui.adapter.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.dropthebit.dropthebit.R;
import com.dropthebit.dropthebit.model.CurrencyData;
import com.dropthebit.dropthebit.model.CurrencyType;
import com.dropthebit.dropthebit.provider.room.RoomProvider;
import com.dropthebit.dropthebit.provider.room.WalletDao;
import com.dropthebit.dropthebit.util.CurrencyUtils;

import java.text.NumberFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by mason-hong on 2017. 12. 29..
 */
public class CurrencyViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.text_coin_name)
    TextView textCoinName;

    @BindView(R.id.text_current_price_number)
    TextView textCurrentPrice;

    @BindView(R.id.text_hold)
    TextView textHold;

    @BindView(R.id.text_predict)
    TextView textPredict;

    private Context context;
    private OnCurrencyClickListener onCurrencyClickListener;
    private WalletDao walletDao;

    public interface OnCurrencyClickListener {
        void onCurrencyClick(CurrencyType type);
    }

    private CurrencyType type;

    public CurrencyViewHolder(View itemView, OnCurrencyClickListener onCurrencyClickListener) {
        super(itemView);
        context = itemView.getContext();
        this.onCurrencyClickListener = onCurrencyClickListener;

        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(v -> {
            if (this.onCurrencyClickListener != null) {
                this.onCurrencyClickListener.onCurrencyClick(type);
            }
        });
        walletDao = RoomProvider.getInstance(itemView.getContext()).getDatabase().walletDao();
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

        walletDao.loadWallet(type.key)
                .subscribeOn(Schedulers.io())
                .map(wallet -> wallet.amount)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(amount -> {
                            textHold.setText(context.getString(R.string.hold_amount_text, amount, type.key));
                            textPredict.setText(context.getString(R.string.hold_krw_text_with_bracket, (long) (amount * CurrencyUtils.getSafetyPrice(data))));
                        }
                        , Throwable::printStackTrace, () -> {
                            textHold.setText(context.getString(R.string.hold_amount_text, 0F, type.key));
                            textPredict.setText(context.getString(R.string.hold_krw_text_with_bracket, 0));
                        });
    }
}
