package com.example.ben.mazegame;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.content.SharedPreferences;

public class LevelSelectionActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnTouchListener{

    private Button btnLevel1, btnLevel2, btnLevel3;
    private Maze maze;
    private boolean backgroundMusic;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get the music player preference from the user and play or not play the background music
        prefs = getSharedPreferences("settings", MODE_PRIVATE);
        editor = prefs.edit();
        backgroundMusic = prefs.getBoolean("bgMusic", true);

        setContentView(R.layout.activity_level_selection);

        // Set the game to full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Setting the orientation of the screen
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // Connecting components
        btnLevel1 = (Button)findViewById(R.id.btn_level_one);
        btnLevel2 = (Button)findViewById(R.id.btn_level_two);
        btnLevel3 = (Button)findViewById(R.id.btn_level_three);

        // Set listeners
        btnLevel1.setOnClickListener(this);
        btnLevel2.setOnClickListener(this);
        btnLevel3.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        // start the correct level according to the level number button pressed
        if (v == btnLevel1) {
            startLevel(getResources().getInteger(R.integer.level_1));
        }
        else if (v == btnLevel2) {
            startLevel(getResources().getInteger(R.integer.level_2));
        }
        else if (v == btnLevel3) {
            startLevel(getResources().getInteger(R.integer.level_3));
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }

    // start the game activity with the correct maze level
    private void startLevel(int level) {
        Intent game = new Intent(LevelSelectionActivity.this,GameActivity.class);
        maze = new Maze(this, level);
        game.putExtra("maze", maze);
        startActivity(game);
    }

    // when onPause() don't continue to play the music
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
