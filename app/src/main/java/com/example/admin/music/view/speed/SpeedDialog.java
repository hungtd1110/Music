package com.example.admin.music.view.speed;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.admin.music.R;

import java.util.ArrayList;

/**
 * Created by admin on 1/6/2019.
 */

public class SpeedDialog extends BottomSheetDialogFragment {
    private RecyclerView rvList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_speed, container, false);

        //controls
        rvList = view.findViewById(R.id.recyclerview_speed_list);

        //init
        show();

        return view;
    }

    private void show() {
        ArrayList<String> list = new ArrayList<>();
        list.add("x0.5");
        list.add("default");
        list.add("x1.5");
        list.add("x2.0");
        SpeedAdapter adapter = new SpeedAdapter(getContext(), list, getTag());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvList.setLayoutManager(layoutManager);
        rvList.setAdapter(adapter);
    }
}
