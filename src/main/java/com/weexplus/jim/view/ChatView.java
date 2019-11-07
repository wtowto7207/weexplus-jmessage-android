package com.weexplus.jim.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.weexplus.jim.R;
import com.weexplus.jim.framework.messages.MessageListCus;

import cn.jiguang.imui.chatinput.ChatInputView;
import cn.jiguang.imui.chatinput.listener.OnCameraCallbackListener;
import cn.jiguang.imui.chatinput.listener.OnClickEditTextListener;
import cn.jiguang.imui.chatinput.listener.OnMenuClickListener;
import cn.jiguang.imui.chatinput.listener.RecordVoiceListener;
import cn.jiguang.imui.chatinput.record.RecordVoiceButton;
import cn.jiguang.imui.messages.MessageList;
import cn.jiguang.imui.messages.MsgListAdapter;

//import static cn.jiguang.imui.chatinput.ChatInputView.KEYBOARD_STATE_HIDE;
//import static cn.jiguang.imui.chatinput.ChatInputView.KEYBOARD_STATE_INIT;
//import static cn.jiguang.imui.chatinput.ChatInputView.KEYBOARD_STATE_SHOW;


public class ChatView extends RelativeLayout {

    final static String TAG = "chat_msg";


    private TextView mTitle;
    private MessageList mMsgList;
    private ChatInputView mChatInput;
    private LinearLayout mMenuLl;
    private RecordVoiceButton mRecordVoiceBtn;
    private ImageButton mSelectAlbumIb;

    private boolean mHasInit;
    private boolean mHasKeyboard;
    private int mHeight;

    private OnKeyboardChangedListener mKeyboardListener;
    private OnSizeChangedListener mSizeChangedListener;

    public ChatView(Context context) {
        super(context);
    }

    public ChatView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChatView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void initModule() {
        mTitle = (TextView) findViewById(R.id.title_bar_title);
        mMsgList = findViewById(R.id.msg_list);
        mMenuLl = (LinearLayout) findViewById(R.id.aurora_ll_menuitem_container);
        mChatInput = (ChatInputView) findViewById(R.id.chat_input);
//        mChatInput.setMenuContainerHeight(819);
        mRecordVoiceBtn = mChatInput.getRecordVoiceButton();
//        mSelectAlbumIb = mChatInput.getSelectAlbumBtn();
        mMsgList.setHasFixedSize(true);
//        mMsgList.setShowReceiverDisplayName(0);
//        mMsgList.setShowSenderDisplayName(1);

        //add Custom Menu View
    }

    public void setTitle(String title) {
        mTitle.setText(title);
    }

    public void setMenuClickListener(OnMenuClickListener listener) {
        mChatInput.setMenuClickListener(listener);
    }

    public void setAdapter(MsgListAdapter adapter) {
        mMsgList.setAdapter(adapter);
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        mMsgList.setLayoutManager(layoutManager);
    }

    public void setRecordVoiceFile(String path, String fileName) {
        mRecordVoiceBtn.setVoiceFilePath(path, fileName);
    }

    public void setCameraCaptureFile(String path, String fileName) {
        mChatInput.setCameraCaptureFile(path, fileName);
    }

    public void setRecordVoiceListener(RecordVoiceListener listener) {
        mRecordVoiceBtn.setRecordVoiceListener(listener);
    }

    public void setOnCameraCallbackListener(OnCameraCallbackListener listener) {
        mChatInput.setOnCameraCallbackListener(listener);
    }

    public void setKeyboardChangedListener(OnKeyboardChangedListener listener) {
        mKeyboardListener = listener;
    }

    public void setOnSizeChangedListener(OnSizeChangedListener listener) {
        mSizeChangedListener = listener;
    }

    public void setOnTouchListener(OnTouchListener listener) {
        mMsgList.setOnTouchListener(listener);
    }

    public void setOnTouchEditTextListener(OnClickEditTextListener listener) {
        mChatInput.setOnClickEditTextListener(listener);
    }

    @Override
    public boolean performClick() {
        super.performClick();
        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mSizeChangedListener != null) {
            mSizeChangedListener.onSizeChanged(w, h, oldw, oldh);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (!mHasInit) {
            mHasInit = true;
            mHeight = b;
//            if (null != mKeyboardListener) {
//                mKeyboardListener.onKeyBoardStateChanged(KEYBOARD_STATE_INIT);
//            }
        } else {
//            if (null != mKeyboardListener) {
//                mKeyboardListener.onKeyBoardStateChanged(KEYBOARD_STATE_INIT);
//            }
            mHeight = mHeight < b ? b : mHeight;
        }
        if (mHasInit && mHeight > b) {
            mHasKeyboard = true;
//            if (null != mKeyboardListener) {
//                mKeyboardListener.onKeyBoardStateChanged(KEYBOARD_STATE_SHOW);
//            }
        }
        if (mHasInit && mHasKeyboard && mHeight == b) {
            mHasKeyboard = false;
//            if (null != mKeyboardListener) {
//                mKeyboardListener.onKeyBoardStateChanged(KEYBOARD_STATE_HIDE);
//            }
        }
    }

    public ChatInputView getChatInputView() {
        return mChatInput;
    }

    public MessageList getMessageListView() {
        return mMsgList;
    }

    public void setMenuHeight(int height) {
        mChatInput.setMenuContainerHeight(height);
    }

    /**
     * Keyboard status changed will invoke onKeyBoardStateChanged
     */
    public interface OnKeyboardChangedListener {

        /**
         * Soft keyboard status changed will invoke this callback, use this callback to do you logic.
         *
         * @param state Three states: init, show, hide.
         */
        public void onKeyBoardStateChanged(int state);
    }

    public interface OnSizeChangedListener {
        void onSizeChanged(int w, int h, int oldw, int oldh);
    }

    /**
     * reset MessageList's height, so that switch to SoftInput or Menu
     * wouldn't cause MessageList scroll
     * @param isTouchMsgList if touch MessageList, reset MessageList's height.
     */
    public void setMsgListHeight(boolean isTouchMsgList, int maxHeight) {
        if (!isTouchMsgList) {
            if (mMsgList == null) return;
            ViewGroup.LayoutParams layoutParams = mMsgList.getLayoutParams();
            int height = mChatInput.getSoftKeyboardHeight();
            if (height > 0) {
                layoutParams.height = mChatInput.getSoftKeyboardHeight();
                mMsgList.setLayoutParams(layoutParams);
                Log.d(TAG, "set MessageList height before, height = " + layoutParams.height);
            }
        } else {
            ViewGroup.LayoutParams layoutParams = mMsgList.getLayoutParams();
            layoutParams.height = maxHeight;
            Log.d(TAG, "set MessageList height, height = " + layoutParams.height);
            mMsgList.setLayoutParams(layoutParams);
        }
    }
}
