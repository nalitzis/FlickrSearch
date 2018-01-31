package ado.com.flickrsearch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ado.com.flickrsearch.network.ServiceApi;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        testSearch();
    }

    private void testSearch() {
        SearchFlickrApp application = (SearchFlickrApp) getApplication();
        ServiceApi serviceApi = application.getServiceApi();
        serviceApi.search("kittens", null);
    }
}
