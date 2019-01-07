package com.example.admin.music.view.add;

import android.content.Context;
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
import com.example.admin.music.view.playlist.PlaylistFragment;

import java.util.ArrayList;

/**
 * Created by admin on 1/6/2019.
 */

public class AddDialog extends DialogFragment implements AddViewListener, View.OnClickListener {
    private AddPresenter presenter;
    private Song song;
    private RecyclerView rvList;
    private LinearLayout llAdd;
    private TextView txtClose;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add, container, false);

        //controls
        rvList = view.findViewById(R.id.recyclerview_add_list);
        llAdd = view.findViewById(R.id.linearlayout_add_add);
        txtClose = view.findViewById(R.id.textview_add_close);

        //init
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        presenter = new AddPresenter(this);

        getData();
        presenter.getData(getContext());

        //events
        llAdd.setOnClickListener(this);
        txtClose.setOnClickListener(this);

        return view;
    }

    @Override
    public void show(ArrayList<Playlist> list) {
        AddAdapter adapter = new AddAdapter(getContext(), list, presenter, song);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvList.setLayoutManager(layoutManager);
        rvList.setAdapter(adapter);
    }

    @Override
    public void success(Context context) {
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
        final AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.view_add_add, (ViewGroup) getView(), false);

        //controls
        final EditText edtContent = view.findViewById(R.id.editext_add_content);
        TextView txtCancel = view.findViewById(R.id.textview_add_cancel);
        TextView txtCreate = view.findViewById(R.id.textview_add_create);

        //init
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setView(view);
        dialog.show();

        //events
        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        txtCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edtContent.getText().toString();
                presenter.saveData(dialog.getContext(), name, song);
                dialog.dismiss();
            }
        });
    }

    private void getData() {
        Bundle bundle = getArguments();
        song = (Song) bundle.getSerializable("song");
    }
}
