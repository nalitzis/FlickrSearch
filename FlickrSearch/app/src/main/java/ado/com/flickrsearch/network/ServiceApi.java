package ado.com.flickrsearch.network;

import java.net.URL;

import ado.com.flickrsearch.domain.ContentResult;
import ado.com.flickrsearch.domain.TextResult;

public interface ServiceApi {

    URL search(String query, SearchListener listener);

    URL fetchContent(long contentId, ContentListener listener);

    void cancel(URL requestUrl);

    interface SearchListener {
        void onCompleted(TextResult searchResult);

        void onError();

    }

    interface ContentListener {
        void onCompleted(ContentResult imageResult);

        void onError();

    }
}
