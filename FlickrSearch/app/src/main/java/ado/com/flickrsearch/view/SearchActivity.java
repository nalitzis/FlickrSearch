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
import android.widget.GridView;
import android.widget.ProgressBar;

import java.util.List;

import ado.com.flickrsearch.R;
import ado.com.flickrsearch.FlickrSearchApp;
import ado.com.flickrsearch.api.ImageResult;
import ado.com.flickrsearch.presenter.FlickrSearchPresenter;
import ado.com.flickrsearch.presenter.SearchPresenter;

public class SearchActivity extends AppCompatActivity implements ImageViewer {
    private static final String TAG = "SearchActivity";

    private SearchPresenter mSearchPresenter;
    private ImageAdapter mImageAdapter;

    private ProgressBar mSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUi();
        final FlickrSearchApp flickrApp = (FlickrSearchApp) getApplication();
        mSearchPresenter = new FlickrSearchPresenter(flickrApp, this);
        handleIntent(getIntent());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSearchPresenter.onDestroy();
        mSearchPresenter = null;
    }

    private void setupUi() {
        setContentView(R.layout.activity_search);
        mSpinner = findViewById(R.id.progressBar1);
        showSpinner(false);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        GridView gridview = findViewById(R.id.gridview);
        mImageAdapter = new ImageAdapter(this);
        gridview.setAdapter(mImageAdapter);
    }

    @Override
    public void showSpinner(final boolean show) {
        if(show) {
            mSpinner.setVisibility(View.VISIBLE);
        } else {
            mSpinner.setVisibility(View.GONE);
        }
    }

    @Override
    public void setTotalSize(int size) {
        mImageAdapter.setSize(size);
    }

    @Override
    public void configureImages(List<? extends ImageResult> images) {
        mImageAdapter.configureImages(images);
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

    @Override
    public void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(final Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.d(TAG, "query is: " + query);
            mSearchPresenter.onSearchCommand(query);
        }
    }

    @Override
    public void resetAdapter() {
        mImageAdapter.clear();
    }

    @Override
    public void setImage(ImageResult result) {
        mImageAdapter.add(result);
    }

    void requestNewImage(final ImageResult image, final int index) {
        mSearchPresenter.onNewImageRequest(image, index);
    }
}
