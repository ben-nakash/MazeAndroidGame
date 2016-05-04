package com.example.ben.mazegame;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class AppEntryBestScoresDAL {
    private AppEntryBestScoresDBHelper helper;

    // constructor
    public AppEntryBestScoresDAL(Context context) {
        helper = new AppEntryBestScoresDBHelper(context);
    }

    // add best score - if there is no best score yet in the DB so add a new row, else
    // override this row with the new best score
    public void addBestScoreEntry(long score, int level) {
        long bestScore = getBestScore(level);

        if (bestScore == 0) {
            addBestScore(score, level);
        }
        else {
            updateBestScore(score, level);
        }
    }

    // get the best score according to the level form the DB
    public long getBestScore(int level) {
        long bestScore = 0;

        SQLiteDatabase db = helper.getReadableDatabase();

        String[] selectionArgs = { level + "" };
        Cursor cursor = db.rawQuery("SELECT " +
                AppEntryBestScoresContract.AppEntryBestScores.BEST_SCORE + " FROM " +
                AppEntryBestScoresContract.AppEntryBestScores.TABLE_NAME + " WHERE " +
                AppEntryBestScoresContract.AppEntryBestScores.LEVEL + " = ? ", selectionArgs);

        while (cursor.moveToNext()) {
            int bestScoreIndex = cursor.getColumnIndex
                    (AppEntryBestScoresContract.AppEntryBestScores.BEST_SCORE);
            bestScore = cursor.getLong(bestScoreIndex);
        }

        db.close();

        return bestScore;
    }

    // update the existing best score to the new score
    private void updateBestScore(long score, int level) {
        SQLiteDatabase db = helper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(AppEntryBestScoresContract.AppEntryBestScores.BEST_SCORE, score);

        String where = AppEntryBestScoresContract.AppEntryBestScores.LEVEL + " = ?";
        String[] whereArgs = { level + ""};

        db.update(AppEntryBestScoresContract.AppEntryBestScores.TABLE_NAME,
                values, where, whereArgs);

        db.close();
    }

    // add a new best score row to the DB
    private void addBestScore(long score, int level) {
        //get DB
        SQLiteDatabase db = helper.getWritableDatabase();

        //values to save
        ContentValues values = new ContentValues();
        values.put(AppEntryBestScoresContract.AppEntryBestScores.BEST_SCORE, score);
        values.put(AppEntryBestScoresContract.AppEntryBestScores.LEVEL, level);

        //save the values
        db.insert(AppEntryBestScoresContract.AppEntryBestScores.TABLE_NAME, null, values);
        db.close();
    }
}
