package ado.com.flickrsearch.network;

import java.util.UUID;

public interface RequestManager {

    void add(Request request);

    void cancel(UUID requestId);
}
