package com.example.admin.music.view.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.music.R;
import com.example.admin.music.model.entity.Playlist;
import com.example.admin.music.model.entity.Singer;
import com.example.admin.music.model.entity.Song;
import com.example.admin.music.presenter.search.SearchPresenter;
import com.example.admin.music.view.detail_search.DetailSearchActivity;
import com.example.admin.music.view.main.MainActivity;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.miguelcatalan.materialsearchview.SearchAdapter;

import java.util.ArrayList;

/**
 * Created by admin on 1/5/2019.
 */

public class SearchFragment extends Fragment implements SearchViewListener, View.OnClickListener {
    private SearchPresenter presenter;
    private LinearLayout llResult;
    private RecyclerView rvHistory, rvSong, rvSinger, rvPlaylist;
    private TextView txtDeleteAll;
    private String word;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        //controls
        llResult = view.findViewById(R.id.linearlayout_search_result);
        rvHistory = view.findViewById(R.id.recycleview_search_history);
        rvSong = view.findViewById(R.id.recycleview_search_song);
        rvSinger = view.findViewById(R.id.recycleview_search_singer);
        rvPlaylist = view.findViewById(R.id.recycleview_search_playlist);
        txtDeleteAll = view.findViewById(R.id.textview_search_deleteall);

        //init
        presenter = new SearchPresenter(this);

        //events
        txtDeleteAll.setOnClickListener(this);
        MainActivity.msvSearch.setOnQueryTextListener(queryText);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.getHistory(getContext());
    }

    @Override
    public void onClick(View view) {
        presenter.deleteALl(getContext());
    }

    @Override
    public void showHistory(ArrayList<String> list) {
        HistoryAdapter adapter = new HistoryAdapter(getContext(), list, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        rvHistory.setLayoutManager(layoutManager);
        rvHistory.setAdapter(adapter);
    }

    @Override
    public void show(ArrayList<Song> listSong, ArrayList<Singer> listSinger, ArrayList<Playlist> listPlaylist) {
        showSong(listSong);
        showSinger(listSinger);
        showPlaylist(listPlaylist);

        llResult.setVisibility(View.VISIBLE);
    }

    @Override
    public void delete(Context context, String word) {
        presenter.delete(context, word);
    }

    @Override
    public void updateHistory(ArrayList<String> list) {
        showHistory(list);

        Toast.makeText(getContext(), getString(R.string.search_delete), Toast.LENGTH_SHORT).show();
    }

    private void showPlaylist(ArrayList<Playlist> listPlaylist) {
        SearchPlaylistAdapter adapter = new SearchPlaylistAdapter(getContext(), listPlaylist, word);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        rvPlaylist.setLayoutManager(layoutManager);
        rvPlaylist.setAdapter(adapter);
    }

    private void showSinger(ArrayList<Singer> listSinger) {
        SearchSingerAdapter adapter = new SearchSingerAdapter(getContext(), listSinger, word);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        rvSinger.setLayoutManager(layoutManager);
        rvSinger.setAdapter(adapter);
    }

    private void showSong(ArrayList<Song> listSong) {
        SearchSongAdapter adapter = new SearchSongAdapter(getContext(), listSong, word);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        rvSong.setLayoutManager(layoutManager);
        rvSong.setAdapter(adapter);
    }

    private MaterialSearchView.OnQueryTextListener queryText = new MaterialSearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            if (query.equals("")) {
                Toast.makeText(getContext(), getString(R.string.search_message), Toast.LENGTH_SHORT).show();
            }
            else {
                Intent intent = new Intent(getContext(), DetailSearchActivity.class);
                intent.putExtra(getString(R.string.key_word), query);
                intent.putExtra(getString(R.string.key_action), getString(R.string.action_search));
                startActivity(intent);
            }

            return false;
        }

        @Override
        public boolean onQueryTextChange(final String newText) {
            if (newText.equals("")) {
                llResult.setVisibility(View.GONE);
            }
            else {
                word = newText;

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        presenter.search(newText);
                    }
                }, 500);
            }
            return false;
        }
    };

}
