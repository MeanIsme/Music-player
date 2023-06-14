package com.mobile.MusicApp;

import java.io.Serializable;

public class MusicModel implements Serializable {
    private String name;
    private String url;
    private String album;
    private String thumbUrl;
    private long duration;

    public MusicModel(String name, String url, String album, String thumbUrl, long duration) {
        this.name = name;
        this.url = url;
        this.album = album;
        this.thumbUrl = thumbUrl;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getthumbUrl() {
        return thumbUrl;
    }

    public void setthumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
