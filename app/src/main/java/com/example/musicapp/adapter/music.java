package com.example.musicapp.adapter;

import android.media.MediaMetadataRetriever;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class music {
    private String path;
    private String title;
    private String artist;
    private String album;
    private Long duration;

    public music(String path, String title, String artist, String album, Long duration) {
        this.path = path;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public static byte[] getAlbumArt(String uri){
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art = retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }
    public static String formatDuration(Long duration) {
        long minutes = TimeUnit.MINUTES.convert(duration,TimeUnit.MILLISECONDS);
        long second = (TimeUnit.SECONDS.convert(duration,TimeUnit.MILLISECONDS) - minutes*TimeUnit.SECONDS.convert(1,TimeUnit.MINUTES));
        return String.format("%d:%d",minutes,second);
    }
}
