package com.cnlive.updownfile.bean;

import java.util.List;

/**
 * @author zww
 * @date on 2018/11/9
 * @function
 * @filename FileRootBean
 */
public class FileRootBean {

    private FileList data;

    public FileList getData() {
        return data;
    }

    public void setData(FileList data) {
        this.data = data;
    }

    public static class FileList {
        private List<FileBean> items;
        private String dupsize;


        public List<FileBean> getItems() {
            return items;
        }

        public void setItems(List<FileBean> items) {
            this.items = items;
        }

        public String getDupsize() {
            return dupsize;
        }

        public void setDupsize(String dupsize) {
            this.dupsize = dupsize;
        }
    }
}