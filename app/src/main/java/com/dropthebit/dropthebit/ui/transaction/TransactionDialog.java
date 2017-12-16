package com.dropthebit.dropthebit.ui.transaction;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dropthebit.dropthebit.R;

import butterknife.ButterKnife;

/**
 * Created by mason-hong on 2017. 12. 16..
 */
public class TransactionDialog extends DialogFragment {

    public static TransactionDialog newInstance() {
        TransactionDialog fragment = new TransactionDialog();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_transaction, container, false);
        ButterKnife.bind(this, view);
        return view;
    }
}
