package com.example.admin.music.view.speed;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.music.R;
import com.example.admin.music.view.detail_song.DetailSongViewListener;

import java.util.ArrayList;

/**
 * Created by admin on 1/6/2019.
 */

public class SpeedAdapter extends RecyclerView.Adapter<SpeedAdapter.ViewHolder> {
    private Context context;
    private ArrayList<String> list;
    private DetailSongViewListener callBack;
    private int select;

    public SpeedAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;

        //init
        callBack = (DetailSongViewListener) context;
        select = 1;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_speed_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.txtSpeed.setText(list.get(position));
        if (position == select) {
            holder.imvSeclect.setVisibility(View.VISIBLE);
        }
        else {
            holder.imvSeclect.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtSpeed;
        private ImageView imvSeclect;

        public ViewHolder(View itemView) {
            super(itemView);

            //controls
            txtSpeed = itemView.findViewById(R.id.textview_speed_speed);
            imvSeclect = itemView.findViewById(R.id.imageview_speed_select);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                default:
                    select = getAdapterPosition();
                    notifyDataSetChanged();
                    updateSpeed(select);
                    break;
            }
        }
    }

    private void updateSpeed(int select) {
        switch (select) {
            case 0:
                callBack.updateSpeed(0.5f);
                break;
            case 1:
                callBack.updateSpeed(1.0f);
                break;
            case 2:
                callBack.updateSpeed(1.5f);
                break;
            case 3:
                callBack.updateSpeed(2.0f);
                break;
        }
    }
}
