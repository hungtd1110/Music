package com.example.admin.music.presenter.main;

import android.content.Context;

import com.example.admin.music.model.entity.Playlist;
import com.example.admin.music.model.entity.Singer;
import com.example.admin.music.model.entity.Song;

import java.util.ArrayList;

/**
 * Created by admin on 1/5/2019.
 */

public interface MainPresenterListener {
    public void getData(Context context);

    public void show(ArrayList<Song> listSong, ArrayList<Song> listFavorite, ArrayList<Singer> listSinger, ArrayList<Playlist> listPlaylist);
}
