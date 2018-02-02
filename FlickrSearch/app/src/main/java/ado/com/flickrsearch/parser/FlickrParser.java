package ado.com.flickrsearch.parser;

import android.util.JsonReader;
import android.util.Log;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ado.com.flickrsearch.domain.FlickrSearchResult;
import ado.com.flickrsearch.domain.FlickrImageUrl;
import ado.com.flickrsearch.api.SearchResult;

public class FlickrParser implements Parser {
    private static final String TAG = "FlickrParser";

    private static final String PHOTOS = "photos";
    private static final String STAT = "stat";
    private static final String PAGE = "page";
    private static final String PAGES = "pages";
    private static final String PERPAGE = "perpage";
    private static final String TOTAL = "total";
    private static final String PHOTO = "photo";

    private static final String ID = "id";
    private static final String SECRET = "secret";
    private static final String SERVER = "server";
    private static final String FARM = "farm";
    private static final String OWNER = "owner";
    private static final String TITLE = "title";
    private static final String ISFAMILY = "isfamily";
    private static final String ISFRIEND = "isfriend";
    private static final String ISPUBLIC = "ispublic";


    @Override
    public SearchResult parse(String input, URL url) throws IOException {
        final StringReader stringReader = new StringReader(input);
        JsonReader reader = new JsonReader(stringReader);
        FlickrSearchResult searchResult = new FlickrSearchResult(url);
        try {
            searchResult = getFlickrSearchResult(reader, searchResult);
        } finally {
            reader.close();
        }
        return searchResult;
    }

    private FlickrSearchResult getFlickrSearchResult(JsonReader reader, FlickrSearchResult searchResult) throws IOException {
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals(PHOTOS)) {
                searchResult = readPhotosObject(reader, searchResult);
            } else if(name.equals(STAT)) {
                reader.nextString();
            }
        }
        reader.endObject();
        return searchResult;
    }

    private FlickrSearchResult readPhotosObject(JsonReader reader, FlickrSearchResult searchResult) throws IOException {
        List<FlickrImageUrl> flickrImageUrls = new ArrayList<>(0);
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if(name.equals(PAGE)) {
                long l = reader.nextLong();
            } else if(name.equals(PAGES) || name.equals(TOTAL)) {
                reader.nextString();
            } else if(name.equals(PERPAGE)) {
                reader.nextLong();
            } else if(name.equals(PHOTO)) {
                flickrImageUrls = readPhotoArray(reader);
            }
        }
        reader.endObject();
        searchResult.setImages(flickrImageUrls);
        return searchResult;
    }


    private List<FlickrImageUrl> readPhotoArray(final JsonReader reader) throws IOException {
        List<FlickrImageUrl> flickrImageUrls = new ArrayList<>();
        reader.beginArray();
        while (reader.hasNext()) {
            flickrImageUrls.add(readPhotoElement(reader));
        }
        reader.endArray();
        return flickrImageUrls;
    }

    private FlickrImageUrl readPhotoElement(final JsonReader reader) throws IOException {
        String server = "";
        String secret = "";
        String id = "";
        int farm = 0;
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if(name.equals(ID)) {
                id = reader.nextString();
            } else if(name.equals(SECRET)) {
                secret = reader.nextString();
            } else if(name.equals(SERVER)) {
                server = reader.nextString();
            } else if(name.equals(FARM)) {
                farm = reader.nextInt();
            } else if(name.equals(OWNER) || name.equals(TITLE)) {
                reader.nextString();
            } else if(name.equals(ISFAMILY) || name.equals(ISFRIEND) || name.equals(ISPUBLIC)) {
                reader.nextInt();
            }
        }
        reader.endObject();
        return new FlickrImageUrl(id, secret, server, farm);
    }
}
