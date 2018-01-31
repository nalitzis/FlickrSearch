package ado.com.flickrsearch.network;

import java.net.URL;

public interface RequestManager {

    void add(Request request, ServiceApi.Listener listener);

    void cancel(URL requestUrl);

    interface RequestListener {
        void onCompleted(URL requestUrl, Response response);

        void onError(URL requestUrl, Exception e);
    }
}
