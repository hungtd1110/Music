package com.example.admin.music.view.lyrics;

import android.content.Context;

import com.example.admin.music.model.entity.Song;

/**
 * Created by admin on 1/13/2019.
 */

public interface LyricsViewListener {
    public void update(Song song);

    public void show(Song song);

    public void run(Context context, int time, String type);

    public void updateLyrics(Context context, int time, String type);
}
