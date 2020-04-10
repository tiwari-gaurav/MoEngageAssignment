package com.moengage.assignment.network;

import android.app.Activity;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.moengage.assignment.database.DatabaseHelper;
import com.moengage.assignment.utils.CommonUtilities;
import com.moengage.assignment.view.NewsModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class DownloadService extends JobIntentService {

    private int result = Activity.RESULT_CANCELED;

    private static final int JOB_ID = 2;
    public static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, DownloadService.class, JOB_ID, intent);
    }
    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        String urlString = intent.getStringExtra("url");
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();

        }
        if(CommonUtilities.haveNetworkConnection(getApplicationContext())) {
            InputStream stream = null;
            HttpsURLConnection connection = null;
            String resultData = null;
            try {
                connection = (HttpsURLConnection) url.openConnection();
                // Timeout for reading InputStream arbitrarily set to 3000ms.
                connection.setReadTimeout(5000);
                // Timeout for connection.connect() arbitrarily set to 3000ms.
                connection.setConnectTimeout(5000);
                // For this use case, set HTTP method to GET.
                connection.setRequestMethod("GET");
                // Already true by default but setting just in case; needs to be true since this request
                // is carrying an input (response) body.
                connection.setDoInput(true);
                // Open communications link (network traffic occurs here).
                connection.connect();
                // publishProgress(DownloadCallback.Progress.CONNECT_SUCCESS);
                int responseCode = connection.getResponseCode();
                if (responseCode != HttpsURLConnection.HTTP_OK) {
                    throw new IOException("HTTP error code: " + responseCode);
                }
                // Retrieve the response body as an InputStream.
                stream = connection.getInputStream();
                //  publishProgress(DownloadCallback.Progress.GET_INPUT_STREAM_SUCCESS, 0);
                if (stream != null) {
                    // Converts Stream to String.
                    resultData = readStream(stream);
                    // Sucessful finished
                    result = Activity.RESULT_OK;
                }
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // Close Stream and disconnect HTTPS connection.
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (connection != null) {
                    connection.disconnect();
                }
            }

            parseJson(resultData);
            Bundle extras = intent.getExtras();
            if (extras != null) {
                // TODO get your messager via the extras
                Messenger messenger = extras.getParcelable("MESSENGER");
                Message msg = Message.obtain();
                msg.arg1 = result;
                Bundle bundle = new Bundle();
                msg.setData(bundle);
                // TODO Send message ensure to put it into a try catch
                try {
                    if (messenger != null)
                        messenger.send(msg);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private String readStream(InputStream stream)
            throws IOException, UnsupportedEncodingException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    private void parseJson(String result) {
        ArrayList<NewsModel>newsList=new ArrayList<>();
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        db.deleteHeadlines();
        try {
            JSONObject obj = new JSONObject(result);
            JSONArray arr = obj.getJSONArray("articles");
            for (int i = 0; i < arr.length(); i++) {
                NewsModel model = new NewsModel();
                JSONObject objItem = arr.getJSONObject(i);
                model.sourceName = objItem.getJSONObject("source").getString("name");
                model.author = objItem.getString("author");
                model.title = objItem.getString("title");
                model.description = objItem.getString("description");
                model.url = objItem.getString("url");
                model.urlToImage = objItem.getString("urlToImage");
                model.publishedAt = objItem.getString("publishedAt");
                model.content = objItem.getString("content");
                newsList.add(model);
                    db.insertHeadlines(model);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
