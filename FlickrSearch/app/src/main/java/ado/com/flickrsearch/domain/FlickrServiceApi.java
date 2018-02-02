package ado.com.flickrsearch.domain;

import android.support.annotation.Nullable;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

import ado.com.flickrsearch.api.ServiceApi;
import ado.com.flickrsearch.api.SearchResult;
import ado.com.flickrsearch.network.NetworkRequest;
import ado.com.flickrsearch.network.Request;
import ado.com.flickrsearch.network.RequestManager;

public class FlickrServiceApi implements ServiceApi {

    private static final String TAG = "FlickrServiceApi";

    private static final String AMP = "&";

    private static final String API_KEY = "api_key=";
    private static final String API_KEY_VALUE = "7a3bf663e19d20c7bdc2bc355e5ae10e";

    private static final String FLICKR_API_ENDPOINT = "https://api.flickr.com/services/rest/?";
    private static final String METHOD = "method=";

    //search api
    private static final String SEARCH_API = "flickr.photos.search";
    private static final String TEXT = "text=";
    private static final String FORMAT = "format=json&nojsoncallback=1";

    //picture api
    //https://farm1.staticflickr.com/2/1418878_1e92283336_m.jpg

    private final RequestManager mRequestManager;

    public FlickrServiceApi(final RequestManager requestManager) {
        mRequestManager = requestManager;
    }

    @Override
    public void search(final String query, final Listener listener) {
        final URL url = buildSearchTextQuery(API_KEY_VALUE, SEARCH_API, query);
        doFetch(url, listener, Request.ExpectedResultType.TEXT);
    }

    @Override
    public void fetchImage(SearchResult textResult, Listener listener) {
        final URL url = buildImageQuery(textResult);
        doFetch(url, listener, Request.ExpectedResultType.IMAGE);
    }

    private void doFetch(final URL url, final Listener listener, Request.ExpectedResultType requestType) {
        if (url != null) {
            Request request = new NetworkRequest(url, requestType);
            mRequestManager.add(request, listener);
        } else {
            if (listener != null) {
                listener.onError(new Exception("url is null"));
            }
        }
    }

    @Override
    public void cancel(URL requestUrl) {
        mRequestManager.cancel(requestUrl);
    }

    @Nullable
    private URL buildSearchTextQuery(String apiKeyValue, String method, String searchText) {
        StringBuilder sb = new StringBuilder();
        sb.append(FLICKR_API_ENDPOINT).append(METHOD).append(method)
                .append(AMP).append(API_KEY).append(apiKeyValue)
                .append(AMP).append(TEXT).append(searchText)
                .append(AMP).append(FORMAT);
        try {
            return new URL(sb.toString());
        } catch (MalformedURLException e) {
            Log.e(TAG, "malformed url: " + e.getMessage());
        }
        return null;
    }

    @Nullable
    private URL buildImageQuery(SearchResult result) {
        //TODO build Url for getting image
        try {
            return new URL("https://farm1.staticflickr.com/2/1418878_1e92283336_m.jpg");
        } catch (MalformedURLException e) {
            Log.e(TAG, "malformed url: " + e.getMessage());
        }
        return null;
    }
}
