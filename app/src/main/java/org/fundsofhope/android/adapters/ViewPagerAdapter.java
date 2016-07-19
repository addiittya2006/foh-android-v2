package org.fundsofhope.android.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.fundsofhope.android.R;

/**
 * Created by addiittya on 19/07/16.
 */

public class ViewPagerAdapter extends PagerAdapter {

    private Context mContext;
    private int[] mResources;
    private Animation _translateAnimation;

    public ViewPagerAdapter(Context mContext, int[] mResources) {
        this.mContext = mContext;
        this.mResources = mResources;
    }

    public ViewPagerAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.splash_pager_item, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.img_pager_item);
//        imageView.setImageResource(mResources[position]);
        imageView.setImageResource(R.drawable.head);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        imageView.setAdjustViewBounds(true);

        container.addView(itemView);

        _translateAnimation = new TranslateAnimation(TranslateAnimation.ABSOLUTE, 0f, TranslateAnimation.ABSOLUTE, -100f, TranslateAnimation.ABSOLUTE, 0f, TranslateAnimation.ABSOLUTE, 0f);
        _translateAnimation.setDuration(3000);
        _translateAnimation.setRepeatCount(-1);
        _translateAnimation.setRepeatMode(Animation.REVERSE);
        _translateAnimation.setInterpolator(new LinearInterpolator());
//        imageView.setCropToPadding(true);
        itemView.setAnimation(_translateAnimation);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}