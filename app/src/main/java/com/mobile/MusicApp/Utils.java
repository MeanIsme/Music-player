package com.mobile.MusicApp;

import android.os.Environment;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Utils {
    public static List<MusicModel> getAllSongs() {

//        Uri allsongsuri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
//
        List<MusicModel> data = new ArrayList<>();
//        Cursor cursor = getContentResolver().query(allsongsuri,null,null,null, selection);
//
//
//
//
//
//        if (cursor != null) {
//            if (cursor.moveToFirst()) {
//                do {
//                    @SuppressLint("Range") String song_name = cursor
//                            .getString(cursor
//                                    .getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
//                    @SuppressLint("Range") int song_id = cursor.getInt(cursor
//                            .getColumnIndex(MediaStore.Audio.Media._ID));
//
//                    @SuppressLint("Range") String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
//                    @SuppressLint("Range") String Duration = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
//                    if(Duration != null){
//                        MusicModel music = new MusicModel(song_name, url, null, null, Long.valueOf(Duration));
//                        data.add(music);
//                    }
//
//                    Log.d("tag", "Song Name ::"+song_name+" Song Id :"+song_id+" url ::"+url+" Duration ::"+Duration);
//
//                } while (cursor.moveToNext());
//
//            }
//            cursor.close();
//
//
//
//
//
        ArrayList<HashMap<String,String>> songList= getPlayList(Environment.getExternalStorageDirectory().getAbsolutePath() );
        if(songList!=null){
            for(int i=0;i<songList.size();i++){
                String fileName=songList.get(i).get("file_name");
                String filePath=songList.get(i).get("file_path");
                //here you will get list of file name and file path that present in your device
                Log.e("file details "," name ="+fileName +" path = "+filePath);
                MusicModel music = new MusicModel(fileName, filePath, null, null, 0);
                data.add(music);
            }
        }
        return data;


    }
    private static ArrayList<HashMap<String,String>> getPlayList(String rootPath) {
        ArrayList<HashMap<String,String>> fileList = new ArrayList<>();


        try {
            File rootFolder = new File(rootPath);
            File[] files = rootFolder.listFiles(); //here you will get NPE if directory doesn't contains  any file,handle it like this.
            for (File file : files) {
                if (file.isDirectory()) {
                    if (getPlayList(file.getAbsolutePath()) != null) {
                        fileList.addAll(getPlayList(file.getAbsolutePath()));
                    } else {
                        break;
                    }
                } else if (file.getName().endsWith(".mp3")) {
                    HashMap<String, String> song = new HashMap<>();
                    song.put("file_path", file.getAbsolutePath());
                    song.put("file_name", file.getName());
                    fileList.add(song);
                }
            }
            return fileList;
        } catch (Exception e) {
            return null;
        }
    }
}
