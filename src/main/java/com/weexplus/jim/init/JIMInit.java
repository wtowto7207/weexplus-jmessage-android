package com.weexplus.jim.init;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.blankj.utilcode.util.Utils;
import com.farwolf.weex.annotation.ModuleEntry;
import com.weexplus.jim.framework.helper.SharedPrefHelper;

//import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;

//此注解会被框架扫描到并执行类的init方法
@ModuleEntry
public class JIMInit extends Application {

    public static JIMInit baseApplication;
    private SharedPrefHelper sharedPrefHelper;
    //**方法明和参数固定，框架会根据反射执行此函数，context 为application类型
    //**如果有第三方需要初始化，请在此处执行
    public void init(Context context) {
        Log.e("jmsg","init");
        baseApplication = this;
        JMessageClient.setDebugMode(true);
        //实例化极光IM，并自动同步聊天记录
        JMessageClient.init(context);
//        Log.d("chat_msg","JIMInit" + sharedPrefHelper);
        sharedPrefHelper = SharedPrefHelper.getInstance();
//        Log.d("chat_msg","JIMInit_shared");
        sharedPrefHelper.setRoaming(true);
        //实例化极光推送
//        JPushInterface.init(context);
        JMessageClient.setNotificationFlag(JMessageClient.FLAG_NOTIFY_SILENCE);
        Utils.init(this);
        sharedPrefHelper.setMusic(false);
        sharedPrefHelper.setVib(false);
        sharedPrefHelper.setAppKey("8b6f6fa6828662599492986e");
    }
}
