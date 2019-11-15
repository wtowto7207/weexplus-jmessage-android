package com.weexplus.jim.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.farwolf.util.StringUtil;
import com.farwolf.view.FreeDialog;
import com.farwolf.weex.view.LoadingDialog;
import com.farwolf.weex.view.LoadingDialog_;
import com.squareup.picasso.Picasso;
import com.weexplus.jim.R;
//import com.weexplus.jim.R2;
import com.weexplus.jim.Receiver.NotificationClickEventReceiver;
import com.weexplus.jim.entity.DefaultUser;
import com.weexplus.jim.entity.MyMessage;
import com.weexplus.jim.entity.UserStateBean;
import com.weexplus.jim.framework.base.BaseActivity;
import com.weexplus.jim.framework.helper.SharedPrefHelper;
import com.weexplus.jim.framework.messages.MessageListCus;
import com.weexplus.jim.framework.network.NetWorkManager;
import com.weexplus.jim.framework.system.SystemStatusManager;
import com.weexplus.jim.framework.utils.RealPathFromUriUtils;
import com.weexplus.jim.framework.utils.StringUtils;
import com.weexplus.jim.framework.utils.TimeUtils;
import com.weexplus.jim.view.ChatView;
import com.weexplus.jim.view.MyAlertDialog;
import com.weexplus.jim.view.MyViewHolder;
import com.weexplus.jim.views.ImgBrowserViewPager;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

//import butterknife.ButterKnife;
//import butterknife.OnClick;
import cn.jiguang.imui.chatinput.ChatInputView;
import cn.jiguang.imui.chatinput.listener.OnCameraCallbackListener;
import cn.jiguang.imui.chatinput.listener.OnMenuClickListener;
import cn.jiguang.imui.chatinput.listener.RecordVoiceListener;
import cn.jiguang.imui.chatinput.model.FileItem;
import cn.jiguang.imui.chatinput.model.VideoItem;
import cn.jiguang.imui.commons.ImageLoader;
import cn.jiguang.imui.commons.models.IMessage;
import cn.jiguang.imui.messages.MessageList;
import cn.jiguang.imui.messages.MsgListAdapter;
import cn.jiguang.imui.messages.ViewHolderController;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.DownloadCompletionCallback;
import cn.jpush.im.android.api.content.FileContent;
import cn.jpush.im.android.api.content.ImageContent;
import cn.jpush.im.android.api.content.MessageContent;
import cn.jpush.im.android.api.content.PromptContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.content.VideoContent;
import cn.jpush.im.android.api.content.VoiceContent;
import cn.jpush.im.android.api.enums.ContentType;
import cn.jpush.im.android.api.enums.MessageDirect;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.event.MessageRetractEvent;
import cn.jpush.im.android.api.event.NotificationClickEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static cn.jiguang.imui.commons.models.IMessage.MessageType.RECEIVE_TEXT;
import static cn.jiguang.imui.commons.models.IMessage.MessageType.RECEIVE_VIDEO;
import static cn.jiguang.imui.commons.models.IMessage.MessageType.RECEIVE_VOICE;
import static cn.jiguang.imui.commons.models.IMessage.MessageType.SEND_CUSTOM;
import static cn.jiguang.imui.commons.models.IMessage.MessageType.SEND_FILE;
import static cn.jiguang.imui.commons.models.IMessage.MessageType.SEND_IMAGE;
import static cn.jiguang.imui.commons.models.IMessage.MessageType.SEND_LOCATION;
import static cn.jiguang.imui.commons.models.IMessage.MessageType.SEND_TEXT;
import static cn.jiguang.imui.commons.models.IMessage.MessageType.SEND_VIDEO;
import static cn.jiguang.imui.commons.models.IMessage.MessageType.SEND_VOICE;
import static cn.jpush.im.android.api.enums.ContentType.file;
import static cn.jpush.im.android.api.enums.ContentType.image;
import static cn.jpush.im.android.api.enums.ContentType.prompt;
import static cn.jpush.im.android.api.enums.ContentType.text;
import static cn.jpush.im.android.api.enums.ContentType.video;
import static cn.jpush.im.android.api.enums.ContentType.voice;
import static cn.jpush.im.android.api.jmrtc.JMRTCInternalUse.getApplicationContext;


