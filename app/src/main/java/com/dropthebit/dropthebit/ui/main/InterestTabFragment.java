package com.dropthebit.dropthebit.ui.main;

import android.os.Bundle;
import android.view.View;

import com.dropthebit.dropthebit.R;
import com.dropthebit.dropthebit.base.BaseFragment;
import com.dropthebit.dropthebit.base.TabFragment;
import com.dropthebit.dropthebit.common.Constants;

/**
 * Created by mason-hong on 2017. 12. 16..
 */
public class InterestTabFragment extends TabFragment {

    public static InterestTabFragment newInstance(String tabTitle) {
        InterestTabFragment fragment = new InterestTabFragment();
        Bundle args = new Bundle();
        args.putString(Constants.ARGUMENT_TAB_TITLE, tabTitle);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_tab;
    }

    @Override
    public String getTabTitle() {
        return getArguments().getString(Constants.ARGUMENT_TAB_TITLE);
    }
    @Override
    public void initView(View view) {
    }
}
