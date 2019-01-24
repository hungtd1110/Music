package com.example.admin.music.view.establish;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.music.R;
import com.example.admin.music.model.entity.Song;
import com.example.admin.music.view.addlyrics.AddLyricsActivity;
import com.example.admin.music.view.detail_song.DetailSongActivity;

import java.util.ArrayList;

/**
 * Created by admin on 1/6/2019.
 */

public class EstablishAdapter extends RecyclerView.Adapter<EstablishAdapter.ViewHolder> {
    private Context context;
    private ArrayList<String> listTitle;
    private ArrayList<Integer> listImage;
    private Song song;
    private EstablishViewListener callBack;

    public EstablishAdapter(Context context, ArrayList<String> listTitle, ArrayList<Integer> listImage, Song song, EstablishViewListener callBack) {
        this.context = context;
        this.listTitle = listTitle;
        this.listImage = listImage;
        this.song = song;
        this.callBack = callBack;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_establish_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.txtOption.setText(listTitle.get(position));
        holder.imvImage.setImageResource(listImage.get(position));
    }

    @Override
    public int getItemCount() {
        return listTitle.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtOption;
        private ImageView imvImage;

        public ViewHolder(View itemView) {
            super(itemView);

            //controls
            txtOption = itemView.findViewById(R.id.textview_establish_option);
            imvImage = itemView.findViewById(R.id.imageview_establish_image);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //get data
            String title = listTitle.get(getAdapterPosition());
            String addLyrics = context.getString(R.string.establish_addlyrics);
            String change = context.getString(R.string.establish_change);

            if (title.equals(addLyrics)) {
                handleLyrics();
            }
            else if (title.equals(change)) {
                handleChange();
            }

            //hide dialog
            callBack.hide();
        }
    }

    private void handleChange() {
        String[] list = new String[2];
        list[0] = context.getString(R.string.establish_item_karaoke);
        list[1] = context.getString(R.string.establish_item_hightlight);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.establish_change))
                .setItems(list, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            DetailSongActivity.callBack.updateType(context.getString(R.string.key_karaoke));
                        }
                        else {
                            DetailSongActivity.callBack.updateType(context.getString(R.string.key_hightlight));
                        }
                    }
                })
                .create();

        builder.show();
    }

    private void handleLyrics() {
        String[] list = new String[2];
        list[0] = context.getString(R.string.establish_item_device);
        list[1] = context.getString(R.string.establish_item_server);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.establish_addlyrics))
                .setItems(list, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            DetailSongActivity.callBack.addLyrics();
                        }
                        else {
                            Intent intent = new Intent(context, AddLyricsActivity.class);
                            intent.putExtra(context.getString(R.string.key_song), song);
                            context.startActivity(intent);
                        }
                    }
                })
                .create();

        builder.show();
    }

}
