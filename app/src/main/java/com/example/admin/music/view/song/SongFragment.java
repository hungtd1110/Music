package com.example.admin.music.view.song;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.admin.music.R;
import com.example.admin.music.model.entity.Song;
import com.example.admin.music.view.main.MainActivity;

import java.util.ArrayList;

/**
 * Created by admin on 1/5/2019.
 */

public class SongFragment extends Fragment implements SongViewListener {
    public static SongViewListener callBack;

    private RecyclerView rvList;
    private ArrayList<Song> list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_song, container, false);

        //controls
        rvList = view.findViewById(R.id.recyclerview_song_list);

        //init
        callBack = this;
        list = MainActivity.listSong;

        show();

        return view;
    }

    @Override
    public void update() {
        list = MainActivity.listSong;
        show();
    }

    private void show() {
        SongAdapter adapter = new SongAdapter(getContext(), list, getFragmentManager());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvList.setLayoutManager(layoutManager);
        rvList.setAdapter(adapter);
    }
}
