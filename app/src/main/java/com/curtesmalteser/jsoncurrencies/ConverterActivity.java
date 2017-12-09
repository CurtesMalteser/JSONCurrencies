package com.curtesmalteser.jsoncurrencies;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.curtesmalteser.jsoncurrencies.databinding.ActivityConverterBinding;
import com.curtesmalteser.jsoncurrencies.model.CurrenciesModel;

public class ConverterActivity extends AppCompatActivity {

    private ActivityConverterBinding converterBinding;

    private static final String TAG = "AJDB";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        converterBinding = DataBindingUtil.setContentView(this, R.layout.activity_converter);
        // TODO: 08/12/2017 Add the main_menu and databinding

        // Get the parcelable object to retrieve the Model Object passed from MainActivity
        if (getIntent().hasExtra("currency")) {
            CurrenciesModel cm = getIntent().getParcelableExtra("currency");
            Log.d(TAG, cm.getCoin().toString());
            converterBinding.baseConverter.tvCurrencyDate.setText(cm.getDate());
            converterBinding.baseConverter.baseLabel.setText(cm.getBase());
            converterBinding.baseConverter.currencyLabel.setText(cm.getCoin());
            converterBinding.baseConverter.tvRate.setText(cm.getCurrency().toString());
        } else {
            Log.d(TAG, "Error passing data between activities!");
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.converter_menu, menu);
        return true;
    }
}
