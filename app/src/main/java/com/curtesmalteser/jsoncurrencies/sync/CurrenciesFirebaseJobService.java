package com.curtesmalteser.jsoncurrencies.sync;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

/**
 * Created by António "Curtes Malteser" Bastião on 09/01/2018.
 */


public class CurrenciesFirebaseJobService extends JobService {

    private Loader mCurrenciesLoader;
    @Override
    public boolean onStartJob(JobParameters jobParameters) {

        Context context = getApplicationContext();

        mCurrenciesLoader = new AsyncTaskLoader(context) {
            @Override
            public Object loadInBackground() {
                CurrenciesSyncTask.syncCurriesData(context);
                CurrenciesSyncUtils.initialize(context);
                jobFinished(jobParameters, false);
                return null;
            }

          /*  @Override
            public void deliverResult(Object data) {
                super.deliverResult(data);
                jobFinished(jobParameters, false);

            }*/

        };

        mCurrenciesLoader.forceLoad();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {

        if (mCurrenciesLoader != null) mCurrenciesLoader.stopLoading();

        return true;
    }
}
