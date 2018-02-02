package ado.com.flickrsearch.domain;

import android.support.annotation.Nullable;

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
        mImageUrl = null;
    }

    public void setImageUrl(URL url) {
        mImageUrl = url;
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
    @Nullable public URL getUrl() {
        return mImageUrl;
    }
}
