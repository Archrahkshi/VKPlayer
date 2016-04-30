package com.firekost.app.VKPlayer;

import java.util.Locale;

public class ObjectItem {

    private String title;
    private String artist;
    private int duration;
    private boolean downloaded;

    public ObjectItem(String title, String artist, int duration, boolean downloaded) {
        this.title = title;
        this.artist = artist;
        this.duration = duration;
        this.downloaded = downloaded;
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

    public String getDuration() {
        return String.format(Locale.ENGLISH, "%2d:%02d", duration/60, duration%60);
    }

    public void setTime(int duration) {
        this.duration = duration;
    }

    public boolean getDownloaded(){
        return downloaded;
    }

    public void setDownloaded(boolean downloaded){
        this.downloaded = downloaded;
    }
}