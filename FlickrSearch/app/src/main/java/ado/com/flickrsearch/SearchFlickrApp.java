package ado.com.flickrsearch;

import android.app.Application;

import ado.com.flickrsearch.network.FlickrParser;
import ado.com.flickrsearch.network.FlickrServiceApi;
import ado.com.flickrsearch.network.NetworkRequestManager;
import ado.com.flickrsearch.network.RequestManager;
import ado.com.flickrsearch.network.ServiceApi;


public class SearchFlickrApp extends Application {

    ServiceApi mServiceApi;

    @Override
    public void onCreate() {
        super.onCreate();
        final RequestManager requestManager = new NetworkRequestManager(new FlickrParser());
        mServiceApi = new FlickrServiceApi(requestManager);
    }

    public ServiceApi getServiceApi() {
        return mServiceApi;
    }
}
