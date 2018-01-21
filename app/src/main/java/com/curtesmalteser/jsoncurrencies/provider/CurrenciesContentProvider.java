package com.curtesmalteser.jsoncurrencies.provider;

import android.arch.persistence.room.Room;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.curtesmalteser.jsoncurrencies.db.CurrenciesDao;
import com.curtesmalteser.jsoncurrencies.db.CurrenciesDatabase;
import com.curtesmalteser.jsoncurrencies.model.CurrenciesModel;

import java.util.ArrayList;

/**
 * Created by António "Curtes Malteser" Bastião on 17/12/2017.
 */

public class CurrenciesContentProvider extends ContentProvider {

    private CurrenciesDao mCurDao;
    private CurrenciesDatabase mDb;

    // The authority of the content provider
    public static final String AUTHORITY = "com.curtesmalteser.jsoncurrencies.provider";

    // Build the URI for the currencies_table
    public static final Uri URI_CURRENCIES = Uri.parse(
            "content://" + AUTHORITY + "/" + CurrenciesModel.TABLE_NAME
    );

    // Define final integer for the directory CURRENCIES_TABLE
    public static final int CURRENCIES_TABLE = 100;

    // Define final integer for the directory CURRENCIES_TABLE
    public static final int CURRENCIES_SINGLE_ITEM = 101;

    // Add the URI matcher
    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        MATCHER.addURI(AUTHORITY, CurrenciesModel.TABLE_NAME, CURRENCIES_TABLE);
        MATCHER.addURI(AUTHORITY, CurrenciesModel.TABLE_NAME + "/*", CURRENCIES_SINGLE_ITEM);
    }


    @Override
    public boolean onCreate() {
        // Instantiate Room DB
        mDb = CurrenciesDatabase.getDatabase(getContext());
        mCurDao = mDb.currenciesDao();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        final int code = MATCHER.match(uri);
        if (code == CURRENCIES_TABLE || code == CURRENCIES_SINGLE_ITEM) {
            final Context context = getContext();
            if (context == null) {
                return null;
            }

            CurrenciesDao currenciesDao = CurrenciesDatabase.getDatabase(context).currenciesDao();
            final Cursor cursor;
            if (code == CURRENCIES_TABLE) {
                cursor = currenciesDao.selectAll();
                Log.d("AJDB", "query: " + cursor.getCount());
            } else {
                cursor = currenciesDao.selectById(ContentUris.parseId(uri));
            }
            cursor.setNotificationUri(context.getContentResolver(), uri);
            return cursor;
        } else {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] strings) {
        /* Users of the delete method will expect the number of rows deleted to be returned. */
        int numRowsDeleted;

        /*
         * If we pass null as the selection to SQLiteDatabase#delete, our entire table will be
         * deleted. However, if we do pass null and delete all of the rows in the table, we won't
         * know how many rows were deleted. According to the documentation for SQLiteDatabase,
         * passing "1" for the selection will delete all rows and return the number of rows
         * deleted, which is what the caller of this method expects.
         */
        if (null == selection) selection = "1";

        switch (MATCHER.match(uri)) {

//          COMPLETED (2) Only implement the functionality, given the proper URI, to delete ALL rows in the weather table
            case CURRENCIES_TABLE:
                numRowsDeleted =
                        mCurDao.deleteCurrencies();

                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        /* If we actually deleted any rows, notify that a change has occurred to this URI */
        if (numRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        Log.d("AJDB", "delete: provider");
//      COMPLETED (3) Return the number of rows deleted
        return numRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        ArrayList<CurrenciesModel> mCurrenciesModelArrayList = new ArrayList<>();
       // int i = 0;
        for (ContentValues value : values) {
            mCurrenciesModelArrayList.add(new CurrenciesModel(
                    value.getAsInteger(CurrenciesModel.COLUMN_ID),
                    value.getAsString(CurrenciesModel.COLUMN_BASE),
                    value.getAsString(CurrenciesModel.COLUMN_DATE),
                    value.getAsString(CurrenciesModel.COLUMN_CURRENCY),
                    value.getAsDouble(CurrenciesModel.COLUMN_RATE)
            ));
            //i++;
        }

        Log.d("AJDB", "bulkInsert: provider");
        mCurDao.addCurrencies(mCurrenciesModelArrayList);
        return super.bulkInsert(uri, values);
    }
}
