package com.curtesmalteser.jsoncurrencies;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.curtesmalteser.jsoncurrencies.db.CurrenciesDao;
import com.curtesmalteser.jsoncurrencies.db.CurrenciesDatabase;
import com.curtesmalteser.jsoncurrencies.model.CurrenciesModel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Created by Curtes Malteser on 14/12/2017.
 */

@RunWith(AndroidJUnit4.class)
public class SimpleReadAndWriteTest {
    private CurrenciesDao mCurDao;
    private CurrenciesDatabase mDb;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();
        mDb = Room.inMemoryDatabaseBuilder(context, CurrenciesDatabase.class).build();
        mCurDao = mDb.currenciesDao();
    }

    @After
    public void closeDB() throws IOException {
        mDb.close();
    }

    @Test
    public void writeCurrencyAndReadList() throws Exception {

        ArrayList<CurrenciesModel> cm = new ArrayList<>();

        cm.add(new CurrenciesModel(0, "EUR","15-12-2017", "CHF", 1.1669));
        cm.add(new CurrenciesModel(1, "EUR","15-12-2017", "USD", 1.1806));
        cm.add(new CurrenciesModel(2, "EUR","15-12-2017", "AUD", 1.5382));

        mCurDao.addCurrencies(cm);
        CurrenciesModel byCurrency = mCurDao.selectSingleCurrency("USD");
        assertThat(byCurrency.getCurrency(), equalTo(cm.get(1).getCurrency()));
        }
}
