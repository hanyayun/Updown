package com.cnlive.updownfile.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cnlive.updownfile.R;
import com.cnlive.updownfile.bean.FileBean;
import com.cnlive.updownfile.util.RayGlide;
import com.xiaoray.raysdk.utils.OkHttpUtils.CloudRequest;

import java.util.List;

/**
 * @author zww
 * @date on 2018/11/9
 * @function
 * @filename showFilesAdapter
 */
public class ShowFilesAdapter extends BaseQuickAdapter<FileBean, BaseViewHolder> {

    public ShowFilesAdapter(int layoutResId, @Nullable List<FileBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FileBean item) {
        RayGlide.loadPath(mContext, CloudRequest.getInstance().getCloudDownloadBaseUrl() + item.getPreviewL2Path()+"?token="+ CloudRequest.getInstance().getToken(), (ImageView) helper.getView(R.id.iv_img_show));
    }
}
