package com.example.admin.music.view.detail_song;

/**
 * Created by admin on 1/5/2019.
 */

public interface DetailSongViewListener {
    public void updateSong(int index);

    public void show(boolean favorite);

    public void updateSpeed(float speed);
}