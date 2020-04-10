package com.moengage.assignment.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.moengage.assignment.R;
import com.moengage.assignment.database.DatabaseHelper;
import com.moengage.assignment.network.DownloadCallback;
import com.moengage.assignment.network.DownloadService;
import com.moengage.assignment.network.NetworkFragment;
import com.moengage.assignment.network.TaskScheduler;
import com.moengage.assignment.utils.CommonUtilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity implements DownloadCallback, ActionBottomDialogFragment.ItemClickListener {
    ProgressBar progressBar;
    private ArrayList<NewsModel> newsList;
    private NewsAdapter mAdapter;
    private RecyclerView recyclerView;
    private DatabaseHelper db;


    // Boolean telling us whether a download is in progress, so we don't trigger overlapping
    // downloads with consecutive button clicks.
    private boolean downloading = false;
    private static final String url = "https://candidate-test-data-moengage.s3.amazonaws.com/Android/news-api-feed/staticRespon\n" +
            "se.json";


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message message) {
            Bundle data = message.getData();
            if (message.arg1 == RESULT_OK) {
                progressBar.setVisibility(View.GONE);
                newsList = db.getAllHeadlines();
                if (mAdapter != null)
                    mAdapter.updateList(newsList);
                if (recyclerView != null)
                    runLayoutAnimation(recyclerView);
                Toast.makeText(MainActivity.this, "Downloaded",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(MainActivity.this, "Download failed.",
                        Toast.LENGTH_LONG).show();
            }

        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progress_bar);
        newsList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new NewsAdapter(this, "main_activity", db);
        recyclerView.setAdapter(mAdapter);

        if (CommonUtilities.haveNetworkConnection(this)) {
            if (!CommonUtilities.isFirstTime(this, "isFirstDownloaded")) {
                startDownload();
                setAlarm();
            } else {
                getDataFromDb();

            }
        } else {
            getDataFromDb();
            Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_LONG).show();
        }

    }

    private void getDataFromDb() {
        newsList = db.getAllHeadlines();
        if (newsList.size() > 0)
            progressBar.setVisibility(View.GONE);
        mAdapter.updateList(newsList);
        runLayoutAnimation(recyclerView);
    }

    private void setAlarm() {
        //getting the alarm manager
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        //creating a new intent specifying the broadcast receiver
        Intent i = new Intent(this, TaskScheduler.class);
        i.putExtra("url", url);

        //creating a pending intent using the intent
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        //setting the repeating alarm that will be fired every day
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime(),
                3 * 60 * 60 * 1000, pi);
        Toast.makeText(this, "Alarm is set", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.stories:
                startActivity(new Intent(this, SavedStoriesActivity.class));
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
    public NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }

    public void startDownload() {

        Intent intent = new Intent(this, DownloadService.class);
        // Create a new Messenger for the communication back
        Messenger messenger = new Messenger(handler);
        // TODO put messages into the intent
        intent.putExtra("MESSENGER", messenger);
        intent.putExtra("url", url);
        DownloadService.enqueueWork(this, intent);
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
        if (item.equalsIgnoreCase("old to new")) {
            sortOldToNew();
        } else
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
        runLayoutAnimation(recyclerView);
    }
}
