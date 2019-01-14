package com.example.admin.music.model;

import android.content.Context;

import com.example.admin.music.model.entity.Song;
import com.example.admin.music.presenter.lyrics.LyricsPresenterListener;
import com.example.admin.music.view.main.MainActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
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

    public void removeLyrics(Context context, Song song) {
        for (int i = 0 ; i < listLyrics.size() ; i++) {
            Song s = listLyrics.get(i);
            if (song.getName().equals(s.getName()) && song.getSinger().equals(s.getSinger())) {
                listLyrics.remove(i);
                writeLyrics(context);
                break;
            }
        }
    }

    private void writeLyrics(Context context) {
        File file = new File(context.getFilesDir(), file_lyrics);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(listLyrics);

            //update list favorite in activity main
            MainActivity.listLyrics = listLyrics;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
