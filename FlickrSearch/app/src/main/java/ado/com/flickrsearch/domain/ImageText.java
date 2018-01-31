package ado.com.flickrsearch.domain;

public class ImageText {
    private final String mId;
    private final String mSecret;
    private final String mServer;
    private final int mFarm;

    public ImageText(String id, String secret, String server, int farm) {
        mId = id;
        mSecret = secret;
        mServer = server;
        mFarm = farm;
    }

    public String getId() {
        return mId;
    }

    public String getSecret() {
        return mSecret;
    }

    public String getmServer() {
        return mServer;
    }

    public int getFarm() {
        return mFarm;
    }
}
