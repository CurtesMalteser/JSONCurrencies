package com.curtesmalteser.jsoncurrencies;

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

import com.curtesmalteser.jsoncurrencies.databinding.ActivityConverterBinding;
import com.curtesmalteser.jsoncurrencies.model.CurrenciesModel;
import com.curtesmalteser.jsoncurrencies.utilities.FixerJsonUtils;
import com.curtesmalteser.jsoncurrencies.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class ConverterActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<CurrenciesModel>{

    private ActivityConverterBinding converterBinding;

    private static final String TAG = "AJDB";

    // AsyncTaskLoader ID
    private static final int SEARCH_LOADER = 84;

    private static final String FIXER_QUERY_EXTRA = "base";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        converterBinding = DataBindingUtil.setContentView(this, R.layout.activity_converter);
        // TODO: 08/12/2017 Add the menu

        // Get the parcelable object to retrieve the Model Object passed from MainActivity
        if (getIntent().hasExtra("currency")) {
            CurrenciesModel cm = getIntent().getParcelableExtra("currency");
            Log.d(TAG, "Extra: " + cm.getCoin().toString());
            converterBinding.baseConverter.tvDateCurrency.setText(cm.getDate());
            converterBinding.baseConverter.baseLabel.setText(cm.getBase());
            converterBinding.baseConverter.currencyLabel.setText(cm.getCoin());
            converterBinding.baseConverter.tvRate.setText(cm.getCurrency().toString());

            // AsyncTaskLoader call
            Bundle currencyReverseBundle = new Bundle();
            currencyReverseBundle.putParcelable(FIXER_QUERY_EXTRA, cm);

            LoaderManager loaderManager = getSupportLoaderManager();

            LoaderCallbacks<CurrenciesModel> callback = ConverterActivity.this;

            loaderManager.initLoader(SEARCH_LOADER, currencyReverseBundle, callback);



        } else {
            Log.d(TAG, "Error passing data between activities!");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.converter_menu, menu);
        return true;
    }

    @Override
    public Loader<CurrenciesModel> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<CurrenciesModel>(this) {

            CurrenciesModel currenciesModel = null;

            @Override
            protected void onStartLoading() {
               if (currenciesModel != null) {
                   Log.d(TAG, "onStartLoading: currenciesModel != null" );
                   deliverResult(currenciesModel);
                   converterBinding.progressBar.setVisibility(View.INVISIBLE);
               } else {
                   Log.d(TAG, "onStartLoading: currenciesModel == null" );
                   converterBinding.progressBar.setVisibility(View.VISIBLE);
                   forceLoad();
               }
            }

            @Override
            public CurrenciesModel loadInBackground() {

                CurrenciesModel cm = args.getParcelable(FIXER_QUERY_EXTRA);
                String base = cm.getCoin();
                Log.d(TAG, "loadInBackground: " + base);

                URL url = NetworkUtils.buildUrlLatest(base);
                try {
                    String data = NetworkUtils.getResponseFromHttpUrl(url);
                    CurrenciesModel currenciesModelResult = FixerJsonUtils.getSingleCurrency("EUR", data);
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
                Log.d(TAG, "delivering Result" );
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<CurrenciesModel> loader, CurrenciesModel data) {
        if (data != null) {
            converterBinding.selectedCurrencyConverter.baseLabel.setText(data.getBase());
            converterBinding.selectedCurrencyConverter.currencyLabel.setText(data.getCoin());
            converterBinding.selectedCurrencyConverter.tvDateCurrency.setText(data.getDate());
            converterBinding.selectedCurrencyConverter.tvRate.setText(data.getCurrency().toString());
        } else {
            Log.d(TAG, "onLoadFinished: data is null");
        }
        converterBinding.progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<CurrenciesModel> loader) {

    }
}
