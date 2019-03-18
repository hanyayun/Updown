package com.cnlive.updownfile.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zww
 * @date on 2018/11/9
 * @function
 * @filename FileBean
 */
public class FileBean {

    private String utime;
    private String mtime;
    private String ctime;
    private String l2url;
    private String l1url;
    private String url;
    private String fname;
    private String size;
    private String file_type;
    private String path;
    private String inode;
    private List<String> tags;
    private String img_position;
    private int pos_id;
    private int width;
    private int height;
    private int user_id;
    private long duration;
    private long file_id;
    private boolean chooseFrom;//用于发布的一个属性

    public String getUtime() {
        return utime;
    }

    public void setUtime(String utime) {
        this.utime = utime;
    }

    public String getL2url() {
        return l2url;
    }

    public void setL2url(String l2url) {
        this.l2url = l2url;
    }

    public int getPos_id() {
        return pos_id;
    }

    public void setPos_id(int pos_id) {
        this.pos_id = pos_id;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getFile_id() {
        return file_id;
    }

    public void setFile_id(long file_id) {
        this.file_id = file_id;
    }

    public long getDir_id() {
        return dir_id;
    }

    public void setDir_id(long dir_id) {
        this.dir_id = dir_id;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    private long dir_id;
    private String md5;


    public FileBean(String inode, String utime, String mtime, String l2url, String l1url,
                     String url, String fname, String size,
                     String file_type, String path) {
        this.utime = utime;
        this.mtime = mtime;
        this.l2url = l2url;
        this.l1url = l1url;
        this.url = url;
        this.fname = fname;
        this.size = size;
        this.file_type = file_type;
        this.path = path;
        this.inode = inode;
    }

    public List<String> getTags() {
        if (tags == null)
            tags = new ArrayList<>();
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getImg_position() {
        return img_position;
    }

    public void setImg_position(String img_position) {
        this.img_position = img_position;
    }

    public String getUpload_time() {
        if (utime == null)
            return mtime;
        return utime;
    }

    public void setUpload_time(String upload_time) {
        this.utime = upload_time;
    }

    public String getPreviewL2Path() {
        return l2url;
    }

    public void setPreviewL2Path(String previewL2Path) {
        l2url = previewL2Path;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }


    public String getFile_type() {
        return file_type;
    }

    public int getFile_type_Int() {
        return Integer.parseInt(file_type);
    }

    public void setFile_type(String file_type) {
        this.file_type = file_type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMtime() {
        return mtime;
    }

    public void setMtime(String mtime) {
        this.mtime = mtime;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getL1url() {
        return l1url;
    }

    public void setL1url(String l1url) {
        this.l1url = l1url;
    }

    public String getInode() {
        return inode;
    }

    public void setInode(String inode) {
        this.inode = inode;
    }

    public void update(FileBean fileTable) {
        if (fileTable.getInode() != null)
            setInode(fileTable.getInode());
        if (fileTable.getUrl() != null)
            setUrl(fileTable.getUrl());
        if (fileTable.getFname() != null)
            setFname(fileTable.getFname());
        if (fileTable.getFile_type() != null)
            setFile_type(fileTable.getFile_type());
        if (fileTable.getTags() != null)
            setTags(fileTable.getTags());
        if (fileTable.getPath() != null)
            setPath(fileTable.getPath());
        if (fileTable.getCtime() != null)
            setCtime(fileTable.getCtime());
        setHeight(fileTable.getHeight());
        setWidth(fileTable.getWidth());
        if (fileTable.getImg_position() != null)
            setImg_position(fileTable.getImg_position());
        if (fileTable.getSize() != null)
            setSize(fileTable.getSize());
        if (fileTable.getL1url() != null)
            setL1url(fileTable.getL1url());
        if (fileTable.getL2url() != null) {
            setL2url(fileTable.getL2url());
            setPreviewL2Path(fileTable.getL2url());
        }

    }

    public boolean isChooseFrom() {
        return chooseFrom;
    }

    public void setChooseFrom(boolean chooseFrom) {
        this.chooseFrom = chooseFrom;
    }
}
