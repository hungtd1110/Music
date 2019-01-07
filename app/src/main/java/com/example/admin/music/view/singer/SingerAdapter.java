package com.example.admin.music.view.singer;

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
import android.widget.Toast;

import com.example.admin.music.R;
import com.example.admin.music.model.entity.Singer;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by admin on 1/5/2019.
 */

public class SingerAdapter extends RecyclerView.Adapter<SingerAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Singer> list;

    public SingerAdapter(Context context, ArrayList<Singer> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_singer_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Singer singer = list.get(position);

        holder.txtName.setText(singer.getName());
        holder.txtTotal.setText(singer.getListSong().size() + " bài hát");

        //get image from file mp3
        try {
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(singer.getListSong().get(0).getPathAudio());
            byte[] artBytes = mmr.getEmbeddedPicture();
            if (artBytes != null) {
                InputStream is = new ByteArrayInputStream(mmr.getEmbeddedPicture());
                Bitmap bm = BitmapFactory.decodeStream(is);
                holder.imvImage.setImageBitmap(bm);
            }
        } catch (Exception e) {
            Log.e("erorrs_itemsinger", e.toString());
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
            imvImage = itemView.findViewById(R.id.imageview_singer_image);
            txtName = itemView.findViewById(R.id.textview_singer_name);
            txtTotal = itemView.findViewById(R.id.textview_singer_total);

            //events
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                default:
                    break;
            }
        }
    }
}
