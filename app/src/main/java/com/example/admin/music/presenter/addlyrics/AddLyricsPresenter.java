package com.example.admin.music.presenter.addlyrics;

import com.example.admin.music.model.AddLyricsModel;
import com.example.admin.music.model.entity.Lyrics;
import com.example.admin.music.view.addlyrics.AddLyricsViewListener;

import java.util.ArrayList;

/**
 * Created by admin on 1/23/2019.
 */

public class AddLyricsPresenter implements AddLyricsPresenterListener {
    private AddLyricsViewListener callBack;
    private AddLyricsModel model;

    public AddLyricsPresenter(AddLyricsViewListener callBack) {
        this.callBack = callBack;

        //init
        model = new AddLyricsModel(this);
    }

    @Override
    public void getData() {
        model.getData();
    }

    @Override
    public void show(ArrayList<Lyrics> list) {
        callBack.show(list);
    }
}
