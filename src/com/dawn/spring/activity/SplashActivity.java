package com.dawn.spring.activity;

import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import com.dawn.spring.R;
import com.dawn.spring.config.Constants;

/**
 * Created by dawn-pc on 2014/11/12.
 */
public class SplashActivity extends BaseActivity{

    public static final String TAG = SplashActivity.class.getSimpleName();
    private ImageView splashView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Constants.SCREEN_DENSITY = metrics.density;
        Constants.SCREEN_HEIGHT = metrics.heightPixels;
        Constants.SCREEN_WIDTH = metrics.widthPixels;

        mHandler = new Handler(getMainLooper());
        findViewById();
        initView();
    }

    @Override
    protected void findViewById() {
        splashView = (ImageView) findViewById(R.id.activity_splash_loading_item);
    }

    @Override
    protected void initView() {
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.splash_loading);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                openActivity(MainActivity.class);
                overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
                SplashActivity.this.finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        splashView.setAnimation(animation);
    }


}