public class ChatMsgActivity extends BaseActivity implements
        View.OnTouchListener, EasyPermissions.PermissionCallbacks, SensorEventListener {

    final static String TAG = "chat_msg";

    private final int RC_RECORD_VOICE = 0x0001;
    private final int RC_CAMERA = 0x0002;
    private final int RC_PHOTO = 0x0003;

//    @BindView(R2.id.title_bar_back)
    ImageView mTitleBarBack;
//    @BindView(R2.id.title_bar_title)
    TextView mTitleBarTitle;
//    @BindView(R2.id.title_options_tv)
    TextView mTitleOptionsTv;
//    @BindView(R2.id.title_options_img)
    ImageView mTitleOptionsImg;
//
//    @BindView(R2.id.title)
    LinearLayout mTitle;
//    @BindView(R2.id.msg_list)
    MessageList mMsgList;
//    @BindView(R2.id.chat_input)
    ChatInputView mChatInput;
//    @BindView(R2.id.chat_view)
    ChatView mChatView;
//    @BindView(R2.id.chat_et)
//    EditText mChatEt;
//    @BindView(R2.id.chat_send)
//    Button mChatSend;
    private SharedPrefHelper helper;

    // 状态栏的高度
    private int statusBarHeight;
    // 软键盘的高度
    private int keyboardHeight;
    // 软键盘的显示状态
    private boolean isShowKeyboard;
    private Context mContext;
    private MsgListAdapter<MyMessage> mAdapter;
    private List<MyMessage> mData = new ArrayList<>();
    private List<MyMessage> list = new ArrayList<>();
    private ImageLoader imageLoader;
    //收发的头像
    private ImageView imageAvatarSend, imageAvatarReceive;
    private String userName = "";
    private String nickName = "";
    //撤回消息的视图msgid
    private String msgID = "";
    private int position;
    List<Conversation> conversations;
    Conversation conversation;
    private UserInfo userInfo;
    private String imgSend = "R.drawable.ironman";
    private String imgRecrive = "R.drawable.ironman";
    MyMessage myMessage;

    InputMethodManager mManager;
    Window mWindow;

    private SensorManager mSensorManager;
    private Sensor mSensor;
    private PowerManager mPowerManager;
    private PowerManager.WakeLock mWakeLock;
    int heightDifference = 0;

    private ArrayList<String> mPathList = new ArrayList<>();
    private ArrayList<String> mMsgIdList = new ArrayList<>();

    @Override
    protected int setContentView() {
        return R.layout.activity_chat;
    }

    public static void start(Context context, String contactId, String userName, String nickName, String msgID,int position) {
        Intent intent = new Intent();
        intent.setClass(context,ChatMsgActivity.class);
        //传入用户账号
        intent.putExtra("USERNAME", userName);
        //传入用户昵称
        intent.putExtra("NICKNAME", nickName);
        //聊天信息的ID
        intent.putExtra("MSGID",msgID);
        intent.putExtra("position",position);
        context.startActivity(intent);
    }

    /*点击通知栏跳转*/
//    public void onEvent(NotificationClickEvent event){
//        Log.d(TAG,"notification click");
//        final Message msg = event.getMessage();
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                Intent notificationIntent = new Intent(mContext, ChatMsgActivity.class);
//                notificationIntent.putExtra("USERNAME", msg.getFromUser().getUserName());
//                notificationIntent.putExtra("NICKNAME", msg.getFromUser().getDisplayName());
//                notificationIntent.putExtra("MSGID","");
//                notificationIntent.putExtra("position",0);
//                mContext.startActivity(notificationIntent);
//            }
//        });
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BarUtils.setNavBarImmersive(this);
        setContentView(R.layout.activity_chat);
        mTitleBarBack = findViewById(R.id.title_bar_back);
        mTitleBarTitle = findViewById(R.id.title_bar_title);
        mTitleOptionsTv = findViewById(R.id.title_options_tv);
        mTitleOptionsImg = findViewById(R.id.title_options_img);
        mTitle = findViewById(R.id.title);
        mMsgList = findViewById(R.id.msg_list);
        mChatView = findViewById(R.id.chat_view);
        mChatInput = findViewById(R.id.chat_input);
        new SystemStatusManager(this).setTranslucentStatus(R.drawable.shape_titlebar);
//        JMessageClient.registerEventReceiver(this);
//        helper=SharedPrefHelper.getInstance();
        initView();
        initData();
        //返回上一页
        chatPageBack();
    }

//    @Override
    protected void initView() {
        this.mManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        editTextHeight();
        mWindow = getWindow();
        registerProximitySensorListener();
        //heightDifference   819
        mChatInput.setMenuContainerHeight(819);
        mChatView.initModule();
//        mChatView.setKeyboardChangedListener(this);
//        mChatView.setOnSizeChangedListener(this);
        mChatView.setOnTouchListener(this);
        initInputView();
        helper = new SharedPrefHelper(this);
        mContext = ChatMsgActivity.this;
        userInfo = JMessageClient.getMyInfo();
        helper.setUserId(userInfo.getUserName());
        //注册接收者
        JMessageClient.registerEventReceiver(this);
        userName = getIntent().getStringExtra("USERNAME");
        nickName = getIntent().getStringExtra("NICKNAME");
        //消息界面关闭通知
        JMessageClient.enterSingleConversation(userName);
        JMessageClient.setNotificationFlag(JMessageClient.FLAG_NOTIFY_WITH_SOUND | JMessageClient.FLAG_NOTIFY_WITH_LED | JMessageClient.FLAG_NOTIFY_WITH_VIBRATE);
        //注册通知栏点击事件
        new NotificationClickEventReceiver(mContext);
        //position从上个页面传递的会话位置
        position = getIntent().getIntExtra("position", 0);
        conversation = JMessageClient.getSingleConversation(userName);
        View view = View.inflate(mContext, R.layout.item_receive_photo, null);
        View view1 = View.inflate(mContext, R.layout.item_send_photo, null);
        imageAvatarSend = (ImageView) view.findViewById(R.id.aurora_iv_msgitem_avatar);
        imageAvatarReceive = (ImageView) view1.findViewById(R.id.aurora_iv_msgitem_avatar);
//        MessageBean bean = (MessageBean) getIntent().getSerializableExtra("bean");
//        Log.e("beanSeriali", bean.getTitle() + "\n"+bean);
        try {
            imgSend = userInfo.getAvatarFile().toURI().toString();
            imgRecrive = StringUtils.isNull(conversation.getAvatarFile().toURI().toString()) ? "R.drawable.ironman" : conversation.getAvatarFile().toURI().toString();

        } catch (Exception e) {
            Log.e(TAG,e + "");
        }
        mData = getMessages();
        initMsgAdapter();
        initTitleBar(nickName);
//        imageLoader.loadImage(imageAvatarSend, userInfo.getAvatarFile().toURI().toString());
        imageLoader.loadImage(imageAvatarReceive, imgRecrive);
        mTitleBarBack.setVisibility(View.VISIBLE);
        mTitle.setPadding(0,80,0,0);
    }


    //sensor listener
    private void registerProximitySensorListener() {
        try {
            mPowerManager = (PowerManager) getSystemService(POWER_SERVICE);
            mWakeLock = mPowerManager.newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, TAG);
            mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
            mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        try {
            if (audioManager.isBluetoothA2dpOn() || audioManager.isWiredHeadsetOn()) {
                return;
            }
            if (mAdapter.getMediaPlayer().isPlaying()) {
                float distance = event.values[0];
                if (distance >= mSensor.getMaximumRange()) {
                    mAdapter.setAudioPlayByEarPhone(0);
                    setScreenOn();
                } else {
                    mAdapter.setAudioPlayByEarPhone(2);
                    ViewHolderController.getInstance().replayVoice();
                    setScreenOff();
                }
            } else {
                if (mWakeLock != null && mWakeLock.isHeld()) {
                    mWakeLock.release();
                    mWakeLock = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void setScreenOn() {
        if (mWakeLock != null) {
            mWakeLock.setReferenceCounted(false);
            mWakeLock.release();
            mWakeLock = null;
        }
    }

    private void setScreenOff() {
        if (mWakeLock == null) {
            mWakeLock = mPowerManager.newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, TAG);
        }
        mWakeLock.acquire();
    }


    /*获取键盘的高度*/
    private void editTextHeight() {
        mChatView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                //获取当前界面可视部分
                ChatMsgActivity.this.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
                //获取屏幕的高度
                int screenHeight = ChatMsgActivity.this.getWindow().getDecorView().getRootView().getHeight();
                //此处就是用来获取键盘的高度的， 在键盘没有弹出的时候 此高度为0 键盘弹出的时候为一个正数
                if (screenHeight - r.bottom > 0) {
                    heightDifference = screenHeight - r.bottom;
                }
//                Log.d(TAG,"heightDifference: " + heightDifference);
//                LogUtils.e(heightDifference + "");
            }
        });
    }

    /*发送消息，当前版本只能发送文本*/
    private void sendMessage(String msg) {
        TextContent content = new TextContent(msg);
        Message message1 = conversation.createSendMessage(content);
        final MyMessage myMessage = new MyMessage(msg, SEND_TEXT);
        myMessage.setMessage(message1);
        myMessage.setTimeString(TimeUtils.ms2date("MM-dd HH:mm", message1.getCreateTime()));
        myMessage.setUserInfo(new DefaultUser(JMessageClient.getMyInfo().getUserName(), userInfo.getDisplayName(), imgSend));
//        myMessage.setMessageStatus(IMessage.MessageStatus.SEND_GOING);
        message1.setOnSendCompleteCallback(new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if (i == 0) {
                    mAdapter.addToStart(myMessage, true);
//                    myMessage.setMessageStatus(IMessage.MessageStatus.SEND_SUCCEED);
//                    mChatEt.setText("");0
                } else {
                    Log.e(TAG + "发送失败？", s);
                }
            }
        });
        JMessageClient.sendMessage(message1);
