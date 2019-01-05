package com.example.admin.music.presenter.main;

import android.content.Context;

import com.example.admin.music.model.MainModel;
import com.example.admin.music.model.entity.Playlist;
import com.example.admin.music.model.entity.Singer;
import com.example.admin.music.model.entity.Song;
import com.example.admin.music.view.main.MainListener;

import java.util.ArrayList;

/**
 * Created by admin on 1/5/2019.
 */

public class MainPresenter implements MainPresenterListener {
    private MainListener callBack;
    private MainModel model = new MainModel(this);

    public MainPresenter(MainListener callBack) {
        this.callBack = callBack;
    }

    @Override
    public void getData(Context context) {
        model.getData(context);
    }

    @Override
    public void show(ArrayList<Song> listSong, ArrayList<Song> listFavorite, ArrayList<Singer> listSinger, ArrayList<Playlist> listPlaylist) {
        callBack.show(listSong, listFavorite, listSinger, listPlaylist);
    }
}
