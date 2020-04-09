package com.moengage.assignment.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.ProgressBar;

import com.moengage.assignment.R;
import com.moengage.assignment.network.DownloadCallback;
import com.moengage.assignment.network.NetworkFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity implements DownloadCallback,ActionBottomDialogFragment.ItemClickListener {
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
private static final String url="https://candidate-test-data-moengage.s3.amazonaws.com/Android/news-api-feed/staticRespon\n" +
        "se.json";
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
        mAdapter=new NewsAdapter(this,"main_activity");
        recyclerView.setAdapter(mAdapter);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDownload();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.stories:
                startActivity(new Intent(this,SavedStoriesActivity.class));
                break;
            case R.id.sort:
                showBottomSheet();
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.stories_menu, menu);
        return true;
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

    private void showBottomSheet() {
        ActionBottomDialogFragment addPhotoBottomDialogFragment =
                ActionBottomDialogFragment.newInstance();
        addPhotoBottomDialogFragment.show(getSupportFragmentManager(),
                ActionBottomDialogFragment.TAG);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onItemClick(String item) {
        if(item.equalsIgnoreCase("old to new")){
            sortOldToNew();
        }
        else
            sortNewToOld();
    }

    private void sortOldToNew() {
        Collections.sort(newsList, new Comparator<NewsModel>() {
            DateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            @Override
            public int compare(NewsModel o1, NewsModel o2) {
                try {
                    return f.parse(o1.publishedAt).compareTo(f.parse(o2.publishedAt));
                } catch (ParseException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        });
        mAdapter.updateList(newsList);
    }

    private void sortNewToOld() {
        Collections.sort(newsList, new Comparator<NewsModel>() {
            DateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            @Override
            public int compare(NewsModel o1, NewsModel o2) {
                try {
                    return f.parse(o1.publishedAt).compareTo(f.parse(o2.publishedAt));
                } catch (ParseException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        });
        Collections.reverse(newsList);
        mAdapter.updateList(newsList);
    }
}
