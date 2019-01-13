package com.example.admin.music.presenter.detail_song;

import android.content.Context;

import com.example.admin.music.model.entity.Song;

/**
 * Created by admin on 1/6/2019.
 */

public interface DetailSongPresenterListener {
    public void getFavorite(Context context, Song song);

    public void showFavorite(boolean favorite);

    public void saveFavorite(Context context, Song song, boolean favorite);

    public void addLyrics(Context context, Song song);

    public void success(String action);
}
