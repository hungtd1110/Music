package com.example.admin.music.view.image;

import com.example.admin.music.model.entity.Song;

/**
 * Created by admin on 1/12/2019.
 */

public interface ImageViewListener {
    public void update(Song song);

    public void animation(boolean run);
}
