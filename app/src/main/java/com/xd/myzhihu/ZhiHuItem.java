package com.xd.myzhihu;

/**
 * Created by XD on 10月02日.
 */
public class ZhiHuItem {
    private String title;
    private String content;
    private String comment;
    private String imgSource;

    public ZhiHuItem(){

    }

    public ZhiHuItem(String title, String content, String comment, String imgSource) {
        this.title = title;
        this.content = content;
        this.comment = comment;
        this.imgSource = imgSource;
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getImgSource() {
        return imgSource;
    }

    public void setImgSource(String imgSource) {
        this.imgSource = imgSource;
    }

    @Override
    public String toString() {
        return "ZhiHuItem{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", comment='" + comment + '\'' +
                ", imgSource='" + imgSource + '\'' +
                '}';
    }
}
