package ado.com.flickrsearch.network;

import ado.com.flickrsearch.domain.SearchResult;

public interface Parser {

    SearchResult parse(String input);
}
