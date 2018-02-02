package ado.com.flickrsearch.network;

import java.net.URL;

public class NetworkRequest implements Request {
    private final URL mUrl;
    private final ExpectedResultType mType;

    public NetworkRequest(URL url, ExpectedResultType type) {
        mUrl = url;
        mType = type;
    }

    @Override
    public URL getUrl() {
        return mUrl;
    }

    @Override
    public ExpectedResultType getType() {
        return mType;
    }
}
