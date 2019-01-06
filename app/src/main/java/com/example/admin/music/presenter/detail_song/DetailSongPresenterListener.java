package com.example.admin.music.presenter.detail_song;

import android.content.Context;

import com.example.admin.music.model.entity.Song;

/**
 * Created by admin on 1/6/2019.
 */

public interface DetailSongPresenterListener {
    public void getData(Context context, Song song);

    public void show(boolean favorite);

    public void saveData(Context context, Song song, boolean favorite);
}
