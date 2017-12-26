package com.dropthebit.dropthebit.ui.transaction;

import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.dropthebit.dropthebit.R;
import com.dropthebit.dropthebit.common.Constants;
import com.dropthebit.dropthebit.model.CurrencyData;
import com.dropthebit.dropthebit.model.CurrencyType;
import com.dropthebit.dropthebit.viewmodel.CurrencyViewModel;

import java.text.NumberFormat;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mason-hong on 2017. 12. 16..
 */
public class TransactionDialog extends DialogFragment {
    public static final int TYPE_BUY = 0;
    public static final int TYPE_SELL = 1;

    @BindView(R.id.text_name)
    TextView textName;

    @BindView(R.id.text_symbol)
    TextView textSymbol;

    @BindView(R.id.text_price)
    TextView textPrice;

    private int type;
    private String key;
    private CurrencyViewModel currencyViewModel;

    public static TransactionDialog newInstance(int type) {
        TransactionDialog dialog = new TransactionDialog();
        Bundle args = new Bundle();
        args.putInt(Constants.ARGUMENT_TYPE, type);
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
        type = getArguments().getInt(Constants.ARGUMENT_TYPE, 0);
        key = CurrencyType.values()[type].key;
        String[] names = getResources().getStringArray(R.array.coinNames);
        textName.setText(names[type]);
        textSymbol.setText(key);
        currencyViewModel = ViewModelProviders.of(getActivity()).get(CurrencyViewModel.class);
        currencyViewModel.getCurrencyList()
                .observe(this, map -> {
                    if (map.containsKey(key)) {
                        NumberFormat numberFormat = NumberFormat.getNumberInstance();
                        textPrice.setText(numberFormat.format(Long.parseLong(map.get(key).getPrice())));
                    }
                });
    }
}