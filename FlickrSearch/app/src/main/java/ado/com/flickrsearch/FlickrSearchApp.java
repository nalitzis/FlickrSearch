package ado.com.flickrsearch;

import android.app.Application;

import ado.com.flickrsearch.network.FlickrRequestManager;
import ado.com.flickrsearch.parser.FlickrParser;
import ado.com.flickrsearch.api.ServiceApi;


public class FlickrSearchApp extends Application {

    private ServiceApi mServiceApi;

    @Override
    public void onCreate() {
        super.onCreate();
        mServiceApi = new FlickrRequestManager(new FlickrParser());
    }

    public ServiceApi getServiceApi() {
        return mServiceApi;
    }
}
