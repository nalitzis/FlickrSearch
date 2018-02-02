package ado.com.flickrsearch.presenter;

import android.util.Log;

import ado.com.flickrsearch.SearchFlickrApp;
import ado.com.flickrsearch.api.ImageResult;
import ado.com.flickrsearch.api.SearchResult;
import ado.com.flickrsearch.api.ServiceApi;

public class SearchFlickrPresenter implements SearchPresenter {
    private static final String TAG = "SearchFlickrPresenter";

    private SearchFlickrApp mApplication;

    public SearchFlickrPresenter(final SearchFlickrApp application) {
        mApplication = application;
    }

    @Override
    public void onSearchCommand(final String search) {
        ServiceApi serviceApi = mApplication.getServiceApi();
        serviceApi.search(search, new SearchListener());
    }

    private class SearchListener implements ServiceApi.Listener<SearchResult> {

        @Override
        public void onCompleted(SearchResult result) {
            Log.d(TAG, "got search result, images size: " + result.getImages().size());;
            final ServiceApi serviceApi = mApplication.getServiceApi();
            serviceApi.fetchImage(result, null);
        }

        @Override
        public void onError(final Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private class ImagesListener implements ServiceApi.Listener<ImageResult> {

        @Override
        public void onCompleted(ImageResult result) {

        }

        @Override
        public void onError(Exception e) {

        }
    }
}
