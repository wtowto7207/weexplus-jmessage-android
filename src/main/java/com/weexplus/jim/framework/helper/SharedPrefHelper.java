package com.weexplus.jim.framework.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.taobao.weex.common.WXModule;

/**
 * Created by wapchief on 2017/7/14.
 * 本地存储
 */

public class SharedPrefHelper extends WXModule {
    private static SharedPrefHelper sharedPrefHelper = null;
    private static SharedPreferences sharedPreferences;

    public SharedPrefHelper() {
        if(this.mWXSDKInstance==null||this.mWXSDKInstance.getContext()==null)
            return ;
        sharedPreferences = this.mWXSDKInstance.getContext().getSharedPreferences("farwolf_weex",Context.MODE_MULTI_PROCESS);
    }

    public static synchronized SharedPrefHelper getInstance() {
        if (null == sharedPrefHelper) {
//            Log.d("chat_msg","SharedPrefHelper:" + sharedPrefHelper);
            sharedPrefHelper = new SharedPrefHelper();
        }
//        Log.d("chat_msg","SharedPrefHelper_after:" + sharedPrefHelper);
        return sharedPrefHelper;
    }

    public SharedPrefHelper(Context context) {
        sharedPreferences = context.getSharedPreferences("farwolf_weex",Context.MODE_PRIVATE);
    }



    public void setUserId(String guestId) {
        sharedPreferences.edit().putString("userName", guestId).apply();
    }

    public String getUserId() {
//        if(this.mWXSDKInstance==null||this.mWXSDKInstance.getContext()==null)
//        {
////            Log.d("chat_msg","getUserId null");
//            return "duoduo";
//        } else {
            Log.d("chat_msg","getUserId: " + sharedPreferences.getString("userName",""));
            return sharedPreferences.getString("userName", "");
//        }
    }
    public void setUserPW(String guestId) {
        sharedPreferences.edit().putString("userPW", guestId).apply();
    }

    public String getUserPW() {
        return sharedPreferences.getString("userPW", "");
    }

    /*昵称*/
    public void setNakeName(String guestId) {
        sharedPreferences.edit().putString("userNakeName", guestId).apply();
    }

    public String getNakeName() {
        return sharedPreferences.getString("userNakeName", "");
    }

    /*漫游开启状态*/
    public void setRoaming(boolean flag) {
        sharedPreferences.edit().putBoolean("roaming", flag).apply();
    }

    public boolean getRoaming() {
        return sharedPreferences.getBoolean("roaming", false);
    }
    /*推送开启状态*/
    public void setPush(boolean flag) {
        sharedPreferences.edit().putBoolean("push", flag).apply();
    }

    public boolean getMusic() {
        return sharedPreferences.getBoolean("push_music", false);
    }

    /*声音推送开启状态*/
    public void setMusic(boolean flag) {
        sharedPreferences.edit().putBoolean("push_music", flag).apply();
    }

    public boolean getPush() {
        return sharedPreferences.getBoolean("push", false);
    }
    /*震动开启状态*/
    public void setVib(boolean flag) {
        sharedPreferences.edit().putBoolean("push_vib", flag).apply();
    }

    public boolean getVib() {
        return sharedPreferences.getBoolean("push_vib", false);
    }

    public void setAppKey(String appKey) {
        sharedPreferences.edit().putString("appkey", appKey).apply();
    }
    public String getAppKey() {
        return sharedPreferences.getString("appkey","");
    }
}
