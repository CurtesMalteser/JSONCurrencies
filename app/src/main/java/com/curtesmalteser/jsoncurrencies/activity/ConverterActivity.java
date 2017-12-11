package com.curtesmalteser.jsoncurrencies.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.curtesmalteser.jsoncurrencies.R;
import com.curtesmalteser.jsoncurrencies.databinding.ActivityConverterBinding;
import com.curtesmalteser.jsoncurrencies.model.CurrenciesModel;
import com.curtesmalteser.jsoncurrencies.model.ConverterModel;
import com.curtesmalteser.jsoncurrencies.utilities.FixerJsonUtils;
import com.curtesmalteser.jsoncurrencies.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class ConverterActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<CurrenciesModel>{

    private ActivityConverterBinding converterBinding;

    private ConverterModel converterModel;

    private static final String TAG = "AJDB";

    // AsyncTaskLoader ID
    private static final int SEARCH_LOADER = 84;

    private static final String FIXER_QUERY_EXTRA = "base";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        converterBinding = DataBindingUtil.setContentView(this, R.layout.activity_converter);

        converterModel = ViewModelProviders.of(this).get(ConverterModel.class);

        // TODO: 08/12/2017 Add the menu
        // Get the parcelable object to retrieve the Model Object passed from MainActivity
        if (getIntent().hasExtra("currency")) {
            CurrenciesModel currenciesModel = getIntent().getParcelableExtra("currency");

            converterBinding.baseConverter.tvDateCurrency.setText(currenciesModel.getDate());

            converterModel.setBase(currenciesModel.getBase());
            converterBinding.baseConverter.baseLabel.setText(converterModel.getBase());

            converterBinding.baseConverter.currencyLabel.setText(currenciesModel.getCoin());

            Log.d(TAG, "getCoin: " + currenciesModel.getCoin());

            converterModel.setBaseRate(currenciesModel.getCurrency());
            converterBinding.baseConverter.tvRate.setText(String.valueOf(converterModel.getBaseRate()));

            // AsyncTaskLoader call
            Bundle currencyReverseBundle = new Bundle();
            currencyReverseBundle.putParcelable(FIXER_QUERY_EXTRA, currenciesModel);

            LoaderManager loaderManager = getSupportLoaderManager();

            LoaderCallbacks<CurrenciesModel> callback = ConverterActivity.this;

            loaderManager.initLoader(SEARCH_LOADER, currencyReverseBundle, callback);

        } else {
            Log.d(TAG, "Error passing data between activities!");
        }

        // Display the MVP data
        converterBinding.baseConverter.tvResult.setText(String.valueOf(converterModel.getBaseResult()));
        converterBinding.selectedCurrencyConverter.tvResult.setText(String.valueOf(converterModel.getReverseResult()));

        converterBinding.baseConverter.buttonGetResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayBaseToForeign();
            }
        });

        converterBinding.selectedCurrencyConverter.buttonGetResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayForeignToBase();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.converter_menu, menu);
        return true;
    }

    // ViewModel Methods
    private void displayBaseToForeign() {

        if ( !converterBinding.baseConverter.etValue.getText().toString().equals("")) {
            converterModel.setBaseInput(Double.parseDouble(converterBinding.baseConverter.etValue.getText().toString()));

            converterModel.setBaseResult(calculator(converterModel.getBaseInput(), converterModel.getBaseRate()));
            converterBinding.baseConverter.tvResult.setText(String.valueOf(converterModel.getBaseResult()));
        } else {
            converterBinding.baseConverter.tvResult.setText("0.00");
        }
    }

    private void displayForeignToBase(){
        if ( !converterBinding.selectedCurrencyConverter.etValue.getText().toString().equals("")) {
            converterModel.setReverseInput(Double.parseDouble(converterBinding.selectedCurrencyConverter.etValue.getText().toString()));

            converterModel.setReverseResult(calculator(converterModel.getReverseInput(), converterModel.getReverseRate()));
            converterBinding.selectedCurrencyConverter.tvResult.setText(String.valueOf(converterModel.getReverseResult()));
        } else {
            converterBinding.selectedCurrencyConverter.tvResult.setText("0.00");
        }
    };

    @Override
    public Loader<CurrenciesModel> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<CurrenciesModel>(this) {

            CurrenciesModel currenciesModel = null;

            @Override
            protected void onStartLoading() {
               if (currenciesModel != null) {
                   deliverResult(currenciesModel);
                   converterBinding.progressBar.setVisibility(View.INVISIBLE);
               } else {
                   converterBinding.progressBar.setVisibility(View.VISIBLE);
                   forceLoad();
               }
            }

            @Override
            public CurrenciesModel loadInBackground() {

                CurrenciesModel cm = args.getParcelable(FIXER_QUERY_EXTRA);
                String base = cm.getCoin();

                URL url = NetworkUtils.buildUrlLatest(base);
                try {
                    String data = NetworkUtils.getResponseFromHttpUrl(url);
                    CurrenciesModel currenciesModelResult = FixerJsonUtils.getSingleCurrency(converterModel.getBase(), data);
                    return currenciesModelResult;

                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(CurrenciesModel data) {
                super.deliverResult(data);
                currenciesModel = data;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<CurrenciesModel> loader, CurrenciesModel data) {
        if (data != null) {

            converterModel.setReverseBase(data.getBase());
            converterBinding.selectedCurrencyConverter.baseLabel.setText(converterModel.getReverseBase());

            converterModel.setReverseCoin(data.getCoin());
            converterBinding.selectedCurrencyConverter.currencyLabel.setText(converterModel.getReverseCoin());

            converterModel.setReverseDate(data.getDate());
            converterBinding.selectedCurrencyConverter.tvDateCurrency.setText(converterModel.getReverseDate());

            converterModel.setReverseRate(data.getCurrency());
            converterBinding.selectedCurrencyConverter.tvRate.setText(String.valueOf(converterModel.getReverseRate()));
        } else {
            Log.d(TAG, "onLoadFinished: data is null");
        }

        converterBinding.progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<CurrenciesModel> loader) {

    }

    private double calculator(double input, double rate) {
        return input * rate;
    }
}
