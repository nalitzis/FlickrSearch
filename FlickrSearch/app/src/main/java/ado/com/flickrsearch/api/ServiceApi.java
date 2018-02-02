package ado.com.flickrsearch.api;

import android.support.annotation.Nullable;

import java.net.URL;

public interface ServiceApi {

    void search(String query, Listener listener);

    void fetchImage(URL imageUrl, Listener listener);

    void cancel(URL requestUrl);

    interface Listener<T> {
        void onCompleted(@Nullable T result);

        void onError(Exception e);
    }
}
