package com.moengage.assignment.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.moengage.assignment.manager.SharedPreferenceManager;
import com.moengage.assignment.network.DownloadService;

public class CommonUtilities {
    public static boolean isFirstTime(Context context, String key) {

        SharedPreferenceManager manager;
        manager = SharedPreferenceManager.getInstance(context);
        boolean shownBefore = manager.getBoolean(key);
        manager.save(key, true);
        return shownBefore;
    }

    public static boolean haveNetworkConnection(Context context) {
        Context cntx = context;
        if (cntx != null) {
            ConnectivityManager con_manager = (ConnectivityManager) cntx
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            return con_manager.getActiveNetworkInfo() != null && con_manager.getActiveNetworkInfo()
                    .isAvailable() && con_manager.getActiveNetworkInfo().isConnected();
        }
        return false;
    }
    public static void cancelAlarm(Context context){
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent myIntent = new Intent(context, DownloadService.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
               context, 0, myIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pendingIntent);
    }
}
