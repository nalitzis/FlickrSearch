package ado.com.flickrsearch.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
    private final Context mContext;
    private final List<ImageResult> mImages = new ArrayList<>();

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

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        //TODO decode before!!!!
        final ImageResult result = mImages.get(position);
        final byte[] data = result.getData();
        final Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
        imageView.setImageBitmap(bmp);

        //imageView.setImageResource(R.drawable.test);
        return imageView;
    }


}
