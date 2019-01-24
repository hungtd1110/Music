package com.example.admin.music.presenter.addplaylist;

import android.content.Context;

import com.example.admin.music.model.AddPlaylistModel;
import com.example.admin.music.model.entity.Song;
import com.example.admin.music.view.addplaylist.AddPlaylistViewListener;

/**
 * Created by admin on 1/6/2019.
 */

public class AddPlaylistPresenter implements AddPlaylistPresenterListener {
    private AddPlaylistViewListener callBack;
    private AddPlaylistModel model;

    public AddPlaylistPresenter(AddPlaylistViewListener callBack) {
        this.callBack = callBack;

        //init
        model = new AddPlaylistModel(this);
    }

    @Override
    public void show() {
        callBack.show();
    }

    @Override
    public void saveData(Context context, String name, Song song) {
        model.saveData(context, name, song);
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
