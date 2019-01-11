package com.example.admin.music.view.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.admin.music.R;
import com.example.admin.music.model.entity.Playlist;
import com.example.admin.music.view.detail_search.DetailSearchActivity;

import java.util.ArrayList;

/**
 * Created by admin on 1/11/2019.
 */

public class SearchPlaylistFragment extends Fragment {
    private RecyclerView rvList;
    private ArrayList<Playlist> list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist, container, false);

        //controls
        rvList = view.findViewById(R.id.recyclerview_playlist_list);

        //init
        list = DetailSearchActivity.listPlaylist;

        show();

        return view;
    }

    private void show() {
        SearchPlaylistAdapter adapter = new SearchPlaylistAdapter(getContext(), list, "");
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvList.setLayoutManager(layoutManager);
        rvList.setAdapter(adapter);
    }
}
