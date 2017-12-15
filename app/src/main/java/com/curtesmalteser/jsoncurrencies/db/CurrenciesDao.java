package com.curtesmalteser.jsoncurrencies.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.curtesmalteser.jsoncurrencies.model.CurrenciesModel;

import java.util.ArrayList;
import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by Curtes Malteser on 14/12/2017.
 */

@Dao
public interface CurrenciesDao {

    @Insert(onConflict = REPLACE)
    void addCurrencies(ArrayList<CurrenciesModel> currenciesModel);

    @Update(onConflict = REPLACE)
    int updateCurrencies(CurrenciesModel... currenciesModel);

    @Query("SELECT * FROM currencies_table")
    public LiveData<List<CurrenciesModel>> getAllCurrencies();

    @Query("SELECT * FROM currencies_table WHERE currency LIKE :selectedCurency")
    public CurrenciesModel selectSingleCurrency(String selectedCurency);

    @Delete
    public void deleteCurrencies(CurrenciesModel... currenciesModel);
}
