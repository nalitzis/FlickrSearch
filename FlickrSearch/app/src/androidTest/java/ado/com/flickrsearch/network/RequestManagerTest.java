package ado.com.flickrsearch.network;

import android.support.annotation.Nullable;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import ado.com.flickrsearch.api.ServiceApi;
import ado.com.flickrsearch.domain.FlickrImageResult;
import ado.com.flickrsearch.network.FlickrRequestManager;
import ado.com.flickrsearch.network.Response;
import ado.com.flickrsearch.parser.FlickrParser;

import static junit.framework.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;


@RunWith(AndroidJUnit4.class)
public class RequestManagerTest {


    @Test
    public void shouldNotAddRequest() {
        //GIVEN
        Map currentTasksMap = Mockito.mock(Map.class);
        Map listenersMap = Mockito.mock(Map.class);
        Map pendingImageRequests = Mockito.mock(Map.class);
        ExecutorService service = Mockito.mock(ExecutorService.class);
        String query = "kittens";
        String page = "1";

        final FlickrRequestManager requestManager = new FlickrRequestManager(Mockito.mock(FlickrParser.class),
                currentTasksMap, listenersMap, pendingImageRequests, service);
        try {
            URL url = new URL("https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=a87d1a33ac9b5c4a06591b64f15eead2&text=" + query + "&page=" + page + "&format=json&nojsoncallback=1");
            when(listenersMap.containsKey(url)).thenReturn(true);
            requestManager.search(query, page, null);
            verifyZeroInteractions(service);
        } catch (MalformedURLException e) {
            fail();
        }
    }
}
