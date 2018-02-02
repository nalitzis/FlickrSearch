package ado.com.flickrsearch.domain;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.net.URL;

import ado.com.flickrsearch.api.ImageResult;
import ado.com.flickrsearch.network.Response;

public class FlickrImageResult implements ImageResult {
    private final URL mUrl;
    private final Bitmap mBitmap;

    public FlickrImageResult(final Response response) {
        mUrl = response.getUrl();
        byte[] data = response.getContents();
        mBitmap = createBitmap(data);
    }

    private Bitmap createBitmap(final byte[] data) {
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }

    @Override
    public URL getUrl() {
        return mUrl;
    }

    @Override
    public Bitmap getBitmap() {
        return mBitmap;
    }
}
