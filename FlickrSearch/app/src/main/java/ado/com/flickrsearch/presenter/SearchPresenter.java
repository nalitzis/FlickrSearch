package ado.com.flickrsearch.presenter;


import ado.com.flickrsearch.api.ImageResult;

public interface SearchPresenter {

    void onNewImageRequest(ImageResult image, int index);

    void onSearchCommand(String search);

    void onDestroy();

}
