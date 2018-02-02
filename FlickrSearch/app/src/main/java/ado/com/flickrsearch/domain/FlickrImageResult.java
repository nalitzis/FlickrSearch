package ado.com.flickrsearch.domain;

import java.net.URL;

import ado.com.flickrsearch.api.ImageResult;
import ado.com.flickrsearch.network.Response;

public class FlickrImageResult implements ImageResult {
    private final URL mUrl;
    private final byte[] mImageData;

    public FlickrImageResult(final Response response) {
        mUrl = response.getUrl();
        mImageData = response.getContents();
    }

    @Override
    public URL getUrl() {
        return mUrl;
    }

    @Override
    public byte[] getData() {
        return mImageData;
    }
}
