package com.moengage.assignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends FragmentActivity implements DownloadCallback {
ProgressBar progressBar;
private ArrayList<NewsModel>newsList;
private NewsAdapter mAdapter;
private RecyclerView recyclerView;
    // Keep a reference to the NetworkFragment, which owns the AsyncTask object
    // that is used to execute network ops.
    private NetworkFragment networkFragment;

    // Boolean telling us whether a download is in progress, so we don't trigger overlapping
    // downloads with consecutive button clicks.
    private boolean downloading = false;
private static final String url="https://candidate-test-data-moengage.s3.amazonaws.com/Android/news-api-feed/staticResponse.json";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         recyclerView = findViewById(R.id.recyclerView);
        progressBar=findViewById(R.id.progress_bar);
        Button start = findViewById(R.id.startDownload);
        newsList=new ArrayList<>();
        networkFragment = NetworkFragment.getInstance(getSupportFragmentManager(), url);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter=new NewsAdapter(this);
        recyclerView.setAdapter(mAdapter);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDownload();
            }
        });


    }


    @Override
    public void updateFromDownload(Object result) {
        progressBar.setVisibility(View.GONE);
        Log.d("success",result.toString());
        parseJson(result.toString());
        runLayoutAnimation(recyclerView);
    }

    private void parseJson(String result) {

        try {
            JSONObject obj = new JSONObject(result);
            JSONArray arr = obj.getJSONArray("articles");
            for(int i=0;i<arr.length();i++){
                NewsModel model = new NewsModel();
                JSONObject objItem = arr.getJSONObject(i);
                model.sourceName= objItem.getJSONObject("source").getString("name");
                model.author=objItem.getString("author");
                model.title=objItem.getString("title");
                model.description=objItem.getString("description");
                model.url=objItem.getString("url");
                model.urlToImage=objItem.getString("urlToImage");
                model.publishedAt=objItem.getString("publishedAt");
                model.content=objItem.getString("content");
                newsList.add(model);
            }
            mAdapter.updateList(newsList);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }

    @Override
    public void onProgressUpdate(int progressCode, int percentComplete) {

    }

    @Override
    public void finishDownloading() {
        downloading = false;
        if (networkFragment != null) {
            networkFragment.cancelDownload();
        }
    }

    private void startDownload() {
        if (!downloading && networkFragment != null) {
            // Execute the async download.
            networkFragment.startDownload();
            downloading = true;
        }
    }

    /*
     * RecyclerView item animation, to give a fall down effect
     * */
    private void runLayoutAnimation(RecyclerView recyclerView) {
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(this,
                R.anim.layout_animation);

        recyclerView.setLayoutAnimation(controller);

        recyclerView.scheduleLayoutAnimation();
    }
}
