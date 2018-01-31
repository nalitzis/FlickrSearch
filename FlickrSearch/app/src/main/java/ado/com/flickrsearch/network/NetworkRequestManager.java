package ado.com.flickrsearch.network;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import ado.com.flickrsearch.domain.SearchResult;

public class NetworkRequestManager implements RequestManager, RequestManager.RequestListener {

    private static final String TAG = "NetworkRequestManager";

    private final ExecutorService mService;

    //private Future<Response> mCurrentRequest;

    private final Map<URL, Future<Response>> mCurrentTasksMap;
    private final Map<URL, ServiceApi.Listener> mListenersMap;

    private final Parser mParser;

    private Handler mMainHandler = new Handler(Looper.getMainLooper());
    //private List<Future<Response>> mCurrentTasksMap;

    //TODO
    //understand what is best to do here: separate search request from img requests??
    public NetworkRequestManager(Parser parser) {
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
       // mService.execute(new RequestExecutor(request));
    }


    @Override
    public void cancel(URL requestUrl) {
        Future<Response> response = mCurrentTasksMap.remove(requestUrl);
        mListenersMap.remove(requestUrl);
        if(!response.isCancelled()) {
            response.cancel(true);
        }
    }

    @Override
    public void onCompleted(final URL requestUrl, final Response response) {
        // from background thread!
        Log.d(TAG, "onCompleted() " + requestUrl.toString());
        if(response.getType() == Response.Type.TEXT) {
            //TODO convert to string response.getContents()
            final SearchResult searchResult = mParser.parse("");
            mCurrentTasksMap.remove(requestUrl);
            final ServiceApi.Listener<SearchResult> listener = mListenersMap.remove(requestUrl);
            //TODO post on main thread!
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    listener.onCompleted(searchResult);
                }
            });

        } else if(response.getType() == Response.Type.IMAGE) {
            //TODO build ImageResult and deliver
        }
    }

    @Override
    public void onError(URL requestUrl) {
        // from background thread!
    }
}
