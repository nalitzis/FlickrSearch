package ado.com.flickrsearch.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import ado.com.flickrsearch.R;
import ado.com.flickrsearch.api.ImageResult;

public class ImageAdapter extends BaseAdapter {
    private static final String TAG = "ImageAdapter";

    private final Context mContext;
    private final List<ImageResult> mImages = new ArrayList<>();

    private static final int IMG_PADDING = 8;

    ImageAdapter(Context c) {
        mContext = c;
    }

    void add(ImageResult result) {
        mImages.add(result);
        notifyDataSetChanged();
    }

    void clear() {
        if(!mImages.isEmpty()) {
            mImages.clear();
            notifyDataSetChanged();
        }
    }

    public int getCount() {
        return mImages.size();
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
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            imageView.setScaleType(ImageView.ScaleType.CENTER);
            imageView.setPadding(IMG_PADDING, IMG_PADDING, IMG_PADDING, IMG_PADDING);
        } else {
            imageView = (ImageView) convertView;
        }
        final ImageResult result = mImages.get(position);
        final Bitmap bmp = result.getBitmap();
        imageView.setImageBitmap(bmp);
        imageView.setAdjustViewBounds(true);
        return imageView;
    }


}
