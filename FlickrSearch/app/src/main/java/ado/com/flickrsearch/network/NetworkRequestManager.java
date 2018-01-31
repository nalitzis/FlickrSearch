package ado.com.flickrsearch.network;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import ado.com.flickrsearch.domain.FlickrImageResult;
import ado.com.flickrsearch.domain.ImageResult;
import ado.com.flickrsearch.domain.SearchResult;

public class NetworkRequestManager implements RequestManager, RequestManager.RequestListener {

    private static final String TAG = "NetworkRequestManager";

    private final ExecutorService mService;

    private final Map<URL, Future<Response>> mCurrentTasksMap;
    private final Map<URL, ServiceApi.Listener> mListenersMap;

    private final Parser mParser;

    private Handler mMainHandler = new Handler(Looper.getMainLooper());

    //TODO
    //understand what is best to do here: separate search request from img requests??
    public NetworkRequestManager(final Parser parser) {
        mParser = parser;
        mService = Executors.newSingleThreadExecutor();
        mCurrentTasksMap = Collections.synchronizedMap(new HashMap<URL, Future<Response>>());
        mListenersMap = Collections.synchronizedMap(new HashMap<URL, ServiceApi.Listener>());
    }

    @Override
    public void add(Request request, ServiceApi.Listener listener) {
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

    @Override
    public void onCompleted(final URL requestUrl, final Response response) {
        Log.d(TAG, "onCompleted() " + requestUrl.toString());
        switch (response.getType()) {
            //TODO extract methods
            case TEXT:
                final String contents;
                try {
                    contents = new String(response.getContents(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    onError(requestUrl, e);
                    return;
                }

                final SearchResult searchResult;
                try {
                    searchResult = mParser.parse(contents, requestUrl);
                } catch (IOException e) {
                    onError(requestUrl, e);
                    return;
                }

                mCurrentTasksMap.remove(requestUrl);
                final ServiceApi.Listener<SearchResult> listener = mListenersMap.remove(requestUrl);
                if (listener != null) {
                    mMainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onCompleted(searchResult);
                        }
                    });
                }
                break;
            case IMAGE:
                final ImageResult imageResult = new FlickrImageResult(response);
                final ServiceApi.Listener<ImageResult> listener2 = mListenersMap.remove(requestUrl);
                if(listener2 != null) {
                    mMainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener2.onCompleted(imageResult);
                        }
                    });
                }
                break;
        }
    }


    @Override
    public void onError(URL requestUrl, Exception e) {
        mCurrentTasksMap.remove(requestUrl);
        final ServiceApi.Listener listener = mListenersMap.remove(requestUrl);
        listener.onError(e);
    }
}
