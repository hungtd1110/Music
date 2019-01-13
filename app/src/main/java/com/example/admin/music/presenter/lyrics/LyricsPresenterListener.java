package com.example.admin.music.presenter.lyrics;

import com.example.admin.music.model.entity.Song;

/**
 * Created by admin on 1/13/2019.
 */

public interface LyricsPresenterListener {
    public void getLyrics(Song song);

    public void show(Song song);
}
