package com.example.admin.music.presenter.option;

import android.content.Context;

import com.example.admin.music.model.OptionModel;
import com.example.admin.music.model.entity.Playlist;
import com.example.admin.music.model.entity.Song;
import com.example.admin.music.view.option.OptionViewListener;

/**
 * Created by admin on 1/6/2019.
 */

public class OptionPresenter implements OptionPresenterListener {
    private OptionViewListener callBack;
    private OptionModel model;

    public OptionPresenter(OptionViewListener callBack) {
        this.callBack = callBack;

        //init
        model = new OptionModel(this);
    }

    @Override
    public void addFavorite(Context context, Song song) {
        model.addFavorite(context, song);
    }

    @Override
    public void subFavorite(Context context, Song song) {
        model.subFavorite(context, song);
    }

    @Override
    public void deleteSong(Context context, Song song) {
        model.deleteSong(context, song);
    }

    @Override
    public void edit(Context context, Playlist playlist, String name) {
        model.edit(context, playlist, name);
    }

    @Override
    public void deletePlaylist(Context context, Playlist playlist) {
        model.deletePlaylist(context, playlist);
    }

    @Override
    public void success(Context context, String action) {
        callBack.success(context, action);
    }

    @Override
    public void fail(Context context, String action) {
        callBack.fail(context, action);
    }
}
