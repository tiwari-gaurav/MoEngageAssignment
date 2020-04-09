package com.moengage.assignment.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.moengage.assignment.R;
import com.moengage.assignment.database.DatabaseHelper;

import java.util.ArrayList;

public class SavedStoriesActivity extends AppCompatActivity {
    private RecyclerView storyRecyclerView;
    private DatabaseHelper db;
    ArrayList<NewsModel>savedStoriesList;
    private NewsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        storyRecyclerView=findViewById(R.id.recyclerView);
        ProgressBar progress = findViewById(R.id.progress_bar);
        progress.setVisibility(View.GONE);
        db=new DatabaseHelper(this);
        savedStoriesList=db.getAllStories();
        storyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter=new NewsAdapter(this, "stories");
        storyRecyclerView.setAdapter(mAdapter);
        mAdapter.updateList(savedStoriesList);
    }
}
