package com.example.admin.music.model;

import com.example.admin.music.model.entity.Song;
import com.example.admin.music.presenter.lyrics.LyricsPresenterListener;
import com.example.admin.music.view.main.MainActivity;

import java.util.ArrayList;

/**
 * Created by admin on 1/13/2019.
 */

public class LyricsModel {
    private LyricsPresenterListener callBack;
    private ArrayList<Song> listLyrics;

    private final String file_lyrics = "lyrics";

    public LyricsModel(LyricsPresenterListener callBack) {
        this.callBack = callBack;

        //init
        listLyrics = MainActivity.listLyrics;
    }

    public void getLyrics(Song song) {
        for (Song s : listLyrics) {
            if (song.getName().equals(s.getName()) && song.getSinger().equals(s.getSinger())) {
                song.setPathLyrics(s.getPathLyrics());

                callBack.show(song);
                break;
            }
        }
    }
}
