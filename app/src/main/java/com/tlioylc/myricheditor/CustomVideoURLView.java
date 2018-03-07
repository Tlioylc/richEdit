package com.tlioylc.myricheditor;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/12/28.
 */
public class CustomVideoURLView extends RelativeLayout{

    private View deleteView;
    private int picPosition;
    private TextView urlText;
    private TextView urlIcon;

    private CustomImageView1.PicListRemove picListRemove;


    public CustomVideoURLView(Context context) {
        super(context);
        init();
        initView();
    }

    public CustomVideoURLView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        initView();
    }

    public CustomVideoURLView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        initView();
    }

    private void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.custom_video_url_view, this);
    }

    public void updateView(String url,int picPosition, CustomImageView1.PicListRemove picListRemove){
        urlText.setText(url);
        this.picListRemove = picListRemove;
        this.picPosition = picPosition;
    }
    private void initView(){
        deleteView = findViewById(R.id.custom_url_delete_view);
        urlIcon = (TextView) findViewById(R.id.icon_lianjie) ;
        urlText = (TextView) findViewById(R.id.text_url) ;
        urlIcon.setTypeface(App.obtain().getIconFace());
        deleteView.setOnClickListener(clickListener);
    }

    private OnClickListener clickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.custom_url_delete_view){
                picListRemove.removeItem(picPosition);
            }
        }
    };


    public void setViewIndex(int picPosition){
        this.picPosition = picPosition;
    }

    public void setPicListRemove(CustomImageView1.PicListRemove picListRemove) {
        this.picListRemove = picListRemove;
    }


}
