package com.curtesmalteser.jsoncurrencies;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.curtesmalteser.jsoncurrencies.databinding.ActivityMainBinding;
import com.curtesmalteser.jsoncurrencies.databinding.LocalCoinBinding;
import com.curtesmalteser.jsoncurrencies.model.CurrenciesModel;
import com.curtesmalteser.jsoncurrencies.utilities.FixerJsonUtils;
import com.curtesmalteser.jsoncurrencies.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

// COMPLETED (1) Implement the proper LoaderCallbacks interface and the methods of that interface
public class MainActivity extends AppCompatActivity implements
        LoaderCallbacks<ArrayList<CurrenciesModel>>{

    /* A constant to save and restore the URL that is being displayed */
    private static final String SEARCH_QUERY_URL_EXTRA = "query";
    /*
    * This number will uniquely identify our Loader and is chosen arbitrarily. You can change this
    * to any number you like, as long as you use the same variable name.
    */
    private static final int SEARCH_LOADER_ID = 22;
    private static final String TAG = "AJDB";

    // COMPLETED - create a membr variable for CurrenciesAdapter
    private CurrenciesAdapter mCurrenciesAdpater;

    private ActivityMainBinding mActivityMainBinding;

    String base = "EUR";

    // COMPLETED (xxx) Cache the currencies data in a member variable and deliver it in onStartLoading.
    ArrayList<CurrenciesModel> mModelArrayList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        // Create an ArrayAdapter with the string array currencies_array
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.currencies_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mActivityMainBinding.localCoin.spinnerSelectCoin.setAdapter(adapter);

        SpinnerActivity activity = new SpinnerActivity();

        mActivityMainBinding.localCoin.spinnerSelectCoin.setOnItemSelectedListener ( activity );

        // COMPLETED - create a LayoutManager (this case LinearLayoutManager)
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        // COMPLETED - assign the layoutManager to the RecyclerView
        mActivityMainBinding.listCurrencies.recyclerviewCurrencies.setLayoutManager(layoutManager);

        // COMPLETED - assign the setHasFixedSize(true) because allows optimizations on our UI
        mActivityMainBinding.listCurrencies.recyclerviewCurrencies.setHasFixedSize(true);

        setData();

    }

    // TODO: 29/11/2017 Replace with real data
    private void setData() {

        int loaderId = SEARCH_LOADER_ID;

        LoaderCallbacks<ArrayList<CurrenciesModel>> callback = MainActivity.this;

        Bundle bundleForLoader = null;

        getSupportLoaderManager().initLoader(loaderId, bundleForLoader, callback);



        mActivityMainBinding.localCoin.labelCurrentCountyFlag.setText("Portugal");

        mActivityMainBinding.localCoin.labelDate.setText("29 / 11 / 2017");
        mActivityMainBinding.localCoin.labelCurrencyDate.setText("N/A");

    }

    // COMPLETED (xxx) Within onCreateLoader, return a new AsyncTaskLoader<ArrayList<CurrenciesModel>>.
    @Override
    public Loader<ArrayList<CurrenciesModel>> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<ArrayList<CurrenciesModel>>(this) {


            String mBase = base;

            @Override
            protected void onStartLoading() {

                if (mModelArrayList != null) {
                    deliverResult(mModelArrayList);
                } else {
                    // TODO (1): 01/12/2017 addr the loading indicator
                    forceLoad();
                }
            }


            @Override
            public ArrayList<CurrenciesModel> loadInBackground() {


                Log.d(TAG, "loadInBackground: " + base);

                ArrayList<CurrenciesModel> mCurrenciesModelArrayList = new ArrayList<>();

                URL url = NetworkUtils.buildUrlLatest(mBase);
                try {
                    String data = NetworkUtils.getResponseFromHttpUrl(url);
                    mCurrenciesModelArrayList = FixerJsonUtils.getCurrencies(data);

                    return mCurrenciesModelArrayList;

                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(ArrayList<CurrenciesModel> data) {
                super.deliverResult(data);
                mModelArrayList = data;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<CurrenciesModel>> loader, ArrayList<CurrenciesModel> data) {

        if ( null == data ) {
            showErrorMessage();
        } else {
            // COMPLETED - set the data that will be passed to our adapter
            mCurrenciesAdpater = new CurrenciesAdapter(data);
            // COMPLETED - set the Adapter
            mActivityMainBinding.listCurrencies
                    .recyclerviewCurrencies.setAdapter(mCurrenciesAdpater);

            mActivityMainBinding.localCoin.labelCurrencyDate.setText(data.get(0).getDate());
        }

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<CurrenciesModel>> loader) {

    }

    public void showErrorMessage() {
        // TODO: 01/12/2017 (2) fix the error message to show something usefull
        Toast.makeText(this, "Error loading data!", Toast.LENGTH_SHORT).show();
    }

    private class SpinnerActivity extends Activity
            implements AdapterView.OnItemSelectedListener {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                base = parent.getItemAtPosition(position).toString();
                Log.d(TAG, "onItemSelected: " + position + " coin: " + base);


                LoaderCallbacks<ArrayList<CurrenciesModel>> callback = MainActivity.this;

                if (mModelArrayList != null) {
                    getSupportLoaderManager().restartLoader(SEARCH_LOADER_ID, null, callback).forceLoad();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        }
}
