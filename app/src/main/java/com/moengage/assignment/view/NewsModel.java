package com.moengage.assignment.view;

public class NewsModel {

    public static final String TABLE_NAME = "stories";
    public static final String TABLE_NAME1="headlines";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_SOURCE = "source";
    public static final String COLUMN_AUTHOR = "author";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_URL = "url";
    public static final String COLUMN_URLTOIMAGE = "urlToImage";
    public static final String COLUMN_PUBLISHEDAT = "publishedAt";
    public static final String COLUMN_CONTENT = "content";

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_SOURCE + " TEXT,"
                    + COLUMN_AUTHOR + " TEXT,"
                    + COLUMN_DESCRIPTION + " TEXT,"
                    + COLUMN_CONTENT + " TEXT,"
                    + COLUMN_TITLE + " TEXT,"
                    + COLUMN_URL + " TEXT,"
                    + COLUMN_URLTOIMAGE + " TEXT,"
                    + COLUMN_PUBLISHEDAT + " TEXT"
                    + ")";

    public static final String CREATE_TABLE_Headlines =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME1 + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_SOURCE + " TEXT,"
                    + COLUMN_AUTHOR + " TEXT,"
                    + COLUMN_DESCRIPTION + " TEXT,"
                    + COLUMN_CONTENT + " TEXT,"
                    + COLUMN_TITLE + " TEXT,"
                    + COLUMN_URL + " TEXT,"
                    + COLUMN_URLTOIMAGE + " TEXT,"
                    + COLUMN_PUBLISHEDAT + " TEXT"
                    + ")";
    public String sourceName;
    public String author;
    public String title;
    public String description;
    public String url;
    public String urlToImage;
    public String publishedAt;
    public String content;


    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceNname) {
        this.sourceName = sourceNname;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }



}