//        EventBus.getDefault().post(new MessageEvent(1,"",message1));
        if (mData != null) {
            mData.clear();
        }
    }

    /*发送图片消息*/
    private void sendImage(String uri) {
        File imageFile = new File(uri);
        try {
//            ImageContent content = new ImageContent(imageFile);
            Message message = conversation.createSendImageMessage(imageFile);
//            Log.d(TAG,"conversation message image: " + message);
            final MyMessage myMessage = new MyMessage(null, SEND_IMAGE);
            mPathList.add(uri);
            myMessage.setMessage(message);
//            Log.d(TAG,"myMessage.getMsgId(): " + myMessage.getMsgId());
            mMsgIdList.add(myMessage.getMsgId());
            myMessage.setTimeString(TimeUtils.ms2date("MM-dd HH:mm",message.getCreateTime()));
            myMessage.setMediaFilePath(uri);
            myMessage.setUserInfo(new DefaultUser(JMessageClient.getMyInfo().getUserName(),userInfo.getDisplayName(),imgSend));
            loadingTipShow();
            message.setOnSendCompleteCallback(new BasicCallback() {
                @Override
                public void gotResult(int i, String s) {
                    if (i == 0) {
                        mAdapter.addToStart(myMessage,true);
                        loadingTipHide();
                    } else {
                        loadingTipHide();
                        Log.e(TAG + "发送失败?", s);
                        Toast.makeText(mContext,"发送失败",Toast.LENGTH_LONG).show();
                    }
                }
            });
            JMessageClient.sendMessage(message);
            if (mData != null) mData.clear();
        } catch (FileNotFoundException e) {
            Log.e(TAG,"找不到文件");
        }
    }
    /*发送视频*/
    private void sendVideo(FileItem item) {
        File videoFile = new File(item.getFilePath());
        Bitmap bitmap = BitmapFactory.decodeFile(item.getFilePath());
        int duration = (int) ((VideoItem) item).getDuration();
        try {
            VideoContent content = new VideoContent(bitmap,"png",videoFile,item.getFileName(),duration);
//            content.setStringExtra(fileName,format);
//            FileContent content = new FileContent(videoFile);
            content.setNumberExtra("duration", duration);
            content.setStringExtra("video", "mp4");
            Message message = conversation.createSendMessage(content);
//            Log.d(TAG,"conversation message video: " + message);
            final MyMessage myMessage = new MyMessage(null,SEND_VIDEO);
            myMessage.setMessage(message);
            myMessage.setDuration(((VideoItem) item).getDuration());
            myMessage.setTimeString(TimeUtils.ms2date("MM-dd HH:mm", message.getCreateTime()));
            myMessage.setMediaFilePath(item.getFilePath());
            myMessage.setUserInfo(new DefaultUser(userInfo.getUserName(),userInfo.getDisplayName(),imgSend));
            loadingTipShow();
            message.setOnSendCompleteCallback(new BasicCallback() {
                @Override
                public void gotResult(int i, String s) {
                    if (i ==0) {
                        mAdapter.addToStart(myMessage,true);
                        loadingTipHide();
                    } else {
                        loadingTipHide();
                        Toast.makeText(mContext,"发送失败",Toast.LENGTH_LONG).show();
                    }
                }
            });
            JMessageClient.sendMessage(message);
            if (mData != null) mData.clear();
        } catch (Exception e) {
            Log.e(TAG,"文件未找到");
        }
    }


    //加载提示
    LoadingDialog process;
    FreeDialog freeDialog;
    private void loadingTipShow() {
        if (freeDialog == null) {
            process = LoadingDialog_.build(mContext);
            freeDialog = new FreeDialog(mContext,process);
            freeDialog.setCancelable(false);
            freeDialog.setCanceledOnTouchOutside(false);
            process.f = freeDialog;
        }
        process.txt.setText("上传中");
        freeDialog.show();
    }
    //隐藏加载提示
    private void loadingTipHide() {
        if (freeDialog == null || !freeDialog.isShowing()) {
            return;
        }

        freeDialog.dismiss();
    }
    /*输入框操作*/
    private void initInputView() {

        mChatInput.setMenuClickListener(new OnMenuClickListener() {
            @Override
            public boolean onSendTextMessage(CharSequence charSequence) {
                scrollToBottom();
                if (charSequence.length() == 0) {
                } else {
                    sendMessage(charSequence.toString());
                }
                return true;
            }

            @Override
            public void onSendFiles(List<FileItem> list) {
                scrollToBottom();
                if (list == null || list.isEmpty()) {
                    return;
                }
                // should reset messageList height
                mChatView.setMsgListHeight(true,ChatMsgActivity.this.getWindow().getDecorView().getRootView().getHeight());
                for (FileItem item : list) {
                    if (item.getType() == FileItem.Type.Image) {
                        sendImage(item.getFilePath());
                    } else if (item.getType() == FileItem.Type.Video) {
                        sendVideo(item);
                    } else {
                        throw new RuntimeException("Invalid FileItem type. Must be Type.Image or Type.Video");
                    }
                }
            }

            @Override
            public boolean switchToMicrophoneMode() {
                scrollToBottom();
                String[] perms = new String[]{
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                };

                if (!EasyPermissions.hasPermissions(ChatMsgActivity.this, perms)) {
                    EasyPermissions.requestPermissions(ChatMsgActivity.this,
                            getResources().getString(R.string.rationale_record_voice),
                            RC_RECORD_VOICE, perms);
                }
                return true;
            }

            @Override
            public boolean switchToGalleryMode() {
                scrollToBottom();
                String[] params = new String[] {
                        Manifest.permission.READ_EXTERNAL_STORAGE
                };
                if (!EasyPermissions.hasPermissions(ChatMsgActivity.this,params)) {
                    EasyPermissions.requestPermissions(ChatMsgActivity.this,
                            "需要获取照片的权限",
                            RC_PHOTO,
                            params);
                }
                mChatView.getChatInputView().getSelectPhotoView().updateData();
                return true;
            }

            @Override
            public boolean switchToCameraMode() {
                scrollToBottom();
                String[] perms = new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO
                };

                if (!EasyPermissions.hasPermissions(ChatMsgActivity.this, perms)) {
                    EasyPermissions.requestPermissions(ChatMsgActivity.this,
                            getResources().getString(R.string.rationale_camera),
                            RC_CAMERA, perms);
                    return false;
                } else {
                    File rootDir = getFilesDir();
                    String fileDir = rootDir.getAbsolutePath() + "/photo";
                    mChatView.setCameraCaptureFile(fileDir, new SimpleDateFormat("yyyy-MM-dd-hhmmss",
                            Locale.getDefault()).format(new Date()));
                }
                return true;
            }

            @Override
            public boolean switchToEmojiMode() {
                scrollToBottom();
                return true;
            }
        });

        mChatInput.getInputView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                scrollToBottom();
                return false;
            }
        });

        //打开相册
        mChatInput.getSelectAlbumBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent=new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
                galleryIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                startActivityForResult(galleryIntent,1);//跳转，传递打开相册请求码
            }

        });

        //拍照并发送
        mChatView.setOnCameraCallbackListener(new OnCameraCallbackListener() {
            @Override
            public void onTakePictureCompleted(String photoPath) {
                sendImage(photoPath);
            }

            @Override
            public void onStartVideoRecord() {

            }

            @Override
            public void onFinishVideoRecord(String videoPath) {

            }

            @Override
            public void onCancelVideoRecord() {

            }
        });


        //录音
        mChatInput.setRecordVoiceListener(new RecordVoiceListener() {
            @Override
            public void onStartRecord() {
                //设置文件保存路径
                File rootDir = mContext.getFilesDir();
                String fileDir = rootDir.getAbsolutePath() + "/voice";
                File desDir = new File(fileDir);
                if (!desDir.exists()){
                    desDir.mkdir();
                }
                mChatView.setRecordVoiceFile(desDir.getPath(), DateFormat.format("yyyy-MM-dd-hhmmss",
                        Calendar.getInstance(Locale.CHINA)) + "");
            }

            @Override
            public void onFinishRecord(File file, int i) {
                try {
                    Message message = conversation.createSendVoiceMessage(file,i);
                    final MyMessage myMessage = new MyMessage(null, IMessage.MessageType.SEND_VOICE);
                    myMessage.setUserInfo(new DefaultUser(String.valueOf(userInfo.getUserID()), userInfo.getDisplayName(), userInfo.getAvatar()));
                    myMessage.setMediaFilePath(file.getPath());
                    myMessage.setDuration(i);
                    myMessage.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
                    loadingTipShow();
                    message.setOnSendCompleteCallback(new BasicCallback() {
                        @Override
                        public void gotResult(int i, String s) {
                            if (i == 0) {
                                mAdapter.addToStart(myMessage, true);
                                loadingTipHide();
                            } else {
                                loadingTipHide();
                                Toast.makeText(mContext,"发送失败",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    JMessageClient.sendMessage(message);
                    if (mData != null) mData.clear();
                } catch (Exception e) {
                    Log.e(TAG,e + "");
                }
            }

            @Override
            public void onCancelRecord() {
                Toast.makeText(mContext,"已取消录音",Toast.LENGTH_LONG).show();
            }
            /**
             * In preview record voice layout, fires when click cancel button
             * Add since chatinput v0.7.3
             */
            @Override
            public void onPreviewCancel() {

            }
            /**
             * In preview record voice layout, fires when click send button
             * Add since chatinput v0.7.3
             */
            @Override
            public void onPreviewSend() {

            }
        });


    }

    //相册点击获取照片的回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            Toast.makeText(mContext,"照片获取失败",Toast.LENGTH_SHORT).show();
        } else {
            if (requestCode == 1) {
                if (data != null) {
                    String realPathFromUri = RealPathFromUriUtils.getRealPathFromUri(mContext,data.getData());
                    sendImage(realPathFromUri);
                } else {
                    Toast.makeText(this, "图片损坏，请重新选择", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    //初始化消息列表
    private List<MyMessage> getMessages() {
        list = new ArrayList<>();
        for (int i = 0; i < conversation.getAllMessage().size(); i++) {
            MyMessage message;
            ContentType type = conversation.getAllMessage().get(i).getContentType();
            //根据消息判断接收方或者发送方类型
            if (conversation.getAllMessage().get(i).getDirect() == MessageDirect.send) {
                //判断消息是否撤回
                if (conversation.getAllMessage().get(i).getContent().getContentType().equals(prompt)) {
                    message = new MyMessage(((PromptContent) conversation.getAllMessage().get(i).getContent()).getPromptText(), SEND_TEXT);
                } else {
                    if ( type == video) {
                        message = new MyMessage(((VideoContent) conversation.getAllMessage().get(i).getContent()).getVideoLocalPath(), SEND_VIDEO);
                        message.setMessage(conversation.getAllMessage().get(i));
                        message.setMediaFilePath(((VideoContent) conversation.getAllMessage().get(i).getContent()).getVideoLocalPath());
                        message.setDuration(((VideoContent) conversation.getAllMessage().get(i).getContent()).getDuration());
                    } else if (type == image) {
                        message = new MyMessage(((ImageContent) conversation.getAllMessage().get(i).getContent()).getLocalThumbnailPath(), SEND_IMAGE);
                        mPathList.add(message.getMediaFilePath());
                        mMsgIdList.add(String.valueOf(message.getId()));
                    } else if (type == file) {
                        message = new MyMessage(((FileContent) conversation.getAllMessage().get(i).getContent()).getLocalPath(), SEND_VIDEO);
                        message.setMediaFilePath(((FileContent) conversation.getAllMessage().get(i).getContent()).getLocalPath());
                    } else if (type == voice) {
                        message = new MyMessage(((VoiceContent) conversation.getAllMessage().get(i).getContent()).getLocalPath(), SEND_VOICE);
                        message.setMediaFilePath(((VoiceContent) conversation.getAllMessage().get(i).getContent()).getLocalPath());
                        message.setDuration(((VoiceContent) conversation.getAllMessage().get(i).getContent()).getDuration());
                    } else {
                        message = new MyMessage(((TextContent) conversation.getAllMessage().get(i).getContent()).getText(), SEND_TEXT);
                    }
                }
                message.setUserInfo(new DefaultUser(userName, userInfo.getDisplayName(), (StringUtils.isNull(imgSend)) ? "R.drawable.ironman" : imgSend));
            } else {
                //判断消息是否撤回
                if (conversation.getAllMessage().get(i).getContent().getContentType().equals(prompt)) {
                    message = new MyMessage(((PromptContent) conversation.getAllMessage().get(i).getContent()).getPromptText(), IMessage.MessageType.RECEIVE_TEXT);
                } else {
                    switch (type) {
                        case video:
                            message = new MyMessage(((VideoContent) conversation.getAllMessage().get(i).getContent()).getVideoLocalPath(), IMessage.MessageType.RECEIVE_VIDEO);
                            message.setMessage(conversation.getAllMessage().get(i));
                            message.setMediaFilePath(((VideoContent) conversation.getAllMessage().get(i).getContent()).getVideoLocalPath());
                            message.setDuration(((VideoContent) conversation.getAllMessage().get(i).getContent()).getDuration());
                            break;
                        case image:
                            message = new MyMessage(((ImageContent) conversation.getAllMessage().get(i).getContent()).getLocalThumbnailPath(), IMessage.MessageType.RECEIVE_IMAGE);
                            mPathList.add(message.getMediaFilePath());
                            mMsgIdList.add(String.valueOf(message.getId()));
                            break;
                        case file:
                            message = new MyMessage(((FileContent) conversation.getAllMessage().get(i).getContent()).getLocalPath(), RECEIVE_VIDEO);
                            message.setMediaFilePath(((FileContent) conversation.getAllMessage().get(i).getContent()).getLocalPath());
                            break;
                        case voice:
                            message = new MyMessage(((VoiceContent) conversation.getAllMessage().get(i).getContent()).getLocalPath(), RECEIVE_VOICE);
                            message.setMediaFilePath(((VoiceContent) conversation.getAllMessage().get(i).getContent()).getLocalPath());
                            message.setDuration(((VoiceContent) conversation.getAllMessage().get(i).getContent()).getDuration());
                            break;
                         default:
                            message = new MyMessage(((TextContent) conversation.getAllMessage().get(i).getContent()).getText(), IMessage.MessageType.RECEIVE_TEXT);
                            break;
                    }
                }
                message.setUserInfo(new DefaultUser(JMessageClient.getMyInfo().getUserName(), nickName, (StringUtils.isNull(imgRecrive)) ? "R.drawable.ironman" : imgRecrive));

            }
            message.setPosition(i);
            message.setMessage(conversation.getAllMessage().get(i));
            message.setMsgID(conversation.getAllMessage().get(i).getServerMessageId());
            message.setTimeString(TimeUtils.ms2date("MM-dd HH:mm", conversation.getAllMessage().get(i).getCreateTime()));
            list.add(message);
        }
        Collections.reverse(list);
        conversation.resetUnreadCount();
        return list;
    }

    @Override
    protected void onResume() {
        friendState();
        super.onResume();
    }

    /*接收到的消息*/
    public void onEvent(MessageEvent event) {
        final Message message = event.getMessage();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //创建一个消息对象
                switch (message.getContentType()) {
                    case image:
                        myMessage = new MyMessage(((ImageContent) message.getContent()).getLocalThumbnailPath(), IMessage.MessageType.RECEIVE_IMAGE);
                        myMessage.setMediaFilePath(((ImageContent) message.getContent()).getLocalThumbnailPath());
                        mPathList.add(myMessage.getMediaFilePath());
                        mMsgIdList.add(String.valueOf(myMessage.getId()));
                        break;
                    case video:
                        myMessage = new MyMessage(((VideoContent) message.getContent()).getVideoLocalPath(), IMessage.MessageType.RECEIVE_VIDEO);
                        myMessage.setMessage(message);
                        myMessage.setDuration(((VideoContent) message.getContent()).getDuration());
                        if (myMessage.getMediaFilePath() == null) {
                            try {
                                final VideoContent videoContent = (VideoContent) message.getContent();
                                videoContent.downloadVideoFile(message, new DownloadCompletionCallback() {
                                    @Override
                                    public void onComplete(int i, String s, File file) {
                                        if (i == 0) {
                                            myMessage.setMediaFilePath(file.getPath());
                                            JMessageClient.sendMessage(message);
//                                            Log.d(TAG,"video receive: " + ((VideoContent) message.getContent()).getDuration());
                                        } else {
                                            Toast.makeText(mContext,"视频下载失败",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } catch (Exception e) {
                                Log.e(TAG,e + "");
                            }
                        }
                        break;
                    case file:
                        myMessage = new MyMessage(((FileContent) message.getContent()).getLocalPath(),RECEIVE_VIDEO);
                        if (myMessage.getMediaFilePath() == null) {
                            try {
                                final FileContent fileContent = (FileContent) message.getContent();
                                fileContent.downloadFile(message, new DownloadCompletionCallback() {
                                    @Override
                                    public void onComplete(int i, String s, File file) {
                                        if (i == 0) {
                                            ((FileContent) message.getContent()).setLocalPath(file.getPath());
                                            myMessage.setMediaFilePath(file.getPath());
                                            myMessage.setDuration(message.getContent().getNumberExtra("duration").longValue());
                                            JMessageClient.sendMessage(message);
                                        } else {
                                            Toast.makeText(mContext,"文件下载失败",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } catch (Exception e) {
                                Log.e(TAG,e + "");
                            }
                        }
                        break;
                    case voice:
                        myMessage = new MyMessage(((VoiceContent) message.getContent()).getLocalPath(),IMessage.MessageType.RECEIVE_VOICE);
                        myMessage.setMediaFilePath(((VoiceContent) message.getContent()).getLocalPath());
                        myMessage.setDuration(((VoiceContent) message.getContent()).getDuration());
                        break;
                    default:
                        myMessage = new MyMessage(((TextContent) message.getContent()).getText(), IMessage.MessageType.RECEIVE_TEXT);
                        myMessage.setText(((TextContent) message.getContent()).getText() + "");
                        break;
                }
                myMessage.setMessage(message);
                myMessage.setMsgID(message.getServerMessageId());
                myMessage.setTimeString(TimeUtils.ms2date("MM-dd HH:mm", message.getCreateTime()));
                myMessage.setUserInfo(new DefaultUser(JMessageClient.getMyInfo().getUserName(), nickName, imgRecrive));

//                if (message.getContentType() == ContentType.text || message.getContentType().equals("text")) {
                mAdapter.addToStart(myMessage, true);
                mAdapter.notifyDataSetChanged();
//                }
                //收到消息时，添加到集合
                list.add(myMessage);
            }

        });

        //do your own business
    }

    /*接收到撤回的消息*/
    public void onEvent(MessageRetractEvent event) {
        final Message message = event.getRetractedMessage();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (list != null && list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getMsgID() == message.getServerMessageId()) {
                            mAdapter.delete(list.get(i));
                            MyMessage message1 = new MyMessage("[对方撤回了一条消息]", IMessage.MessageType.RECEIVE_TEXT);
                            mAdapter.addToStart(message1, true);
                            mAdapter.notifyDataSetChanged();
                            mAdapter.updateMessage(message1);
                        }
                    }
                }
            }

        });

    }

    @Override
    protected void onDestroy() {
        JMessageClient.unRegisterEventReceiver(this);
        JMessageClient.exitConversation();
        super.onDestroy();
        //接收事件解绑
        mSensorManager.unregisterListener(this);
    }

    //初始化adapter
    private void initMsgAdapter() {
        final float density = getResources().getDisplayMetrics().density;
        final float MIN_WIDTH = 60 * density;
        final float MAX_WIDTH = 200 * density;
        final float MIN_HEIGHT = 60 * density;
        final float MAX_HEIGHT = 200 * density;
        //加载头像图片的方法
        imageLoader = new ImageLoader() {
            @Override
            public void loadAvatarImage(ImageView imageView, String s) {
                Picasso.with(getApplication())
                        .load(s)
                        .placeholder(R.drawable.icon_user)
                        .into(imageView);
            }

            @Override
            public void loadImage(final ImageView imageView, String s) {
                //缩略图
                Glide.with(getApplicationContext())
                        .asBitmap()
                        .load(s)
                        .apply(new RequestOptions().fitCenter().placeholder(R.drawable.aurora_picture_not_found))
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                int imageWidth = resource.getWidth();
                                int imageHeight = resource.getHeight();
                                //裁剪bitmap
                                float width,height;
                                if (imageWidth > imageHeight) {
                                    if (imageWidth > MAX_WIDTH) {
                                        float temp = MAX_WIDTH / imageWidth * imageHeight;
                                        height = temp > MIN_HEIGHT ? temp : MIN_HEIGHT;
                                        width = MAX_WIDTH;
                                    } else if (imageWidth < MIN_WIDTH) {
                                        float temp = MIN_WIDTH / imageWidth * imageHeight;
                                        height = temp < MAX_HEIGHT ? temp : MAX_HEIGHT;
                                        width = MIN_WIDTH;
                                    } else {
                                        float ratio = imageWidth / imageHeight;
                                        if (ratio > 3) {
                                            ratio = 3;
                                        }
                                        height = imageHeight * ratio;
                                        width = imageWidth;
                                    }
                                } else {
                                    if (imageHeight > MAX_HEIGHT) {
                                        float temp = MAX_HEIGHT / imageHeight * imageWidth;
                                        width = temp > MIN_WIDTH ? temp : MIN_WIDTH;
                                        height = MAX_HEIGHT;
                                    } else if (imageHeight < MIN_HEIGHT) {
                                        float temp = MIN_HEIGHT / imageHeight * imageWidth;
                                        width = temp < MAX_WIDTH ? temp : MAX_WIDTH;
                                        height = MIN_HEIGHT;
                                    } else {
                                        float ratio = imageHeight / imageWidth;
                                        if (ratio > 3) {
                                            ratio = 3;
                                        }
                                        width = imageWidth * ratio;
                                        height = imageHeight;
                                    }
                                }
                                ViewGroup.LayoutParams params = imageView.getLayoutParams();
                                params.width = (int) width;
                                params.height = (int) height;
                                imageView.setLayoutParams(params);
                                Matrix matrix = new Matrix();
                                float scaleWidth = width / imageWidth;
                                float scaleHeight = height / imageHeight;
                                matrix.postScale(scaleWidth,scaleHeight);
                                imageView.setImageBitmap(Bitmap.createBitmap(resource,0,0,imageWidth,imageHeight,matrix,true));
                            }
                        });
            }

            @Override
            public void loadVideo(ImageView imageCover, String uri) {
                long interval = 5000 * 1000;
                Glide.with(ChatMsgActivity.this)
                        .asBitmap()
                        .load(uri)
                        .apply(new RequestOptions()
                                .frame(interval)
                                .override(200,400)
                                .error(R.drawable.aurora_preview_record_video_start))
                        .into(imageCover);
            }
        };


        /**
         * 自定义viewholder
         */
        class MyTexViewHolder extends MyViewHolder<IMessage> {
            public MyTexViewHolder(View itemView, boolean isSender) {
                super(itemView, isSender);
            }
        }

        /**
         * 1、Sender Id: 发送方 Id(唯一标识)。
         * 2、HoldersConfig，可以用这个对象来构造自定义消息的 ViewHolder 及布局界面。
         * 如果不自定义则使用默认的布局
         * 3、ImageLoader 的实例，用来展示头像。如果为空，将会隐藏头像。
         */
        final MsgListAdapter.HoldersConfig holdersConfig = new MsgListAdapter.HoldersConfig();
        mAdapter = new MsgListAdapter<MyMessage>(helper.getUserId(), holdersConfig, imageLoader);
        //单击消息事件，可以选择查看大图或者播放视频
        mAdapter.setOnMsgClickListener(new MsgListAdapter.OnMsgClickListener<MyMessage>() {
            @Override
            public void onMessageClick(MyMessage message) {
                // do something
//                Log.d(TAG,"click video: " + message.getMediaFilePath());
                if (message.getType() == IMessage.MessageType.RECEIVE_VIDEO.ordinal()
                        || message.getType() == IMessage.MessageType.SEND_VIDEO.ordinal()) {

                    if (!TextUtils.isEmpty(message.getMediaFilePath())) {
                        Intent intent = new Intent(mContext, MsgVideoActivity.class);
                        intent.putExtra(MsgVideoActivity.VIDEO_PATH, message.getMediaFilePath());
                        startActivity(intent);
                    }
                } else if (message.getType() == IMessage.MessageType.RECEIVE_IMAGE.ordinal()
                    || message.getType() == SEND_IMAGE.ordinal()){
                    if (!TextUtils.isEmpty(message.getMediaFilePath())) {
                        Intent intent = new Intent(mContext, BrowserImageActivity.class);
                        intent.putExtra("id",String.valueOf(message.getId()));
                        intent.putStringArrayListExtra("pathList", mPathList);
                        intent.putStringArrayListExtra("idList", mMsgIdList);
                        mContext.startActivity(intent);
                    }
                } else {

                }
            }
        });

        //长按消息
        mAdapter.setMsgLongClickListener(new MsgListAdapter.OnMsgLongClickListener<MyMessage>() {
            @Override
            public void onMessageLongClick(View view,final MyMessage message) {
                String[] strings;
                msgID = message.getMsgId();

                //判断消息类型
                if (message.getType() == SEND_TEXT.ordinal()
                        || message.getType() == SEND_CUSTOM.ordinal()
                        || message.getType() == SEND_FILE.ordinal()
                        || message.getType() == SEND_IMAGE.ordinal()
                        || message.getType() == SEND_LOCATION.ordinal()
                        || message.getType() == SEND_VIDEO.ordinal()) {
                    strings = new String[]{"复制", "撤回"};
//                    , "转发", "删除"
                } else {
                    strings = new String[]{"复制"};
//                    , "删除", "转发"
                }
                final MyAlertDialog dialog = new MyAlertDialog(ChatMsgActivity.this,
                        strings
                        , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
                                //复制：当消息类型为文字的时候才可以复制
                                if (message.getType() == SEND_TEXT.ordinal()
                                        || message.getType() == RECEIVE_TEXT.ordinal()) {
                                    if (Build.VERSION.SDK_INT > 11) {
                                        ClipboardManager clipboard = (ClipboardManager) mContext
                                                .getSystemService(Context.CLIPBOARD_SERVICE);
                                        ClipData clip = ClipData.newPlainText("Simple text", message.getText());
                                        clipboard.setPrimaryClip(clip);
                                    } else {
                                        android.text.ClipboardManager clip = (android.text.ClipboardManager) mContext
                                                .getSystemService(Context.CLIPBOARD_SERVICE);
                                        if (clip.hasText()) {
                                            clip.getText();
                                        }
                                    }

                                    showToast(ChatMsgActivity.this, "复制成功");
                                } else {
                                    showToast(ChatMsgActivity.this, "复制类型错误");
                                }
                                break;

                            case 1:
                                //撤回：发送方才可撤回
                                conversation.retractMessage(message.getMessage(), new BasicCallback() {
                                    @Override
                                    public void gotResult(int i, String s) {
                                        if (i == 0) {
                                            showToast(ChatMsgActivity.this, "撤回了一条消息");
                                            mAdapter.deleteById(message.getMsgId());
                                            mAdapter.updateMessage(message);
                                        } else {
                                            showToast(ChatMsgActivity.this, "撤回失败：" + s);
                                        }
                                    }
                                });
                                break;
                            case 2:
                                //转发
                                break;
                            default:
                                //2\从本地删除
                                conversation.deleteMessage(Integer.valueOf(message.getMsgId()));
                                //移除视图
                                mAdapter.deleteById(message.getMsgId());
                                mAdapter.notifyDataSetChanged();
                                break;
                        }
                    }
                });

                dialog.initDialog(Gravity.CENTER);
                dialog.dialogSize(200, 0, 0, 55);
            }


        });

        //重新发送
//        mAdapter.setMsgResendListener(new MsgListAdapter.OnMsgResendListener<MyMessage>() {
//            @Override
//            public void onMessageResend(MyMessage message) {
//                // resend message here
//            }
//        });

        MyMessage message = new MyMessage("Hello World", IMessage.MessageType.RECEIVE_TEXT);
        message.setUserInfo(new DefaultUser("0", "Deadpool", "R.drawable.deadpool"));

        mAdapter.addToEnd(mData);
        mAdapter.setOnLoadMoreListener(new MsgListAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore(int page, int totalCount) {
                if (totalCount <= mData.size()) {
//                    Log.i(TAG + "MessageList", "Loading next page");
//                    loadNextPage();
                }
            }
        });

        mMsgList.setAdapter(mAdapter);
        mAdapter.getLayoutManager().scrollToPosition(0);
    }


    /*加载更多*/
    private void loadNextPage() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter.addToEnd(mData);
            }
        }, 1000);
    }

    private void initKeyBoard() {
        Log.e(TAG + "height======", "" + keyboardHeight);

    }

