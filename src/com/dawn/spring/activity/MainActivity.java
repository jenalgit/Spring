package com.dawn.spring.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.text.*;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.dawn.spring.R;
import com.dawn.spring.adapter.EmoViewPagerAdapter;
import com.dawn.spring.adapter.EmoteAdapter;
import com.dawn.spring.adapter.MessageChatAdapter;
import com.dawn.spring.bean.FaceText;
import com.dawn.spring.config.Constants;
import com.dawn.spring.custom.EmotionsEditText;
import com.dawn.spring.custom.HeaderLayout;
import com.dawn.spring.custom.SlidingDrawerView;
import com.dawn.spring.custom.xlist.XListView;
import com.dawn.spring.utils.CacheUtils;
import com.dawn.spring.utils.FaceTextUtils;
import com.dawn.spring.utils.NetworkUtils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.io.File;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

/**
 * Created by dawn-pc on 2014/11/12.
 */
public class MainActivity extends BaseActivity implements View.OnClickListener,XListView.IXListViewListener,EventListener{

    private SlidingMenu slidingMenu;
    private Button btn_chat_emo, btn_chat_send, btn_chat_add,btn_chat_keyboard, btn_speak, btn_chat_voice;

    XListView mListView;

    EmotionsEditText edit_user_comment;

    String targetId = "";

    private static int MsgPagerNum;

    private LinearLayout layout_more, layout_emo, layout_add;

    private ViewPager pager_emo;

    private TextView tv_picture, tv_camera, tv_location;

    // 语音有关
    RelativeLayout layout_record;
    TextView tv_voice_tips;
    ImageView iv_record;

