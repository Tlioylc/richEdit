package com.tlioylc.myricheditor;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;

/**
 * Created by Administrator on 2016/12/28.
 */
public class CustomImageView1 extends RelativeLayout{

    private ImageView contentImage;
    private View deleteView;
    private int picPosition;
    private String imagePath;
    private View custoImageShadow;
    private View tryAgain;

    private PicListRemove picListRemove;


    private View progress;
    public CustomImageView1(Context context) {
        super(context);
        init();
        initView();
    }

    public CustomImageView1(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        initView();
    }

    public CustomImageView1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        initView();
    }

    private void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.custom_image_view, this);
    }

    private void initView(){
        contentImage = (ImageView)findViewById(R.id.custom_image_image);
        deleteView = findViewById(R.id.custom_image_delete_view);
        progress = findViewById(R.id.custom_image_progress);
        custoImageShadow = findViewById(R.id.custom_image_shadow);
        tryAgain =  findViewById(R.id.custom_image_try_again);

        deleteView.setOnClickListener(clickListener);
        contentImage.setOnClickListener(clickListener);
        tryAgain.setOnClickListener(clickListener);
    }

    private OnClickListener clickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.custom_image_delete_view){
                contentImage.setImageBitmap(null);
                picListRemove.removeItem(picPosition);
            }else if(v.getId() == R.id.custom_image_try_again){
                if(picListRemove != null){
                    picListRemove.tryUpLoadAgain(imagePath,picPosition);
                    progress.setVisibility(VISIBLE);
                    tryAgain.setVisibility(GONE);
                }
            }else {//跳转到放大页面
//                Intent intent = new Intent();
//                intent.setClass(getContext(), ComplaintsImageShowActivity.class);
//                intent.putExtra("bitmap", imagePath);
//                getContext().startActivity(intent);
            }
        }
    };

    public void setImagePath(String imagePath){
        this.imagePath = imagePath;
        Glide.with(getContext()).load(imagePath).into(contentImage);//可改为项目继承的App.java
//        contentImage.setImageURI(Uri.fromFile(new File(imagePath)));

        progress.setVisibility(VISIBLE);
        custoImageShadow.setVisibility(VISIBLE);
    }
    public void setViewIndex(int picPosition){
        this.picPosition = picPosition;
    }

    public void setPicListRemove(PicListRemove picListRemove) {
        this.picListRemove = picListRemove;
    }
    public void setImageBitmap(Bitmap bitmap){
        contentImage.setImageBitmap(bitmap);
    }

    public void initUpLoad() {
        progress.setVisibility(VISIBLE);
        custoImageShadow.setVisibility(VISIBLE);
    }
    public void uploadSuccess(){
        progress.setVisibility(GONE);
        custoImageShadow.setVisibility(GONE);
//        File file = new File(imagePath);
//        if(file.exists()) {
//            file.delete();
//        }
    }
    public void uploadFail(){
        progress.setVisibility(GONE);
        tryAgain.setVisibility(VISIBLE);
    }
    public interface PicListRemove{
        void  removeItem(int index);
        void  tryUpLoadAgain(String upLoadPath, int position);

    }

}
