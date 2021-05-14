package com.curtesmalteser.jsoncurrencies.activity;

import androidx.lifecycle.ViewModelProviders;
import androidx.databinding.DataBindingUtil;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.loader.app.LoaderManager.LoaderCallbacks;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

        // Used to change EditText's ID, other away the APP will identify them as being only one
        converterBinding.baseConverter.etValue.setId(R.id.base_et_value);
        converterBinding.selectedCurrencyConverter.etValue.setId(R.id.selected_et_Value);

        converterModel = ViewModelProviders.of(this).get(ConverterModel.class);

        // TODO: 08/12/2017 Add the menu
        // Get the parcelable object to retrieve the Model Object passed from MainActivity
        if (getIntent().hasExtra("currency")) {
            CurrenciesModel currenciesModel = getIntent().getParcelableExtra("currency");

            converterModel.setBaseDate(currenciesModel.getDate());

            converterBinding.baseConverter.tvDateCurrency.setText(converterModel.getBaseDate());

            converterModel.setBaseCoin(currenciesModel.getBase());
            converterBinding.baseConverter.baseLabel.setText(converterModel.getBaseCoin());

            converterBinding.baseConverter.currencyLabel.setText(currenciesModel.getCurrency());

            Log.d(TAG, "getCurrency: " + currenciesModel.getCurrency());

            converterModel.setBaseRate(currenciesModel.getRate());
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

        converterBinding.baseConverter.etValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                displayBaseToForeign();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        converterBinding.selectedCurrencyConverter.etValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                displayForeignToBase();
            }

            @Override
            public void afterTextChanged(Editable editable) {

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
                String base = cm.getCurrency();

                URL url = NetworkUtils.buildUrlLatest(base);
                try {
                    String data = NetworkUtils.getResponseFromHttpUrl(url);
                    CurrenciesModel currenciesModelResult = FixerJsonUtils.getSingleCurrency(converterModel.getBaseCoin(), data);
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

            converterModel.setReverseCoin(data.getCurrency());
            converterBinding.selectedCurrencyConverter.currencyLabel.setText(converterModel.getReverseCoin());

            converterModel.setReverseDate(data.getDate());
            converterBinding.selectedCurrencyConverter.tvDateCurrency.setText(converterModel.getReverseDate());

            converterModel.setReverseRate(data.getRate());
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