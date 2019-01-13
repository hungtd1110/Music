package com.example.admin.music.presenter.lyrics;

import com.example.admin.music.model.LyricsModel;
import com.example.admin.music.model.entity.Song;
import com.example.admin.music.view.lyrics.LyricsViewListener;

/**
 * Created by admin on 1/13/2019.
 */

public class LyricsPresenter implements LyricsPresenterListener {
    private LyricsViewListener callBack;
    private LyricsModel model;

    public LyricsPresenter(LyricsViewListener callBack) {
        this.callBack = callBack;

        //init
        model = new LyricsModel(this);
    }

    @Override
    public void getLyrics(Song song) {
        model.getLyrics(song);
    }

    @Override
    public void show(Song song) {
        callBack.show(song);
    }
}
