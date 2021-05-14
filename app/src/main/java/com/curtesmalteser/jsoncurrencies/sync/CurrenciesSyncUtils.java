package com.curtesmalteser.jsoncurrencies.sync;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import androidx.annotation.NonNull;
import android.util.Log;

import com.curtesmalteser.jsoncurrencies.model.CurrenciesModel;
import com.curtesmalteser.jsoncurrencies.provider.CurrenciesContentProvider;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

/**
 * Created by António "Curtes Malteser" Bastião on 09/01/2018.
 */


public class CurrenciesSyncUtils {

    private static final int SYNC_INTERVAL_HOURS = 3;
    //private static final int SYNC_INTERVAL_HOURS = 1;
    private static final int SYNC_INTERVAL_SECONDS = (int) TimeUnit.HOURS.toSeconds(SYNC_INTERVAL_HOURS);
    //private static final int SYNC_INTERVAL_SECONDS = (int) TimeUnit.MINUTES.toSeconds(SYNC_INTERVAL_HOURS);
    private static final int SYNC_FLEXTIME_SECONDS = SYNC_INTERVAL_SECONDS / 3;

    private static boolean sInitialized;

    private static final String CURRENCIES_SYNC_TAG = "currencies_sync";

    static void scheduleFirebaseJobDispatcher(@NonNull final Context context) {

        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        Job syncCurrenciesJob = dispatcher.newJobBuilder()
                .setService(CurrenciesFirebaseJobService.class)
                .setTag(CURRENCIES_SYNC_TAG)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(
                        Trigger.executionWindow(
                                SYNC_INTERVAL_SECONDS,
                                SYNC_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS
                        )
                ).setReplaceCurrent(true)
                .build();

        dispatcher.schedule(syncCurrenciesJob);

    }

    synchronized public static void initialize(@NonNull final Context context) {

        if (sInitialized) return;

        sInitialized = true;

        scheduleFirebaseJobDispatcher(context);

        Thread checkForEmpty = new Thread(new Runnable() {
            @Override
            public void run() {

                Uri currenciesQueryUri = CurrenciesContentProvider.URI_CURRENCIES;

                String[] projectionColumns = {CurrenciesModel.COLUMN_ID};

                // TODO: 09/01/2018 handle case the date is changed like in exercise S10.02
        /*String selectionStatement = WeatherContract.WeatherEntry
                .getSqlSelectForTodayOnwards();*/

                Cursor cursor = context.getContentResolver().query(
                        currenciesQueryUri,
                        projectionColumns,
                        null,
                        null,
                        null
                );

                if (null == cursor || cursor.getCount() == 0) {
                    startImmediateSync(context);
                }

                // Make sure to close the Cursor to avoid memory leaks!
                cursor.close();
            }
        });

        /* Finally, once the thread is prepared, fire it off to perform our checks. */
        checkForEmpty.start();
    }

    public static void startImmediateSync(@NonNull final Context context) {
        Log.d("AJDB", "3 - startImmediateSync: ");
        Intent intentToSyncImmediately = new Intent(context, CurrenciesSyncIntentService.class);
        context.startService(intentToSyncImmediately);
    }
}