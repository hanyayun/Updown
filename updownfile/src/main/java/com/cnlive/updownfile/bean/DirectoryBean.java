package com.cnlive.updownfile.bean;

import java.util.List;

/**
 * @author zww
 * @date on 2018/11/12
 * @function
 * @filename DirectoryBean
 */
public class DirectoryBean  {
    private List<FileBean> data;

    public List<FileBean> getData() {
        return data;
    }

    public void setData(List<FileBean> data) {
        this.data = data;
    }
}