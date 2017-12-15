package com.dropthebit.dropthebit.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.dropthebit.dropthebit.R;

/**
 * Created by mason-hong on 2017. 12. 13..
 */
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_main, MainFragment.newInstance(1000), MainFragment.class.getName())
                .commit();
    }
}
