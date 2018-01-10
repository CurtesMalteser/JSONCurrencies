package com.curtesmalteser.jsoncurrencies.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.curtesmalteser.jsoncurrencies.provider.CurrenciesContentProvider;
import com.curtesmalteser.jsoncurrencies.utilities.FixerJsonUtils;
import com.curtesmalteser.jsoncurrencies.utilities.NetworkUtils;
import com.curtesmalteser.jsoncurrencies.utilities.NotificationUtils;

import java.net.URL;

/**
 * Created by António "Curtes Malteser" Bastião on 08/01/2018.
 */


public class CurrenciesSyncTask {

    private static final String TAG = CurrenciesSyncTask.class.getSimpleName();

    synchronized public static void syncCurriesData(Context context) {
        try {

            URL currenciesRequestUrl = NetworkUtils.getUrl(context);
            String jsonCurrenciesResponse = NetworkUtils.getResponseFromHttpUrl(currenciesRequestUrl);

            ContentValues[] contentValues= FixerJsonUtils.getCurrenciesContentValues(context, jsonCurrenciesResponse);

            if (contentValues != null && contentValues.length != 0) {
                ContentResolver currenciesContentResolver = context.getContentResolver();

                currenciesContentResolver.delete(
                        CurrenciesContentProvider.URI_CURRENCIES,
                        null,
                        null);

                currenciesContentResolver.bulkInsert(
                        CurrenciesContentProvider.URI_CURRENCIES,
                        contentValues);

                NotificationUtils.notifyUserOfNewRates(context);
            }
        } catch (Exception e) {
            Log.e(TAG, "syncCurriesData: ", e);
        }
    }
}
