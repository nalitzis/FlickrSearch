package ado.com.flickrsearch.network;

import android.support.annotation.Nullable;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

public class FlickrServiceApi implements ServiceApi {

    private static final String TAG = "FlickrServiceApi";

    private static final String AMP = "&";

    private static final String API_KEY = "api_key=";
    private static final String API_KEY_VALUE = "7a3bf663e19d20c7bdc2bc355e5ae10e";

    private static final String FLICKR_API_ENDPOINT = "https://api.flickr.com/services/rest/?";
    private static final String METHOD = "method=";
    private static final String SEARCH_API = "flickr.photos.search";

    private static final String TEXT = "text=";
    private static final String FORMAT = "format=json&nojsoncallback=1";

    private final RequestManager mRequestManager;

    public FlickrServiceApi(final RequestManager requestManager) {
        mRequestManager = requestManager;
    }

    @Override
    public URL search(final String query, final SearchListener listener) {
        URL url = buildSearchTextQuery(API_KEY_VALUE, SEARCH_API, query);
        if (url != null) {
            Request request = new NetworkRequest(url, Request.ExpectedResultType.TEXT);
            mRequestManager.add(request);
            return request.getUrl();
        } else {
            if (listener != null) {
                listener.onError();
            }
            return null;
        }
    }

    @Override
    public URL fetchContent(long contentId, ContentListener listener) {
        URL url = buildImageQuery();
        //TODO avoid code duplication with method above
        if (url != null) {
            Request request = new NetworkRequest(url, Request.ExpectedResultType.BLOB);
            mRequestManager.add(request);
            return request.getUrl();
        } else {
            if (listener != null) {
                listener.onError();
            }
            return null;
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
    private URL buildImageQuery() {
        //TODO build Url for getting image
        return null;
    }
}
