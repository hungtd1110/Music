package com.example.admin.music.view.lyrics;

import com.example.admin.music.model.entity.Song;

/**
 * Created by admin on 1/13/2019.
 */

public interface LyricsViewListener {
    public void update(Song song);

    public void show(Song song);

    public void run(int time, String type);

    public void updateLyrics(int time, String type);
}
