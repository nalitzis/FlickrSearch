package ado.com.flickrsearch.network;

import java.net.URL;

public class NetworkResponse implements Response {
    private final URL mUrl;
    private final byte[] mContents;
    private final Type mType;

    NetworkResponse(final URL url, final byte[] contents, final Type type) {
        mUrl = url;
        mContents = contents;
        mType = type;
    }

    @Override
    public byte[] getContents() {
        return mContents;
    }

    @Override
    public Type getType() {
        return mType;
    }

    @Override
    public URL getUrl() {
        return mUrl;
    }
}
