package com.weexplus.jim.framework.messages;

import android.content.Context;

import cn.jiguang.imui.messages.MessageList;

public class MessageListCus extends MessageList {

    private int mMaxHeight;

    public MessageListCus(Context context) {
        super(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (mMaxHeight == 0) {
            mMaxHeight = b;
        }
    }

    public int getMaxHeight() {
        return mMaxHeight;
    }
}
