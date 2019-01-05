package com.example.admin.music.view.main;

import com.example.admin.music.model.entity.Playlist;
import com.example.admin.music.model.entity.Singer;
import com.example.admin.music.model.entity.Song;

import java.util.ArrayList;

/**
 * Created by admin on 1/5/2019.
 */

public interface MainListener {
    public void show(ArrayList<Song> listSong, ArrayList<Song> listFavorite, ArrayList<Singer> listSinger, ArrayList<Playlist> listPlaylist);
}
