package com.example.admin.music.view.detail_search;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.admin.music.R;
import com.example.admin.music.model.entity.Playlist;
import com.example.admin.music.model.entity.Singer;
import com.example.admin.music.model.entity.Song;
import com.example.admin.music.view.search.SearchPlaylistFragment;
import com.example.admin.music.view.search.SearchSingerFragment;
import com.example.admin.music.view.search.SearchSongFragment;

import java.util.ArrayList;

/**
 * Created by admin on 1/11/2019.
 */

public class DetailSearchAdapter extends FragmentPagerAdapter {
    private Context context;

    public DetailSearchAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new SearchSongFragment();
                break;
            case 1:
                fragment = new SearchSingerFragment();
                break;
            case 2:
                fragment = new SearchPlaylistFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position) {
            case 0:
                title = context.getString(R.string.detailsearch_song);
                break;
            case 1:
                title = context.getString(R.string.detailsearch_singer);
                break;
            case 2:
                title = context.getString(R.string.detailsearch_playlist);
                break;
        }
        return title;
    }
}
