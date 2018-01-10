package com.curtesmalteser.jsoncurrencies.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

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
        String action = intent.getAction();
        ReminderTasks.executeTask(this, action);
    }
}
