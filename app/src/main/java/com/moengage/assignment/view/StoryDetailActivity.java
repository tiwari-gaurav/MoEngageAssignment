package com.moengage.assignment.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.moengage.assignment.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StoryDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_detail);
       String title= getIntent().getStringExtra("title");
       String desc=getIntent().getStringExtra("content");
       String author=getIntent().getStringExtra("author");
       String source = getIntent().getStringExtra("source");
       String banner_image=getIntent().getStringExtra("banner_image");
       String published = getIntent().getStringExtra("publishedAt");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        TextView tv_title=findViewById(R.id.title_text);
        TextView tv_desc=findViewById(R.id.description_text);
        TextView tv_author =findViewById(R.id.author_text);
        TextView tv_source = findViewById(R.id.source);
        TextView publishedAt=findViewById(R.id.publish);
        ImageView image=findViewById(R.id.banner_image);
        publishedAt.setText(convertDate(published));
        if(title!=null)
        tv_title.setText(title);
        if(desc!=null)
        tv_desc.setText(desc);
        if(source!=null)
        tv_source.setText(source);
        if(author!=null)
        tv_author.setText("Author:"+" "+author);
        Glide.with(this).load(banner_image).apply(
                new RequestOptions().placeholder(R.drawable.ic_news_placeholder_small)
                        .error(R.drawable.ic_news_placeholder_small)).into(image);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
              finish();
            return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private String convertDate(String dateInput){
        String  formattedDate  = "";
        DateFormat readFormat  = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        DateFormat writeFormat  = new SimpleDateFormat("MMM d", Locale.ENGLISH);
        Date date  = null;
        try {
            date = readFormat.parse(dateInput);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (date != null) {
            formattedDate = writeFormat.format(date);
        }
        return formattedDate;
    }
}
