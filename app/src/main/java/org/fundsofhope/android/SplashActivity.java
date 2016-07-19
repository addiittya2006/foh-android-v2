package org.fundsofhope.android;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import org.fundsofhope.android.adapters.ViewPagerAdapter;

public class SplashActivity extends AppCompatActivity {

//    protected View view;
    private ViewPager intro_images;
    private ViewPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        int deviceWidthInPixels = getResources().getDisplayMetrics().widthPixels;
//        int deviceHeightInPixels = getResources().getDisplayMetrics().heightPixels;

//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


//        view = LayoutInflater.from(this).inflate(R.layout.activity_viewpager_demo,container);

        intro_images = (ViewPager) findViewById(R.id.scroll_view_pager);
//        btnNext = (ImageButton) view.findViewById(R.id.btn_next);
//        btnFinish = (ImageButton) view.findViewById(R.id.btn_finish);

//        pager_indicator = (LinearLayout) view.findViewById(R.id.viewPagerCountDots);

//        mAdapter = new ViewPagerAdapter(SplashActivity.this, mImageResources);
        mAdapter = new ViewPagerAdapter(SplashActivity.this);
        intro_images.setAdapter(mAdapter);
        intro_images.setCurrentItem(0);
//        intro_images.setOnPageChangeListener(this);
//        setUiPageViewController();

    }
}