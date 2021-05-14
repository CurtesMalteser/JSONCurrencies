package com.curtesmalteser.jsoncurrencies.sync;

import android.app.IntentService;
import android.content.Intent;
import androidx.annotation.Nullable;
import android.util.Log;

import com.curtesmalteser.jsoncurrencies.utilities.ReminderTasks;

/**
 * Created by António "Curtes Malteser" Bastião on 09/01/2018.
 */


public class CurrenciesSyncIntentService extends IntentService {

    public CurrenciesSyncIntentService() {
        super("CurrenciesSyncIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d("AJDB", "3 - onHandleIntent: ");
        String action = intent.getAction();
        ReminderTasks.executeTask(this, action);
    }
}
