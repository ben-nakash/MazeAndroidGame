package com.example.ben.mazegame;

import android.provider.BaseColumns;


public class AppEntryBestScoresContract {
    public AppEntryBestScoresContract() {

    }

    public static abstract class AppEntryBestScores implements BaseColumns {
        public static final String TABLE_NAME = "scores";
        public static final String BEST_SCORE = "best_score";
        public static final String LEVEL = "level";
    }
}
