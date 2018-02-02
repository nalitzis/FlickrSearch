package ado.com.flickrsearch.domain;

import java.net.URL;
import java.util.List;

import ado.com.flickrsearch.api.SearchResult;

public class FlickrSearchResult implements SearchResult {
    private URL mUrl;
    private List<FlickrImageUrl> mImages;

    public FlickrSearchResult(URL url) {
        mUrl = url;
    }

    public void setImages(List<FlickrImageUrl> images) {
        mImages = images;
    }

    @Override
    public URL getUrl() {
        return mUrl;
    }

    @Override
    public List<FlickrImageUrl> getImagesUrl() {
        return mImages;
    }
}
