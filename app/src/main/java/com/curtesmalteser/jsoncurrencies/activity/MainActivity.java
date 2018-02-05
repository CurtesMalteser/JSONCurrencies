package com.curtesmalteser.jsoncurrencies.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.curtesmalteser.jsoncurrencies.R;
import com.curtesmalteser.jsoncurrencies.fragment.MasterListFragment;

public class MainActivity extends AppCompatActivity {

    public static Context appContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MasterListFragment masterListFragment = new MasterListFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .add(R.id.masterListFragment, masterListFragment)
                .commit();

    }


}