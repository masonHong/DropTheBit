package com.dropthebit.dropthebit.ui.transaction;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.dropthebit.dropthebit.R;
import com.dropthebit.dropthebit.common.Constants;
import com.dropthebit.dropthebit.model.CurrencyType;
import com.dropthebit.dropthebit.viewmodel.CurrencyViewModel;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.NumberFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mason-hong on 2017. 12. 16..
 */
public class TransactionDialog extends DialogFragment {
    public static final int TYPE_BUY = 0;
    public static final int TYPE_SELL = 1;

    // 원하는 값만 사용하도록 경고해주는 용도
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TYPE_BUY, TYPE_SELL})
    public @interface TransactionType {}

    @BindView(R.id.text_title)
    TextView textTitle;

    @BindView(R.id.text_name)
    TextView textName;

    @BindView(R.id.text_symbol)
    TextView textSymbol;

    @BindView(R.id.text_price)
    TextView textPrice;

    @BindView(R.id.text_amount)
    TextView textAmount;

    @TransactionType
    private int transactionType;

    private CurrencyType currencyType;
    private CurrencyViewModel currencyViewModel;

    public static TransactionDialog newInstance(@TransactionType int transactionType, CurrencyType currencyType) {
        TransactionDialog dialog = new TransactionDialog();
        Bundle args = new Bundle();
        args.putInt(Constants.ARGUMENT_TYPE, transactionType);
        args.putSerializable(Constants.ARGUMENT_CURRENCY_TYPE, currencyType);
        dialog.setArguments(args);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_transaction, null);
        ButterKnife.bind(this, view);
        initView();
        return new AlertDialog.Builder(getContext())
                .setView(view)
                .create();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog().getWindow() != null) {
            WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            getDialog().getWindow().setGravity(Gravity.BOTTOM);
        }
    }

    private void initView() {
        transactionType = getArguments().getInt(Constants.ARGUMENT_TYPE, 0);
        currencyType = (CurrencyType) getArguments().getSerializable(Constants.ARGUMENT_CURRENCY_TYPE);
        String[] names = getResources().getStringArray(R.array.coinNames);
        textName.setText(names[currencyType.ordinal()]);
        textSymbol.setText(currencyType.key);
        currencyViewModel = ViewModelProviders.of(getActivity()).get(CurrencyViewModel.class);
        currencyViewModel.getCurrencyList()
                .observe(this, map -> {
                    if (map.containsKey(currencyType.key)) {
                        NumberFormat numberFormat = NumberFormat.getNumberInstance();
                        textPrice.setText(numberFormat.format(Long.parseLong(map.get(currencyType.key).getPrice())));
                    }
                });
        switch (transactionType) {
            case TYPE_BUY:
                textTitle.setText(R.string.buying);
                textAmount.setText(R.string.amount_of_buying);
                break;
            case TYPE_SELL:
                textTitle.setText(R.string.selling);
                textAmount.setText(R.string.amount_of_selling);
                break;
        }
    }
}