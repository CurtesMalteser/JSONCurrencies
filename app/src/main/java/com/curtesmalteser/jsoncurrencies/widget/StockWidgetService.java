package com.curtesmalteser.jsoncurrencies.widget;

import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

/**
 * Created by António "Curtes Malteser" Bastião on 21/01/2018.
 */


public class StockWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.d("AJDB", "StockWidgetService: ");
        return new StockWidgetDataProvider(this.getApplicationContext(), intent);
    }
}
