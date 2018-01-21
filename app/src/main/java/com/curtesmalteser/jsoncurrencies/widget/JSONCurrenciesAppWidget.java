package com.curtesmalteser.jsoncurrencies.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.RemoteViews;

import com.curtesmalteser.jsoncurrencies.R;
import com.curtesmalteser.jsoncurrencies.activity.MainActivity;

/**
 * Implementation of App Widget functionality.
 */
public class JSONCurrenciesAppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget);

        setRemoteAdapter(context, views);

        // PendingIntent to launch the MainActivity when the widget is clicked
        Intent launchMain = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, launchMain, 0);
        views.setOnClickPendingIntent(R.id.widget, pendingIntent);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_listView);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
        Log.d("AJDB", "updateAppWidget: ");

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    // Set the adapter for out widget
    private static void setRemoteAdapter(Context context, @NonNull final RemoteViews views) {
        views.setRemoteAdapter(R.id.widget_listView,
                new Intent(context, StockWidgetService.class));
        Log.d("AJDB", "setRemoteAdapter: sdfbsdfbsdf");
    }
}

