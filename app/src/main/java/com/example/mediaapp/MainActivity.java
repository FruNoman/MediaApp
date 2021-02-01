package com.example.mediaapp;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.chibde.visualizer.SquareBarVisualizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public Uri uri;
    public  SquareBarVisualizer squareBarVisualizer;
    public Button play;
    public Button stop;
    public Button next;
    public TextView trackName;
    public int nowPlaying = 0;
    public List<Song> songs = new ArrayList<>();
   public  int seekLength = 0;

    public   MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat
                .requestPermissions(
                        MainActivity.this,
                        new String[]{
                                Manifest.permission.BLUETOOTH,
                                Manifest.permission.BLUETOOTH_ADMIN,
                                Manifest.permission.MODIFY_AUDIO_SETTINGS,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.RECORD_AUDIO
                        },
                        1);

        uri = Uri.parse("/storage/emulated/0/samples/audio/A_000001_02_Tetanus.mp3");
        squareBarVisualizer = findViewById(R.id.visualizer);
        play = findViewById(R.id.play);
        stop = findViewById(R.id.stop);
        next = findViewById(R.id.next);
        trackName = findViewById(R.id.track);

        setSongsList();

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(this,uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    playSong(nowPlaying);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               pauseSong();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    nextSong();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

// set custom color to the line.
        squareBarVisualizer.setColor(ContextCompat.getColor(this, R.color.colorAccent));

// define custom number of bars you want in the visualizer between (10 - 256).
        squareBarVisualizer.setDensity(65);

// Set Spacing
        squareBarVisualizer.setGap(2);


        startService(new Intent(this, MediaPlaybackService.class));

// Set your media player to the visualizer.
    }



    void setSongsList(){

        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        String sortOrder = MediaStore.MediaColumns.DISPLAY_NAME+"";

        String[] projection = {
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION
        };


        Cursor cursor = getApplicationContext().getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                sortOrder);

        while(cursor.moveToNext()){
            songs.add(new Song(cursor.getString(0),cursor.getString(1),cursor.getString(2),Long.parseLong(cursor.getString(3))));
        }
    }

    void playSong(int index) throws Exception {
        if(index != nowPlaying){
            seekLength = 0;
        }
        nowPlaying = index;
        playSong();
    }

    void playSong() throws Exception {
        mediaPlayer.reset();
        Uri path =       Uri.parse("/storage/emulated/0/samples/audio/A_000001_02_Tetanus.mp3");

        mediaPlayer.setDataSource(String.valueOf(path));
        mediaPlayer.prepare();
        mediaPlayer.seekTo(seekLength);
        mediaPlayer.start();
        trackName.setText(songs.get(nowPlaying).getTitle());
        squareBarVisualizer.setPlayer(mediaPlayer.getAudioSessionId());

    }

    void pauseSong(){
        mediaPlayer.pause();
        seekLength = mediaPlayer.getCurrentPosition();
    }


    void nextSong() throws Exception {
        nowPlaying = nowPlaying+1;
        if (nowPlaying == songs.size()){
            nowPlaying = 0;
        }
        seekLength = 0;
        if(mediaPlayer.isPlaying()){
            playSong();
        }
    }
}