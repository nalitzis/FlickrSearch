package ado.com.flickrsearch.network;

import java.io.InputStream;

public interface Response {

    enum Type {
        TEXT,
        IMAGE
    }

    byte[] getContents();

    Type getType();
}
