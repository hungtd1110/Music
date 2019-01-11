package com.example.admin.music.view.search;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.music.R;
import com.example.admin.music.model.entity.Song;
import com.example.admin.music.view.detail_search.DetailSearchActivity;
import com.example.admin.music.view.detail_song.DetaiSongActivity;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by admin on 1/10/2019.
 */

public class SearchSongAdapter extends RecyclerView.Adapter<SearchSongAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Song> list;
    private String word;
    private boolean noResult;

    public SearchSongAdapter(Context context, ArrayList<Song> list, String word) {
        this.context = context;
        this.list = list;
        this.word = word;

        //init
        if (list.size() == 0) {
            noResult = true;
            list.add(new Song());
        }
        else {
            noResult = false;
            if (!word.equals("")) {
                list.add(new Song());
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        View itemView = null;

        if (viewType == 0) {
            itemView = inflater.inflate(R.layout.view_search_notify, parent, false);
        }
        else {
            itemView = inflater.inflate(R.layout.item_search_song, parent, false);
        }

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (!noResult) {
            if (!word.equals("") && position == list.size() - 1) {
                holder.txtNotify.setText(context.getString(R.string.search_notify));
                holder.txtNotify.setTextColor(ContextCompat.getColor(context, R.color.search_notify));
            }
            else {
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

                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (noResult) {
            return 0;
        }
        else if (!word.equals("") && position == list.size() - 1) {
            return 0;
        }
        return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imvImage;
        private TextView txtName, txtSinger, txtNotify;

        public ViewHolder(View itemView) {
            super(itemView);

            //controls
            imvImage = itemView.findViewById(R.id.imageview_song_image);
            txtName = itemView.findViewById(R.id.textview_song_name);
            txtSinger = itemView.findViewById(R.id.textview_song_singer);
            txtNotify = itemView.findViewById(R.id.textview_search_notify);

            //events
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (!noResult) {
                if (!word.equals("") && getAdapterPosition() == list.size() - 1) {
                    Intent intent = new Intent(context, DetailSearchActivity.class);
                    intent.putExtra(context.getString(R.string.key_word), word);
                    intent.putExtra(context.getString(R.string.key_action), context.getString(R.string.action_song));
                    context.startActivity(intent);
                }
                else {
                    Intent intent = new Intent(context, DetaiSongActivity.class);

                    //put data
                    intent.putExtra(context.getString(R.string.key_list), list);
                    intent.putExtra(context.getString(R.string.key_index), getAdapterPosition());

                    context.startActivity(intent);
                }
            }
        }
    }
}
