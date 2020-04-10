package com.moengage.assignment.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.moengage.assignment.R;
import com.moengage.assignment.database.DatabaseHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    ArrayList<NewsModel>headlinesList;
    private Context mContext;
    private DatabaseHelper db;
    private String TAG="";

    public NewsAdapter(Context context, String main_activity, DatabaseHelper db){
    this.mContext=context;
    headlinesList=new ArrayList<>();
    this.db =db;
    TAG=main_activity;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new NewsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final NewsViewHolder holder, final int position) {
        if(TAG.equalsIgnoreCase("stories"))
            holder.tv_option_menu.setVisibility(View.GONE);
        else
            holder.tv_option_menu.setVisibility(View.VISIBLE);
        holder.tv_title.setText(headlinesList.get(position).getTitle());
        holder.tv_desc.setText(headlinesList.get(position).getContent());
        holder.tv_source.setText(headlinesList.get(position).getSourceName());
        holder.tv_published.setText(convertDate(headlinesList.get(position).getPublishedAt()));
        holder.tv_author.setText("Author:"+ " "+headlinesList.get(position).getAuthor());
        Glide.with(mContext).load(headlinesList.get(position).getUrlToImage()).apply(
               new RequestOptions().placeholder(R.drawable.ic_news_placeholder_small)
                        .error(R.drawable.ic_news_placeholder_small)).into(holder.iv_thumbnail);
        holder.tv_option_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //creating a popup menu
                PopupMenu popup = new PopupMenu(mContext, holder.tv_option_menu);
                //inflating menu from xml resource
                popup.inflate(R.menu.menu_options);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu1:
                                shareStories(headlinesList.get(position).getUrl());
                                return true;
                            case R.id.menu2:
                                //handle menu2 click
                                db.insertNote(headlinesList.get(position));
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                //displaying the popup
                popup.show();

            }
        });
        holder.parentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TAG.equalsIgnoreCase("stories")){
                    Intent intent = new Intent(mContext,StoryDetailActivity.class);
                    intent.putExtra("title",headlinesList.get(position).title);
                    intent.putExtra("description",headlinesList.get(position).description);
                    intent.putExtra("author",headlinesList.get(position).author);
                    intent.putExtra("content",headlinesList.get(position).content);
                    intent.putExtra("publishedAt",headlinesList.get(position).publishedAt);
                    intent.putExtra("source",headlinesList.get(position).sourceName);
                    intent.putExtra("banner_image",headlinesList.get(position).urlToImage);

                    mContext.startActivity(intent);
                }else
                        createDialog(mContext,headlinesList.get(position).getUrl());
            }
        });

    }

    private void shareStories(String url) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, url);
        mContext.startActivity(Intent.createChooser(shareIntent, "Share stories using"));
    }

    @Override
    public int getItemCount() {
        return headlinesList.size();
    }

    public void updateList(ArrayList<NewsModel> list){
        this.headlinesList=list;
        notifyDataSetChanged();
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

    private void createDialog(Context context, String url) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        WebView wv = new WebView(context);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setDomStorageEnabled(true);
        wv.loadUrl(url);
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);

                return true;
            }
        });

        alert.setView(wv);
        alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = alert.create();
        alertDialog.show();
    }
    public class NewsViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_title,tv_desc,tv_author,tv_published,tv_source,tv_option_menu;
        private ImageView iv_thumbnail;
        private View parentView;
        public NewsViewHolder(View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.title);
            tv_desc=itemView.findViewById(R.id.desc);
            tv_author=itemView.findViewById(R.id.site);
            tv_published=itemView.findViewById(R.id.updatedAt);
            tv_source=itemView.findViewById(R.id.category);
            iv_thumbnail=itemView.findViewById(R.id.small_img);
            tv_option_menu=itemView.findViewById(R.id.textViewOptions);
            parentView= itemView.findViewById(R.id.parent_small);

        }
    }


}
