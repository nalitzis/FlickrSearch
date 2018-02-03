package ado.com.flickrsearch.view;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.SparseArray;
import android.widget.ImageView;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import ado.com.flickrsearch.api.ImageResult;
import ado.com.flickrsearch.domain.FlickrImageResult;
import ado.com.flickrsearch.network.Utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class ImageAdapterTest {

    @Test
    public void testShouldRequestImage() {
        SearchActivity searchActivity = mock(SearchActivity.class);
        ImageAdapter adapter = new ImageAdapter(searchActivity);
        FlickrImageResult result = new FlickrImageResult("a", "b", "c", 1);
        adapter.configureImages(createListOfImages(result));
        adapter.getView(0, mock(ImageView.class), null);
        verify(searchActivity).requestNewImage(result,0);
    }

    @Test
    public void testShouldDrawBmp() {
        SearchActivity searchActivity = mock(SearchActivity.class);
        ImageView imageView = mock(ImageView.class);
        ImageAdapter adapter = new ImageAdapter(searchActivity);
        FlickrImageResult result = new FlickrImageResult("a", "b", "c", 1);

        try {
            Context testContext = InstrumentationRegistry.getInstrumentation().getContext();
            InputStream is = testContext.getAssets().open("test.jpg");
            result.setBitmap(Utils.readStream(is));
            adapter.configureImages(createListOfImages(result));
            adapter.getView(0, imageView, null);
            verify(imageView).setImageBitmap(result.getBitmap());
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void testShouldClearAdapter() {
        SearchActivity searchActivity = mock(SearchActivity.class);
        SparseArray<ImageResult> images = mock(SparseArray.class);
        when(images.size()).thenReturn(2);
        ImageAdapter adapter = new ImageAdapter(searchActivity, images);
        adapter.clear();
        verify(images).clear();
    }

    private List<? extends ImageResult> createListOfImages(FlickrImageResult result) {
        List<FlickrImageResult> list = new ArrayList<>();
        list.add(result);
        return list;
    }
}
