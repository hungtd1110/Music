package com.example.admin.music.presenter.detail_song;

import android.content.Context;

import com.example.admin.music.model.DetailSongModel;
import com.example.admin.music.model.entity.Song;
import com.example.admin.music.view.detail_song.DetailSongViewListener;

/**
 * Created by admin on 1/6/2019.
 */

public class DetailSongPresenter implements DetailSongPresenterListener {
    private DetailSongViewListener callBack;
    private DetailSongModel model;

    public DetailSongPresenter(DetailSongViewListener callBack) {
        this.callBack = callBack;

        //init
        model = new DetailSongModel(this);
    }

    @Override
    public void getData(Context context, Song song) {
        model.getData(context, song);
    }

    @Override
    public void show(boolean favorite) {
        callBack.show(favorite);
    }

    @Override
    public void saveData(Context context, Song song, boolean favorite) {
        model.saveData(context, song, favorite);
    }
}
