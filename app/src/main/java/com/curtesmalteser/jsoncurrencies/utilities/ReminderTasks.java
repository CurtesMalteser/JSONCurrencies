package com.curtesmalteser.jsoncurrencies.utilities;

import android.content.Context;
import android.util.Log;

/**
 * Created by António "Curtes Malteser" Bastião on 10/01/2018.
 */


public class ReminderTasks {

    public static final String ACTION_DISMISS_NOTIFICATION = "dismiss-notification";
    public static void executeTask(Context context, String action) {
        if (ACTION_DISMISS_NOTIFICATION.equals(action)) {
            NotificationUtils.clearAllNotifications(context);
        }
    }
}
