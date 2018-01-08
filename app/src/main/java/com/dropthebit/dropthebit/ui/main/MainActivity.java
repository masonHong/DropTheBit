package com.dropthebit.dropthebit.ui.main;

import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.TextAppearanceSpan;
import android.widget.TextView;
import android.widget.Toast;

import com.dropthebit.dropthebit.R;
import com.dropthebit.dropthebit.base.TabFragment;
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

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    private List<TabFragment> tabList = new ArrayList<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Map<CurrencyType, CurrencyData> currencyDataMap;
    private InterestViewModel interestViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        textActivityTitle.setText(R.string.activity_title_my_wallet);
        // 탭 리스트 추가
        tabList.add(InterestTabFragment.newInstance(getString(R.string.interest_tab_title)));
        tabList.add(TotalTabFragment.newInstance(getString(R.string.total_tab_title)));
        // 탭 어뎁터 설정
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        // 탭의 최대 개수 2 (탭의 계속적인 생성 방지
        viewPager.setOffscreenPageLimit(2);
        // 탭 레이아웃 설정
        tabLayout.setupWithViewPager(viewPager);

        CommonPref commonPref = CommonPref.getInstance(this);
        if (commonPref.isFirstPayment()) {
            commonPref.setFirstPayment(false);
            Toast.makeText(this, R.string.payment_first_message, Toast.LENGTH_SHORT).show();
            commonPref.addKRW(Constants.PAYMENT_FIRST);
        }

        subscribeTotal();

        interestViewModel = ViewModelProviders.of(this).get(InterestViewModel.class);
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

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return tabList.get(position);
        }

        @Override
        public int getCount() {
            return tabList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabList.get(position).getTabTitle();
        }
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
