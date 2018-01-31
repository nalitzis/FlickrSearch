package ado.com.flickrsearch.network;

import android.support.annotation.Nullable;
import android.util.JsonReader;
import android.util.Log;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ado.com.flickrsearch.domain.FlickrSearchResult;
import ado.com.flickrsearch.domain.ImageText;
import ado.com.flickrsearch.domain.SearchResult;

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
                Log.d(TAG, "read photos");
                searchResult = readPhotosObject(reader, searchResult);
            } else if(name.equals(STAT)) {
                reader.nextString();
            }
        }
        reader.endObject();
        return searchResult;
    }

    private FlickrSearchResult readPhotosObject(JsonReader reader, FlickrSearchResult searchResult) throws IOException {
        List<ImageText> imageTexts = new ArrayList<>(0);
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if(name.equals(PAGE)) {
                long l = reader.nextLong();
                Log.d(TAG, "read PAGE " + l);
            } else if(name.equals(PAGES) || name.equals(TOTAL)) {
                reader.nextString();
            } else if(name.equals(PERPAGE)) {
                reader.nextLong();
            } else if(name.equals(PHOTO)) {
                imageTexts = readPhotoArray(reader);
            }
        }
        Log.d(TAG, "end photos");
        reader.endObject();
        searchResult.setImages(imageTexts);
        return searchResult;
    }


    private List<ImageText> readPhotoArray(final JsonReader reader) throws IOException {
        List<ImageText> imageTexts = new ArrayList<>();
        reader.beginArray();
        int i = 0;
        while (reader.hasNext()) {
            Log.d(TAG, "reading photo element " + i);
            imageTexts.add(readPhotoElement(reader));
            Log.d(TAG, "read photo element" + i);
            i++;
        }
        reader.endArray();
        return imageTexts;
    }

    private ImageText readPhotoElement(final JsonReader reader) throws IOException {
        String server = "";
        String secret = "";
        String id = "";
        int farm = 0;
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if(name.equals(ID)) {
                id = reader.nextString();
                Log.d(TAG, "read ID " + id);
            } else if(name.equals(SECRET)) {
                secret = reader.nextString();
                Log.d(TAG, "read SECRET " + secret);
            } else if(name.equals(SERVER)) {
                server = reader.nextString();
                Log.d(TAG, "read SERVER " + server);
            } else if(name.equals(FARM)) {
                farm = reader.nextInt();
                Log.d(TAG, "read LONG " + farm);
            } else if(name.equals(OWNER) || name.equals(TITLE)) {
                reader.nextString();
            } else if(name.equals(ISFAMILY) || name.equals(ISFRIEND) || name.equals(ISPUBLIC)) {
                reader.nextInt();
            }
        }
        reader.endObject();
        return new ImageText(id, secret, server, farm);
    }
}
