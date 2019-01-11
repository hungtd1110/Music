package com.example.admin.music.view.playlist;

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
import com.example.admin.music.model.entity.Playlist;
import com.example.admin.music.view.detail_playlist.DetailPlaylistActivity;
import com.example.admin.music.view.option.OptionDialog;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by admin on 1/6/2019.
 */

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Playlist> list;
    private FragmentManager fragmentManager;

    public PlaylistAdapter(Context context, ArrayList<Playlist> list, FragmentManager fragmentManager) {
        this.context = context;
        this.list = list;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_playlist_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Playlist playlist = list.get(position);

        holder.txtName.setText(playlist.getName());
        holder.txtTotal.setText(playlist.getListSong().size() + " bài hát");

        //get image from file mp3
        try {
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(playlist.getListSong().get(0).getPathAudio());
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
            Log.e("erorrs_itemplaylist", e.toString());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imvImage, imvOption;
        private TextView txtName, txtTotal;

        public ViewHolder(View itemView) {
            super(itemView);

            //controls
            imvImage = itemView.findViewById(R.id.imageview_playlist_image);
            imvOption = itemView.findViewById(R.id.imageview_playlist_option);
            txtName = itemView.findViewById(R.id.textview_playlist_name);
            txtTotal = itemView.findViewById(R.id.textview_playlist_total);

            //events
            imvOption.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //get data
            Playlist playlist = list.get(getAdapterPosition());

            switch (view.getId()) {
                case R.id.imageview_playlist_option:
                    OptionDialog dialog = new OptionDialog();

                    //put data
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(context.getString(R.string.key_playlist), playlist);

                    dialog.setArguments(bundle);
                    dialog.show(fragmentManager, context.getString(R.string.key_playlist) + "");
                    break;
                default:
                    Intent intent = new Intent(context, DetailPlaylistActivity.class);

                    //put data
                    intent.putExtra(context.getString(R.string.key_playlist), playlist);
                    intent.putExtra(context.getString(R.string.key_action), context.getString(R.string.action_see));

                    context.startActivity(intent);
                    break;
            }
        }
    }
}
