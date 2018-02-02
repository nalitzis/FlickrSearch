package ado.com.flickrsearch.presenter;

import android.util.Log;

import ado.com.flickrsearch.FlickrSearchApp;
import ado.com.flickrsearch.api.ImageResult;
import ado.com.flickrsearch.api.ImageUrl;
import ado.com.flickrsearch.api.SearchResult;
import ado.com.flickrsearch.api.ServiceApi;
import ado.com.flickrsearch.view.ImageViewer;

public class FlickrSearchPresenter implements SearchPresenter {
    private static final String TAG = "FlickrSearchPresenter";

    private FlickrSearchApp mApplication;
    private ImageViewer mView;

    private final SearchListener mSearchListener = new SearchListener();
    private final ImagesListener mImagesListener = new ImagesListener();

    public FlickrSearchPresenter(final FlickrSearchApp application, ImageViewer view) {
        mApplication = application;
        mView = view;
    }

    @Override
    public void onSearchCommand(final String search) {
        mView.resetAdapter();
        ServiceApi serviceApi = mApplication.getServiceApi();
        serviceApi.search(search, mSearchListener);
    }

    @Override
    public void onDestroy() {
        mView = null;
        mApplication = null;
    }

    private class SearchListener implements ServiceApi.Listener<SearchResult> {

        @Override
        public void onCompleted(SearchResult result) {
            Log.d(TAG, "got search result, images size: " + result.getImagesUrl().size());

            //mView.onNewImage(null);

            final ServiceApi serviceApi = mApplication.getServiceApi();
            for (ImageUrl imgUrl : result.getImagesUrl()) {
                Log.d(TAG, "fetching " + imgUrl.getUrl());
                serviceApi.fetchImage(imgUrl.getUrl(), mImagesListener);
            }

        }

        @Override
        public void onError(final Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }



    private class ImagesListener implements ServiceApi.Listener<ImageResult> {

        @Override
        public void onCompleted(ImageResult result) {
            if(mView != null) {
                mView.onNewImage(result);
            }
        }

        @Override
        public void onError(Exception e) {

        }
    }
}
