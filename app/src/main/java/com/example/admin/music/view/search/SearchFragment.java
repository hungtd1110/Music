package com.example.admin.music.view.search;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.admin.music.R;
import com.example.admin.music.view.main.MainActivity;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

/**
 * Created by admin on 1/5/2019.
 */

public class SearchFragment extends Fragment {
    private LinearLayout llResult;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        //controls
        llResult = view.findViewById(R.id.linearlayout_search_result);

        //init

        //events
        MainActivity.msvSearch.setOnQueryTextListener(queryText);

        return view;
    }

    private MaterialSearchView.OnQueryTextListener queryText = new MaterialSearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            llResult.setVisibility(View.VISIBLE);
            return false;
        }
    };
}
