package ado.com.flickrsearch.domain;

import java.util.List;

public interface SearchResult extends Result {

    List<ImageText> getImages();
}
