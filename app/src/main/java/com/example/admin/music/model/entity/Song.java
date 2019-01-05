package com.example.admin.music.model.entity;

import java.io.Serializable;

/**
 * Created by admin on 8/13/2018.
 */

public class Song implements Serializable{
    private String name;
    private String singer;
    private String pathAudio;
    private String pathLyrics;

    public Song() {
        this.name = "";
        this.singer = "";
        this.pathAudio = "";
        this.pathLyrics = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getPathAudio() {
        return pathAudio;
    }

    public void setPathAudio(String pathAudio) {
        this.pathAudio = pathAudio;
    }

    public String getPathLyrics() {
        return pathLyrics;
    }

    public void setPathLyrics(String pathLyrics) {
        this.pathLyrics = pathLyrics;
    }
}
