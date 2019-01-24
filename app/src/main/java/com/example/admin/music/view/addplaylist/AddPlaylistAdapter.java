package com.example.admin.music.view.addplaylist;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.music.R;
import com.example.admin.music.model.entity.Playlist;
import com.example.admin.music.model.entity.Song;
import com.example.admin.music.presenter.addplaylist.AddPlaylistPresenter;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by admin on 1/6/2019.
 */

public class AddPlaylistAdapter extends RecyclerView.Adapter<AddPlaylistAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Playlist> list;
    private AddPlaylistPresenter presenter;
    private Song song;

    public AddPlaylistAdapter(Context context, ArrayList<Playlist> list, AddPlaylistPresenter presenter, Song song) {
        this.context = context;
        this.list = list;
        this.presenter = presenter;
        this.song = song;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_add_list, parent, false);
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
            Log.e("erorrs_itemadd", e.toString());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imvImage;
        private TextView txtName, txtTotal;

        public ViewHolder(View itemView) {
            super(itemView);

            //controls
            imvImage = itemView.findViewById(R.id.imageview_add_image);
            txtName = itemView.findViewById(R.id.textview_add_name);
            txtTotal = itemView.findViewById(R.id.textview_add_total);

            //events
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            String name = list.get(getAdapterPosition()).getName();
            presenter.saveData(context, name, song);
        }
    }
}
