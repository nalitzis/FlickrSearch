package ado.com.flickrsearch.domain;

import java.net.URL;

import ado.com.flickrsearch.api.ImageUrl;

public class FlickrImageUrl implements ImageUrl {
    private final String mId;
    private final String mSecret;
    private final String mServer;
    private final int mFarm;
    private URL mImageUrl;

    public FlickrImageUrl(String id, String secret, String server, int farm) {
        mId = id;
        mSecret = secret;
        mServer = server;
        mFarm = farm;
        //TODO convert params to url
        mImageUrl = null;
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

    @Override
    public URL getUrl() {
        return mImageUrl;
    }
}
