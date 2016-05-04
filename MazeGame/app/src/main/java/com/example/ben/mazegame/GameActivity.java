package com.example.ben.mazegame;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.WindowManager;


public class GameActivity extends Activity{

    private Bundle extras;
    private Maze maze;
    private GameView gameView;
    private boolean backgroundMusic;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        // get the music player preference from the user and play or not play the background music
        prefs = getSharedPreferences("settings", MODE_PRIVATE);
        editor = prefs.edit();
        backgroundMusic = prefs.getBoolean("bgMusic", true);

        // Set the game to full screen and keep the screen on
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Setting the orientation of the screen
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // Gets the maze in order to show it in this intent.
        extras = getIntent().getExtras();
        maze = (Maze)extras.get("maze");
        gameView = new GameView(this,maze);
        setContentView(gameView);
    }

    // when obPause() don't continue to play the music
    @Override
    public void onPause() {
        super.onPause();
        MusicManager.start(this, false);
    }

    // when onResume() check the user preference and play or not play the music
    @Override
    public void onResume() {
        super.onResume();
        MusicManager.start(this, prefs.getBoolean("bgMusic", true));
    }
}
