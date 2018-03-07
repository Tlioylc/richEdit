package com.tlioylc.myricheditor;

/**
 * Created by Administrator on 2017/11/6.
 */

public class RichEditBean {
    private int inputType;// 0文本 1图片
    private String txt;
    private String localImageUrl;

    public String getH() {
        return h;
    }

    public void setH(String h) {
        this.h = h;
    }

    public String getW() {

        return w;
    }

    public void setW(String w) {
        this.w = w;
    }

    private String w;
    private String h;

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public int getInputType() {
        return inputType;
    }

    public void setInputType(int inputType) {
        this.inputType = inputType;
    }

    public String getLocalImageUrl() {
        return localImageUrl;
    }

    public void setLocalImageUrl(String localImageUrl) {
        this.localImageUrl = localImageUrl;
    }
}
