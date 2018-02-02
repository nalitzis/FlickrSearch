package ado.com.flickrsearch.network;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import ado.com.flickrsearch.api.ServiceApi;
import ado.com.flickrsearch.domain.FlickrImageResult;
import ado.com.flickrsearch.api.ImageResult;
import ado.com.flickrsearch.api.SearchResult;
import ado.com.flickrsearch.domain.FlickrImageUrl;
import ado.com.flickrsearch.domain.FlickrSearchResult;
import ado.com.flickrsearch.parser.FlickrParser;

public class FlickrRequestManager implements ServiceApi, RequestListener {

    private static final String TAG = "FlickrRequestManager";

    private static final String AMP = "&";

    private static final String API_KEY = "api_key=";
    private static final String API_KEY_VALUE = "7a3bf663e19d20c7bdc2bc355e5ae10e";

    private static final String FLICKR_API_ENDPOINT = "https://api.flickr.com/services/rest/?";
    private static final String METHOD = "method=";

    //search api
    private static final String SEARCH_API = "flickr.photos.search";
    private static final String TEXT = "text=";
    private static final String FORMAT = "format=json&nojsoncallback=1";

    //image api
    //example: https://farm{farm-id}.staticflickr.com/{server-id}/{id}_{secret}_[mstzb].jpg
    //q = 150px square
    private static final String SQUARE = "q";

    private final ExecutorService mService;

    private final Map<URL, Future<Response>> mCurrentTasksMap;
    private final Map<URL, ServiceApi.Listener> mListenersMap;

    private final FlickrParser mParser;

    private Handler mMainHandler = new Handler(Looper.getMainLooper());

    public FlickrRequestManager(final FlickrParser parser) {
        mParser = parser;
        mService = Executors.newSingleThreadExecutor();
        mCurrentTasksMap = Collections.synchronizedMap(new HashMap<URL, Future<Response>>());
        mListenersMap = Collections.synchronizedMap(new HashMap<URL, ServiceApi.Listener>());
    }

    @Override
    public void search(final String query, final Listener listener) {
        final URL url = buildSearchTextQuery(API_KEY_VALUE, SEARCH_API, query);
        doFetch(url, listener, Request.ExpectedResultType.TEXT);
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

    @Override
    public void fetchImage(URL imageUrl, Listener listener) {
        doFetch(imageUrl, listener, Request.ExpectedResultType.IMAGE);
    }

    private void doFetch(final URL url, final Listener listener, Request.ExpectedResultType requestType) {
        if (url != null) {
            Request request = new NetworkRequest(url, requestType);
            add(request, listener);
        } else {
            if (listener != null) {
                listener.onError(new Exception("url is null"));
            }
        }
    }

    private void add(Request request, ServiceApi.Listener listener) {
        Future<Response> task = mService.submit(new RequestExecutor(request, this));
        mCurrentTasksMap.put(request.getUrl(), task);
        mListenersMap.put(request.getUrl(), listener);
    }

    @Override
    public void cancel(URL requestUrl) {
        Future<Response> response = mCurrentTasksMap.remove(requestUrl);
        mListenersMap.remove(requestUrl);
        if (!response.isCancelled()) {
            response.cancel(true);
        }
    }

    ///////// RequestExecutor callbacks (from background thread) ////////
    @Override
    public void onCompleted(final URL requestUrl, final Response response) {
        Log.d(TAG, "onCompleted() " + requestUrl.toString());
        switch (response.getType()) {
            //TODO extract methods
            case TEXT:
                try {
                    final String contents = new String(response.getContents(), "UTF-8");
                    final FlickrSearchResult searchResult = (FlickrSearchResult) mParser.parse(contents, requestUrl);
                    generateImageUrls(searchResult);
                    final ServiceApi.Listener<SearchResult> listener = mListenersMap.get(requestUrl);
                    if (listener != null) {
                        mMainHandler.post(() -> listener.onCompleted(searchResult));
                    }
                } catch (IOException e) {
                    onError(requestUrl, e);
                } finally {
                    removeRequest(requestUrl);
                }
                break;
            case IMAGE:
                final ImageResult imageResult = new FlickrImageResult(response);
                final ServiceApi.Listener<ImageResult> listener = mListenersMap.remove(requestUrl);
                if (listener != null) {
                    mMainHandler.post(() -> listener.onCompleted(imageResult));
                }
                break;
        }
    }

    private Listener removeRequest(URL requestUrl) {
        mCurrentTasksMap.remove(requestUrl);
        return mListenersMap.remove(requestUrl);
    }

    private void generateImageUrls(final FlickrSearchResult result) throws MalformedURLException {
        for (FlickrImageUrl img : result.getImagesUrl()) {
            final String url = String.format(Locale.US, "https://farm%d.staticflickr.com/%s/%s_%s_%s.jpg",
                    img.getFarm(), img.getmServer(), img.getId(), img.getSecret(), SQUARE);
            img.setImageUrl(new URL(url));
        }
    }

    @Override
    public void onError(URL requestUrl, Exception e) {
        final Listener listener = removeRequest(requestUrl);
        if (listener != null) {
            mMainHandler.post(() -> onError(requestUrl, e));
        }
    }
}
