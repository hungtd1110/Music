package com.example.admin.music.presenter.main;

import android.content.Context;

import com.example.admin.music.model.MainModel;
import com.example.admin.music.model.entity.Playlist;
import com.example.admin.music.model.entity.Singer;
import com.example.admin.music.model.entity.Song;
import com.example.admin.music.view.main.MainViewListener;

import java.util.ArrayList;

/**
 * Created by admin on 1/5/2019.
 */

public class MainPresenter implements MainPresenterListener {
    private MainViewListener callBack;
    private MainModel model;

    public MainPresenter(MainViewListener callBack) {
        this.callBack = callBack;

        //init
        model = new MainModel(this);
    }

    @Override
    public void getData(Context context, String action) {
        model.getData(context, action);
    }

    @Override
    public void show() {
        callBack.show();
    }

    @Override
    public void showUpdate() {
        callBack.showUpdate();
    }
}
