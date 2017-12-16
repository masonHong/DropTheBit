package com.dropthebit.dropthebit.ui.main;

import android.os.Bundle;
import android.view.View;

import com.dropthebit.dropthebit.R;
import com.dropthebit.dropthebit.base.BaseFragment;

/**
 * Created by mason-hong on 2017. 12. 16..
 */
public class InterestTabFragment extends BaseFragment {

    public static InterestTabFragment newInstance() {
        InterestTabFragment fragment = new InterestTabFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_tab_interest;
    }

    @Override
    public void initView(View view) {

    }
}
