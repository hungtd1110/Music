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
    public void getFavorite(Context context, Song song) {
        model.getFavorite(context, song);
    }

    @Override
    public void showFavorite(boolean favorite) {
        callBack.showFavorite(favorite);
    }

    @Override
    public void saveFavorite(Context context, Song song, boolean favorite) {
        model.saveFavorite(context, song, favorite);
    }

    @Override
    public void addLyrics(Context context, Song song) {
        model.addLyrics(context, song);
    }

    @Override
    public void success(String action) {
        callBack.success(action);
    }
}