    private Drawable[] drawable_Anims;// 话筒动画


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById();
        initView();
        initSlidingMenu();

    }

    @Override
    protected void findViewById() {

    }

    @Override
    protected void initView() {
        mHeaderLayout = (HeaderLayout) findViewById(R.id.common_actionbar);
        mListView = (XListView) findViewById(R.id.mListView);
        initTopBarForLeft("精灵机器人为您服务");
        initBottomView();
        initXListView();
        initVoiceView();
    }


    /**
     * 初始化语音布局
     *
     * @Title: initVoiceView
     * @Description: TODO
     * @param
     * @return void
     * @throws
     */
    private void initVoiceView() {
        layout_record = (RelativeLayout) findViewById(R.id.layout_record);
        tv_voice_tips = (TextView) findViewById(R.id.tv_voice_tips);
        iv_record = (ImageView) findViewById(R.id.iv_record);
        btn_speak.setOnTouchListener(new VoiceTouchListen());
        initVoiceAnimRes();
        initRecordManager();
    }

    private void initRecordManager(){
        // 语音相关管理器
//        recordManager = BmobRecordManager.getInstance(this);
//        // 设置音量大小监听--在这里开发者可以自己实现：当剩余10秒情况下的给用户的提示，类似微信的语音那样
//        recordManager.setOnRecordChangeListener(new OnRecordChangeListener() {
//
//            @Override
//            public void onVolumnChanged(int value) {
//                // TODO Auto-generated method stub
//                iv_record.setImageDrawable(drawable_Anims[value]);
//            }
//
//            @Override
//            public void onTimeChanged(int recordTime, String localPath) {
//                // TODO Auto-generated method stub
//                BmobLog.i("voice", "已录音长度:" + recordTime);
//                if (recordTime >= BmobRecordManager.MAX_RECORD_TIME) {// 1分钟结束，发送消息
//                    // 需要重置按钮
//                    btn_speak.setPressed(false);
//                    btn_speak.setClickable(false);
//                    // 取消录音框
//                    layout_record.setVisibility(View.INVISIBLE);
//                    // 发送语音消息
//                    sendVoiceMessage(localPath, recordTime);
//                    //是为了防止过了录音时间后，会多发一条语音出去的情况。
//                    handler.postDelayed(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            // TODO Auto-generated method stub
//                            btn_speak.setClickable(true);
//                        }
//                    }, 1000);
//                }else{
//
//                }
//            }
//        });
    }

    /**
     * 长按说话
     * @ClassName: VoiceTouchListen
     * @Description: TODO
     * @author smile
     * @date 2014-7-1 下午6:10:16
     */
    class VoiceTouchListen implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (!CacheUtils.checkSdCard()) {
                        ShowToast("发送语音需要sdcard支持！");
                        return false;
                    }
                    try {
                        v.setPressed(true);
                        layout_record.setVisibility(View.VISIBLE);
                        tv_voice_tips.setText(getString(R.string.voice_cancel_tips));
                        // 开始录音
//                        recordManager.startRecording(targetId);
                    } catch (Exception e) {
                    }
                    return true;
                case MotionEvent.ACTION_MOVE: {
                    if (event.getY() < 0) {
                        tv_voice_tips
                                .setText(getString(R.string.voice_cancel_tips));
                        tv_voice_tips.setTextColor(Color.RED);
                    } else {
                        tv_voice_tips.setText(getString(R.string.voice_up_tips));
                        tv_voice_tips.setTextColor(Color.WHITE);
                    }
                    return true;
                }
                case MotionEvent.ACTION_UP:
                    v.setPressed(false);
                    layout_record.setVisibility(View.INVISIBLE);
                    try {
                        if (event.getY() < 0) {// 放弃录音
//                            recordManager.cancelRecording();
                        } else {
////                            int recordTime = recordManager.stopRecording();
//                            if (recordTime > 1) {
//                                // 发送语音文件
////                                BmobLog.i("voice", "发送语音");
////                                sendVoiceMessage(
////                                        recordManager.getRecordFilePath(targetId),
////                                        recordTime);
//                            } else {// 录音时间过短，则提示录音过短的提示
//                                layout_record.setVisibility(View.GONE);
//                                showShortToast().show();
//                            }
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                    return true;
                default:
                    return false;
            }
        }
    }

    Toast toast;

    /**
     * 显示录音时间过短的Toast
     * @Title: showShortToast
     * @return void
     * @throws
     */
    private Toast showShortToast() {
        if (toast == null) {
            toast = new Toast(this);
        }
        View view = LayoutInflater.from(this).inflate(
                R.layout.include_chat_voice_short, null);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        return toast;
    }

    /**
     * 初始化语音动画资源
     * @Title: initVoiceAnimRes
     * @Description: TODO
     * @param
     * @return void
     * @throws
     */
    private void initVoiceAnimRes() {
        drawable_Anims = new Drawable[] {
                getResources().getDrawable(R.drawable.chat_icon_voice2),
                getResources().getDrawable(R.drawable.chat_icon_voice3),
                getResources().getDrawable(R.drawable.chat_icon_voice4),
                getResources().getDrawable(R.drawable.chat_icon_voice5),
                getResources().getDrawable(R.drawable.chat_icon_voice6) };
    }

    /**
     * 界面刷新
     * @Title: initOrRefresh
     * @Description: TODO
     * @param
     * @return void
     * @throws
     */
    private void initOrRefresh() {
        if (mAdapter != null) {
//            if (MyMessageReceiver.mNewNum != 0) {// 用于更新当在聊天界面锁屏期间来了消息，这时再回到聊天页面的时候需要显示新来的消息
//                int news=  MyMessageReceiver.mNewNum;//有可能锁屏期间，来了N条消息,因此需要倒叙显示在界面上
//                int size = initMsgData().size();
//                for(int i=(news-1);i>=0;i--){
//                    mAdapter.add(initMsgData().get(size-(i+1)));// 添加最后一条消息到界面显示
//                }
//                mListView.setSelection(mAdapter.getCount() - 1);
//            } else {
//                mAdapter.notifyDataSetChanged();
//            }
        } else {
//            mAdapter = new MessageChatAdapter(this, initMsgData());
//            mListView.setAdapter(mAdapter);
        }
    }

    private void initAddView() {
        tv_picture = (TextView) findViewById(R.id.tv_picture);
        tv_camera = (TextView) findViewById(R.id.tv_camera);
        tv_location = (TextView) findViewById(R.id.tv_location);
        tv_picture.setOnClickListener(this);
        tv_location.setOnClickListener(this);
        tv_camera.setOnClickListener(this);
    }

    private void initBottomView() {
        // 最左边
        btn_chat_add = (Button) findViewById(R.id.btn_chat_add);
        btn_chat_emo = (Button) findViewById(R.id.btn_chat_emo);
        btn_chat_add.setOnClickListener(this);
        btn_chat_emo.setOnClickListener(this);
        // 最右边
        btn_chat_keyboard = (Button) findViewById(R.id.btn_chat_keyboard);
        btn_chat_voice = (Button) findViewById(R.id.btn_chat_voice);
        btn_chat_voice.setOnClickListener(this);
        btn_chat_keyboard.setOnClickListener(this);
        btn_chat_send = (Button) findViewById(R.id.btn_chat_send);
        btn_chat_send.setOnClickListener(this);
        // 最下面
        layout_more = (LinearLayout) findViewById(R.id.layout_more);
        layout_emo = (LinearLayout) findViewById(R.id.layout_emo);
        layout_add = (LinearLayout) findViewById(R.id.layout_add);
        initAddView();
        initEmoView();

        // 最中间
        // 语音框
        btn_speak = (Button) findViewById(R.id.btn_speak);
        // 输入框
        edit_user_comment = (EmotionsEditText) findViewById(R.id.edit_user_comment);
        edit_user_comment.setOnClickListener(this);
        edit_user_comment.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub
                if (!TextUtils.isEmpty(s)) {
                    btn_chat_send.setVisibility(View.VISIBLE);
                    btn_chat_keyboard.setVisibility(View.GONE);
                    btn_chat_voice.setVisibility(View.GONE);
                } else {
                    if (btn_chat_voice.getVisibility() != View.VISIBLE) {
                        btn_chat_voice.setVisibility(View.VISIBLE);
                        btn_chat_send.setVisibility(View.GONE);
                        btn_chat_keyboard.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }
        });

    }

    List<FaceText> emos;

    /**
     * 初始化表情布局
     * @Title: initEmoView
     * @Description: TODO
     * @param
     * @return void
     * @throws
     */
    private void initEmoView() {
        pager_emo = (ViewPager) findViewById(R.id.pager_emo);
        emos = FaceTextUtils.faceTexts;

        List<View> views = new ArrayList<View>();
        for (int i = 0; i < 2; ++i) {
            views.add(getGridView(i));
        }
        pager_emo.setAdapter(new EmoViewPagerAdapter(views));
    }

    private View getGridView(final int i) {
        View view = View.inflate(this, R.layout.include_emo_gridview, null);
        GridView gridview = (GridView) view.findViewById(R.id.gridview);
        List<FaceText> list = new ArrayList<FaceText>();
        if (i == 0) {
            list.addAll(emos.subList(0, 21));
        } else if (i == 1) {
            list.addAll(emos.subList(21, emos.size()));
        }
        final EmoteAdapter gridAdapter = new EmoteAdapter(MainActivity.this,
                list);
        gridview.setAdapter(gridAdapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                FaceText name = (FaceText) gridAdapter.getItem(position);
                String key = name.text.toString();
                try {
                    if (edit_user_comment != null && !TextUtils.isEmpty(key)) {
                        int start = edit_user_comment.getSelectionStart();
                        CharSequence content = edit_user_comment.getText()
                                .insert(start, key);
                        edit_user_comment.setText(content);
                        // 定位光标位置
                        CharSequence info = edit_user_comment.getText();
                        if (info instanceof Spannable) {
                            Spannable spanText = (Spannable) info;
                            Selection.setSelection(spanText,
                                    start + key.length());
                        }
                    }
                } catch (Exception e) {

                }

            }
        });
        return view;
    }

    MessageChatAdapter mAdapter;

    private void initXListView() {
        // 首先不允许加载更多
//        mListView.setPullLoadEnable(false);
//        // 允许下拉
//        mListView.setPullRefreshEnable(true);
//        // 设置监听器
//        mListView.setXListViewListener(this);
//        mListView.pullRefreshing();
//        mListView.setDividerHeight(0);
//        // 加载数据
//        initOrRefresh();
//        mListView.setSelection(mAdapter.getCount() - 1);
//        mListView.setOnTouchListener(new View.OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View arg0, MotionEvent arg1) {
//                // TODO Auto-generated method stub
//                hideSoftInputView();
//                layout_more.setVisibility(View.GONE);
//                layout_add.setVisibility(View.GONE);
//                btn_chat_voice.setVisibility(View.VISIBLE);
//                btn_chat_keyboard.setVisibility(View.GONE);
//                btn_chat_send.setVisibility(View.GONE);
//                return false;
//            }
//        });

        // 重发按钮的点击事件
    }
