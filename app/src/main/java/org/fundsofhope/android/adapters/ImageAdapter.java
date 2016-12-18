package org.fundsofhope.android.adapters;

/**
 * Created by anip on 16/12/16.
 */

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import org.fundsofhope.android.R;

import java.util.ArrayList;

/**
 * Created by anip on 16/07/16.
 */
public class ImageAdapter extends PagerAdapter {
    private ArrayList<String> imageArray;
    int[] mResources = {
            R.drawable.saladdays,
            R.drawable.saladdays,
            R.drawable.saladdays,
            R.drawable.saladdays,
            R.drawable.saladdays,
            R.drawable.saladdays
    };

    Context mContext;
    LayoutInflater mLayoutInflater;

    public ImageAdapter(Context context, ArrayList<String> images) {
        mContext = context;
        imageArray = images;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return imageArray.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.project_image, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
        Picasso.with(mContext).load("http://api.fundsofhope.org"+imageArray.get(position)).into(imageView);

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
