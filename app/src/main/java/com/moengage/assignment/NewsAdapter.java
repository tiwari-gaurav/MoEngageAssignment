package com.moengage.assignment;

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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    ArrayList<NewsModel>headlinesList;
    private Context mContext;

    public NewsAdapter(Context context){
    this.mContext=context;
    headlinesList=new ArrayList<>();
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
        holder.tv_title.setText(headlinesList.get(position).title);
        holder.tv_desc.setText(headlinesList.get(position).content);
        holder.tv_source.setText(headlinesList.get(position).sourceName);
        holder.tv_published.setText(convertDate(headlinesList.get(position).publishedAt));
        holder.tv_author.setText("Author:"+ " "+headlinesList.get(position).author);
        Glide.with(mContext).load(headlinesList.get(position).urlToImage).apply(
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
                                shareStories(headlinesList.get(position).url);
                                return true;
                            case R.id.menu2:
                                //handle menu2 click
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
                        createDialog(mContext,headlinesList.get(position).url);
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
