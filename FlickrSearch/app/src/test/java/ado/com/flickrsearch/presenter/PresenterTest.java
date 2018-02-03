package ado.com.flickrsearch.presenter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import ado.com.flickrsearch.FlickrSearchApp;
import ado.com.flickrsearch.api.SearchResult;
import ado.com.flickrsearch.api.ServiceApi;
import ado.com.flickrsearch.domain.FlickrImageResult;
import ado.com.flickrsearch.view.ImageViewer;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PresenterTest {

    @Mock
    private FlickrSearchApp mApp;
    @Mock
    private ImageViewer mView;
    @Mock
    private ServiceApi.Listener<SearchResult> mSearchListener;
    @Mock
    private ServiceApi.Listener<FlickrImageResult> mImageListener;
    @Mock
    private ServiceApi mApi;
    @Mock
    private FlickrImageResult mImageResult;
    @Mock
    private SearchResult mSearchResult;

    @Test
    public void testOnSearchCommand() {
        when(mApp.getServiceApi()).thenReturn(mApi);
        FlickrSearchPresenter presenter = new FlickrSearchPresenter(mApp,mView);
        presenter.onSearchCommand("ff");
        verify(mView).resetAdapter();
        verify(mView).showSpinner(true);
    }

    @Test
    public void testOnNewImageRequest() {
        when(mApp.getServiceApi()).thenReturn(mApi);
        FlickrSearchPresenter presenter = new FlickrSearchPresenter(mApp,mView, mSearchListener, mImageListener);
        presenter.onNewImageRequest(mImageResult,0);
        verify(mApi).fetchImage(mImageResult, mImageListener);
    }

    @Test
    public void testOnNewImageRequestNoImageObject() {
        when(mApp.getServiceApi()).thenReturn(mApi);
        FlickrSearchPresenter presenter = new FlickrSearchPresenter(mApp,mView, mSearchListener, mImageListener);
        presenter.onSearchCommand("ff");
        presenter.onNewImageRequest(null,0);
        verify(mApi, times(2)).search("ff", "" + 1, mSearchListener);
    }

    @Test
    public void testSearchCallback() {
        FlickrSearchPresenter presenter = new FlickrSearchPresenter(mApp,mView);
        when(mSearchResult.getTotalSize()).thenReturn("105");
        presenter.getSearchListener().onCompleted(mSearchResult);
        verify(mView).showSpinner(false);
        verify(mView).setTotalSize(105);
    }

    @Test
    public void testImageCallback() {
        FlickrSearchPresenter presenter = new FlickrSearchPresenter(mApp,mView);
        presenter.getImageListener().onCompleted(mImageResult);
        verify(mView).setImage(mImageResult);
    }
}
