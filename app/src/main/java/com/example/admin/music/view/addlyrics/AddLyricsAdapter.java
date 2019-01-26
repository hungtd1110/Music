package com.example.admin.music.view.addlyrics;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.music.R;
import com.example.admin.music.config.Data;
import com.example.admin.music.model.entity.Lyrics;
import com.example.admin.music.model.entity.Song;
import com.example.admin.music.utils.APIUtils;
import com.example.admin.music.view.lyrics.LyricsFragment;
import com.example.admin.music.view.main.MainActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by admin on 1/23/2019.
 */

public class AddLyricsAdapter extends RecyclerView.Adapter<AddLyricsAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Lyrics> list;
    private Song song;
    private ArrayList<Song> listLyrics;
    private AddLyricsViewListener callBack;

    public AddLyricsAdapter(Context context, ArrayList<Lyrics> list, Song song) {
        this.context = context;
        this.list = list;
        this.song = song;

        //init
        listLyrics = new ArrayList<>();
        callBack = (AddLyricsViewListener) context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_addlyrics_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Lyrics lyrics = list.get(position);

        holder.txtIndex.setText(position + 1 + "");
        holder.txtName.setText(lyrics.getName());
        holder.txtSinger.setText(lyrics.getSinger());
        holder.txtDownload.setText("Tải xuống : " + lyrics.getDownload());
        holder.txtId.setText("Id : " + lyrics.getIdUser());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtIndex, txtName, txtSinger, txtDownload, txtId;
        private ImageView imvDetail, imvSelect;

        public ViewHolder(View itemView) {
            super(itemView);

            //controls
            txtIndex = itemView.findViewById(R.id.textview_addlyrics_index);
            txtName = itemView.findViewById(R.id.textview_addlyrics_name);
            txtSinger = itemView.findViewById(R.id.textview_addlyrics_singer);
            txtDownload = itemView.findViewById(R.id.textview_addlyrics_download);
            txtId = itemView.findViewById(R.id.textview_addlyrics_id);
            imvDetail = itemView.findViewById(R.id.imageview_addlyrics_detail);
            imvSelect = itemView.findViewById(R.id.imageview_addlyrics_select);

            //
            imvDetail.setOnClickListener(this);
            imvSelect.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Lyrics lyrics = list.get(getAdapterPosition());
            switch (view.getId()) {
                case R.id.imageview_addlyrics_detail:
                    handleDetail(lyrics);
                    break;
                case R.id.imageview_addlyrics_select:
                    handleSelect(lyrics);
                    break;
            }
        }
    }

    private void handleSelect(Lyrics lyrics) {
        listLyrics = MainActivity.listLyrics;

        //save lyrics to file
        saveLyrics(lyrics);

        //delete song if song exits in list
        updateLyrics(song);

        listLyrics.add(song);

        writeLyrics();

        //update
        Toast.makeText(context, "Đã thêm file", Toast.LENGTH_SHORT).show();
        LyricsFragment.callBack.update(song);
        callBack.hide();

        updateDownload(lyrics);
    }

    private void updateDownload(Lyrics lyrics) {
        Data data = APIUtils.getData();
        Call<Void> call = data.update(lyrics.getId());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.i("update_download", "ok");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.i("update_download", t.toString());
            }
        });
    }

    private void writeLyrics() {
        File file = new File(context.getFilesDir(), "lyrics");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(listLyrics);

            //update list favorite in activity main
            MainActivity.listLyrics = listLyrics;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveLyrics(Lyrics lyrics) {
        String path = Environment.getExternalStorageDirectory() + "/Music";

        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }

        //set data
        song.setPathLyrics(file.getAbsolutePath() + "/" + lyrics.getName() + ".txt");
        Log.i("path_add_lyrics", song.getPathLyrics());

        try {
            FileWriter fw = new FileWriter(song.getPathLyrics(), false);
            fw.write(lyrics.getContent());
            fw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateLyrics(Song song) {
        for (int i = 0 ; i < listLyrics.size() ; i++) {
            Song s = listLyrics.get(i);
            if (song.getName().equals(s.getName()) && song.getSinger().equals(s.getSinger())) {
                listLyrics.remove(i);
                break;
            }
        }
    }

    private void handleDetail(Lyrics lyrics) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(lyrics.getName())
                .setMessage(lyrics.getContent())
                .setPositiveButton("Đóng", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create()
                .show();
    }
}
