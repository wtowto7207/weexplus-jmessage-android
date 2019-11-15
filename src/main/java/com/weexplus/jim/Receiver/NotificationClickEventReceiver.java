package com.weexplus.jim.Receiver;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.weexplus.jim.activity.ChatMsgActivity;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.event.NotificationClickEvent;
import cn.jpush.im.android.api.model.Message;

public class NotificationClickEventReceiver {
    private Context mContext;
    final static String TAG = "chat_msg";

    public NotificationClickEventReceiver(Context context) {
        mContext = context;
        JMessageClient.registerEventReceiver(this);
    }

    public void onEvent(NotificationClickEvent event) {
//        Log.d(TAG,"notification click");
        final Message msg = event.getMessage();
        Intent notificationIntent = new Intent(mContext, ChatMsgActivity.class);
        notificationIntent.putExtra("USERNAME", msg.getFromUser().getUserName());
        notificationIntent.putExtra("NICKNAME", msg.getFromUser().getDisplayName());
        notificationIntent.putExtra("MSGID","");
        notificationIntent.putExtra("position",0);
        JMessageClient.unRegisterEventReceiver(this);
        mContext.startActivity(notificationIntent);//自定义跳转到指定页面
    }
}
