package com.example.admin.music.presenter.search;

import android.content.Context;

import com.example.admin.music.model.entity.Playlist;
import com.example.admin.music.model.entity.Singer;
import com.example.admin.music.model.entity.Song;

import java.util.ArrayList;

/**
 * Created by admin on 1/10/2019.
 */

public interface SearchPresenterListener {
    public void getHistory(Context context);

    public void showHistory(ArrayList<String> list);

    public void search(String word);

    public void show(ArrayList<Song> listSong, ArrayList<Singer> listSinger, ArrayList<Playlist> listPlaylist);

    public void delete(Context context, String word);

    public void updateHistory(ArrayList<String> list);

    public void deleteALl(Context context);
}
