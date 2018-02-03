package ado.com.flickrsearch.api;

import java.util.List;

import ado.com.flickrsearch.domain.FlickrImageResult;

public interface SearchResult extends Result {

    List<FlickrImageResult> getImagesUrl();

    String getTotalSize();

    int getPage();
}
