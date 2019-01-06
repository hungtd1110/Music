package com.example.admin.music.view.list;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.music.R;
import com.example.admin.music.model.entity.Song;
import com.example.admin.music.view.detail_song.DetailSongViewListener;

import java.util.ArrayList;

/**
 * Created by admin on 1/5/2019.
 */

public class ListApdater extends RecyclerView.Adapter<ListApdater.ViewHolder> {
    private Context context;
    private ArrayList<Song> list;
    private int index;
    private DetailSongViewListener callBack;
    private ColorStateList colorNormal;

    public ListApdater(Context context, ArrayList<Song> list, int index) {
        this.context = context;
        this.list = list;
        this.index = index;
        callBack = (DetailSongViewListener) context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_list_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Song song = list.get(position);

        holder.txtIndex.setText(position + 1 + "");
        holder.txtName.setText(song.getName());
        holder.txtSinger.setText(song.getSinger());

        if (position == index) {
            holder.txtIndex.setTextColor(context.getResources().getColor(R.color.list_select));
            holder.txtName.setTextColor(context.getResources().getColor(R.color.list_select));
            holder.txtSinger.setTextColor(context.getResources().getColor(R.color.list_select));
        }
        else {
            holder.txtIndex.setTextColor(context.getResources().getColor(R.color.list_index));
            holder.txtName.setTextColor(context.getResources().getColor(R.color.list_name));
            holder.txtSinger.setTextColor(colorNormal);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtIndex, txtName, txtSinger;

        public ViewHolder(View itemView) {
            super(itemView);

            //controls
            txtIndex = itemView.findViewById(R.id.textview_list_index);
            txtName = itemView.findViewById(R.id.textview_list_name);
            txtSinger = itemView.findViewById(R.id.textview_list_singer);

            //init
            colorNormal = txtSinger.getTextColors();

            //events
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                default:
                    index = getAdapterPosition();
                    notifyDataSetChanged();
                    callBack.updateSong(index);
                    break;
            }
        }
    }
}
