package com.curtesmalteser.jsoncurrencies.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.curtesmalteser.jsoncurrencies.R;
import com.curtesmalteser.jsoncurrencies.adapter.CurrenciesAdapter;
import com.curtesmalteser.jsoncurrencies.databinding.ActivityMainBinding;
import com.curtesmalteser.jsoncurrencies.db.CurrenciesDao;
import com.curtesmalteser.jsoncurrencies.db.CurrenciesDatabase;
import com.curtesmalteser.jsoncurrencies.model.CurrenciesModel;
import com.curtesmalteser.jsoncurrencies.sync.CurrenciesSyncUtils;
import com.curtesmalteser.jsoncurrencies.utilities.FixerJsonUtils;
import com.curtesmalteser.jsoncurrencies.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

// COMPLETED (1) Implement the proper LoaderCallbacks interface and the methods of that interface
public class MainActivity extends AppCompatActivity implements
        LoaderCallbacks<ArrayList<CurrenciesModel>>, CurrenciesAdapter.ListItemClickListener,
        AdapterView.OnItemSelectedListener{

    /* A constant to save and restore the URL that is being displayed */
    private static final String SEARCH_QUERY_URL_EXTRA = "query";
    /*
    * This number will uniquely identify our Loader and is chosen arbitrarily. You can change this
    * to any number you like, as long as you use the same variable name.
    */
    private static final int SEARCH_LOADER_ID = 22;
    private static final String TAG = MainActivity.class.getSimpleName();

    // COMPLETED - create a member variable for CurrenciesAdapter
    private CurrenciesAdapter mCurrenciesAdpater;

    private ActivityMainBinding mainBinding;

    // Room variables
    private CurrenciesDao mCurDao;
    private CurrenciesDatabase mDb;


    public static Context appContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appContext = getApplicationContext();

        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        // Use to set the vectorDrawables color
        mainBinding.localCoin.localCoin.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.SRC_IN);

        mainBinding.localCoin.labelCurrentCountyFlag.setText("Portugal");

        mainBinding.localCoin.labelDate.setText("29 / 11 / 2017");
        mainBinding.localCoin.labelCurrencyDate.setText("N/A");

        // Create an ArrayAdapter with the string array currencies_array
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.currencies_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mainBinding.localCoin.spinnerSelectCoin.setAdapter(adapter);

        SharedPreferences sharedPreferences = this.getSharedPreferences("currencies", MODE_PRIVATE);
        String index = sharedPreferences.getString("base", "EUR");

        String[] array = getResources().getStringArray(R.array.currencies_array);
        int value = Arrays.asList(array).indexOf(index);
        mainBinding.localCoin.spinnerSelectCoin.setSelection(value);

        mainBinding.localCoin.spinnerSelectCoin.setOnItemSelectedListener(this);

        // COMPLETED - create a LayoutManager (this case LinearLayoutManager)
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        // COMPLETED - assign the layoutManager to the RecyclerView
        mainBinding.listCurrencies.recyclerviewCurrencies.setLayoutManager(layoutManager);

        // COMPLETED - assign the setHasFixedSize(true) because allows optimizations on our UI
        mainBinding.listCurrencies.recyclerviewCurrencies.setHasFixedSize(true);

        // todo data from shared preferences
        LoaderManager loaderManager = getSupportLoaderManager();

        loaderManager.initLoader(SEARCH_LOADER_ID, null, this);

        CurrenciesSyncUtils.initialize(this);

        // Instantiate Room DB
        mDb = CurrenciesDatabase.getDatabase(this);
        mCurDao = mDb.currenciesDao();

    }

    private void setData(String base) {

        LoaderCallbacks<ArrayList<CurrenciesModel>> callback = MainActivity.this;

        Bundle bundleForLoader = new Bundle();

        bundleForLoader.putString("base", base);

        // Use the code restartLoader will also make the initLoader case the Loader doesn't exist
        // So I'm saving lines of code here
        getSupportLoaderManager().restartLoader(SEARCH_LOADER_ID, bundleForLoader, callback).forceLoad();

    }

    // COMPLETED Within onCreateLoader, return a new AsyncTaskLoader<ArrayList<CurrenciesModel>>.
    @Override
    public Loader<ArrayList<CurrenciesModel>> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<ArrayList<CurrenciesModel>>(this) {

            // COMPLETED - Cache the currencies data in a member variable and deliver it in onStartLoading.
            ArrayList<CurrenciesModel> mModelArrayList = null;

            @Override
            protected void onStartLoading() {


                if (mModelArrayList != null) {
                    deliverResult(mModelArrayList);
                    mainBinding.progressBar.setVisibility(View.INVISIBLE);
                } else {
                    mainBinding.progressBar.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }


            @Override
            public ArrayList<CurrenciesModel> loadInBackground() {

                String argsString;

                SharedPreferences sharedPreferences = getContext().getSharedPreferences("currencies", MODE_PRIVATE);
                String shared = sharedPreferences.getString("base", "EUR");
                if(args != null) {

                    argsString = args.getString("base");
                } else {
                    argsString = shared;
                }

                ArrayList<CurrenciesModel> mCurrenciesModelArrayList;

                URL url = NetworkUtils.buildUrlLatest(argsString);
                try {
                    String data = NetworkUtils.getResponseFromHttpUrl(url);
                    mCurrenciesModelArrayList = FixerJsonUtils.getCurrencies(MainActivity.this, data);

                    mCurDao.addCurrencies(mCurrenciesModelArrayList);

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
            mCurrenciesAdpater = new CurrenciesAdapter(data,this);
            // COMPLETED - set the Adapter
            mainBinding.listCurrencies
                    .recyclerviewCurrencies.setAdapter(mCurrenciesAdpater);

            mainBinding.localCoin.labelCurrencyDate.setText(data.get(0).getDate());


        }
        mainBinding.progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<CurrenciesModel>> loader) {

    }

    public void showErrorMessage() {
        // TODO: 01/12/2017 (2) fix the error message to show something usefull
        Toast.makeText(this, "Error loading data!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onListItemClick(CurrenciesModel currenciesModel) {

        Intent intent = new Intent(MainActivity.this, ConverterActivity.class);

        // Pass the currencies model as an extra to the ConverterActivity
        intent.putExtra("currency", currenciesModel);

        startActivity(intent);
    }

    // Spinner methods....
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        SharedPreferences sharedPreferences = this.getSharedPreferences("currencies", MODE_PRIVATE);
        String base = parent.getItemAtPosition(position).toString();

        //Get the value stored in shared pref
        String sharedPrefVal = sharedPreferences.getString("base", "EUR");
        if(!base.equals(sharedPrefVal)){
            setData(base);
            sharedPreferences(base);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    // TODO (3) - Override onCreateOtionsMenu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // TODO (4) - getMenuInflater to inflate main_menu_menu.xml resource
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    // TODO (5) - override onOptionsItemSelected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // COMPLETED - Add a switch and add an action to which case
        switch (item.getItemId() ) {

            case R.id.action_share:
                actionShare();
                Toast.makeText(this, R.string.share, Toast.LENGTH_SHORT).show();
                break;

            case R.id.action_test :
                Toast.makeText(this, "action_test", Toast.LENGTH_SHORT).show();
                break;
                
            case R.id.action_settings:
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                break;

                default:
                    Toast.makeText(this, "default", Toast.LENGTH_SHORT).show();

        }
        return super.onOptionsItemSelected(item);
    }

    private void actionShare() {
        String mimeType = "text/Plain";
        String title = "JSONCurrencies";

        ShareCompat.IntentBuilder
                .from(this)
                .setChooserTitle(title)
                .setType(mimeType)
                .setText(getString(R.string.text_to_share))
                .setSubject("JSONCurrencies")
                .startChooser();
    }


    private void sharedPreferences(String base) {
        // SharedPreferences
        SharedPreferences.Editor  sharedPreferences = getSharedPreferences("currencies", Context.MODE_PRIVATE).edit();
        sharedPreferences.putString("base", base);
        sharedPreferences.commit();
    }
}