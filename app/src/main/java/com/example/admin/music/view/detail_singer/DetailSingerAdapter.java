package com.example.admin.music.view.detail_singer;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.example.admin.music.R;
import com.example.admin.music.model.entity.Song;
import com.example.admin.music.view.add.AddDialog;
import com.example.admin.music.view.detail_song.DetailSongActivity;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by admin on 1/10/2019.
 */

public class DetailSingerAdapter extends RecyclerView.Adapter<DetailSingerAdapter.ViewHolder> {
    private Context context;
    private FragmentManager fragmentManager;
    private ArrayList<Song> list;
    private String action;

    public DetailSingerAdapter(Context context, FragmentManager fragmentManager, ArrayList<Song> list, String action) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.list = list;
        this.action = action;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_detailsinger_list, parent, false);
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
            Log.e("erorrs_itemdetailsinger", e.toString());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imvImage, imvAdd;
        private TextView txtName, txtSinger;
        private RelativeLayout rlItem;
        private SwipeRevealLayout swItem;

        public ViewHolder(View itemView) {
            super(itemView);

            //controls
            imvImage = itemView.findViewById(R.id.imageview_detailsinger_image);
            imvAdd = itemView.findViewById(R.id.imageview_detailsinger_add);
            txtName = itemView.findViewById(R.id.textview_detailsinger_name);
            txtSinger = itemView.findViewById(R.id.textview_detailsinger_singer);
            rlItem = itemView.findViewById(R.id.relativelayout_detailsinger_item);
            swItem = itemView.findViewById(R.id.swipelayout_detailsinger_item);

            //init
            if (action.equals(context.getString(R.string.action_see))) {
                swItem.setLockDrag(false);
            }
            else if (action.equals(context.getString(R.string.action_search))) {
                swItem.setLockDrag(true);
            }

            //events
            imvAdd.setOnClickListener(this);
            rlItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.imageview_detailsinger_add:
                    Song song = list.get(getAdapterPosition());
                    AddDialog dialog = new AddDialog();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(context.getString(R.string.key_song), song);
                    dialog.setArguments(bundle);
                    dialog.show(fragmentManager, "");
                    break;
                default:
                    Intent intent = new Intent(context, DetailSongActivity.class);

                    //put data
                    intent.putExtra(context.getString(R.string.key_list), list);
                    intent.putExtra(context.getString(R.string.key_index), getAdapterPosition());

                    context.startActivity(intent);
            }
        }
    }
}
