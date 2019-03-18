package com.cnlive.updownfile.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.cnlive.updownfile.R;

public class RayGlide {

    /**
     * 默认加载
     */
    public static void loadLocalPath(Context mContext, String path, int defaultResId, ImageView mImageView) {
        Glide.with(mContext).load(path).apply(normalOptions(defaultResId)).into(mImageView);
    }

    /**
     * 默认加载
     */
    public static void loadPath(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).apply(normalOptions(R.mipmap.refresh_loading01)).into(mImageView);
    }

    public static void loadPathNoBase(Context mContext, String path, int defaultResId, ImageView mImageView) {
        Glide.with(mContext).load(path).apply(normalOptions(defaultResId)).into(mImageView);
    }

    public static void loadPathOptions(Context mContext, String path, int defaultResId, ImageView mImageView) {
        Glide.with(mContext).load(path).apply(normalOptions(defaultResId)).into(mImageView);
    }

    /**
     * 默认加载
     */
    public static void loadFromRes(Context mContext, int resId, ImageView mImageView) {
        Glide.with(mContext).load(resId).into(mImageView);
    }






    private static RequestOptions normalOptions(int defaultResId) {
        return new RequestOptions()
                .centerCrop().placeholder(defaultResId)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .error(defaultResId).priority(Priority.HIGH);
    }






    /**
     * 清除磁盘缓存，需要在子线程中执行
     */
    public static void GlideClearDiskCache(Context mContext) {
        Glide.get(mContext).clearDiskCache();
    }

    /**
     * 清除内存缓存, 清理内存缓存，可以在UI线程中进行
     */
    public static void GlideClearMemory(Context mContext) {
        Glide.get(mContext).clearMemory();
    }
}
