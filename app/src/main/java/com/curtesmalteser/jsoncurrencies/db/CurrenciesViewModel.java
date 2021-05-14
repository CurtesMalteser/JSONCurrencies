package com.curtesmalteser.jsoncurrencies.db;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import com.curtesmalteser.jsoncurrencies.model.CurrenciesModel;

import java.util.List;

/**
 * Created by António "Curtes Malteser" Bastião on 31/01/2018.
 */


public class CurrenciesViewModel extends AndroidViewModel {

    private CurrenciesDatabase mCurrenciesDB;

    private final LiveData<List<CurrenciesModel>> mGetAllCurrencies;

    public CurrenciesViewModel(@NonNull Application application) {
        super(application);

        mCurrenciesDB = CurrenciesDatabase.getDatabase(this.getApplication());

        mGetAllCurrencies =  mCurrenciesDB.currenciesDao().getAllCurrencies();
    }

    public LiveData<List<CurrenciesModel>> getAllCurrenciesLiveData() {
        return mGetAllCurrencies;
    }
}
