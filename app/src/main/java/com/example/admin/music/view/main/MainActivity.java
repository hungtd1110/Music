package com.example.admin.music.view.main;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.admin.music.R;
import com.example.admin.music.view.music.MusicFragment;
import com.example.admin.music.view.search.SearchFragment;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

public class MainActivity extends AppCompatActivity {
    public static MaterialSearchView msvSearch;

    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set ToolBar
        Toolbar toolbar = findViewById(R.id.toolbar_main_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.main_search);

        //controls
        msvSearch = findViewById(R.id.materialsearchview_main_search);

        //init
        //show music
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.framlayout_main_content, new MusicFragment());
        fragmentTransaction.commit();

        //events
        msvSearch.setOnSearchViewListener(searchView);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (msvSearch.isSearchOpen()) {
                    msvSearch.closeSearch();
                } else {
                    msvSearch.showSearch(false);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private MaterialSearchView.SearchViewListener searchView = new MaterialSearchView.SearchViewListener() {
        SearchFragment fragment = new SearchFragment();
        @Override
        public void onSearchViewShown() {
            //show content
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.framlayout_main_content, fragment);
            fragmentTransaction.commit();
        }

        @Override
        public void onSearchViewClosed() {
            //close content, show music
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.remove(fragment);
            fragmentTransaction.commit();
        }
    };
}
