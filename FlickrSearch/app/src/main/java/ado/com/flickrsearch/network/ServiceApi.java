package ado.com.flickrsearch.network;

import java.net.URL;

import ado.com.flickrsearch.domain.ImageResult;
import ado.com.flickrsearch.domain.SearchResult;

public interface ServiceApi {

    URL search(String query, Listener listener);

    URL fetchImage(SearchResult searchResult, Listener listener);

    void cancel(URL requestUrl);

    interface SearchListener {
        void onCompleted(SearchResult searchResult);

        void onError();

    }

    interface Listener<T> {
        void onCompleted(T result);

        void onError();

    }

    interface ImageListener {
        void onCompleted(ImageResult imageResult);

        void onError();

    }
}
