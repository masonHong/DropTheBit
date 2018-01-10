package com.dropthebit.dropthebit.ui.main;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;
import android.widget.TextView;
import android.widget.Toast;

import com.dropthebit.dropthebit.R;
import com.dropthebit.dropthebit.common.Constants;
import com.dropthebit.dropthebit.model.CurrencyData;
import com.dropthebit.dropthebit.model.CurrencyType;
import com.dropthebit.dropthebit.provider.pref.CommonPref;
import com.dropthebit.dropthebit.provider.room.RoomProvider;
import com.dropthebit.dropthebit.provider.room.Wallet;
import com.dropthebit.dropthebit.provider.room.WalletDao;
import com.dropthebit.dropthebit.ui.detail.DetailActivity;
import com.dropthebit.dropthebit.ui.adapter.viewholder.CurrencyViewHolder;
import com.dropthebit.dropthebit.util.CurrencyUtils;
import com.dropthebit.dropthebit.util.StringUtils;
import com.dropthebit.dropthebit.viewmodel.CurrencyViewModel;
import com.dropthebit.dropthebit.viewmodel.InterestViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by mason-hong on 2017. 12. 13..
 */
public class MainActivity extends AppCompatActivity implements CurrencyViewHolder.OnCurrencyClickListener {

    @BindView(R.id.text_activity_title)
    TextView textActivityTitle;

    @BindView(R.id.text_total)
    TextView textTotal;

    @BindView(R.id.button_toggle)
    TextView buttonToggle;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Map<CurrencyType, CurrencyData> currencyDataMap;
    private InterestViewModel interestViewModel;
    private List<Fragment> fragmentList;
    private List<String> tagList;
    private int currentFragment = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        textActivityTitle.setText(R.string.activity_title_my_wallet);
        CommonPref commonPref = CommonPref.getInstance(this);
        if (commonPref.isFirstPayment()) {
            commonPref.setFirstPayment(false);
            Toast.makeText(this, R.string.payment_first_message, Toast.LENGTH_SHORT).show();
            commonPref.addKRW(Constants.PAYMENT_FIRST);
        }

        subscribeTotal();

        interestViewModel = ViewModelProviders.of(this).get(InterestViewModel.class);
        fragmentList = new ArrayList<>();
        tagList = new ArrayList<>();
        fragmentList.add(TotalFragment.newInstance());
        tagList.add(Constants.FRAGMENT_TOTAL);
        fragmentList.add(InterestFragment.newInstance());
        tagList.add(Constants.FRAGMENT_INTEREST);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_fragment, fragmentList.get(currentFragment), tagList.get(currentFragment))
                .commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
        compositeDisposable.clear();
    }

    @Override
    public void onCurrencyClick(CurrencyType type) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(Constants.ARGUMENT_TYPE, type);
        startActivity(intent);
    }

    @Override
    public void onCurrencyLongClick(CurrencyType type) {
        new AlertDialog.Builder(this)
                .setItems(new CharSequence[]{"add", "remove"}, (dialog, which) -> {
                    Timber.i("dialog which: %d", which);
                    if (which == 0) {
                        interestViewModel.addInterestCoin(type.key);
                    } else {
                        interestViewModel.removeInterestCoin(type.key);
                    }
                    dialog.dismiss();
                })
                .create()
                .show();
    }

    @OnClick(R.id.image_menu)
    void onClickMenu() {
        Toast.makeText(this, "메뉴 아이콘 클릭", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.button_toggle)
    void onToggle() {
        currentFragment = (currentFragment + 1) % fragmentList.size();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_fragment, fragmentList.get(currentFragment), tagList.get(currentFragment))
                .commit();
    }

    private void subscribeTotal() {
        ViewModelProviders.of(this).get(CurrencyViewModel.class)
                .getCurrencyList()
                .observe(this, map -> {
                    currencyDataMap = map;
                    updateTotal();
                });
    }

    private void updateTotal() {
        WalletDao walletDao = RoomProvider.getInstance(getBaseContext()).getDatabase().walletDao();
        walletDao.loadAllWallet()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                            long total = 0;
                            if (currencyDataMap != null) {
                                for (Wallet wallet : list) {
                                    String price = currencyDataMap.get(CurrencyUtils.findByName(wallet.name)).getPrice();
                                    if (price.contains(".")) {
                                        price = price.substring(0, price.indexOf("."));
                                    }
                                    total += wallet.amount * Long.parseLong(price);
                                }
                            }
                            total += CommonPref.getInstance(getBaseContext()).getKRW();
                            setTotalText(total);
                        }, Throwable::printStackTrace,
                        () -> setTotalText(CommonPref.getInstance(getBaseContext()).getKRW()));
    }

    private void setTotalText(long number) {
        SpannableString ss = new SpannableString(StringUtils.getPriceString(number, Constants.UNIT_WON));
        int positionKRW = ss.length() - Constants.UNIT_WON.length();
        ss.setSpan(new AbsoluteSizeSpan(28, true), 0, positionKRW, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new StyleSpan(Typeface.BOLD), 0, positionKRW, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new AbsoluteSizeSpan(15, true), positionKRW, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textTotal.setText(ss);
    }
}
