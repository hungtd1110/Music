package com.example.admin.music.view.music;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.admin.music.R;
import com.example.admin.music.view.favorite.FavoriteFragment;
import com.example.admin.music.view.playlist.PlaylistFragment;
import com.example.admin.music.view.singer.SingerFragment;
import com.example.admin.music.view.song.SongFragment;

/**
 * Created by admin on 1/5/2019.
 */

public class MusicAdapter extends FragmentPagerAdapter {
    private Context context;

    public MusicAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new SongFragment();
                break;
            case 1:
                fragment = new FavoriteFragment();
                break;
            case 2:
                fragment = new SingerFragment();
                break;
            case 3:
                fragment = new PlaylistFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        switch (position) {
            case 0:
                title = context.getString(R.string.music_song);
                break;
            case 1:
                title = context.getString(R.string.music_favorite);
                break;
            case 2:
                title = context.getString(R.string.music_singer);
                break;
            case 3:
                title = context.getString(R.string.music_playlist);
                break;
        }
        return title;
    }
}
