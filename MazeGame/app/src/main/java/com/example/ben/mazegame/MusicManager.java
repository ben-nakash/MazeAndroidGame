package com.example.ben.mazegame;

import android.content.Context;
import android.media.MediaPlayer;

public class MusicManager {
    private static MediaPlayer mediaPlayer;

    // start the music if playMusic is true, else stop or not start the music
    public static void start(Context context, boolean playMusic) {
        // pause the music
        if (!playMusic) {
            pause();
            return;
        }
        // play the music
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                mediaPlayer.start();
        } else {
            mediaPlayer = MediaPlayer.create(context, R.raw.game_soundtrack);
        }
        // set the music to loop indefinitely
        if (mediaPlayer != null) {
            try {
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
            } catch (Exception e) { }
        }
    }

    // pause the music
    public static void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }
}