//    public void onSizeChanged(int w, int h, int oldw, int oldh) {
//        if (oldh - h > 300) {
//            mChatInput.setMenuContainerHeight(oldh - h);
//            mChatView.setMenuHeight(oldh - h);
//        }
//        scrollToBottom();
//    }

    /*滚动到底部*/
    private void scrollToBottom() {
//        mAdapter.getLayoutManager().scrollToPosition(0);

        mChatView.setMsgListHeight(false,ChatMsgActivity.this.getWindow().getDecorView().getRootView().getHeight());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                Log.d(TAG,"getMessageListView: " + mChatView.getMessageListView());
                if (mChatView.getMessageListView() == null) return;
                mChatView.getMessageListView().smoothScrollToPosition(0);
            }
        }, 200);
    }

//    private ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
//
//        @Override
//        public void onGlobalLayout() {
//            // 应用可以显示的区域。此处包括应用占用的区域，
//            // 以及ActionBar和状态栏，但不含设备底部的虚拟按键。
//            Rect r = new Rect();
//            mChatView.getWindowVisibleDisplayFrame(r);
//
//            // 屏幕高度。这个高度不含虚拟按键的高度
//            int screenHeight = mChatView.getRootView().getHeight();
//
//            int heightDiff = screenHeight - (r.bottom - r.top);
//
//            // 在不显示软键盘时，heightDiff等于状态栏的高度
//            // 在显示软键盘时，heightDiff会变大，等于软键盘加状态栏的高度。
//            // 所以heightDiff大于状态栏高度时表示软键盘出现了，
//            // 这时可算出软键盘的高度，即heightDiff减去状态栏的高度
//            if (keyboardHeight == 0 && heightDiff > statusBarHeight) {
//                keyboardHeight = heightDiff - statusBarHeight;
//            }
//            Log.e("onkeyboardHeight", ":" + keyboardHeight);
//            mChatInput.setMenuContainerHeight(keyboardHeight);
//
//            if (isShowKeyboard) {
//                // 如果软键盘是弹出的状态，并且heightDiff小于等于状态栏高度，
//                // 说明这时软键盘已经收起
//                if (heightDiff <= statusBarHeight) {
//                    isShowKeyboard = false;
//                    onHideKeyboard();
//                }
//            } else {
//                // 如果软键盘是收起的状态，并且heightDiff大于状态栏高度，
//                // 说明这时软键盘已经弹出
//                if (heightDiff > statusBarHeight) {
//                    isShowKeyboard = true;
//                    onShowKeyboard();
//                }
//            }
//        }
//    };


    private void onShowKeyboard() {
        // 在这里处理软键盘弹出的回调
        Log.e(TAG + "onShowKeyboard:", "" + keyboardHeight);
//        mChatInput.setMenuContainerHeight(keyboardHeight);


    }

    private void onHideKeyboard() {
        // 在这里处理软键盘收回的回调
        Log.e(TAG + "onHidekey = ", "dd");
//        mChatInput.setMenuContainerHeight(-keyboardHeight);
    }

    /*标题栏*/
    private void initTitleBar(String nickName) {
        mTitleBarBack.setVisibility(View.INVISIBLE);
        mTitleBarBack.setImageDrawable(getResources().getDrawable(R.mipmap.icon_back));
        mTitleOptionsImg.setVisibility(View.GONE);
        mTitleOptionsTv.setVisibility(View.VISIBLE);
        mTitleBarTitle.setText(nickName);
    }

    @Override
    protected void initData() {

    }


    //返回按钮
    private void chatPageBack() {
        mTitleBarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

//    @SuppressLint("InvalidR2Usage")
////    @OnClick({R2.id.title_bar_back,R2.id.title_bar_title, R2.id.title_options_tv, R2.id.chat_send})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
////            case R.id.title_bar_title:
////                Intent intent = new Intent(mContext, UserInfoActivity.class);
////                intent.putExtra("USERNAME", userName);
////                break;
//            case R.id.title_bar_back:
//                finish();
//                //重置会话未读
//                conversation.resetUnreadCount();
//                break;
//            case R.id.title_options_tv:
//                //
//                MyAlertDialog dialog = new MyAlertDialog(this, new String[]{"清空聊天记录", "清空并删除会话"}, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        switch (i) {
//                            case 0:
//                                showProgressDialog("正在删除聊天记录");
//                                if (conversation.deleteAllMessage()) {
//                                    mAdapter.clear();
//                                    mData.clear();
//                                    mAdapter.notifyDataSetChanged();
//                                    dismissProgressDialog();
//                                }
//
//                                break;
//                            case 1:
//                                showProgressDialog("正在删除");
////                                if (JMessageClient.deleteSingleConversation(userName)) {
////                                    startActivity(new Intent(ChatMsgActivity.this, MainActivity.class));
////                                }
//                                break;
//                        }
//                    }
//                });
//                dialog.initDialog(Gravity.RIGHT | Gravity.TOP);
//                dialog.dialogSize(200, 0, 0, 55);
//
//                break;
////            case R2.id.chat_send:
////                Log.d(TAG,"send: " + mChatEt.getText());
////                sendMessage(mChatEt.getText().toString());
////                break;
//        }
//    }



    /*获取对方在线状态*/
    String state;

    public void friendState() {
        NetWorkManager.isFriendState(userName, new Callback<UserStateBean>() {
            @Override
            public void onResponse(Call<UserStateBean> call, Response<UserStateBean> response) {
                if (response.code() == 200) {
                    if (response.body().online) {
                        state = "[在线]";
                    } else {
                        state = "[离线]";
                    }
                    mTitleBarTitle.setText(nickName + state);
                } else {
                    mTitleBarTitle.setText(nickName);
                }
            }

            @Override
            public void onFailure(Call<UserStateBean> call, Throwable throwable) {
            }
        });

    }

    @Override
//    public void onKeyBoardStateChanged(int state) {
//        switch (state) {
//            case ChatInputView.KEYBOARD_STATE_INIT:
//                ChatInputView chatInputView = mChatView.getChatInputView();
//                if (mManager != null) {
//                    mManager.isActive();
//                }
//                if (chatInputView.getMenuState() == View.INVISIBLE
//                        || (!chatInputView.getSoftInputState()
//                        && chatInputView.getMenuState() == View.GONE)) {
//
//                    mWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
//                            | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//                    try {
//                        Thread.sleep(100);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    chatInputView.dismissMenuLayout();
//                }
//                break;
//        }
//    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                ChatInputView chatInputView = mChatView.getChatInputView();

                if (view.getId() == chatInputView.getInputView().getId()) {

                    if (chatInputView.getMenuState() == View.VISIBLE
                            && !chatInputView.isKeyboardVisible()) {
                        chatInputView.dismissMenuLayout();
                        return false;
                    } else {
                        return false;
                    }
                }
                if (chatInputView.getMenuState() == View.VISIBLE) {
                    chatInputView.dismissMenuLayout();
                }
                mChatView.setMsgListHeight(true,ChatMsgActivity.this.getWindow().getDecorView().getRootView().getHeight());
                try {
                    View v = getCurrentFocus();
                    if (mManager != null && v != null) {
                        mManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        mWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                        view.clearFocus();
//                        chatInputView.setShowBottomMenu(false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case MotionEvent.ACTION_UP:
                view.performClick();
                break;
        }
        return false;
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
