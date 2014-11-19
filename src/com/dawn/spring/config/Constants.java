package com.dawn.spring.config;

import android.os.Environment;

/**
 * Created by dawn-pc on 2014/11/12.
 */
public class Constants {
    public static int SCREEN_HEIGHT = 1280; //屏幕高度
    public static int SCREEN_WIDTH = 720;   //屏幕宽度
    public static float SCREEN_DENSITY = 1.5F;  //屏幕密度


    /**
     * 存放发送图片的目录
     */
    public static String BMOB_PICTURE_PATH = Environment.getExternalStorageDirectory()	+ "/bmobimdemo/image/";

    /**
     * 我的头像保存目录
     */
    public static String MyAvatarDir = "/sdcard/bmobimdemo/avatar/";
    /**
     * 拍照回调
     */
    public static final int REQUESTCODE_UPLOADAVATAR_CAMERA = 1;//拍照修改头像
    public static final int REQUESTCODE_UPLOADAVATAR_LOCATION = 2;//本地相册修改头像
    public static final int REQUESTCODE_UPLOADAVATAR_CROP = 3;//系统裁剪头像

    public static final int REQUESTCODE_TAKE_CAMERA = 0x000001;//拍照
    public static final int REQUESTCODE_TAKE_LOCAL = 0x000002;//本地图片
    public static final int REQUESTCODE_TAKE_LOCATION = 0x000003;//位置
    public static final String EXTRA_STRING = "extra_string";
}
