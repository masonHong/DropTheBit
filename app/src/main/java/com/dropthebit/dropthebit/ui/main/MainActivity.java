package com.dropthebit.dropthebit.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.dropthebit.dropthebit.R;
import com.dropthebit.dropthebit.base.TabFragment;
import com.dropthebit.dropthebit.model.CurrencyType;
import com.dropthebit.dropthebit.provider.pref.CommonPref;
import com.dropthebit.dropthebit.provider.room.InterestCoin;
import com.dropthebit.dropthebit.provider.room.RoomProvider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by mason-hong on 2017. 12. 13..
 */
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;

    @BindView(R.id.text_total)
    TextView textTotal;

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    private List<TabFragment> tabList = new ArrayList<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (CommonPref.getInstance(this).isFirstStart()) {
            Disposable disposable = Observable.just(RoomProvider.getInstance(this).getDatabase().intersetCoinDao())
                    .subscribeOn(Schedulers.io())
                    .subscribe(dao -> {
                        dao.insertIntersetCoin(new InterestCoin(CurrencyType.BitCoin.key));
                        dao.insertIntersetCoin(new InterestCoin(CurrencyType.Etherium.key));
                        dao.insertIntersetCoin(new InterestCoin(CurrencyType.Ripple.key));
                        CommonPref.getInstance(this).setFirstStart(false);
                    }, Throwable::printStackTrace);
            compositeDisposable.add(disposable);
        }

        setSupportActionBar(toolbar);
        // 탭 리스트 추가
        tabList.add(InterestTabFragment.newInstance(getString(R.string.interest_tab_title)));
        tabList.add(TotalTabFragment.newInstance(getString(R.string.total_tab_title)));
        // 탭 어뎁터 설정
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        // 탭의 최대 개수 2 (탭의 계속적인 생성 방지
        viewPager.setOffscreenPageLimit(2);
        // 탭 레이아웃 설정
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void onStop() {
        super.onStop();
        compositeDisposable.dispose();
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
}
