package com.cnlive.updownfile.util;

import android.content.Context;

import com.xiaoray.raysdk.RayLib;
import com.xiaoray.raysdk.entity.bean.LocalCloudPath;
import com.xiaoray.raysdk.service.TaskDownloadService;

import java.util.ArrayList;

/**
 * 下载工具类
 */
public class DownloadUtil {

    /**
     * 小睿下载文件到本地
     */
    public static void addDownload(Context context,ArrayList<LocalCloudPath> arrayLists) {
        RayLib.controlDownload(context, TaskDownloadService.FROM_SELECT_DOWNLOAD, arrayLists);
    }

}
