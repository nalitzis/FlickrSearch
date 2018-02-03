package ado.com.flickrsearch.view;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.VisibleForTesting;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.List;

import ado.com.flickrsearch.api.ImageResult;

public class ImageAdapter extends BaseAdapter {
    private static final String TAG = "ImageAdapter";

    private final SearchActivity mSearchActivity;

    private final SparseArray<ImageResult> mImages;

    private int mTotalSize;

    private static final int IMG_PADDING = 8;

    ImageAdapter(SearchActivity searchActivity) {
        this(searchActivity, new SparseArray<>());
    }

    @VisibleForTesting
    ImageAdapter(SearchActivity searchActivity, SparseArray<ImageResult> images) {
        mSearchActivity = searchActivity;
        mImages = images;
    }

    void setSize(int size) {
        mTotalSize = size;
    }

    void configureImages(List<? extends ImageResult> list) {
        for(ImageResult img : list) {
            mImages.put(img.getIndex(), img);
        }
        notifyDataSetChanged();
    }

    void add(ImageResult result) {
        mImages.put(result.getIndex(), result);
        notifyDataSetChanged();
    }

    void clear() {
        if(mImages.size() > 0) {
            mImages.clear();
            mTotalSize = 0;
            notifyDataSetChanged();
        }
    }

    public int getCount() {
        return mTotalSize;
    }

    public Object getItem(int position) {
        return mImages.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mSearchActivity);
            imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            imageView.setScaleType(ImageView.ScaleType.CENTER);
            imageView.setPadding(IMG_PADDING, IMG_PADDING, IMG_PADDING, IMG_PADDING);
            imageView.setBackgroundColor(Color.LTGRAY);
            imageView.setAdjustViewBounds(true);
        } else {
            imageView = (ImageView) convertView;
        }
        final ImageResult result = mImages.get(position);
        if (result != null && result.getBitmap() != null) {
            final Bitmap bmp = result.getBitmap();
            imageView.setImageBitmap(bmp);
        } else {
            imageView.setImageBitmap(null);
            mSearchActivity.requestNewImage(result, position);
        }
        return imageView;
    }
}
