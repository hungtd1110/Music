package com.example.admin.music.view.add;

import android.content.Context;

import com.example.admin.music.model.entity.Playlist;

import java.util.ArrayList;

/**
 * Created by admin on 1/6/2019.
 */

public interface AddViewListener {
    public void show();

    public void success(Context context);

    public void fail(Context context);
}
