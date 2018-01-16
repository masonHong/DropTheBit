package com.dropthebit.dropthebit.ui.adapter.viewholder;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dropthebit.dropthebit.R;
import com.dropthebit.dropthebit.common.Constants;
import com.dropthebit.dropthebit.model.CurrencyData;
import com.dropthebit.dropthebit.model.CurrencyType;
import com.dropthebit.dropthebit.provider.room.RoomProvider;
import com.dropthebit.dropthebit.provider.room.WalletDao;
import com.dropthebit.dropthebit.util.AndroidUtils;
import com.dropthebit.dropthebit.util.CurrencyUtils;
import com.dropthebit.dropthebit.util.RxUtils;
import com.dropthebit.dropthebit.util.StringUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by mason-hong on 2017. 12. 29..
 */
public class CurrencyViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.image_coin)
    ImageView imageCoin;

    @BindView(R.id.text_coin_name)
    TextView textCoinName;

    @BindView(R.id.text_current_price_number)
    TextView textCurrentPrice;

    @BindView(R.id.text_hold)
    TextView textHold;

    @BindView(R.id.text_predict)
    TextView textPredict;

    @BindView(R.id.text_diff)
    TextView textDiff;

    private Context context;
    private OnCurrencyClickListener onCurrencyClickListener;
    private WalletDao walletDao;

    public interface OnCurrencyClickListener {
        void onCurrencyClick(CurrencyType type);

        void onCurrencyLongClick(CurrencyType type);
    }

    private CurrencyType type;

    public CurrencyViewHolder(View itemView, OnCurrencyClickListener onCurrencyClickListener) {
        super(itemView);
        context = itemView.getContext();
        this.onCurrencyClickListener = onCurrencyClickListener;

        ButterKnife.bind(this, itemView);
        RxUtils.clickOne(itemView, 1000, v -> {
            if (this.onCurrencyClickListener != null) {
                this.onCurrencyClickListener.onCurrencyClick(type);
            }
        });
        itemView.setOnLongClickListener(v -> {
            if (this.onCurrencyClickListener != null) {
                this.onCurrencyClickListener.onCurrencyLongClick(type);
            }
            return false;
        });
        walletDao = RoomProvider.getInstance(itemView.getContext()).getDatabase().walletDao();
    }

    public void bind(CurrencyData data) {
        this.type = data.getType();
        Picasso.with(context)
                .load(CurrencyUtils.getCoinImage(type))
                .into(imageCoin);
        textCoinName.setText(data.getName());
        textCurrentPrice.setText(StringUtils.getPriceString(CurrencyUtils.getSafetyPrice(data), ""));
        String diff = CurrencyUtils.getDiffString(data);
        textDiff.setText(diff);
        if (diff.startsWith("+")) {
            textDiff.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.app_red));
        } else {
            textDiff.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.app_blue));
        }

        walletDao.loadWallet(type.key)
                .subscribeOn(Schedulers.io())
                .map(wallet -> wallet.amount)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(amount -> {
                            textHold.setText(context.getString(R.string.hold_amount_text, amount, type.key));
                            textPredict.setText(StringUtils.getPriceString(amount * CurrencyUtils.getSafetyPrice(data), Constants.UNIT_WON));
                        }
                        , Throwable::printStackTrace, () -> {
                            textHold.setText(context.getString(R.string.hold_amount_text, 0F, type.key));
                            textPredict.setText(StringUtils.getPriceString(0, Constants.UNIT_WON));
                        });
    }
}
