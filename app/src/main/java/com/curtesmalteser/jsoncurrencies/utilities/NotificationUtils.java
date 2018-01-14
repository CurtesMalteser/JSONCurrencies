package com.curtesmalteser.jsoncurrencies.utilities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;

import com.curtesmalteser.jsoncurrencies.R;
import com.curtesmalteser.jsoncurrencies.activity.MainActivity;
import com.curtesmalteser.jsoncurrencies.model.CurrenciesModel;
import com.curtesmalteser.jsoncurrencies.provider.CurrenciesContentProvider;
import com.curtesmalteser.jsoncurrencies.sync.CurrenciesSyncIntentService;

/**
 * Created by António "Curtes Malteser" Bastião on 10/01/2018.
 */


public class NotificationUtils {

    public static final String[] CURRENCIES_NOTIFICATION_PROJECTION = {
            CurrenciesModel.COLUMN_ID,
            CurrenciesModel.COLUMN_BASE,
            CurrenciesModel.COLUMN_DATE,
            CurrenciesModel.COLUMN_CURRENCY,
            CurrenciesModel.COLUMN_RATE
    };

    private static final int CURRENCIES_NOTIFICATION_ID = 2002;

    private static final String CURRENCIES_REMINDER_NOTIFICATION_CHANNEL_ID = "reminder_notification_channel";

    private static final int ACTION_IGNORE_PENDING_INTENT_ID = 23;

    public static void clearAllNotifications(Context context) {
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    public static void notifyUserOfNewRates(Context context) {

        // TODO: 10/01/2018 make a URI with date like the one from  ???
        Uri todayCurrenciesUri = CurrenciesContentProvider.URI_CURRENCIES;

        Cursor todayCurrenciesCursor = context.getContentResolver()
                .query(
                        todayCurrenciesUri,
                        CURRENCIES_NOTIFICATION_PROJECTION,
                        null,
                        null,
                        null
                );

        // Get the default notification ringtone URI
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        if (todayCurrenciesCursor.moveToFirst()) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel mChannel = new NotificationChannel(
                        CURRENCIES_REMINDER_NOTIFICATION_CHANNEL_ID,
                        context.getString(R.string.main_notification_channel_name),
                        NotificationManager.IMPORTANCE_HIGH
                );
            }


            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CURRENCIES_REMINDER_NOTIFICATION_CHANNEL_ID)
                    .setColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
                    .setSmallIcon(R.drawable.ic_money)
                    .setContentTitle("JSON Currencies")
                    .setContentText("Check the latest rates for your favourite currency")
                    .setSound(notification)
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .addAction(ignoreReminderAction(context))
                    .setAutoCancel(true);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                    && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
            }

            Intent intentCurrencies = new Intent(context, MainActivity.class);
            intentCurrencies.setData(todayCurrenciesUri);

            TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
            taskStackBuilder.addNextIntentWithParentStack(intentCurrencies);
            PendingIntent resultPendingIntent = taskStackBuilder
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            notificationBuilder.setContentIntent(resultPendingIntent);

            NotificationManager notificationManager = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(CURRENCIES_NOTIFICATION_ID, notificationBuilder.build());

            // TODO: 10/01/2018 implement prefernces
            // SunshinePreferences.saveLastNotificationTime(context, System.currentTimeMillis());

        }

        todayCurrenciesCursor.close();
    }

    private static NotificationCompat.Action ignoreReminderAction(Context context) {

        Intent ignoreReminderIntent = new Intent(context, CurrenciesSyncIntentService.class);

        ignoreReminderIntent.setAction(ReminderTasks.ACTION_DISMISS_NOTIFICATION);

        PendingIntent ignoreReminderPendingIntent = PendingIntent.getService(
                context,
                ACTION_IGNORE_PENDING_INTENT_ID,
                ignoreReminderIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Action ignoreReminderAction = new NotificationCompat.Action(R.drawable.ic_action_cancel,
                "No, thanks.",
                ignoreReminderPendingIntent);

        return ignoreReminderAction;
    }


}