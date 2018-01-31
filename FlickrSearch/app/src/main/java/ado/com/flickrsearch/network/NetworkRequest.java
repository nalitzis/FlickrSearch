package ado.com.flickrsearch.network;

import java.net.URL;
import java.util.UUID;

public class NetworkRequest implements Request {
    private final UUID mId;
    private final URL mUrl;
    private final ExpectedResultType mType;

    NetworkRequest(URL url, ExpectedResultType type) {
        mId = UUID.randomUUID();
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
