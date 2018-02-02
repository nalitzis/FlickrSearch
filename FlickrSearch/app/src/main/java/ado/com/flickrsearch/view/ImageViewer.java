package ado.com.flickrsearch.view;

import ado.com.flickrsearch.api.ImageResult;

public interface ImageViewer {

    void resetAdapter();

    void showSpinner(boolean show);

    void onNewImage(ImageResult result);
}
