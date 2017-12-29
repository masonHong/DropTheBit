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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dropthebit.dropthebit.R;
import com.dropthebit.dropthebit.common.Constants;
import com.dropthebit.dropthebit.model.CurrencyType;
import com.dropthebit.dropthebit.viewmodel.CurrencyViewModel;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

/**
 * Created by mason-hong on 2017. 12. 16..
 */
public class TransactionDialog extends DialogFragment {
    public static final int TYPE_BUY = 0;
    public static final int TYPE_SELL = 1;

    // 원하는 값만 사용하도록 경고해주는 용도
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TYPE_BUY, TYPE_SELL})
    public @interface TransactionType {
    }

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

    @BindView(R.id.text_time)
    TextView textTime;

    @BindView(R.id.edit_amount)
    EditText editAmount;

    @BindView(R.id.edit_predict_price)
    EditText editPredictPrice;

    @BindView(R.id.progress_time)
    ProgressBar progressTime;

    @TransactionType
    private int transactionType;

    private CurrencyType currencyType;
    private CurrencyViewModel currencyViewModel;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private long realTimePrice = 0;
    private long currentPrice = 0;
    private int currentCount = 0;
    private boolean isFirstLoaded = true;

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
        Disposable disposable = Flowable.interval(0, Constants.PERIOD_TRANSACTION_INTERVAL, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(time -> {
                    currentCount += Constants.PERIOD_TRANSACTION_INTERVAL;
                    float rate = currentCount / (float) Constants.PERIOD_TRANSACTION_REFRESH;
                    progressTime.setProgress((int) (rate * 100));
                    if (currentCount == Constants.PERIOD_TRANSACTION_REFRESH) {
                        currentCount = 0;
                        currentPrice = realTimePrice;
                        NumberFormat numberFormat = NumberFormat.getNumberInstance();
                        textPrice.setText(numberFormat.format(currentPrice));
                    }
                    if (currentCount % 500 == 0) {
                        textTime.setText(String.format(Locale.getDefault(), "%.01f초", (Constants.PERIOD_TRANSACTION_REFRESH - currentCount) / 1000F));
                    }
                });
        compositeDisposable.add(disposable);
    }

    @Override
    public void onStop() {
        super.onStop();
        compositeDisposable.clear();
    }

    @OnTextChanged(R.id.edit_amount)
    public void onAmountChanged(CharSequence text, int start, int end, int length) {
        if (editAmount.hasFocus()) {
            double amount = 0;
            if (!text.toString().isEmpty()) {
                amount = Double.parseDouble(text.toString());
            }
            long predictPrice = (long) (amount * currentPrice);
            editPredictPrice.setText(String.format(Locale.getDefault(), "%d", predictPrice));
        }
    }

    @OnTextChanged(R.id.edit_predict_price)
    public void onPredictPriceChanged(CharSequence text, int start, int end, int length) {
        if (editPredictPrice.hasFocus()) {
            long predictPrice = 0;
            if (!text.toString().isEmpty()) {
                predictPrice = Long.parseLong(text.toString());
            }
            double amount = (double) predictPrice / currentPrice;
            editAmount.setText(String.format(Locale.getDefault(), "%f", amount));
        }
    }

    @OnClick(R.id.button_cancel)
    public void onClickCancel() {
        dismiss();
    }

    @OnClick(R.id.button_confirm)
    public void onClickConfirm() {
        dismiss();
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
                    if (map.containsKey(currencyType)) {
                        realTimePrice = Integer.parseInt(map.get(currencyType).getPrice());
                        if (isFirstLoaded) {
                            NumberFormat numberFormat = NumberFormat.getNumberInstance();
                            currentPrice = realTimePrice;
                            textPrice.setText(numberFormat.format(realTimePrice));
                            isFirstLoaded = false;
                        }
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