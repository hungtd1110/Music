package com.example.admin.music.view.detail_song;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.music.R;
import com.example.admin.music.model.entity.Song;

import java.util.ArrayList;

/**
 * Created by admin on 1/5/2019.
 */

public class ListDialog extends BottomSheetDialogFragment {
    private ArrayList<Song> list;
    private TextView txtTotal;
    private RecyclerView rvList;
    private int index;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_list, container, false);

        //controls
        txtTotal = view.findViewById(R.id.textview_list_total);
        rvList = view.findViewById(R.id.recyclerview_list_list);

        //init
        getData();
        txtTotal.setText("Danh sách phát: " + list.size() + " bài");
        show();

        return view;
    }

    private void show() {
        ListApdater adapter = new ListApdater(getContext(), list, index);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvList.setLayoutManager(layoutManager);
        rvList.setAdapter(adapter);
    }

    private void getData() {
        Bundle bundle = getArguments();
        list = (ArrayList<Song>) bundle.getSerializable("list");
        index = bundle.getInt("index");
    }
}
