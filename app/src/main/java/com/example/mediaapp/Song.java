package com.example.mediaapp;

import android.provider.MediaStore;

public class Song {
    private String artist;
    private String title;
    private String data;
    private long duration;

    public Song(String artist, String title, String data, long duration) {
        this.artist = artist;
        this.title = title;
        this.data = data;
        this.duration = duration;
    }

    public String getData(){
        return this.data;
    }

    public String getTitle() {
        return title;
    }
}
