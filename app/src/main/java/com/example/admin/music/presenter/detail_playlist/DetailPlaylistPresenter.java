package com.example.admin.music.presenter.detail_playlist;

import android.content.Context;

import com.example.admin.music.model.DetailPlaylistModel;
import com.example.admin.music.model.entity.Playlist;
import com.example.admin.music.view.detail_playlist.DetailPlaylistViewListener;

/**
 * Created by admin on 1/10/2019.
 */

public class DetailPlaylistPresenter implements DetailPlaylistPresenterListener {
    private DetailPlaylistViewListener callBack;
    private DetailPlaylistModel model;

    public DetailPlaylistPresenter(DetailPlaylistViewListener callBack) {
        this.callBack = callBack;

        //init
        model = new DetailPlaylistModel(this);
    }

    @Override
    public void save(Context context, Playlist playlist) {
        model.save(context, playlist);
    }

    @Override
    public void success(Context context) {
        callBack.success(context);
    }

    @Override
    public void fail(Context context) {
        callBack.fail(context);
    }
}
