package com.moengage.assignment.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.moengage.assignment.view.NewsModel;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "news_db";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
// create notes table
        db.execSQL(NewsModel.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + NewsModel.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public void insertNote(NewsModel data) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(NewsModel.COLUMN_SOURCE, data.getSourceName());
        values.put(NewsModel.COLUMN_AUTHOR, data.getAuthor());
        values.put(NewsModel.COLUMN_CONTENT, data.getContent());
        values.put(NewsModel.COLUMN_DESCRIPTION, data.getDescription());
        values.put(NewsModel.COLUMN_PUBLISHEDAT, data.getPublishedAt());
        values.put(NewsModel.COLUMN_TITLE, data.getTitle());
        values.put(NewsModel.COLUMN_URL, data.getUrl());
        values.put(NewsModel.COLUMN_URLTOIMAGE, data.getUrlToImage());
        // insert row
        long id = db.insert(NewsModel.TABLE_NAME, null, values);

        // close db connection
        db.close();
    }

    public ArrayList<NewsModel> getAllStories() {
        ArrayList<NewsModel> notes = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + NewsModel.TABLE_NAME + " ORDER BY " +
                NewsModel.COLUMN_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                NewsModel note = new NewsModel();
                note.setAuthor(cursor.getString(cursor.getColumnIndex(NewsModel.COLUMN_AUTHOR)));
                note.setTitle(cursor.getString(cursor.getColumnIndex(NewsModel.COLUMN_TITLE)));
                note.setDescription(cursor.getString(cursor.getColumnIndex(NewsModel.COLUMN_DESCRIPTION)));
                note.setContent(cursor.getString(cursor.getColumnIndex(NewsModel.COLUMN_CONTENT)));
                note.setSourceName(cursor.getString(cursor.getColumnIndex(NewsModel.COLUMN_SOURCE)));
                note.setPublishedAt(cursor.getString(cursor.getColumnIndex(NewsModel.COLUMN_PUBLISHEDAT)));
                note.setUrl(cursor.getString(cursor.getColumnIndex(NewsModel.COLUMN_URL)));
                note.setUrlToImage(cursor.getString(cursor.getColumnIndex(NewsModel.COLUMN_URLTOIMAGE)));

                notes.add(note);
            } while (cursor.moveToNext());
        }
        // close db connection
        db.close();

        // return notes list
        return notes;
    }
    }
