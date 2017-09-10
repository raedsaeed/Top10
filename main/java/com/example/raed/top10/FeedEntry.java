package com.example.raed.top10;

import java.io.Serializable;

/**
 * Created by Raed on 08/08/2017.
 */

public class FeedEntry implements Serializable{
    private static final String TAG = "FeedEntry";
    private static final long serialVersionUID = 1L;
    private String title ;
    private String summary;
    private String artist;
    private String image;

    public FeedEntry(String title, String summary, String artist, String image) {
        this.title = title;
        this.summary = summary;
        this.artist = artist;
        this.image = image;
    }

    public FeedEntry() {
    }

    public String getArtist() {
        return artist;
    }

    public String getImage() {
        return image;
    }

    public String getSummary() {
        return summary;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "FeedEntry{" +
                "title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", artist='" + artist + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
