package com.example.admin.music.view.music;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.admin.music.R;

/**
 * Created by admin on 1/5/2019.
 */

public class MusicFragment extends Fragment {
    private TabLayout tlTab;
    private ViewPager vpContent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music, container, false);

        //controls
        tlTab = view.findViewById(R.id.tablayout_music_tab);
        vpContent = view.findViewById(R.id.viewpager_music_content);

        //init
        MusicAdapter adapter = new MusicAdapter(getFragmentManager(), getContext());
        vpContent.setAdapter(adapter);
        vpContent.setOffscreenPageLimit(4);
        tlTab.setupWithViewPager(vpContent);

        return view;
    }
}
