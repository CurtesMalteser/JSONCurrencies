package com.curtesmalteser.jsoncurrencies.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import android.database.Cursor;

import com.curtesmalteser.jsoncurrencies.model.CurrenciesModel;

import java.util.ArrayList;
import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

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
    LiveData<List<CurrenciesModel>> getAllCurrencies();

    @Query("SELECT * FROM currencies_table WHERE selected_currency LIKE :selectedCurrency")
    CurrenciesModel selectSingleCurrency(String selectedCurrency);

    @Delete
    int deleteCurrencies(CurrenciesModel... currenciesModel);

    //***************** Methods to use with ContentProvider *****************//
    // This methods returns a Cursor to use on CurrenciesContentProvider and select all currencies
    @Query("SELECT * FROM " + CurrenciesModel.TABLE_NAME)
    Cursor selectAll();

    // Select a cheese by the ID
    // ATTENTION: it has to to take a long id because that's uri parse pass
    @Query("SELECT * FROM " + CurrenciesModel.TABLE_NAME + " WHERE " + CurrenciesModel.COLUMN_ID + " = :id")
    Cursor selectById(long id);
}
