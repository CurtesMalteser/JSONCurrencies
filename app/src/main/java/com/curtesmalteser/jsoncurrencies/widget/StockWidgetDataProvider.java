package com.curtesmalteser.jsoncurrencies.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Binder;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.curtesmalteser.jsoncurrencies.R;
import com.curtesmalteser.jsoncurrencies.model.CurrenciesModel;
import com.curtesmalteser.jsoncurrencies.provider.CurrenciesContentProvider;

/**
 * Created by António "Curtes Malteser" Bastião on 21/01/2018.
 */


public class StockWidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {

    private Context context;
    private Intent intent;
    private Cursor cursor;


    // For obtaining the activity's context and intent
    public StockWidgetDataProvider(Context context, Intent intent) {
        this.context = context;
        this.intent = intent;
    }

    @Override
    public void onCreate() {

        initCursor();
        if (cursor != null) {
            cursor.moveToFirst();
        }

    }

    @Override
    public void onDataSetChanged() {

        /** Listen for data changes and initialize the cursor again **/
        initCursor();

    }

    @Override
    public void onDestroy() {
        cursor.close();
    }

    @Override
    public int getCount() {
        return cursor.getCount();
        //return 0;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Log.d("AJDB", "getViewAt: ");
        /// Populate your widget's single list item
        cursor.moveToPosition(position);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.list_widget_item);
        remoteViews.setTextViewText(R.id.tv_base,cursor.getString(cursor.getColumnIndex(CurrenciesModel.COLUMN_BASE)));
        remoteViews.setTextViewText(R.id.tv_base,cursor.getString(cursor.getColumnIndex(CurrenciesModel.COLUMN_DATE)));
        remoteViews.setTextViewText(R.id.tv_currency,cursor.getString(cursor.getColumnIndex(CurrenciesModel.COLUMN_CURRENCY)));
        remoteViews.setTextViewText(R.id.tv_rate,cursor.getString(cursor.getColumnIndex(CurrenciesModel.COLUMN_RATE)));
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    private void initCursor(){
        if (cursor != null) {
            cursor.close();
        }

        final long identityToken = Binder.clearCallingIdentity();
        /**This is done because the widget runs as a separate thread
         when compared to the current app and hence the app's data won't be accessible to it
         because I'm using a content provided **/

        Log.d("AJDB", "I'm here! :) ");
        new AsyncTaskLoader(context) {
            @Nullable
            @Override
            public Object loadInBackground() {
                Cursor newCursor = context.getContentResolver().query(
                        CurrenciesContentProvider.URI_CURRENCIES,
                /* Columns; leaving this null returns every column in the table */
                        null,
                /* Optional specification for columns in the "where" clause above */
                        null,
                /* Values for "where" clause */
                        null,
                /* Sort order to return in Cursor */
                        null);
                Log.d("AJDB", "initCursor: " + cursor.getCount());

                cursor = newCursor;

                return cursor;
            }
        };

        Binder.restoreCallingIdentity(identityToken);

    }
}
