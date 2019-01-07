package com.example.admin.music.view.option;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.music.R;
import com.example.admin.music.model.entity.Song;
import com.example.admin.music.view.add.AddDialog;

import java.util.ArrayList;

/**
 * Created by admin on 1/6/2019.
 */

public class OptionAdapter extends RecyclerView.Adapter<OptionAdapter.ViewHolder> {
    private Context context;
    private ArrayList<String> listTitle;
    private ArrayList<Integer> listImage;
    private Song song;
    private FragmentManager fragmentManager;
    private OptionViewListener callBack;

    public OptionAdapter(Context context, ArrayList<String> listTitle, ArrayList<Integer> listImage,
                         Song song, FragmentManager fragmentManager, OptionDialog optionDialog) {
        this.context = context;
        this.listTitle = listTitle;
        this.listImage = listImage;
        this.song = song;
        this.fragmentManager = fragmentManager;

        //init
        callBack = optionDialog;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_option_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.txtOption.setText(listTitle.get(position));
        holder.imvImage.setImageResource(listImage.get(position));
    }

    @Override
    public int getItemCount() {
        return listTitle.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtOption;
        private ImageView imvImage;

        public ViewHolder(View itemView) {
            super(itemView);

            //controls
            txtOption = itemView.findViewById(R.id.textview_option_option);
            imvImage = itemView.findViewById(R.id.imageview_option_image);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //get data
            String title = listTitle.get(getAdapterPosition());
            String add = context.getString(R.string.option_item_add);
            String addFavorite = context.getString(R.string.option_item_addfavorite);
            String subFavorite = context.getString(R.string.option_item_subfavorite);
            String delete = context.getString(R.string.option_item_delete);

            if (title.equals(add)) {
                AddDialog dialog = new AddDialog();
                Bundle bundle = new Bundle();
                bundle.putSerializable(context.getString(R.string.key_song), song);
                dialog.setArguments(bundle);
                dialog.show(fragmentManager, "");
            }
            else if (title.equals(addFavorite)) {
                callBack.addFavorite(song);
            }
            else if (title.equals(subFavorite)) {
                callBack.subFavorite(song);
            }
            else if (title.equals(delete)) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(context.getString(R.string.option_title_confirm))
                        .setMessage(context.getString(R.string.option_message) + " \"" + song.getName() + "\" không?")
                        .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                callBack.delete(context, song);
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

            //hide dialog
            callBack.hide();
        }
    }
}
