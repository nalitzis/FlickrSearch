package ado.com.flickrsearch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import ado.com.flickrsearch.domain.SearchResult;
import ado.com.flickrsearch.network.ServiceApi;

public class SearchActivity extends AppCompatActivity {
    private static final String TAG = "SearchActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        //TODO move logic to presenter
        testSearch();
    }

    private void testSearch() {
        SearchFlickrApp application = (SearchFlickrApp) getApplication();
        ServiceApi serviceApi = application.getServiceApi();
        serviceApi.search("kittens", new MyListener());

    }

    private class MyListener implements ServiceApi.Listener<SearchResult> {

        @Override
        public void onCompleted(SearchResult result) {
            Log.d(TAG, "got search result");
            SearchFlickrApp application = (SearchFlickrApp) getApplication();
            ServiceApi serviceApi = application.getServiceApi();
            serviceApi.fetchImage(result, null);
        }

        @Override
        public void onError() {

        }
    }
}
