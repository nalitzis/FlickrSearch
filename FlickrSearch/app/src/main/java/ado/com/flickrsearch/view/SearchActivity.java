package ado.com.flickrsearch.view;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import ado.com.flickrsearch.R;
import ado.com.flickrsearch.SearchFlickrApp;
import ado.com.flickrsearch.presenter.SearchFlickrPresenter;
import ado.com.flickrsearch.presenter.SearchPresenter;

public class SearchActivity extends AppCompatActivity implements ImageViewer {
    private static final String TAG = "SearchActivity";

    private SearchPresenter mSearchPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUi();
        final SearchFlickrApp flickrApp = (SearchFlickrApp) getApplication();
        mSearchPresenter = new SearchFlickrPresenter(flickrApp);
        handleIntent(getIntent());
    }

    private void setupUi() {
        setContentView(R.layout.activity_search);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        GridView gridview = findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));

        gridview.setOnItemClickListener((parent, v, position, id) -> {
                Toast.makeText(SearchActivity.this, "" + position,
                        Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        return true;
    }

    public void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(final Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.d(TAG, "query is: " +query);
        }

    }
}
