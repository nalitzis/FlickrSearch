package ado.com.flickrsearch.network;

import android.util.Log;

import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class NetworkRequestManager implements RequestManager, RequestManager.Listener {

    private static final String TAG = "NetworkRequestManager";

    private final ExecutorService mService;

    //private Future<Response> mCurrentRequest;

    private Map<URL, Future<Response>> mCurrentRequests;
    //private List<Future<Response>> mCurrentRequests;

    //TODO
    //understand what is best to do here: separate search request from img requests??
    public NetworkRequestManager() {
        mService = Executors.newSingleThreadExecutor();
        mCurrentRequests = Collections.synchronizedMap(new HashMap<URL, Future<Response>>());
        //mCurrentRequests = Collections.synchronizedList(new ArrayList<Future<Response>>());
    }

    @Override
    public void add(Request request) {
        Future<Response> response = mService.submit(new RequestExecutor(request, this));
        mCurrentRequests.put(request.getUrl(), response);
       // mService.execute(new RequestExecutor(request));
    }


    @Override
    public void cancel(URL requestUrl) {
        Future<Response> response = mCurrentRequests.remove(requestUrl);
        if(!response.isCancelled()) {
            response.cancel(true);
        }
    }

    @Override
    public void onCompleted(URL requestUrl, Response response) {
        // from background thread!
        Log.d(TAG, "onCompleted() " + requestUrl.toString());
        mCurrentRequests.remove(requestUrl);
    }

    @Override
    public void onError(URL requestUrl) {
        // from background thread!
    }
}
