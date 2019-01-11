package com.example.admin.music.view.detail_song;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.admin.music.R;
import com.example.admin.music.model.entity.Song;
import com.example.admin.music.view.image.ImageFragment;
import com.example.admin.music.view.lyrics.LyricsFragment;

/**
 * Created by admin on 1/10/2019.
 */

public class DetailSongAdapter extends FragmentPagerAdapter {
    private Context context;
    private Song song;

    public DetailSongAdapter(FragmentManager fm, Context context, Song song) {
        super(fm);
        this.context = context;
        this.song = song;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new ImageFragment();
                break;
            case 1:
                fragment = new LyricsFragment();
                break;
        }

        Bundle bundle = new Bundle();
        bundle.putSerializable(context.getString(R.string.key_song), song);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
