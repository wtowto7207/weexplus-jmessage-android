package com.weexplus.jim.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jiguang.imui.commons.BitmapLoader;
import com.weexplus.jim.R;
import com.weexplus.jim.views.ImgBrowserViewPager;
import com.weexplus.jim.views.photoview.PhotoView;

import org.jetbrains.annotations.NotNull;


public class BrowserImageActivity extends Activity {

    private ImgBrowserViewPager mViewPager;
    private List<String> mPathList = new ArrayList<>();
    private List<String> mMsgIdList = new ArrayList<>();
    private String msgId = "";
    private LruCache<String, Bitmap> mCache;
    private int mWidth;
    private int mHeight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_browser);
        mPathList = getIntent().getStringArrayListExtra("pathList");
        mMsgIdList = getIntent().getStringArrayListExtra("idList");
        msgId = getIntent().getStringExtra("id");
//        Log.d("chat_msg", "id: " + msgId + "  mMsgList: " + mMsgIdList);
        mViewPager = findViewById(R.id.img_browser_viewpager);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        mWidth = dm.widthPixels;
        mHeight = dm.heightPixels;

        int maxMemory = (int) (Runtime.getRuntime().maxMemory());
        int cacheSize = maxMemory / 4;
        mCache = new LruCache<>(cacheSize);
        mViewPager.setAdapter(pagerAdapter);
        initCurrentItem();
    }

    private void initCurrentItem() {
        PhotoView photoView = new PhotoView(true, this);
        int position = mMsgIdList.indexOf(msgId);
        String path = mPathList.get(position);
        if (path != null) {
            Bitmap bitmap = mCache.get(path);
            if (bitmap != null) {
                photoView.setImageBitmap(bitmap);
            } else {
                File file = new File(path);
                if (file.exists()) {
//                    Log.d("chat_msg","width: " + mWidth + " height: " + mHeight);
                    bitmap = BitmapLoader.getBitmapFromFile(path, mWidth, mHeight);
                    if (bitmap != null) {
                        photoView.setImageBitmap(bitmap);
                        mCache.put(path, bitmap);
                    } else {
                        photoView.setImageResource(R.drawable.aurora_picture_not_found);
                    }
                } else {
                    photoView.setImageResource(R.drawable.aurora_picture_not_found);
                }
            }
        } else {
            photoView.setImageResource(R.drawable.aurora_picture_not_found);
        }
        mViewPager.setCurrentItem(position);
    }

    PagerAdapter pagerAdapter = new PagerAdapter() {
        @Override
        public int getCount() {
            return mPathList.size();
        }

        @Override
        public boolean isViewFromObject(@NotNull View view, @NotNull Object object) {
            return view == object;
        }

        @NotNull
        @Override
        public Object instantiateItem(@NotNull ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(true, BrowserImageActivity.this);
            photoView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            photoView.setTag(position);
            String path = mPathList.get(position);
            if (path != null) {
                Bitmap bitmap = mCache.get(path);
                if (bitmap != null) {
                    photoView.setImageBitmap(bitmap);
                } else {
                    File file = new File(path);
                    if (file.exists()) {
                        bitmap = BitmapLoader.getBitmapFromFile(path, mWidth, mHeight);
                        if (bitmap != null) {
                            photoView.setImageBitmap(bitmap);
                            mCache.put(path, bitmap);
                        } else {
                            photoView.setImageResource(R.drawable.aurora_picture_not_found);
                        }
                    } else {
                        photoView.setImageResource(R.drawable.aurora_picture_not_found);
                    }
                }
            } else {
                photoView.setImageResource(R.drawable.aurora_picture_not_found);
            }
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getItemPosition(Object object) {
            View view = (View) object;
            int currentPage = mViewPager.getCurrentItem();
            if (currentPage == (Integer) view.getTag()) {
                return POSITION_NONE;
            } else {
                return POSITION_UNCHANGED;
            }
        }
    };


}
