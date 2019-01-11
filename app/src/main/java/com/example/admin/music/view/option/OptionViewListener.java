package com.example.admin.music.view.option;

import android.content.Context;

import com.example.admin.music.model.entity.Playlist;
import com.example.admin.music.model.entity.Song;

/**
 * Created by admin on 1/6/2019.
 */

public interface OptionViewListener {
    public void hide();

    public void addFavorite(Song song);

    public void subFavorite(Song song);

    public void deleteSong(Context context, Song song);

    public void edit(Context context, Playlist playlist,String name);

    public void deletePlaylist(Context context, Playlist playlist);

    public void success(Context context, String action);

    public void fail(Context context, String action);
}
