package ado.com.flickrsearch.api;

import android.support.annotation.Nullable;

import java.net.URL;

public interface ServiceApi {

    void search(String query, String page, Listener listener);

    void fetchImage(ImageResult image, Listener listener);

    void cancel(URL requestUrl);

    interface Listener<T> {
        void onCompleted(@Nullable T result);

        void onError(Exception e);
    }
}
