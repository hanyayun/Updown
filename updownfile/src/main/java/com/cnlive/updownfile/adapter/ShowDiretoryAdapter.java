package com.cnlive.updownfile.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cnlive.updownfile.R;
import com.cnlive.updownfile.bean.FileBean;
import com.xiaoray.raysdk.constant.FileType;

import java.util.List;

/**
 * @author zww
 * @date on 2018/11/12
 * @function
 * @filename ShowDiretoryAdapter
 */
public class ShowDiretoryAdapter extends BaseQuickAdapter<FileBean, BaseViewHolder> {
    public ShowDiretoryAdapter(int layoutResId, @Nullable List<FileBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FileBean item) {
        if (item.getFile_type_Int() == FileType.TYPE_DIRECTORY)
            Glide.with(mContext).load(R.drawable.icon_folder).into((ImageView) helper.getView(R.id.iv_item_file_icon));
        else
            Glide.with(mContext).load(R.drawable.bg_txt).into((ImageView) helper.getView(R.id.iv_item_file_icon));
        helper.setText(R.id.tv_item_file_name, item.getFname());
    }
}
