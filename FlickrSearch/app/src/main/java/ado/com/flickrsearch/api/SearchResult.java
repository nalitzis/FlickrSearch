package ado.com.flickrsearch.api;

import java.util.List;

public interface SearchResult extends Result {

    List<? extends ImageUrl> getImagesUrl();
}
