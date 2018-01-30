package ado.com.flickrsearch.network;

import java.net.URL;
import java.util.UUID;

public interface Request {

    UUID getId();

    URL getUrl();

    ExpectedResultType getType();

    enum ExpectedResultType {
        TEXT,
        BLOB
    }
}
