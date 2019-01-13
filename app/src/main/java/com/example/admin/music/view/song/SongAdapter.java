package com.example.admin.music.view.song;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.music.R;
import com.example.admin.music.model.entity.Song;
import com.example.admin.music.view.detail_song.DetailSongActivity;
import com.example.admin.music.view.option.OptionDialog;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by admin on 1/5/2019.
 */

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Song> list;
    private FragmentManager fragmentManager;

    public SongAdapter(Context context, ArrayList<Song> list, FragmentManager fragmentManager) {
        this.context = context;
        this.list = list;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_song_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Song song = list.get(position);

        holder.txtName.setText(song.getName());
        holder.txtSinger.setText(song.getSinger());

        //get image from file mp3
        try {
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(song.getPathAudio());
            byte[] artBytes = mmr.getEmbeddedPicture();
            if (artBytes != null) {
                InputStream is = new ByteArrayInputStream(mmr.getEmbeddedPicture());
                Bitmap bm = BitmapFactory.decodeStream(is);
                holder.imvImage.setImageBitmap(bm);
            }
            else {
                holder.imvImage.setImageResource(R.drawable.all_imagesong);
            }
        } catch (Exception e) {
            Log.e("erorrs_itemsong", e.toString());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imvImage, imvOption;
        private TextView txtName, txtSinger;

        public ViewHolder(View itemView) {
            super(itemView);

            //controls
            imvImage = itemView.findViewById(R.id.imageview_song_image);
            imvOption = itemView.findViewById(R.id.imageview_song_option);
            txtName = itemView.findViewById(R.id.textview_song_name);
            txtSinger = itemView.findViewById(R.id.textview_song_singer);

            //events
            imvOption.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.imageview_song_option:
                    OptionDialog dialog = new OptionDialog();

                    //get data
                    Song song = list.get(getAdapterPosition());

                    //put data
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(context.getString(R.string.key_song), song);

                    dialog.setArguments(bundle);
                    dialog.show(fragmentManager, context.getString(R.string.key_song));
                    break;
                default:
                    Intent intent = new Intent(context, DetailSongActivity.class);

                    //put data
                    intent.putExtra(context.getString(R.string.key_list), list);
                    intent.putExtra(context.getString(R.string.key_index), getAdapterPosition());

                    context.startActivity(intent);
                    break;
            }
        }
    }
}
