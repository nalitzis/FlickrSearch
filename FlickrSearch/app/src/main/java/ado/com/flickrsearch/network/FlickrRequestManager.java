package ado.com.flickrsearch.network;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
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
    private static final String PAGE = "page=";
    private static final String FORMAT = "format=json&nojsoncallback=1";

    //image api
    //example: https://farm{farm-id}.staticflickr.com/{server-id}/{id}_{secret}_[mstzb].jpg
    //q = 150px square
    private static final String SQUARE = "q";

    private final ExecutorService mService;

    private final Map<String, Future<Response>> mCurrentTasksMap;
    private final Map<String, ServiceApi.Listener> mListenersMap;
    private final Map<String, FlickrImageResult> mPendingImageRequests;

    private final FlickrParser mParser;

    private Handler mMainHandler = new Handler(Looper.getMainLooper());

    private static final int NUM_OF_THREADS = 4;

    public FlickrRequestManager(final FlickrParser parser) {
        this(parser, Collections.synchronizedMap(new HashMap<>()), Collections.synchronizedMap(new HashMap<>()),
                Collections.synchronizedMap(new HashMap<>()), Executors.newFixedThreadPool(NUM_OF_THREADS));
    }

    @VisibleForTesting
    FlickrRequestManager(final FlickrParser parser, final Map<String, Future<Response>> tasks,
                         final Map<String, ServiceApi.Listener> listeners, final Map<String, FlickrImageResult> pendingImageRequests,
                         final ExecutorService service) {
        mParser = parser;
        mCurrentTasksMap = tasks;
        mListenersMap = listeners;
        mPendingImageRequests = pendingImageRequests;
        mService = service;
    }

    @Override
    public void search(final String query, final String pageNumber, final Listener listener) {
        final URL url = buildSearchTextQuery(API_KEY_VALUE, SEARCH_API, query, pageNumber);
        doFetch(url, listener, Request.ExpectedResultType.TEXT);
    }

    //URL: https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=a87d1a33ac9b5c4a06591b64f15eead2&text=kittens&page=1&format=json&nojsoncallback=1&api_sig=95e07a28fa601b0071a2c550c90f900f
    @Nullable
    private URL buildSearchTextQuery(final String apiKeyValue, final String method, final String searchText, final String pageNumber) {
        StringBuilder sb = new StringBuilder();
        sb.append(FLICKR_API_ENDPOINT).append(METHOD).append(method)
                .append(AMP).append(API_KEY).append(apiKeyValue)
                .append(AMP).append(TEXT).append(searchText)
                .append(AMP).append(PAGE).append(pageNumber)
                .append(AMP).append(FORMAT);
        try {
            return new URL(sb.toString());
        } catch (MalformedURLException e) {
            Log.e(TAG, "malformed url: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void fetchImage(final ImageResult imageResult, final Listener listener) {
        mPendingImageRequests.put(imageResult.getUrl(), (FlickrImageResult)imageResult);
        try {
            final URL url = new URL(imageResult.getUrl());
            doFetch(url, listener, Request.ExpectedResultType.IMAGE);
        } catch (MalformedURLException e) {
            Log.e(TAG, "malformed url: " + e.getMessage());
        }

    }

    private void doFetch(final URL url, final Listener listener, final Request.ExpectedResultType requestType) {
        if (mListenersMap.containsKey(url.toString())) {
            return;
        }

        final Request request = new NetworkRequest(url, requestType);
        add(request, listener);
    }

    private void add(final Request request, final ServiceApi.Listener listener) {
        Future<Response> task = mService.submit(new RequestExecutor(request, this));
        mCurrentTasksMap.put(request.getUrl().toString(), task);
        mListenersMap.put(request.getUrl().toString(), listener);
    }

    @Override
    public void cancel(final String requestUrl) {
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
            case TEXT:
                try {
                    final String contents = new String(response.getContents(), "UTF-8");
                    final FlickrSearchResult searchResult = (FlickrSearchResult) mParser.parse(contents, requestUrl);
                    configureImageResult(searchResult);
                    final ServiceApi.Listener<SearchResult> listener = mListenersMap.get(requestUrl.toString());
                    if (listener != null) {
                        mMainHandler.post(() -> listener.onCompleted(searchResult));
                    }
                } catch (final IOException | IllegalStateException e) {
                    onError(requestUrl, e);
                } finally {
                    removeRequest(requestUrl.toString());
                }
                break;
            case IMAGE:
                final FlickrImageResult imageResult = mPendingImageRequests.get(requestUrl.toString());
                imageResult.setBitmap(response.getContents());
                final ServiceApi.Listener<ImageResult> listener = mListenersMap.get(requestUrl.toString());
                if (listener != null) {
                    mMainHandler.post(() -> listener.onCompleted(imageResult));
                }
                removeRequest(requestUrl.toString());
                break;
        }
    }

    private Listener removeRequest(final String requestUrl) {
        mPendingImageRequests.remove(requestUrl);
        mCurrentTasksMap.remove(requestUrl);
        return mListenersMap.remove(requestUrl);
    }

    private void configureImageResult(final FlickrSearchResult result) throws MalformedURLException {
        int i = 0;
        for (final FlickrImageResult img : result.getImagesUrl()) {
            final String url = String.format(Locale.US, "https://farm%d.staticflickr.com/%s/%s_%s_%s.jpg",
                    img.getFarm(), img.getServer(), img.getId(), img.getSecret(), SQUARE);
            img.setUrl(url);
            img.setIndex(FlickrSearchResult.getIndex(result.getPage(), i));
            i++;
        }
    }

    @Override
    public void onError(final URL requestUrl, final Exception e) {
        final Listener listener = removeRequest(requestUrl.toString());
        if (listener != null) {
            mMainHandler.post(() -> onError(requestUrl, e));
        }
    }
}
