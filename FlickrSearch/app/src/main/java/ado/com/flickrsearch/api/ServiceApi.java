package ado.com.flickrsearch.api;

import java.net.URL;

public interface ServiceApi {

    void search(String query, Listener listener);

    void fetchImage(SearchResult searchResult, Listener listener);

    void cancel(URL requestUrl);

    interface Listener<T> {
        void onCompleted(T result);

        void onError(Exception e);

    }
}
