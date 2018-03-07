package com.tlioylc.myricheditor;

import android.app.Application;
import android.graphics.Typeface;

import java.io.File;

/**
 * Created by Administrator on 2018/3/7.
 */

public class App extends Application {
    private static App instance;

    private Typeface iconFace;
    public Typeface getIconFace(){
        if (iconFace == null ){
            iconFace = Typeface.createFromAsset(getAssets(),"iconfont/icomoon.ttf");
        }
        return  iconFace;
    }
    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
    }

    public static App obtain(){
        return instance;
    }

    public String getTempPath(){
        String path = getExternalCacheDir() + "/tmp";
        checkDirectory(path);
        return path;
    }
    public boolean checkDirectory(String dirName) {
        File path = new File(dirName);
        if (!path.exists()) {
            if (!path.mkdirs()) {
                return false;
            }
        }
        return path.exists() && path.isDirectory();
    }
}
