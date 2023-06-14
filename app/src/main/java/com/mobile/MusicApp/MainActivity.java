package com.mobile.MusicApp;

import static com.mobile.MusicApp.MusicPlayerService.ACTION_LOAD_MUSIC_DONE;
import static com.mobile.MusicApp.MusicPlayerService.ACTION_PLAY_MUSIC;
import static com.mobile.MusicApp.MusicPlayerService.ACTION_START_LOAD_MUSIC_DONE;
import static com.mobile.MusicApp.MusicPlayerService.KEY_IS_PLAYING_MUSIC;
import static com.mobile.MusicApp.MusicPlayerService.UPDATE_STATUS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_WRITE_STORAGE = 1;
    public static final String MINI_CONTROLLER_TAG = "MINI_CONTROL_TAG";
    private MusicAdapter adapter;
    private ProgressBar progressBar;
    private MusicsDoneReciver reciver = new MusicsDoneReciver();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupView();
        checkPermission();
        registerReciver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReciver();
    }

    private void registerReciver(){
        IntentFilter intentFilter = new IntentFilter(ACTION_LOAD_MUSIC_DONE);
        intentFilter.addAction(UPDATE_STATUS);
        registerReceiver(reciver,intentFilter);
    }
    private void unregisterReciver(){
        unregisterReceiver(reciver);
    }
    private void setupView() {
        progressBar = findViewById(R.id.pbBar);
        RecyclerView recyclerView = findViewById(R.id.rvMusicList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MusicAdapter(new MusicAdapter.Callback() {
            @Override
            public void onClickItem(int position) {
                onMusicClick(position);
            }
        });
        recyclerView.setAdapter(adapter);
    }
    private boolean hasStoragePermission() {
        return (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    private void checkPermission() {
        boolean hasPermission = hasStoragePermission();
        if (hasPermission) {
            getAllSongs();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE);
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_STORAGE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getAllSongs();
        }
    }


    private void getAllSongs() {
        progressBar.setVisibility(View.VISIBLE);
        Intent intent = new Intent(this, MusicPlayerService.class);
        intent.setAction(ACTION_START_LOAD_MUSIC_DONE);
        startService(intent);



    }
    public void onMusicClick(int position){
        Intent intent = new Intent(this, MusicPlayerService.class);
        intent.setAction(ACTION_PLAY_MUSIC);
        intent.putExtra(MusicPlayerService.MUSIC_POISTION, position);
        startService(intent);
    }

    class MusicsDoneReciver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction() == ACTION_LOAD_MUSIC_DONE){
                progressBar.setVisibility(View.GONE);
                adapter.setData(MusicPlayerService.musicModels);
            }
            else
            {
                Bundle bundle = new Bundle();
                boolean isPlaying = intent.getBooleanExtra(KEY_IS_PLAYING_MUSIC, false);
                if(isPlaying){
                    addFragmentIfNeed();
                }
                else {
                    removeFragmentIfNeed();
                }
            }
        }
    }
    private void addFragmentIfNeed(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentByTag(MINI_CONTROLLER_TAG)==null){
            MusicControlFragment fragment = MusicControlFragment.newInstance(true);
            fragmentManager.beginTransaction().add(R.id.smallController,fragment, MINI_CONTROLLER_TAG).commit();

        }
    }

    private void removeFragmentIfNeed(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(MINI_CONTROLLER_TAG);
        if (fragment!=null){

            fragmentManager.beginTransaction().remove(fragment);

        }
    }
}