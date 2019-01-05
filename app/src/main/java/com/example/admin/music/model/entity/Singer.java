package com.example.admin.music.model.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by admin on 11/6/2018.
 */

public class Singer implements Serializable {
    private String name;
    private ArrayList<Song> listSong;

    public Singer() {
        this.name = "";
        this.listSong = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Song> getListSong() {
        return listSong;
    }

    public void setListSong(ArrayList<Song> listSong) {
        this.listSong = listSong;
    }
}
