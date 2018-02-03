package ado.com.flickrsearch.parser;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import ado.com.flickrsearch.api.SearchResult;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class ParserTest {

    @Test
    public void testReadSuccessful() {
        FlickrParser parser = new FlickrParser();
        Context testContext = InstrumentationRegistry.getInstrumentation().getContext();
        try {
            InputStream testInput = testContext.getAssets().open("sample.json");
            SearchResult result = parser.parse(fromInputStreamToString(testInput),  new URL("http://someurl.com"));
            assertEquals(result.getPage(), 1);
            assertEquals(result.getTotalSize(), "245349");
            assertEquals(result.getImagesUrl().size(), 100);
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void testReadUnsuccessful() {
        FlickrParser parser = new FlickrParser();
        Context testContext = InstrumentationRegistry.getInstrumentation().getContext();
        try {
            InputStream testInput = testContext.getAssets().open("sample-invalid.json");
            SearchResult result = parser.parse(fromInputStreamToString(testInput),  new URL("http://someurl.com"));
            fail();
        } catch (IOException | IllegalStateException e) {
            assertNotNull(e);
        }
    }

    private String fromInputStreamToString(final InputStream is) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = is.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toString("UTF-8");
    }
}
