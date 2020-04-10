package com.moengage.assignment.utils;

import android.content.Context;

import com.moengage.assignment.manager.SharedPreferenceManager;

public class CommonUtilities {
    public static boolean isFirstTime(Context context, String key) {

        SharedPreferenceManager manager;
        manager = SharedPreferenceManager.getInstance(context);
        boolean shownBefore = manager.getBoolean(key);
        manager.save(key, true);
        return shownBefore;
    }
}
