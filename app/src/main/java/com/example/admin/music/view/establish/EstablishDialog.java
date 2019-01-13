package com.example.admin.music.view.establish;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.admin.music.R;
import com.example.admin.music.view.detail_song.DetailSongActivity;

import java.util.ArrayList;

/**
 * Created by admin on 1/6/2019.
 */

public class EstablishDialog extends BottomSheetDialogFragment implements EstablishViewListener {
    private RecyclerView rvList;
    private ArrayList<String> listTitle;
    private ArrayList<Integer> listImage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_establish, container, false);

        //controls
        rvList = view.findViewById(R.id.recyclerview_establish_list);

        //init
        listTitle = new ArrayList<>();
        listImage = new ArrayList<>();

        show();

        return view;
    }

    @Override
    public void hide() {
        dismiss();
    }

    private void show() {
        //set data item
        listTitle.add(getString(R.string.establish_addlyrics));
        listTitle.add(getString(R.string.establish_change));

        listImage.add(R.mipmap.establish_lyrics);
        listImage.add(R.mipmap.establish_change);

        //show
        EstablishAdapter adapter = new EstablishAdapter(getContext(), listTitle, listImage, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvList.setLayoutManager(layoutManager);
        rvList.setAdapter(adapter);
    }

}
