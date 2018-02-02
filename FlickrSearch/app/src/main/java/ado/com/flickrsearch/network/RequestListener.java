package ado.com.flickrsearch.network;

import java.net.URL;

interface RequestListener {
    void onCompleted(URL requestUrl, Response response);

    void onError(URL requestUrl, Exception e);
}