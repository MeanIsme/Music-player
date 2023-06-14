package com.mobile.MusicApp;

import android.app.Service;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MusicPlayerService extends Service {
    public static final String MUSIC_URL_KEY = "Url_key";
    public static final String ACTION_LOAD_MUSIC_DONE = "com.mobile.MusicApp.load_music_done";
    public static final String ACTION_START_LOAD_MUSIC_DONE = "com.mobile.MusicApp.start_load_music_done";
    public static final String ACTION_PLAY_MUSIC = "com.mobile.MusicApp.play_music";
    public static final String MUSIC_POISTION = "com.mobile.MusicApp.position";
    public static final String UPDATE_STATUS = "update_status";
    public static final String KEY_IS_PLAYING_MUSIC = "is_playing";
    public static final String KEY_PLAYING_SONG = "playing_song";


    public static List<MusicModel> musicModels = new ArrayList<>();
    MediaPlayer mediaPlayer = new MediaPlayer();
    private int interval = 5000; // 5 seconds by default, can be changed later
    private Handler handler;

    private MusicModel currentMusic;

    public MusicPlayerService() {
    }


    Runnable statusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                updateStatus(); //this function can change value of mInterval.
            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
                handler.postDelayed(statusChecker, interval);
            }
        }
    };
    private void startRepeatingTask() {
        statusChecker.run();
    }
    private void stopRepeatingTask() {
        handler.removeCallbacks(statusChecker);
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer.setAudioAttributes(
                new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
        );
        handler = new Handler();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        switch (action){
            case ACTION_START_LOAD_MUSIC_DONE:{
                loadMusics();
                break;
            }
            case ACTION_PLAY_MUSIC:{
                int position = intent.getIntExtra(MUSIC_POISTION, 0);

                playmusic(position);
                break;
            }
            default:{
                break;
            }

        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void playmusic(int position) {
        try {
            mediaPlayer.stop();
            mediaPlayer.reset();
            MusicModel model = MusicPlayerService.musicModels.get(position);
            // Set new data source and prepare for playback
            mediaPlayer.setDataSource(model.getUrl());
            mediaPlayer.prepare();
            mediaPlayer.start();
            startRepeatingTask();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }








    private void loadMusics() {
        List<MusicModel> musicModels = Utils.getAllSongs();
        MusicPlayerService.musicModels.addAll(musicModels);
        Intent intent = new Intent(ACTION_LOAD_MUSIC_DONE);
        sendBroadcast(intent);
    }
    private void updateStatus(){
        Intent intent = new Intent(UPDATE_STATUS);
        Bundle bundle = new Bundle();


        bundle.putSerializable(KEY_PLAYING_SONG, currentMusic);
        bundle.putBoolean(KEY_IS_PLAYING_MUSIC, mediaPlayer.isPlaying());
        intent.putExtras(bundle);
        sendBroadcast(intent);

    }

}