package ado.com.flickrsearch;

import android.app.Application;

import ado.com.flickrsearch.parser.FlickrParser;
import ado.com.flickrsearch.domain.FlickrServiceApi;
import ado.com.flickrsearch.network.NetworkRequestManager;
import ado.com.flickrsearch.network.RequestManager;
import ado.com.flickrsearch.api.ServiceApi;


public class SearchFlickrApp extends Application {

    private ServiceApi mServiceApi;

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
