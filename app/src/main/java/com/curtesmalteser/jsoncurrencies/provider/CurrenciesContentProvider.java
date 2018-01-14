package com.curtesmalteser.jsoncurrencies.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.curtesmalteser.jsoncurrencies.db.CurrenciesDao;
import com.curtesmalteser.jsoncurrencies.db.CurrenciesDatabase;
import com.curtesmalteser.jsoncurrencies.model.CurrenciesModel;

/**
 * Created by António "Curtes Malteser" Bastião on 17/12/2017.
 */

public class CurrenciesContentProvider extends ContentProvider {

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
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
