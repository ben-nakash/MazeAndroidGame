package com.example.ben.mazegame;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class AppEntryBestScoresDBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "AppTimeBestScores.db";

    public AppEntryBestScoresDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // create the table needed to save the scores
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + AppEntryBestScoresContract.AppEntryBestScores.TABLE_NAME + "(" +
                        AppEntryBestScoresContract.AppEntryBestScores.BEST_SCORE + " INTEGER, " +
                        AppEntryBestScoresContract.AppEntryBestScores.LEVEL + " INTEGER, " +
                        "PRIMARY KEY(" + AppEntryBestScoresContract.AppEntryBestScores.LEVEL + "));"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
