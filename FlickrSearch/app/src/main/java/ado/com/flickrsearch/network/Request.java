package ado.com.flickrsearch.network;

import java.net.URL;

public interface Request {

    URL getUrl();

    ExpectedResultType getType();

    enum ExpectedResultType {
        TEXT,
        BLOB
    }
}
