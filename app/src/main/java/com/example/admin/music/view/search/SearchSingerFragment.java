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
import com.example.admin.music.model.entity.Singer;
import com.example.admin.music.view.detail_search.DetailSearchActivity;

import java.util.ArrayList;

/**
 * Created by admin on 1/11/2019.
 */

public class SearchSingerFragment extends Fragment {
    private RecyclerView rvList;
    private ArrayList<Singer> list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_singer, container, false);

        //controls
        rvList = view.findViewById(R.id.recyclerview_singer_list);

        //init
        list = DetailSearchActivity.listSinger;

        show();

        return view;
    }

    private void show() {
        SearchSingerAdapter adapter = new SearchSingerAdapter(getContext(), list, "");
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvList.setLayoutManager(layoutManager);
        rvList.setAdapter(adapter);
    }
}
