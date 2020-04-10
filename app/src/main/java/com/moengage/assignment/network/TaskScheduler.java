package com.moengage.assignment.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.moengage.assignment.view.BackgroundServiceListener;


public class TaskScheduler extends BroadcastReceiver {
    private BackgroundServiceListener mListener;
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"Alarm ringed",Toast.LENGTH_SHORT).show();

    }
}
