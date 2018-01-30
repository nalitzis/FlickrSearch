package ado.com.flickrsearch.network;

import java.util.UUID;

public interface ServiceApi {

    UUID search(String query, ApiListener listener);

    UUID fetchContent(long contentId, ApiListener listener);

    void cancel(UUID requestId);

    interface ApiListener {
        void onCompleted(Response response);

        void onError();

    }
}
