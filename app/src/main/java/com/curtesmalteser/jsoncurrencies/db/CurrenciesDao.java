package com.curtesmalteser.jsoncurrencies.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;

import com.curtesmalteser.jsoncurrencies.model.CurrenciesModel;

import java.util.ArrayList;
import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by António "Curtes Malteser" Bastião on 14/12/2017.
 */

@Dao
public interface CurrenciesDao {

    @Insert(onConflict = REPLACE)
    void addCurrencies(ArrayList<CurrenciesModel> currenciesModel);

    @Update(onConflict = REPLACE)
    int updateCurrencies(CurrenciesModel... currenciesModel);

    @Query("SELECT * FROM " + CurrenciesModel.TABLE_NAME)
    public LiveData<List<CurrenciesModel>> getAllCurrencies();

    @Query("SELECT * FROM currencies_table WHERE selected_currency LIKE :selectedCurrency")
    public CurrenciesModel selectSingleCurrency(String selectedCurrency);

    @Delete
    public void deleteCurrencies(CurrenciesModel... currenciesModel);

    //***************** Methods to use with ContentProvider *****************//
    // This methods returns a Cursor to use on CurrenciesContentProvider and select all currencies
    @Query("SELECT * FROM " + CurrenciesModel.TABLE_NAME)
    Cursor selectAll();

    // Select a cheese by the ID
    // ATTENTION: it has to to take a long id because that's uri parse pass
    @Query("SELECT * FROM " + CurrenciesModel.TABLE_NAME + " WHERE " + CurrenciesModel.COLUMN_ID + " = :id")
    Cursor selectById(long id);
}
