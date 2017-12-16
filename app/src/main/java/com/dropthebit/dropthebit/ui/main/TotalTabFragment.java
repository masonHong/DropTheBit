package com.dropthebit.dropthebit.ui.main;

import android.os.Bundle;
import android.view.View;

import com.dropthebit.dropthebit.R;
import com.dropthebit.dropthebit.base.BaseFragment;

/**
 * Created by mason-hong on 2017. 12. 16..
 */
public class TotalTabFragment extends BaseFragment {

    public static TotalTabFragment newInstance() {
        TotalTabFragment fragment = new TotalTabFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_tab_total;
    }

    @Override
    public void initView(View view) {

    }
}
