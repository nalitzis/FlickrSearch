package ado.com.flickrsearch.network;

import java.io.IOException;
import java.net.URL;

import ado.com.flickrsearch.domain.SearchResult;

public interface Parser {

    SearchResult parse(String input, URL url) throws IOException;
}
