package com.cnlive.updownfile.util;

import android.app.Activity;
import android.content.Context;

import com.cnlive.libs.base.application.AppConfig;
import com.cnlive.libs.base.logic.callback.DataCallback;
import com.cnlive.libs.base.logic.callback.ProgressCallback;
import com.cnlive.libs.base.util.StringUtils;
import com.cnlive.updownfile.view.FileUtil;
import com.cnlive.updownfile.view.IDUtils;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.xiaoray.raysdk.RayLib;
import com.xiaoray.raysdk.entity.bean.LocalCloudPath;
import com.xiaoray.raysdk.service.TaskUploadService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.cnlive.libs.base.logic.Config.ERROR_UPLOAD;
import static com.cnlive.libs.base.logic.Config.SUCCESS;


/**
 * 上传工具类
 */
public class UploadUtil {

    private static final String TEST_OBJECT_PIC__KEY = "802/img/%s/%s/%s.%s";
    private static final String RELEASE_OBJECT_PIC__KEY = "769/img/%s/%s/%s.%s";
    private static final String OBJECT_PIC_KEY = AppConfig.isDebug() ? TEST_OBJECT_PIC__KEY : RELEASE_OBJECT_PIC__KEY;

    /**
     * 七牛文件上传
     * @param context
     * @param file
     * @param objectKey
     * @param uploadToken
     * @param callback
     * @param progressCallback
     */
    public static void qiniuDoUpload(final Context context, File file, String objectKey, String uploadToken, final DataCallback<Integer> callback, final ProgressCallback progressCallback) {
        UploadManager uploadManager = new UploadManager();
        uploadManager.put(file, objectKey, uploadToken, new UpCompletionHandler() {
            @Override
            public void complete(String key, final ResponseInfo info, JSONObject response) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (info.isOK()) {
                            callback.callback(0, "", SUCCESS);
                        } else {
                            callback.callback(info.statusCode, info.error, ERROR_UPLOAD);
                        }
                    }
                });
            }
        }, new UploadOptions(null, null, false, new UpProgressHandler() {
            @Override
            public void progress(String key, double percent) {
                progressCallback.onProgress(percent);
            }
        }, null));
    }


    /**
     * 上传多张图片
     * @param context
     * @param file
     * @param uploadToken
     * @param callback
     * @param progressCallback
     */
    public static void qiniuDoUploadPic(final Context context, final List<String> file, String uploadToken, final DataCallback<ArrayList<String>> callback, final ProgressCallback progressCallback) {
        final ArrayList<String> netpiclist = new ArrayList<>();
        UploadManager uploadManager = new UploadManager();
        //正式环境：769/img/2018/01/29/"时间戳".jpg
        //测试环境：802/img/2018/01/29/"时间戳".jpg
        for (int i = 0; i < file.size(); i++) {
            Date date = new Date();
            String date1 = StringUtils.formatDate(date, "yyyy/MM/dd");
            String id = IDUtils.createID();
            String extName = FileUtil.getExtensionName((String) file.get(0));
            String objectKey = String.format(OBJECT_PIC_KEY, i, date1, id, extName);
            uploadManager.put(new File(file.get(i)), objectKey, uploadToken, new UpCompletionHandler() {
                @Override
                public void complete(String key, final ResponseInfo info, JSONObject response) {
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (info.isOK()) {
                                try {
                                    netpiclist.add((String) info.response.get("key"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                if (netpiclist.size() == file.size()) {
                                    callback.callback(0, "", netpiclist);
                                }
                            } else {
                                callback.callback(info.statusCode, info.error, null);
                            }
                        }
                    });
                }
            }, new UploadOptions(null, null, false, new UpProgressHandler() {
                @Override
                public void progress(String key, double percent) {
                    progressCallback.onProgress(percent);
                }
            }, null));
        }
    }

    /**
     * 小睿上传文件
     * @param context
     * @param filePath
     */
    public static void xiaoRayUploadFile(Context context,ArrayList<LocalCloudPath> filePath){
        RayLib.addNewUpload(context, filePath, TaskUploadService.FROM_SELECT_UPLOAD);
    }

}
