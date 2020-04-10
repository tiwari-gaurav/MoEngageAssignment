package com.moengage.assignment.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Messenger;
import android.widget.Toast;


public class TaskScheduler extends BroadcastReceiver {
    private static final String url = "https://candidate-test-data-moengage.s3.amazonaws.com/Android/news-api-feed/staticRespon\n" +
            "se.json";
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"Alarm ringed",Toast.LENGTH_SHORT).show();

        Intent serviceIntent = new Intent(context, DownloadService.class);
        // Create a new Messenger for the communication back
       // Messenger messenger = new Messenger(handler);
        // TODO put messages into the intent
       // intent.putExtra("MESSENGER", messenger);
        serviceIntent.putExtra("url",url);
        DownloadService.enqueueWork(context, serviceIntent);

    }
}
