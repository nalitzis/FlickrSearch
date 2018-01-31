package ado.com.flickrsearch.network;

import java.net.URL;

public interface RequestManager {

    void add(Request request);

    void cancel(URL requestUrl);

    interface Listener {
        void onCompleted(URL requestUrl, Response response);

        void onError(URL requestUrl);
    }
}
