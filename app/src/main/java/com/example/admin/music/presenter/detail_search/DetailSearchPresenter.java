package com.example.admin.music.presenter.detail_search;

import android.content.Context;

import com.example.admin.music.model.DetailSearchModel;
import com.example.admin.music.model.entity.Playlist;
import com.example.admin.music.model.entity.Singer;
import com.example.admin.music.model.entity.Song;
import com.example.admin.music.view.detail_search.DetailSearchViewListener;

import java.util.ArrayList;

/**
 * Created by admin on 1/11/2019.
 */

public class DetailSearchPresenter implements DetailSearchPresenterListener {
    private DetailSearchViewListener callBack;
    private DetailSearchModel model;

    public DetailSearchPresenter(DetailSearchViewListener callBack) {
        this.callBack = callBack;

        //init
        model = new DetailSearchModel(this);
    }

    @Override
    public void search(String word) {
        model.search(word);
    }

    @Override
    public void show() {
        callBack.show();
    }

    @Override
    public void save(Context context, String word) {
        model.save(context, word);
    }
}
