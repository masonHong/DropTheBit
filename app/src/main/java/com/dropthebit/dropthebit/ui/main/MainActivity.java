package com.dropthebit.dropthebit.ui.main;

import android.content.Intent;
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
import com.dropthebit.dropthebit.common.Constants;
import com.dropthebit.dropthebit.model.CurrencyType;
import com.dropthebit.dropthebit.ui.detail.DetailActivity;
import com.dropthebit.dropthebit.ui.adapter.viewholder.CurrencyViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by mason-hong on 2017. 12. 13..
 */
public class MainActivity extends AppCompatActivity implements CurrencyViewHolder.OnCurrencyClickListener {

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
        compositeDisposable.clear();
    }

    @Override
    public void onCurrencyClick(CurrencyType type) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(Constants.ARGUMENT_TYPE, type);
        startActivity(intent);
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
