package ado.com.flickrsearch.parser;

import android.util.JsonReader;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ado.com.flickrsearch.domain.FlickrImageResult;
import ado.com.flickrsearch.domain.FlickrSearchResult;
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
    public SearchResult parse(String input, URL url) throws IOException, IllegalStateException {
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

    private FlickrSearchResult getFlickrSearchResult(JsonReader reader, FlickrSearchResult searchResult) throws IOException, IllegalStateException {
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

    private FlickrSearchResult readPhotosObject(JsonReader reader, FlickrSearchResult searchResult) throws IOException, IllegalStateException {
        List<FlickrImageResult> flickrImageResults = new ArrayList<>(0);
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if(name.equals(PAGE)) {
                searchResult.setPage(reader.nextInt());
            } else if(name.equals(PAGES)) {
                reader.nextString();
            } else if(name.equals(TOTAL)) {
                searchResult.setTotalSize(reader.nextString());
            } else if(name.equals(PERPAGE)) {
                reader.nextInt();
            } else if(name.equals(PHOTO)) {
                flickrImageResults = readPhotoArray(reader);
            }
        }
        reader.endObject();
        searchResult.setImages(flickrImageResults);
        return searchResult;
    }


    private List<FlickrImageResult> readPhotoArray(final JsonReader reader) throws IOException, IllegalStateException {
        List<FlickrImageResult> flickrImages = new ArrayList<>();
        reader.beginArray();
        while (reader.hasNext()) {
            flickrImages.add(readPhotoElement(reader));
        }
        reader.endArray();
        return flickrImages;
    }

    private FlickrImageResult readPhotoElement(final JsonReader reader) throws IOException, IllegalStateException {
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
        return new FlickrImageResult(id, secret, server, farm);
    }
}
