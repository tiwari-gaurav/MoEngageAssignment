package com.moengage.assignment.manager;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Author:  Gaurav Tiwari.
 * Date: 11/02/2018
 * Info: Manager class to store and retrieve data from sharedPreference.
 */
public class SharedPreferenceManager {

    // constants.
    private final String PREFERENCES = "guroo_preferences";


    // members
    private static SharedPreferenceManager sSingleton;
    private SharedPreferences mSharedPreference;

    ///
    // Get instance.
    ///
    public static synchronized SharedPreferenceManager getInstance(Context context) {
        if (sSingleton == null) {
            sSingleton = new SharedPreferenceManager(context);
        }
        return sSingleton;
    }


    ///
    // constructor
    ///
    private SharedPreferenceManager(Context context) {
        mSharedPreference = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
    }


    /*
     * Method to remove key from sharedpreference
     *
     * */
    public void remove(String key) {
        try {
            SharedPreferences.Editor editor = mSharedPreference.edit();
            editor.remove(key);
            editor.commit();
        } catch (Exception ex) {
            //Silent fail
        }
    }


    /*
     * Save string value in shared pref.
     * */

    public void save(String key, String value) {
        SharedPreferences.Editor editor = mSharedPreference.edit();
        editor.putString(key, value);

        editor.commit();
    }


    /*
     Save boolean value in shared pref.
    */
    public void save(String key, boolean value) {
        SharedPreferences.Editor editor = mSharedPreference.edit();
        editor.putBoolean(key, value);

        editor.commit();
    }



    /*
     * Save int value in shared pref.
     * */

    public void save(String key, int value) {
        SharedPreferences.Editor editor = mSharedPreference.edit();
        editor.putInt(key, value);
        editor.commit();
    }


    /*
    Save float value in shared pref.
    */

    public void save(String key, float value) {
        SharedPreferences.Editor editor = mSharedPreference.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    /**
     * Save the values stored in the map in shared preferences.
     *
     * @param values values to save.
     */
    public void save(Map<String, String> values) {

        SharedPreferences.Editor editor = mSharedPreference.edit();

        Set<String> keySet = values.keySet();

        Iterator<String> iterator = keySet.iterator();

        while (iterator.hasNext()) {

            String key = iterator.next();

            String value = values.get(key);

            editor.putString(key, value);

        }


        editor.commit();

    }


    /**
     * Save the values stored in the map in shared preferences.
     *
     * @param values values to save.
     */
    public void saveList(Map<String, List<String>> values) {

        SharedPreferences.Editor editor = mSharedPreference.edit();

        Set<String> keySet = values.keySet();

        Iterator<String> iterator = keySet.iterator();

        while (iterator.hasNext()) {

            String key = iterator.next();
            Set<String> set = new HashSet<String>();
            set.addAll(values.get(key));
            //List<String> value = values.get(key);

            editor.putStringSet(key, set);

        }

        editor.commit();

    }


    public void saveArrayList(Map<String, ArrayList<String>> values) {

        SharedPreferences.Editor editor = mSharedPreference.edit();

        Set<String> keySet = values.keySet();

        Iterator<String> iterator = keySet.iterator();

        while (iterator.hasNext()) {

            String key = iterator.next();
            Set<String> set = new HashSet<String>();
            set.addAll(values.get(key));
            //List<String> value = values.get(key);

            editor.putStringSet(key, set);

        }

        editor.commit();

    }

    ///
    // Get String
    ///
    public String getString(String key) {
        return mSharedPreference.getString(key, "");
    }

    /*
     *   Get float.
     * */

    public float getFloat(String key) {
        return mSharedPreference.getFloat(key, 0.0f);
    }


    /*
     * Get integer.
     * */

    public int getInt(String key) {
        return mSharedPreference.getInt(key, 0);
    }

    /*
     * Get StringList
     *
     */

    public Set<String> getStringList(String key) {
        return mSharedPreference.getStringSet(key, null);
    }

    public Set<String> getStringArrayList(String key) {
        return mSharedPreference.getStringSet(key, null);
    }

    /*
     * Get boolean
     * */


    public boolean getBoolean(String key) {
        return mSharedPreference.getBoolean(key, false);
    }

    /*
     * Get boolean with default value
     * */
    public boolean getBooleanWithDefaultTrue(String key) {
        return mSharedPreference.getBoolean(key, true);
    }


    /*
     * clear data from shared pref.
     * */

    public void clear() {
        SharedPreferences.Editor editor = mSharedPreference.edit();
        editor.clear();
        editor.commit();
    }

    public void save(String key, long value) {
        SharedPreferences.Editor editor = mSharedPreference.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public long getLong(String key) {
        return mSharedPreference.getLong(key, 0L);
    }


}
