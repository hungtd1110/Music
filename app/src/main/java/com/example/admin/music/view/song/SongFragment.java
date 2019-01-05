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

public class SongFragment extends Fragment {
    private RecyclerView rvList;
    private ArrayList<Song> listSong;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_song, container, false);

        //controls
        rvList = view.findViewById(R.id.recyclerview_song_list);

        //init
        listSong = MainActivity.listSong;
        show();

        return view;
    }

    private void show() {
        SongAdapter adapter = new SongAdapter(getContext(), listSong);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvList.setLayoutManager(layoutManager);
        rvList.setAdapter(adapter);
    }
}
