package com.weexplus.jim.module;

import android.util.Log;
import android.widget.Toast;

import com.farwolf.weex.annotation.WeexModule;
import com.farwolf.weex.base.WXModuleBase;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.weexplus.jim.activity.ChatMsgActivity;

import java.util.HashMap;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.android.api.options.RegisterOptionalUserInfo;
import cn.jpush.im.api.BasicCallback;

//此注解将自动注册module到weex
@WeexModule(name="JMessage")

public class JIMWeexModule extends WXModuleBase {

    private static String TAG = "JMessage";
    private static String MESSAGE = "message";
    private static String CODE = "code";
    //异步返回

    //注册账号
    @JSMethod(uiThread = false)
    public void register(String username, String password, String nikeName, final JSCallback callback) {
        RegisterOptionalUserInfo registerOptionalUserInfo = new RegisterOptionalUserInfo();
        registerOptionalUserInfo.setNickname(nikeName);
        JMessageClient.register(username, password, registerOptionalUserInfo, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                HashMap res = new HashMap();
                res.put(CODE,i);
                res.put(MESSAGE,s);
                Log.i(TAG,"register" + s);
                callback.invoke(res);
            }
        });
    }

    //登陆
    @JSMethod(uiThread = false)
    public void login(String username, String password, final JSCallback callback) {
        Log.i(TAG,username);
        JMessageClient.login(username, password, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                HashMap res = new HashMap();
                res.put(CODE,i);
                res.put(MESSAGE,s);
                Log.i(TAG,"login" + s);
                callback.invoke(res);
            }
        });
    }

//    @JSMethod
//    public void getOnlineStatus() {
//    }

    //获取JMessage的SDK版本
    @JSMethod(uiThread = false)
    public void getSDKVersion(JSCallback callback) {
        String version = JMessageClient.getSdkVersionString();
        callback.invoke(version);
    }

    //获取用户信息
    @JSMethod(uiThread = false)
    public void getUserInfo(String username, final JSCallback callback) {
        JMessageClient.getUserInfo(username, new GetUserInfoCallback() {
            @Override
            public void gotResult(int i, String s, UserInfo userInfo) {
                HashMap res = new HashMap();
                res.put(CODE,i);
                res.put(MESSAGE,s);
                res.put("userInfo",userInfo);
//                Log.i(TAG,s);
                callback.invoke(res);
            }
        });
    }

    //跳转至聊天界面
    @JSMethod (uiThread = false)
    public void startChat(HashMap param) {
        String account = param.get("account") + "";
        String userName = param.get("userName") + "";
        String title = param.get("nickName") + "";
        String msgID = param.get("msgID") + "";
        String position = param.get("position") + "";
//        MessageListActivity.start(getContext(),account);
//        SimplePage.start(getContext());
//        Sample.start(getContext(),account,userName,title);
        ChatMsgActivity.start(getContext(),account,userName,title,msgID,Integer.valueOf(position));
//        Log.d("chat_msg", "username:" + userName + ",msgID:" + msgID + ",position:" + position);
//        ChatViewActivity.start(getContext(),account,userName,title,msgID,Integer.valueOf(position));
    }

    //设置debug模式
    @JSMethod (uiThread = false)
    public void setDebugMode(boolean debug) {
        JMessageClient.setDebugMode(debug);
        Log.i(TAG, "setDebugMode:"+debug);
    }

    //创建文字消息
    /**
     * 创建一条单聊文本消息，此方法是创建message的快捷接口，对于不需要关注会话实例的开发者可以使用此方法
     *
     * @param username 聊天对象用户名
     * @param appKey   聊天对象所属应用的appKey  为空则使用默认的
     * @param text     文本内容
     * @return 消息对象
     */
    @JSMethod (uiThread = false)
    public void createSingleTextMsg(String username, String text, String appKey, JSCallback callback){
        if (text != null && username != null) {
            JMessageClient.createSingleTextMessage(username,text);
        } else {
            Toast.makeText(getContext(),"未设置聊天对象",Toast.LENGTH_SHORT).show();
            callback.invoke("");
        }
    }

    @JSMethod (uiThread = false)
    public void getConversationList(String userName, JSCallback callback) {
        Conversation list;
        list = JMessageClient.getSingleConversation(userName);
//        Log.d("chat_msg","single: " + list + " userName: " + userName);
        if (list == null || list.getAllMessage().size() == 0) {
            callback.invoke("暂无会话");
        } else {
            callback.invoke(list);
        }
    }

    @JSMethod (uiThread = false)
    public void getConversationAllList(JSCallback callback) {
        List<Conversation> list;
        list = JMessageClient.getConversationList();
        if (list.isEmpty()) {
            callback.invoke(null);
        } else {
            callback.invoke(list);
        }
    }

//    @JSMethod (uiThread = false)
//    public void createChatView() {
//
//    }

    
}
