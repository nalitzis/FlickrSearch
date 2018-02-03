package ado.com.flickrsearch.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import ado.com.flickrsearch.FlickrSearchApp;
import ado.com.flickrsearch.api.ImageResult;
import ado.com.flickrsearch.api.SearchResult;
import ado.com.flickrsearch.api.ServiceApi;
import ado.com.flickrsearch.domain.FlickrImageResult;
import ado.com.flickrsearch.domain.FlickrSearchResult;
import ado.com.flickrsearch.view.ImageViewer;

public class FlickrSearchPresenter implements SearchPresenter {
    private static final String TAG = "FlickrSearchPresenter";

    private FlickrSearchApp mApplication;
    private ImageViewer mView;

    private String mCurrentSearchString;

    private ServiceApi.Listener<SearchResult> mSearchListener = new SearchListener();
    private ServiceApi.Listener<FlickrImageResult> mImagesListener = new ImagesListener();

    public FlickrSearchPresenter(@NonNull final FlickrSearchApp application, @NonNull ImageViewer view) {
        mApplication = application;
        mView = view;
    }

    @VisibleForTesting
    FlickrSearchPresenter(@NonNull final FlickrSearchApp application, @NonNull final ImageViewer view,
                                 @NonNull final ServiceApi.Listener searchListener, @NonNull final ServiceApi.Listener imageListener) {

        this(application, view);
        mSearchListener = searchListener;
        mImagesListener = imageListener;
    }

    @VisibleForTesting
    ServiceApi.Listener<SearchResult> getSearchListener() {
        return mSearchListener;
    }

    @VisibleForTesting
    ServiceApi.Listener<FlickrImageResult> getImageListener() {
        return mImagesListener;
    }

    @Override
    public void onSearchCommand(final String search) {
        mView.resetAdapter();
        mCurrentSearchString = search;
        doSearch(1);
        mView.showSpinner(true);
    }

    private void doSearch(int page) {
        ServiceApi serviceApi = mApplication.getServiceApi();
        serviceApi.search(mCurrentSearchString, "" + page, mSearchListener);
    }

    @Override
    public void onDestroy() {
        mView = null;
        mApplication = null;
    }

    private class SearchListener implements ServiceApi.Listener<SearchResult> {

        @Override
        public void onCompleted(SearchResult result) {
            mView.showSpinner(false);
            mView.setTotalSize(Integer.parseInt(result.getTotalSize()));
            mView.configureImages(result.getImagesUrl());
        }

        @Override
        public void onError(final Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void onNewImageRequest(ImageResult image, int index) {
        if(image != null) {
            final ServiceApi serviceApi = mApplication.getServiceApi();
            serviceApi.fetchImage(image, mImagesListener);
        } else {
            doSearch(FlickrSearchResult.getPageFromIndex(index));
        }
    }

    private class ImagesListener implements ServiceApi.Listener<FlickrImageResult> {

        @Override
        public void onCompleted(FlickrImageResult result) {
            mView.setImage(result);
        }

        @Override
        public void onError(Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }
}
