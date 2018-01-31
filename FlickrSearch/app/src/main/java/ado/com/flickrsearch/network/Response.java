package ado.com.flickrsearch.network;

import java.net.URL;

public interface Response {

    enum Type {
        TEXT,
        IMAGE
    }

    byte[] getContents();

    Type getType();

    URL getUrl();

}
