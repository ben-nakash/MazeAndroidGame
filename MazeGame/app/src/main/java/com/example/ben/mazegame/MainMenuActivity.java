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

public class MainMenuActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnTouchListener{

    private Button btnNewGame, btnExitGame, btnSound;
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

        setContentView(R.layout.activity_main_menu);

        // Set the game to full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Setting the orientation of the screen
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // Connecting the components of the program
        btnNewGame = (Button)findViewById(R.id.btn_new_game);
        btnExitGame = (Button)findViewById(R.id.btn_exit_game);
        btnSound = (Button)findViewById(R.id.btn_sound);

        // Connecting the listeners
        btnNewGame.setOnClickListener(this);
        btnExitGame.setOnClickListener(this);
        btnSound.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        // start the level selection activity
        if (v == btnNewGame) {
            Intent levelSelection = new Intent(v.getContext(), LevelSelectionActivity.class);
            startActivity(levelSelection);
        }
        // exit the game
        else if (v == btnExitGame) {
            finish();
        }
        // stop or start the music and save it to preferences
        else if (v == btnSound) {
            backgroundMusic = !backgroundMusic;
            editor.putBoolean("bgMusic", backgroundMusic);
            editor.commit();
            MusicManager.start(this, backgroundMusic);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
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
        MusicManager.start(this, backgroundMusic);
    }
}
