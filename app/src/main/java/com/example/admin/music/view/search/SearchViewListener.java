package com.example.admin.music.view.search;

import android.content.Context;

import com.example.admin.music.model.entity.Playlist;
import com.example.admin.music.model.entity.Singer;
import com.example.admin.music.model.entity.Song;

import java.util.ArrayList;

/**
 * Created by admin on 1/10/2019.
 */

public interface SearchViewListener {
    public void showHistory(ArrayList<String> list);

    public void show(ArrayList<Song> listSong, ArrayList<Singer> listSinger, ArrayList<Playlist> listPlaylist);

    public void delete(Context context, String word);

    public void updateHistory(ArrayList<String> list);
}
