package com.dawn.spring.custom;

import android.app.Activity;
import android.view.View;
import com.dawn.spring.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/**
 * 自定义 slidingMenu 侧拉菜单
 * Created by dawn-pc on 2014/11/13.
 */
public class SlidingDrawerView implements View.OnClickListener{
    private final Activity activity;
    SlidingMenu localSlidingMenu;
    private SwitchButton nightModeButton;


    public SlidingDrawerView(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onClick(View v) {

    }

    public SlidingMenu initSlidingMenu(){
        localSlidingMenu = new SlidingMenu(activity);
        localSlidingMenu.setMode(SlidingMenu.LEFT); //设置左右滑菜单
        localSlidingMenu.setTouchModeAbove(SlidingMenu.SLIDING_WINDOW);//设置要使菜单滑动，触碰屏幕的范围
        localSlidingMenu.setShadowWidthRes(R.dimen.shadow_width);   //设置阴影图片宽度
        localSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);    //SlidingMenu划出时主页面显示的剩余宽度
        localSlidingMenu.setFadeDegree(0.35f);  //SlidingMenu滑动时的渐变程度
        localSlidingMenu.attachToActivity(activity, SlidingMenu.RIGHT);  //使SlidingMenu附加在Activity右边
        localSlidingMenu.setMenu(R.layout.fragment_left_sliding);
        localSlidingMenu.setSecondaryShadowDrawable(R.drawable.shadowright);
        localSlidingMenu.setOnOpenedListener(new SlidingMenu.OnOpenedListener() {
            public void onOpened() {

            }
        });
        return localSlidingMenu;
    }

}
