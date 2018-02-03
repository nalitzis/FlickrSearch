package ado.com.flickrsearch.view;

import java.util.List;

import ado.com.flickrsearch.api.ImageResult;

public interface ImageViewer {

    void resetAdapter();

    void showSpinner(boolean show);

    void setTotalSize(int size);

    void configureImages(List<? extends ImageResult> imagesResult);

    void setImage(ImageResult result);
}
