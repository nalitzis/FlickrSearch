package ado.com.flickrsearch.domain;

import java.util.List;

import ado.com.flickrsearch.api.SearchResult;

public class FlickrSearchResult implements SearchResult {
    private String mUrl;
    private int mPage;
    private String mTotalSize;
    private List<FlickrImageResult> mImages;
    private static final int PAGE_SIZE = 100;
    private static final int FIRST_PAGE_INDEX = 1;

    public FlickrSearchResult(String url) {
        mUrl = url;
    }

    public void setImages(List<FlickrImageResult> images) {
        mImages = images;
    }

    public void setTotalSize(String size) {
        mTotalSize = size;
    }

    public void setPage(int page) {
        mPage = page;
    }

    @Override
    public String getUrl() {
        return mUrl;
    }

    @Override
    public List<FlickrImageResult> getImagesUrl() {
        return mImages;
    }

    @Override
    public String getTotalSize() {
        return mTotalSize;
    }

    @Override
    public int getPage() {
        return mPage;
    }

    public static int getIndex(int page, int offset) {
        return PAGE_SIZE * (page - FIRST_PAGE_INDEX) + offset;
    }

    public static int getPageFromIndex(int index) {
        return index/PAGE_SIZE + FIRST_PAGE_INDEX;
    }
}
