package com.curtesmalteser.jsoncurrencies;

import android.arch.persistence.room.Room;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.curtesmalteser.jsoncurrencies.db.CurrenciesDao;
import com.curtesmalteser.jsoncurrencies.db.CurrenciesDatabase;
import com.curtesmalteser.jsoncurrencies.model.CurrenciesModel;
import com.curtesmalteser.jsoncurrencies.provider.CurrenciesContentProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Created by Curtes Malteser on 14/12/2017.
 */

@RunWith(AndroidJUnit4.class)
public class SimpleReadAndWriteTest {
    private CurrenciesDao mCurDao;
    private CurrenciesDatabase mDb;
    private static final String TAG = "AndroidJUnit4";

    ArrayList<CurrenciesModel> cm = new ArrayList<>();

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();
        mDb = Room.inMemoryDatabaseBuilder(context, CurrenciesDatabase.class).build();
        mCurDao = mDb.currenciesDao();

        cm.add(new CurrenciesModel(0, "EUR", "15-12-2017", "CHF", 1.1669));
        cm.add(new CurrenciesModel(1, "EUR", "15-12-2017", "USD", 1.1806));
        cm.add(new CurrenciesModel(2, "EUR", "15-12-2017", "AUD", 1.5382));
    }

    @After
    public void closeDB() throws IOException {
        mDb.close();
    }

   /*@Test
    public void writeCurrencyAndReadList() throws Exception {

        mCurDao.addCurrencies(cm);
        CurrenciesModel byCurrency = mCurDao.selectSingleCurrency("USD");
        assertThat(byCurrency.getCurrency(), equalTo(cm.get(1).getCurrency()));

    }*/

    @Test
    public void testBulkInsert() {

        Context context = InstrumentationRegistry.getTargetContext();

        ContentValues[] currenciesContentValues = new ContentValues[cm.size()];

        for (int i = 0; i < cm.size(); i++) {

            ContentValues contentValues = new ContentValues();

            contentValues.put( CurrenciesModel.COLUMN_ID, cm.get(i).getId());
            contentValues.put( CurrenciesModel.COLUMN_BASE, cm.get(i).getBase());
            contentValues.put( CurrenciesModel.COLUMN_DATE, cm.get(i).getDate());
            contentValues.put( CurrenciesModel.COLUMN_CURRENCY, cm.get(i).getCurrency());
            contentValues.put( CurrenciesModel.COLUMN_RATE, cm.get(i).getRate());

            currenciesContentValues[i] = contentValues;

        }

        for(ContentValues contentValues : currenciesContentValues) {
            Log.d(TAG, "testBulkInsert: " + contentValues.get("selected_currency"));
        }
        /* Perform the ContentProvider query */
        ContentResolver currenciesContentResolver = context.getContentResolver();

        int insertCount = currenciesContentResolver.bulkInsert(
                CurrenciesContentProvider.URI_CURRENCIES,
                currenciesContentValues);


        Log.d(TAG, "testBulkInsert: " + insertCount);
        String queryFailed = "Query failed to return a valid Cursor";
        assertEquals(queryFailed, cm.size(), insertCount);


    }

    @Test
    public void testQuery() {
        Context context = InstrumentationRegistry.getTargetContext();


         /* Perform the ContentProvider query */
        Cursor taskCursor = context.getContentResolver().query(
                CurrenciesContentProvider.URI_CURRENCIES,
                /* Columns; leaving this null returns every column in the table */
                null,
                /* Optional specification for columns in the "where" clause above */
                null,
                /* Values for "where" clause */
                null,
                /* Sort order to return in Cursor */
                null);

        int count = taskCursor.getCount();
        Log.d("AndroidJUnit4", "testQuery: " + count);
        String queryFailed = "Query failed to return a valid Cursor";
        assertTrue(queryFailed, taskCursor != null);

        /* We are done with the cursor, close it now. */
        taskCursor.close();
    }

}
