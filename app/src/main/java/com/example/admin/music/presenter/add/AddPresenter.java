package com.example.admin.music.presenter.add;

import android.content.Context;

import com.example.admin.music.model.AddModel;
import com.example.admin.music.model.entity.Playlist;
import com.example.admin.music.model.entity.Song;
import com.example.admin.music.view.add.AddViewListener;

import java.util.ArrayList;

/**
 * Created by admin on 1/6/2019.
 */

public class AddPresenter implements AddPresenterListener {
    private AddViewListener callBack;
    private AddModel model;

    public AddPresenter(AddViewListener callBack) {
        this.callBack = callBack;

        //init
        model = new AddModel(this);
    }

    @Override
    public void getData(Context context) {
        model.getData(context);
    }

    @Override
    public void show(ArrayList<Playlist> list) {
        callBack.show(list);
    }

    @Override
    public void saveData(Context context, String name, Song song) {
        model.saveData(context, name, song);
    }
}
