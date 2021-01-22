package com.example.mediaapp;

import android.Manifest;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.chibde.visualizer.SquareBarVisualizer;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    public Uri uri;
    public  SquareBarVisualizer squareBarVisualizer;
    public Button play;
    public   MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat
                .requestPermissions(
                        MainActivity.this,
                        new String[]{

                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.RECORD_AUDIO
                        },
                        1);

        uri = Uri.parse("/storage/emulated/10/samples/audio/A_000001_02_Tetanus.mp3");
        squareBarVisualizer = findViewById(R.id.visualizer);
        play = findViewById(R.id.play);
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(this,uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    squareBarVisualizer.setPlayer(mediaPlayer.getAudioSessionId());
                } catch (IOException e) {
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

// Set your media player to the visualizer.
    }
}