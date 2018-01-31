package ado.com.flickrsearch.domain;

import java.net.URL;
import java.util.List;

public class FlickrSearchResult implements SearchResult {
    private URL mUrl;
    private List<ImageText> mImages;

    public FlickrSearchResult(URL url) {
        mUrl = url;
    }

    public void setImages(List<ImageText> images) {
        mImages = images;
    }

    @Override
    public URL getUrl() {
        return mUrl;
    }

    @Override
    public List<ImageText> getImages() {
        return mImages;
    }
}
