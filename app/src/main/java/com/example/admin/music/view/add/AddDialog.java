package com.example.admin.music.view.add;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.music.R;
import com.example.admin.music.model.entity.Playlist;
import com.example.admin.music.model.entity.Song;
import com.example.admin.music.presenter.add.AddPresenter;
import com.example.admin.music.view.main.MainActivity;
import com.example.admin.music.view.playlist.PlaylistFragment;

import java.util.ArrayList;

/**
 * Created by admin on 1/6/2019.
 */

public class AddDialog extends DialogFragment implements AddViewListener, View.OnClickListener {
    private AddPresenter presenter;
    private Song song;
    private ArrayList<Playlist> list;
    private RecyclerView rvList;
    private LinearLayout llAdd;
    private TextView txtClose;
    private AddAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add, container, false);

        //controls
        rvList = view.findViewById(R.id.recyclerview_add_list);
        llAdd = view.findViewById(R.id.linearlayout_add_add);
        txtClose = view.findViewById(R.id.textview_add_close);

        //init
        list = MainActivity.listPlaylist;
        presenter = new AddPresenter(this);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        getData();
        show();

        //events
        llAdd.setOnClickListener(this);
        txtClose.setOnClickListener(this);

        return view;
    }

    @Override
    public void show() {
        adapter = new AddAdapter(getContext(), list, presenter, song);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvList.setLayoutManager(layoutManager);
        rvList.setAdapter(adapter);
    }

    @Override
    public void success(Context context) {
        //reset show list
        list = MainActivity.listPlaylist;
        show();

        //update playlist
        PlaylistFragment.callBack.update();

        Toast.makeText(context, context.getString(R.string.add_notify_success), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void fail(Context context) {
        Toast.makeText(context, context.getString(R.string.add_notify_fail), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linearlayout_add_add:
                handleAdd();
                this.dismiss();
                break;
            case R.id.textview_add_close:
                this.dismiss();
                break;
        }
    }

    private void handleAdd() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        //get view
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.view_add_add, null, false);

        //controls
        final EditText edtContent = view.findViewById(R.id.editext_add_content);

        builder.setTitle(builder.getContext().getString(R.string.add_title))
                .setView(view)
                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String name = edtContent.getText().toString();
                        presenter.saveData(builder.getContext(), name, song);
                    }
                })
                .setNeutralButton("Hủy bỏ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create();

        builder.show();
    }

    private void getData() {
        Bundle bundle = getArguments();
        song = (Song) bundle.getSerializable(getString(R.string.key_song));
    }
}
