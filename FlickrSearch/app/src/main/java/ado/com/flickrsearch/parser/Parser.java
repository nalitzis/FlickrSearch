package ado.com.flickrsearch.parser;

import java.io.IOException;
import java.net.URL;

import ado.com.flickrsearch.api.SearchResult;

public interface Parser {

    SearchResult parse(String input, URL url) throws IOException;
}
