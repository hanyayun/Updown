package com.cnlive.updownfile.util;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;


import com.cnlive.libs.base.logic.Config;
import com.cnlive.libs.base.logic.callback.DataCallback;
import com.cnlive.libs.base.util.AlertUtil;
import com.cnlive.media.picker.utils.FileTypeUtil;
import com.cnlive.updownfile.view.MD5;
import com.qiniu.pili.droid.shortvideo.PLShortVideoTranscoder;
import com.qiniu.pili.droid.shortvideo.PLVideoSaveListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;


/**
 * 压缩工具类
 */
public class ZipUtil {

    private static final String TYPE_IMAGE_PATH = "/strike/image/";
    private static final String TYPE_VIDEO_PATH = "/strike/cache/";

    /**
     * 压缩文件
     * @param context
     * @param imageFiles
     * @param callback
     * @return
     */
    public static Disposable zipFiles(final Context context, final List<String> imageFiles, final DataCallback<List<String>> callback) {
        if (imageFiles == null || imageFiles.size() == 0) {
            callback.callback(0, "", imageFiles);
        } else {
            final List<String> files = new ArrayList<>();
            for (int i = 0; i < imageFiles.size(); i++) {
                if ("gif".equals(FileTypeUtil.getFileType(imageFiles.get(i)))) {
                    files.add(imageFiles.get(i));
                    if (files.size() == imageFiles.size()) {
                        callback.callback(0, "", files);
                    }
                } else {
                    Luban.with(context)
                            .load(imageFiles.get(i))
                            .ignoreBy(500)
                            .setTargetDir(getPath(TYPE_IMAGE_PATH))
                            .filter(new CompressionPredicate() {
                                @Override
                                public boolean apply(String path) {
                                    return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
                                }
                            })
                            .setCompressListener(new OnCompressListener() {
                                @Override
                                public void onStart() {
                                    // TODO 压缩开始前调用，可以在方法内启动 loading UI
                                }

                                @Override
                                public void onSuccess(File file) {
                                    // TODO 压缩成功后调用，返回压缩后的图片文件
                                    files.add(file.getPath());
                                    if (files.size() == imageFiles.size()) {
                                        callback.callback(0, "", files);
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
                                    // TODO 当压缩过程出现问题时调用
                                    AlertUtil.showDeftToast(context, "图片压缩失败，请重新上传");
                                }
                            }).launch();
                }
            }
        }
        return null;
    }

    /**
     * 压缩视频
     * @param context
     * @param path
     * @param compressed
     * @param callback
     * @return
     */
    public static Disposable zipVideo(Context context, String path, boolean compressed, DataCallback<String> callback) {
        boolean huawei = isHUAWEI();
        if (TextUtils.isEmpty(path) || compressed ) {
            callback.callback(Config.FAIL, "", path);
        } else {
            try {
                //  String outPath = getPath(TYPE_VIDEO_PATH) + MD5Utils.getFileMD5(new File(path)) + ".mp4";
                // String outPath = getPath(TYPE_VIDEO_PATH) + MD5.getFileMD5(new File(path)) + ".mp4";
                compressVideoResouce(context, path, callback);
                //    TranscoderUtil.doTranscode(context, path, outPath, callback);
            } catch (Exception e) {
                callback.callback(0, "", path);
            }
        }
        return null;
    }

    /**
     * 压缩视频
     *
     * @param mContext
     * @param filepath
     */
    public static void compressVideoResouce(Context mContext, final String filepath, final DataCallback<String> callback) {
        if (TextUtils.isEmpty(filepath)) {
            AlertUtil.showDeftToast(mContext, "请先选择转码文件！");
            return;
        }
        //PLShortVideoTranscoder初始化，三个参数，第一个context，第二个要压缩文件的路径，第三个视频压缩后输出的路径
        PLShortVideoTranscoder mShortVideoTranscoder = new PLShortVideoTranscoder(mContext, filepath, getPath(TYPE_VIDEO_PATH) + MD5.getFileMD5(new File(filepath)) + ".mp4");
        MediaMetadataRetriever retr = new MediaMetadataRetriever();
        retr.setDataSource(filepath);
        String height = retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT); // 视频高度
        String width = retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH); // 视频宽度
        String rotation = retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION); // 视频旋转方向
        int transcodingBitrateLevel = 7;//我这里选择的4000*1000压缩，这里可以自己选择合适的压缩比例
        mShortVideoTranscoder.transcode(Integer.parseInt(width), Integer.parseInt(height), getEncodingBitrateLevel(transcodingBitrateLevel), false, new PLVideoSaveListener() {
            @Override
            public void onSaveVideoSuccess(String s) {
                if (callback != null)
                    callback.callback(Config.SUCCESS, "success", s);
            }

            @Override
            public void onSaveVideoFailed(final int errorCode) {
                if (callback != null)
                    callback.callback(Config.FAIL, "", filepath);

              /*  runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        switch (errorCode) {
                            case ERROR_NO_VIDEO_TRACK:
                                ToastUtils.getInstance().showToast("该文件没有视频信息！");
                                break;
                            case ERROR_SRC_DST_SAME_FILE_PATH:
                                ToastUtils.getInstance().showToast("源文件路径和目标路径不能相同！");
                                break;
                            case ERROR_LOW_MEMORY:
                                ToastUtils.getInstance().showToast("手机内存不足，无法对该视频进行时光倒流！");
                                break;
                            default:
                                ToastUtils.getInstance().showToast("transcode failed: " + errorCode);
                        }
                    }
                });*/
            }

            @Override
            public void onSaveVideoCanceled() {
//                LogUtil.e("onSaveVideoCanceled");
            }

            @Override
            public void onProgressUpdate(float percentage) {
//                LogUtil.e("onProgressUpdate==========" + percentage);
            }
        });
    }

    private static String getPath(String cachePath) {
        String path = Environment.getExternalStorageDirectory() + cachePath;
        File file = new File(path);
        if (file.mkdirs()) {
            return path;
        }
        return path;
    }


    public static boolean isHUAWEI() {
        String manufacturer = Build.MANUFACTURER;
        if ("huawei".equalsIgnoreCase(manufacturer)) {
            return true;
        }
        return false;
    }


    /**
     * 设置压缩质量
     *
     * @param position
     * @return
     */
    private static int getEncodingBitrateLevel(int position) {
        return ENCODING_BITRATE_LEVEL_ARRAY[position];
    }

    /**
     * 选的越高文件质量越大，质量越好
     */
    public static final int[] ENCODING_BITRATE_LEVEL_ARRAY = {
            500 * 1000,
            800 * 1000,
            1000 * 1000,
            1200 * 1000,
            1600 * 1000,
            2000 * 1000,
            2500 * 1000,
            4000 * 1000,
            8000 * 1000,
    };
}
