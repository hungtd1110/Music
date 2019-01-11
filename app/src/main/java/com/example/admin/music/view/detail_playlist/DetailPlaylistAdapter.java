package com.example.admin.music.view.detail_playlist;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.support.v7.app.AlertDialog;
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
import com.example.admin.music.view.detail_song.DetaiSongActivity;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by admin on 1/10/2019.
 */

public class DetailPlaylistAdapter extends RecyclerView.Adapter<DetailPlaylistAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Song> list;
    private String action;
    private DetailPlaylistViewListener callBack;

    public DetailPlaylistAdapter(Context context, ArrayList<Song> list, String action) {
        this.context = context;
        this.list = list;
        this.action = action;

        //init
        callBack = (DetailPlaylistViewListener) context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_detailplaylist_list, parent, false);
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
            Log.e("erorrs_itemdetailpl", e.toString());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imvImage, imvDelete;
        private TextView txtName, txtSinger;
        private RelativeLayout rlItem;
        private SwipeRevealLayout swItem;

        public ViewHolder(View itemView) {
            super(itemView);

            //controls
            imvImage = itemView.findViewById(R.id.imageview_detailplaylist_image);
            txtName = itemView.findViewById(R.id.textview_detailplaylist_name);
            imvDelete = itemView.findViewById(R.id.imageview_detailplaylist_delete);
            txtSinger = itemView.findViewById(R.id.textview_detailplaylist_singer);
            rlItem = itemView.findViewById(R.id.relativelayout_detailplaylist_item);
            swItem = itemView.findViewById(R.id.swipelayout_detailplaylist_item);

            //init
            if (action.equals(context.getString(R.string.action_see))) {
                swItem.setLockDrag(false);
            }
            else if (action.equals(context.getString(R.string.action_search))) {
                swItem.setLockDrag(true);
            }

            //events
            imvDelete.setOnClickListener(this);
            rlItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.imageview_detailplaylist_delete:
                    Song song = list.get(getAdapterPosition());
                    handleDelete(song);
                    break;
                default:
                    Intent intent = new Intent(context, DetaiSongActivity.class);

                    //put data
                    intent.putExtra(context.getString(R.string.key_list), list);
                    intent.putExtra(context.getString(R.string.key_index), getAdapterPosition());

                    context.startActivity(intent);
                    break;
            }
        }
    }

    private void handleDelete(final Song song) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.option_title_confirm))
                .setMessage(context.getString(R.string.option_message_song) + " \"" + song.getName() + "\" khỏi playlist không?")
                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //delete song into list
                        for (int index = 0 ; index < list.size() ; index++) {
                            Song s = list.get(index);
                            if (song.getName().equals(s.getName()) && song.getSinger().equals(s.getSinger())) {
                                list.remove(index);
                                break;
                            }
                        }

                        //delete song into file (update playlist)
                        callBack.save(context, list);

                        dialogInterface.dismiss();
                    }
                })
                .setNeutralButton("Hủy bỏ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.show();
    }
}
