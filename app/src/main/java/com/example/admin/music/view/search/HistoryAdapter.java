package com.example.admin.music.view.search;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.music.R;
import com.example.admin.music.model.entity.Song;
import com.example.admin.music.view.detail_search.DetailSearchActivity;

import java.util.ArrayList;

/**
 * Created by admin on 1/10/2019.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private Context context;
    private ArrayList<String> list;
    private SearchViewListener callBack;

    public HistoryAdapter(Context context, ArrayList<String> list, SearchViewListener callBack) {
        this.context = context;
        this.list = list;
        this.callBack = callBack;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_search_history, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String word = list.get(position);
        holder.txtName.setText(word);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imvDelete;
        private TextView txtName;

        public ViewHolder(View itemView) {
            super(itemView);

            //controls
            imvDelete = itemView.findViewById(R.id.imageview_search_delete);
            txtName = itemView.findViewById(R.id.textview_search_name);

            //events
            imvDelete.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            String word = list.get(getAdapterPosition());

            switch (view.getId()) {
                case R.id.imageview_search_delete:
                    callBack.delete(context, word);
                    break;
                default:
                    Intent intent = new Intent(context, DetailSearchActivity.class);
                    intent.putExtra(context.getString(R.string.key_word), word);
                    intent.putExtra(context.getString(R.string.key_action), context.getString(R.string.action_search));
                    context.startActivity(intent);
                    break;
            }
        }
    }
}
