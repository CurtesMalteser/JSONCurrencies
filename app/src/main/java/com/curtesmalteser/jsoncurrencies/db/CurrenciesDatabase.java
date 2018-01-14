package com.curtesmalteser.jsoncurrencies.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.curtesmalteser.jsoncurrencies.model.CurrenciesModel;

/**
 * Created by António "Curtes Malteser" Bastião on 15/12/2017.
 */

@Database(entities = {CurrenciesModel.class}, version = 1)
public abstract class CurrenciesDatabase extends RoomDatabase {

    private static CurrenciesDatabase INSTANCE;

    public static CurrenciesDatabase getDatabase(Context context) {
        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                    context.getApplicationContext(),
                    CurrenciesDatabase.class,
                    "currencies.db").build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    public abstract CurrenciesDao currenciesDao();
}
