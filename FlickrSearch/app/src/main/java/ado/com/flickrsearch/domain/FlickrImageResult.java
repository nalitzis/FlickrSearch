package ado.com.flickrsearch.domain;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.net.URL;

import ado.com.flickrsearch.api.ImageResult;

public class FlickrImageResult implements ImageResult {
    private URL mUrl;
    private Bitmap mBitmap;
    private int mIndex;
    private final String mId;
    private final String mSecret;
    private final String mServer;
    private final int mFarm;

    public FlickrImageResult(String id, String secret, String server, int farm) {
        mId = id;
        mSecret = secret;
        mServer = server;
        mFarm = farm;
        mUrl = null;
    }

    public String getId() {
        return mId;
    }

    public String getSecret() {
        return mSecret;
    }

    public String getServer() {
        return mServer;
    }

    public int getFarm() {
        return mFarm;
    }


    @Override
    public URL getUrl() {
        return mUrl;
    }

    @Override
    public Bitmap getBitmap() {
        return mBitmap;
    }

    @Override
    public int getIndex() {
        return mIndex;
    }

    public void setIndex(int index) {
        mIndex = index;
    }

    public void setUrl(URL url) {
        mUrl = url;
    }

    public void setBitmap(byte[] data) {
        mBitmap = createBitmap(data);
    }

    private Bitmap createBitmap(final byte[] data) {
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }

}