//    } mAdapter.setOnInViewClickListener(R.id.iv_fail_resend,
//                new MessageChatAdapter.onInternalClickListener() {
//
//                    @Override
//                    public void OnClickListener(View parentV, View v,
//                                                Integer position, Object values) {
//                        // 重发消息
//                        showResendDialog(parentV, v, values);
//                    }
//                });

    /**
     * 显示重发按钮 showResendDialog
     * @Title: showResendDialog
     * @Description: TODO
     * @param @param recent
     * @return void
     * @throws
     */
    public void showResendDialog(final View parentV, View v, final Object values) {
//        DialogTips dialog = new DialogTips(this, "确定重发该消息", "确定", "取消", "提示",
//                true);
//        // 设置成功事件
//        dialog.SetOnSuccessListener(new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialogInterface, int userId) {
//                if (((BmobMsg) values).getMsgType() == BmobConfig.TYPE_IMAGE
//                        || ((BmobMsg) values).getMsgType() == BmobConfig.TYPE_VOICE) {// 图片和语音类型的采用
//                    resendFileMsg(parentV, values);
//                } else {
//                    resendTextMsg(parentV, values);
//                }
//                dialogInterface.dismiss();
//            }
//        });
//        // 显示确认对话框
//        dialog.show();
//        dialog = null;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.edit_user_comment:// 点击文本输入框
                mListView.setSelection(mListView.getCount() - 1);
                if (layout_more.getVisibility() == View.VISIBLE) {
                    layout_add.setVisibility(View.GONE);
                    layout_emo.setVisibility(View.GONE);
                    layout_more.setVisibility(View.GONE);
                }
                break;
            case R.id.btn_chat_emo:// 点击笑脸图标
                if (layout_more.getVisibility() == View.GONE) {
                    showEditState(true);
                } else {
                    if (layout_add.getVisibility() == View.VISIBLE) {
                        layout_add.setVisibility(View.GONE);
                        layout_emo.setVisibility(View.VISIBLE);
                    } else {
                        layout_more.setVisibility(View.GONE);
                    }
                }

                break;
            case R.id.btn_chat_add:// 添加按钮-显示图片、拍照、位置
                if (layout_more.getVisibility() == View.GONE) {
                    layout_more.setVisibility(View.VISIBLE);
                    layout_add.setVisibility(View.VISIBLE);
                    layout_emo.setVisibility(View.GONE);
                    hideSoftInputView();
                } else {
                    if (layout_emo.getVisibility() == View.VISIBLE) {
                        layout_emo.setVisibility(View.GONE);
                        layout_add.setVisibility(View.VISIBLE);
                    } else {
                        layout_more.setVisibility(View.GONE);
                    }
                }

                break;
            case R.id.btn_chat_voice:// 语音按钮
                edit_user_comment.setVisibility(View.GONE);
                layout_more.setVisibility(View.GONE);
                btn_chat_voice.setVisibility(View.GONE);
                btn_chat_keyboard.setVisibility(View.VISIBLE);
                btn_speak.setVisibility(View.VISIBLE);
                hideSoftInputView();
                break;
            case R.id.btn_chat_keyboard:// 键盘按钮，点击就弹出键盘并隐藏掉声音按钮
                showEditState(false);
                break;
            case R.id.btn_chat_send:// 发送文本
                final String msg = edit_user_comment.getText().toString();
                if (msg.equals("")) {
                    ShowToast("请输入发送消息!");
                    return;
                }
                boolean isNetConnected = NetworkUtils.isNetworkAvailable(this);
                if (!isNetConnected) {
                    ShowToast(R.string.network_tips);
                    // return;
                }
                // 组装BmobMessage对象
//                BmobMsg message = BmobMsg.createTextSendMsg(this, targetId, msg);
//                // 默认发送完成，将数据保存到本地消息表和最近会话表中
//                manager.sendTextMessage(targetUser, message);
//                // 刷新界面
//                refreshMessage(message);

                break;
            case R.id.tv_camera:// 拍照
                selectImageFromCamera();
                break;
            case R.id.tv_picture:// 图片
                selectImageFromLocal();
                break;
            case R.id.tv_location:// 位置
                selectLocationFromMap();
                break;
            default:
                break;
        }
    }

    /**
     * 启动地图
     *
     * @Title: selectLocationFromMap
     * @Description: TODO
     * @param
     * @return void
     * @throws
     */
    private void selectLocationFromMap() {
//        Intent intent = new Intent(this, LocationActivity.class);
//        intent.putExtra("type", "select");
//        startActivityForResult(intent, BmobConstants.REQUESTCODE_TAKE_LOCATION);
    }

    private String localCameraPath = "";// 拍照后得到的图片地址

    /**
     * 启动相机拍照 startCamera
     *
     * @Title: startCamera
     * @throws
     */
    public void selectImageFromCamera() {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File dir = new File(Constants.BMOB_PICTURE_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, String.valueOf(System.currentTimeMillis())
                + ".jpg");
        localCameraPath = file.getPath();
        Uri imageUri = Uri.fromFile(file);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(openCameraIntent,
                Constants.REQUESTCODE_TAKE_CAMERA);
    }

    /**
     * 选择图片
     * @Title: selectImage
     * @Description: TODO
     * @param
     * @return void
     * @throws
     */
    public void selectImageFromLocal() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
        } else {
            intent = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, Constants.REQUESTCODE_TAKE_LOCAL);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constants.REQUESTCODE_TAKE_CAMERA:// 当取到值的时候才上传path路径下的图片到服务器
                    sendImageMessage(localCameraPath);
                    break;
                case Constants.REQUESTCODE_TAKE_LOCAL:
                    if (data != null) {
                        Uri selectedImage = data.getData();
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(
                                    selectedImage, null, null, null, null);
                            cursor.moveToFirst();
                            int columnIndex = cursor.getColumnIndex("_data");
                            String localSelectPath = cursor.getString(columnIndex);
                            cursor.close();
                            if (localSelectPath == null
                                    || localSelectPath.equals("null")) {
                                ShowToast("找不到您想要的图片");
                                return;
                            }
                            sendImageMessage(localSelectPath);
                        }
                    }
                    break;
                case Constants.REQUESTCODE_TAKE_LOCATION:// 地理位置
                    double latitude = data.getDoubleExtra("x", 0);// 维度
                    double longtitude = data.getDoubleExtra("y", 0);// 经度
                    String address = data.getStringExtra("address");
                    if (address != null && !address.equals("")) {
                        sendLocationMessage(address, latitude, longtitude);
                    } else {
                        ShowToast("无法获取到您的位置信息!");
                    }

                    break;
            }
        }
    }

    /**
     * 发送位置信息
     * @Title: sendLocationMessage
     * @Description: TODO
     * @param @param address
     * @param @param latitude
     * @param @param longtitude
     * @return void
     * @throws
     */
    private void sendLocationMessage(String address, double latitude,
                                     double longtitude) {
        if (layout_more.getVisibility() == View.VISIBLE) {
            layout_more.setVisibility(View.GONE);
            layout_add.setVisibility(View.GONE);
            layout_emo.setVisibility(View.GONE);
        }
//        // 组装BmobMessage对象
//        BmobMsg message = BmobMsg.createLocationSendMsg(this, targetId,
//                address, latitude, longtitude);
//        // 默认发送完成，将数据保存到本地消息表和最近会话表中
//        manager.sendTextMessage(targetUser, message);
//        // 刷新界面
//        refreshMessage(message);
    }

    /**
     * 默认先上传本地图片，之后才显示出来 sendImageMessage
     * @Title: sendImageMessage
     * @Description: TODO
     * @param @param localPath
     * @return void
     * @throws
     */
    private void sendImageMessage(String local) {
        if (layout_more.getVisibility() == View.VISIBLE) {
            layout_more.setVisibility(View.GONE);
            layout_add.setVisibility(View.GONE);
            layout_emo.setVisibility(View.GONE);
        }
//        manager.sendImageMessage(targetUser, local, new UploadListener() {
//
//            @Override
//            public void onStart(BmobMsg msg) {
//                // TODO Auto-generated method stub
//                ShowLog("开始上传onStart：" + msg.getContent() + ",状态："
//                        + msg.getStatus());
//                refreshMessage(msg);
//            }
//
//            @Override
//            public void onSuccess() {
//                // TODO Auto-generated method stub
//                mAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onFailure(int error, String arg1) {
//                // TODO Auto-generated method stub
//                ShowLog("上传失败 -->arg1：" + arg1);
//                mAdapter.notifyDataSetChanged();
//            }
//        });
    }

    /**
     * 根据是否点击笑脸来显示文本输入框的状态
     * @Title: showEditState
     * @Description: TODO
     * @param @param isEmo: 用于区分文字和表情
     * @return void
     * @throws
     */
    private void showEditState(boolean isEmo) {
        edit_user_comment.setVisibility(View.VISIBLE);
        btn_chat_keyboard.setVisibility(View.GONE);
        btn_chat_voice.setVisibility(View.VISIBLE);
        btn_speak.setVisibility(View.GONE);
        edit_user_comment.requestFocus();
        if (isEmo) {
            layout_more.setVisibility(View.VISIBLE);
            layout_more.setVisibility(View.VISIBLE);
            layout_emo.setVisibility(View.VISIBLE);
            layout_add.setVisibility(View.GONE);
            hideSoftInputView();
        } else {
            layout_more.setVisibility(View.GONE);
            showSoftInputView();
        }
    }

    // 显示软键盘
    public void showSoftInputView() {
        if (getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                        .showSoftInput(edit_user_comment, 0);
        }
    }


    private void initSlidingMenu(){
        slidingMenu = new SlidingDrawerView(this).initSlidingMenu();
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
