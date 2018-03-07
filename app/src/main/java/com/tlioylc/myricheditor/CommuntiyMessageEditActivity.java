package com.tlioylc.myricheditor;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import static com.tlioylc.myricheditor.PictureResizeUtils.getCompressedImgPath;

//import org.greenrobot.eventbus.EventBus;


/**
 * Created by Administrator on 2016/12/15.
 */
public class CommuntiyMessageEditActivity extends AppCompatActivity implements CustomImageView1.PicListRemove ,RichAdapter.FocusViewListener,IconAdapter.SetIcon{

    private TextView cost100Icon,cost500Icon,zixunIcon;
    private TextView cost100Txt,cost500Txt,zixunTxt;

    private static final int REQUEST_CODE_PICK_IMAGE = 1023;
    private static final int REQUEST_CODE_TAKE_PHOTO = 1024;
    public static final int TXT = 0;
    public static final int IMAGE = 1;
    public static final int VIDEO = 2;
    public static final int TITLE = 3;
    public static final int HIDDEN = 99;

    private String QI_NIU_TOKE = "";
    private static final String SHARE_PREFERENCE_NAME = "CommuntiyMessageEditActivity";
    private static final String SHARE_PREFERENCE_SOFT_INPUT_HEIGHT = "soft_input_height";
    private RecyclerView mRecyclerView;
    private RichAdapter richAdapter;
    private IconAdapter iconAdapter;
    private ArrayList<RichEditBean> list;
    private TextView btn1, btn2, btn3,btn4;
    private EditText currentText;
    private int focusIndex;
    private EditText focusView;

    private SharedPreferences sp;
    AssetManager assets = null;
    private RecyclerView iconRecyclerView;
    private List<String> mIconDatas;
    private View bottomView;
    private View shadowView;
    private EditText videoHtmlText;
    private TextView videoConfirm;
    private TextView videoText;
    private View videoInsertContentView;

    private static final File PHOTO_DIR = new File(
            Environment.getExternalStorageDirectory() + "/DCIM/Camera");

    private File mCurrentPhotoFile;// 照相机拍照得到的图片


    private int postFee = 9999;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_message_edit_activity);
        initView();

    }

    private void initView(){
        cost100Icon = (TextView) findViewById(R.id.community_message_edit_100_icon);
        cost500Icon = (TextView) findViewById(R.id.community_message_edit_500_icon);
        zixunIcon = (TextView) findViewById(R.id.community_message_edit_zixun_icon);
        cost100Txt = (TextView) findViewById(R.id.community_message_edit_100_txt);
        cost500Txt = (TextView) findViewById(R.id.community_message_edit_500_txt);
        zixunTxt = (TextView) findViewById(R.id.community_message_edit_zixun_txt);
        videoHtmlText = (EditText) findViewById(R.id.community_message_video_html_text);
        videoConfirm = (TextView) findViewById(R.id.community_message_video_html_btn);
        videoText = (TextView) findViewById(R.id.community_message_edit_video_text);
        bottomView  = findViewById(R.id.community_message_edit_bottom_view);
        shadowView  = findViewById(R.id.shadow_view);
        videoInsertContentView = findViewById(R.id.community_message_edit_vide_content);
        cost100Icon.setTypeface(App.obtain().getIconFace());
        cost500Icon.setTypeface(App.obtain().getIconFace());
        zixunIcon.setTypeface(App.obtain().getIconFace());

        cost100Icon.setOnClickListener(clickListener);
        cost500Icon.setOnClickListener(clickListener);
        zixunIcon.setOnClickListener(clickListener);
        cost100Txt.setOnClickListener(clickListener);
        cost500Txt.setOnClickListener(clickListener);
        zixunTxt.setOnClickListener(clickListener);
        videoConfirm.setOnClickListener(clickListener);



        cost100Icon.setSelected(true);
        mRecyclerView = (RecyclerView)findViewById(R.id.content);
        iconRecyclerView = (RecyclerView)findViewById(R.id.recycyler_view);
        btn1 = (TextView)findViewById(R.id.button1);
        btn2 = (TextView)findViewById(R.id.button2);
        btn3 = (TextView)findViewById(R.id.button3);
        btn4 = (TextView)findViewById(R.id.button4);

        btn1.setOnClickListener(clickListener);
        btn2.setOnClickListener(clickListener);
        btn3.setOnClickListener(clickListener);
        btn4.setOnClickListener(clickListener);

        btn1.setTypeface(App.obtain().getIconFace());
        btn2.setTypeface(App.obtain().getIconFace());
        btn3.setTypeface(App.obtain().getIconFace());
        btn4.setTypeface(App.obtain().getIconFace());

        sp = getSharedPreferences(SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        //创建并设置Adapter
        list = new ArrayList<>();
        RichEditBean richEditBeanTitle = new RichEditBean();
        richEditBeanTitle.setTxt("");
        richEditBeanTitle.setInputType(3);
        list.add(richEditBeanTitle);
        RichEditBean richEditBean = new RichEditBean();
        richEditBean.setTxt("");
        list.add(richEditBean);
        richAdapter = new RichAdapter(list,this);
        mRecyclerView.setAdapter(richAdapter);



        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,6);
        iconRecyclerView.setLayoutManager(gridLayoutManager);

        mIconDatas = new ArrayList<>();
        assets = getAssets();
        try {
            mIconDatas = Arrays.asList(assets.list("emoticon_small"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        iconAdapter = new IconAdapter(mIconDatas,this);
        iconRecyclerView.setAdapter(iconAdapter);
        shadowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentText != null){
                    btn1.setText(R.string.icon_shequ_fatie1);
                    currentText.requestFocus();
                    showIme(CommuntiyMessageEditActivity.this);
                    if(iconRecyclerView !=null && iconRecyclerView.getVisibility() == View.VISIBLE){
                        iconRecyclerView.removeCallbacks(hideIcon);
                        iconRecyclerView.postDelayed(hideIcon,400);
                    }
                }
            }
        });
//        showIme(CommuntiyMessageEditActivity.this);
    }
    /**
     * 隐藏/显示文字编辑功能栏
     * **/
    public void setInputVisibity(int visibity){
        bottomView.setVisibility(visibity);
    }



    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           if(v.getId() == R.id.community_message_edit_100_icon || v.getId() == R.id.community_message_edit_100_txt){
                cost100Icon.setText(R.string.icon_selected);
                cost100Icon.setSelected(true);
                cost100Icon.setTextColor(Color.parseColor("#18ffff"));
                cost500Icon.setText(R.string.icon_select);
                cost500Icon.setSelected(false);
                cost500Icon.setTextColor(Color.parseColor("#99ffffff"));
                zixunIcon.setText(R.string.icon_select);
                zixunIcon.setSelected(false);
                zixunIcon.setTextColor(Color.parseColor("#99ffffff"));
            }else if(v.getId() == R.id.community_message_edit_500_icon || v.getId() == R.id.community_message_edit_500_txt){
                cost500Icon.setText(R.string.icon_selected);
                cost500Icon.setSelected(true);
                cost500Icon.setTextColor(Color.parseColor("#18ffff"));
                cost100Icon.setText(R.string.icon_select);
                cost100Icon.setSelected(false);
                cost100Icon.setTextColor(Color.parseColor("#99ffffff"));


                zixunIcon.setText(R.string.icon_select);
                zixunIcon.setSelected(false);
                zixunIcon.setTextColor(Color.parseColor("#99ffffff"));
            }else if(v.getId() == R.id.community_message_edit_zixun_icon || v.getId() == R.id.community_message_edit_zixun_txt){
                cost500Icon.setText(R.string.icon_select);
                cost500Icon.setSelected(false);
                cost500Icon.setTextColor(Color.parseColor("#99ffffff"));
                cost100Icon.setText(R.string.icon_select);
                cost100Icon.setSelected(false);
                cost100Icon.setTextColor(Color.parseColor("#99ffffff"));

                zixunIcon.setText(R.string.icon_selected);
                zixunIcon.setSelected(true);
                zixunIcon.setTextColor(Color.parseColor("#18ffff"));
            }else  if (v.getId() == R.id.button2) {

                videoInsertContentView.setVisibility(View.GONE);
                videoText.setVisibility(View.GONE);
                // 打开系统相册
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");// 相片类型
                startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
            } else if (v.getId() == R.id.button1) {

                videoInsertContentView.setVisibility(View.GONE);
                videoText.setVisibility(View.GONE);
                if(iconRecyclerView.getVisibility() == View.GONE){
                    showIconView();
                }else {
                    if(!isSoftInputShown()){
                        showIme(CommuntiyMessageEditActivity.this);
                        hideIconView();
                    }else {
                        showIconView();
                    }
                }
            } else if (v.getId() == R.id.button3) {
                videoInsertContentView.setVisibility(View.GONE);
                videoText.setVisibility(View.GONE);
               //拍照
//                try {
//                    PHOTO_DIR.mkdirs();// 创建照片的存储目录
//                    mCurrentPhotoFile = new File(PHOTO_DIR, getPhotoFileName());// 给新照的照片文件命名
//                    final Intent intent = getTakePickIntent(mCurrentPhotoFile);
//                    startActivityForResult(intent, REQUEST_CODE_TAKE_PHOTO);
//                } catch (ActivityNotFoundException e) {
//                }
            }else if (v.getId() == R.id.button4) {
                if(iconRecyclerView.getVisibility() == View.VISIBLE){
                    iconRecyclerView.setVisibility(View.GONE);
                }
                if(videoInsertContentView.getVisibility() == View.GONE){
                    videoInsertContentView.setVisibility(View.VISIBLE);
                    videoText.setVisibility(View.VISIBLE);
                    videoHtmlText.requestFocus();
                    videoHtmlText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if(!hasFocus){
                                videoInsertContentView.setVisibility(View.GONE);
                                videoText.setVisibility(View.GONE);
                                videoHtmlText.setOnFocusChangeListener(null);
                            }
                        }
                    });

                }else {
                    videoInsertContentView.setVisibility(View.GONE);
                    videoText.setVisibility(View.GONE);
                }
            }else if (v.getId() == R.id.community_message_video_html_btn){
                String url = videoHtmlText.getText().toString();
                if(TextUtils.isEmpty(url)){
                    Toast.makeText(CommuntiyMessageEditActivity.this,"请输入视频地址",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!verifyVideoURL(url)){
                    Toast.makeText(CommuntiyMessageEditActivity.this,"视频地址错误",Toast.LENGTH_SHORT).show();
                    return;
                }
                RichEditBean richEditBean = new RichEditBean();
                richEditBean.setInputType(VIDEO);
                richEditBean.setTxt(videoHtmlText.getText().toString());
                insertMessageItem(richEditBean,null);
                videoInsertContentView.setVisibility(View.GONE);
                videoText.setVisibility(View.GONE);
            }
        }
    };

    private boolean verifyVideoURL(String s){//视频链接验证，也可有服务器处理

        try {
            java.net.URL  url = new  java.net.URL(s);
            String host = url.getHost();
            int index = -1;
            int index2 = -1;
            if(host.equals("v.qq.com")){
                index   = s.lastIndexOf("/");
                index2  = s.lastIndexOf(".html");
            }else if(host.equals("v.youku.com")){
                index  = s.lastIndexOf("/id_");
                index2  = s.lastIndexOf(".html");
            }else {
                return false;
            }

            if(index == -1 || index2 == -1)
                return false;

            String id = s.subSequence(index+1,index2).toString();

            if(!TextUtils.isEmpty(id)){
                return true;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }

    public static Intent getTakePickIntent(File f) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        return intent;
    }
    /**
     * 用当前时间给取得的图片命名
     */
    private String getPhotoFileName() {
        return "'IMG'"+  UUID.randomUUID().toString() + ".jpg";
    }


    /**
     * 隐藏输入法。
     *
     * @param context context。
     * @param view    view。
     */
    public static void hideIme(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    /**
     * 显示输入法。
     *
     * @param context context。
     */
    public static void showIme(final Context context) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

                if (imm != null) {
                    imm.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT);
                }
            }

        }, 200);
    }


    private Runnable hideIcon = new Runnable() {
        @Override
        public void run() {
            iconRecyclerView.setVisibility(View.GONE);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }
    };
    /**
     * 隐藏表情VIEW 显示键盘
     * @return
     */
    public void hideIconView() {
        btn1.setText(R.string.icon_shequ_fatie1);
        iconRecyclerView.removeCallbacks(hideIcon);
        iconRecyclerView.postDelayed(hideIcon,400);
    }
    /**
     * 显示表情VIEW 隐藏键盘
     * @return
     */
    private void showIconView() {
        btn1.setText(R.string.icon_shequ_fatie4);
        iconRecyclerView.removeCallbacks(hideIcon);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        hideIme(CommuntiyMessageEditActivity.this, btn2);

        iconRecyclerView.setVisibility(View.VISIBLE);
        int softInputHeight = getSupportSoftInputHeight();
        if (softInputHeight == 0) {
            softInputHeight = getKeyBoardHeight();
        }
        iconRecyclerView.getLayoutParams().height = softInputHeight;
    }

    public int getKeyBoardHeight(){
        return sp.getInt(SHARE_PREFERENCE_SOFT_INPUT_HEIGHT, 787);

    }

    /**
     * 是否显示软件盘
     * @return
     */
    private boolean isSoftInputShown() {
        return getSupportSoftInputHeight() != 0;
    }

    /**
     * 获取软件盘的高度
     * @return
     */
    private int getSupportSoftInputHeight() {
        Rect r = new Rect();
        /**
         * decorView是window中的最顶层view，可以从window中通过getDecorView获取到decorView。
         * 通过decorView获取到程序显示的区域，包括标题栏，但不包括状态栏。
         */
        this.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
        //获取屏幕的高度
        int screenHeight = this.getWindow().getDecorView().getRootView().getHeight();
        //计算软件盘的高度
        int softInputHeight = screenHeight - r.bottom;

        /**
         * 某些Android版本下，没有显示软键盘时减出来的高度总是144，而不是零，
         * 这是因为高度是包括了虚拟按键栏的(例如华为系列)，所以在API Level高于20时，
         * 我们需要减去底部虚拟按键栏的高度（如果有的话）
         */
        if (Build.VERSION.SDK_INT >= 20) {
            // When SDK Level >= 20 (Android L), the softInputHeight will contain the height of softButtonsBar (if has)
            softInputHeight = softInputHeight - getSoftButtonsBarHeight();
        }

        if (softInputHeight < 0) {
            Log.w("test","EmotionKeyboard--Warning: value of softInputHeight is below zero!");
        }
        //存一份到本地
        if (softInputHeight > 0) {
            sp.edit().putInt(SHARE_PREFERENCE_SOFT_INPUT_HEIGHT, softInputHeight).apply();
        }
        return softInputHeight;
    }

    /**
     * 底部虚拟按键栏的高度
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private int getSoftButtonsBarHeight() {
        DisplayMetrics metrics = new DisplayMetrics();
        //这个方法获取可能不是真实屏幕的高度
        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int usableHeight = metrics.heightPixels;
        //获取当前屏幕的真实高度
        this.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int realHeight = metrics.heightPixels;
        if (realHeight > usableHeight) {
            return realHeight - usableHeight;
        } else {
            return 0;
        }
    }


    /**
     * 根据Uri获取图片文件的绝对路径
     */
    public String getRealFilePath(final Uri uri) {
        if (null == uri) {
            return null;
        }

        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = getContentResolver().query(uri,
                    new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_PICK_IMAGE) {
            Uri uri = data.getData();
            RichEditBean richEditBean = new RichEditBean();
            richEditBean.setInputType(IMAGE);
            richEditBean.setLocalImageUrl(getRealFilePath(uri));
            richEditBean.setTxt("");

            String upLoadPath =getCompressedImgPath(richEditBean.getLocalImageUrl());

            insertMessageItem(richEditBean,upLoadPath);
        }else if (requestCode ==REQUEST_CODE_TAKE_PHOTO) {
            Uri uri =  Uri.parse(mCurrentPhotoFile.getAbsolutePath());
            RichEditBean richEditBean = new RichEditBean();
            richEditBean.setInputType(IMAGE);
            richEditBean.setLocalImageUrl(getRealFilePath(uri));
            richEditBean.setTxt("");

            String upLoadPath =getCompressedImgPath(richEditBean.getLocalImageUrl());

            insertMessageItem(richEditBean,upLoadPath);
        }
    }

    private void insertMessageItem(RichEditBean richEditBean,String upLoadPath) {
        int index = focusIndex + 1;
        int isFirstShow = 0;
        RichEditBean richEditBean1 = null;
        //判断图片上一行是不是可见的第一行，且内容为空，若为空，则删除该行，视为直接插入图片
        for(int i = focusIndex - 1;i >= 0 ; i--){
            if(list.get(i).getInputType() != HIDDEN){
                isFirstShow = 1;
                break;
            }
        }

        if(isFirstShow == 0 && list.get(focusIndex).getInputType() == TXT && TextUtils.isEmpty(list.get(focusIndex).getTxt())){
            index = focusIndex;
        }else {
            int indexText = focusView.getSelectionStart();
            String s = focusView.getText().toString();

            list.get(focusIndex).setTxt(s.substring(0,indexText));
            richEditBean1 = new RichEditBean();

            richEditBean1.setTxt(""+s.substring(indexText,s.length()));
        }

        if(!TextUtils.isEmpty(upLoadPath)){
            uploadImage("file://" + upLoadPath,index);//上传
        }

        list.add(index,richEditBean);
        richAdapter.notifyDataSetChanged();
        mRecyclerView.smoothScrollToPosition(focusIndex);
//        if (requestCode == REQUEST_CODE_PICK_IMAGE) {
//            Uri uri = data.getData();
//            RichEditBean richEditBean = new RichEditBean();
//            richEditBean.setInputType(IMAGE);
//            richEditBean.setLocalImageUrl(getRealFilePath(uri));
//            richEditBean.setTxt("");
//
//            String upLoadPath =getCompressedImgPath(richEditBean.getLocalImageUrl());
//            uploadImage("file://" + upLoadPath,index);//上传
//
//            list.add(index,richEditBean);
//            richAdapter.notifyDataSetChanged();
//            mRecyclerView.smoothScrollToPosition(focusIndex);
////            insertBitmap(getRealFilePath(uri));
//        }else if (requestCode ==REQUEST_CODE_TAKE_PHOTO) {
//            Uri uri =  Uri.parse(mCurrentPhotoFile.getAbsolutePath());
//            RichEditBean richEditBean = new RichEditBean();
//            richEditBean.setInputType(IMAGE);
//            richEditBean.setLocalImageUrl(getRealFilePath(uri));
//            richEditBean.setTxt("");
//
//            String upLoadPath =getCompressedImgPath(richEditBean.getLocalImageUrl());
//            uploadImage("file://" + upLoadPath,index);//上传
//
//            list.add(index,richEditBean);
//            richAdapter.notifyDataSetChanged();
//            mRecyclerView.smoothScrollToPosition(focusIndex);
//        }
        if(richEditBean1 != null){
            list.add(index+1,richEditBean1);
        }
    }

    public void uploadImage(String portrait,int index){
        if (TextUtils.isEmpty(QI_NIU_TOKE)){
            getQiniuToken(true,portrait,index);//获取上传所需token
        }
        else{
//            UpLoadImgMgr.obtain().upLoadFile(portrait, QI_NIU_TOKE, new UploadCallBack(index));
            //自行上传并设置回调 如下
        }
    }

    private void getQiniuToken(final boolean isUpLoad ,final String portrait ,final int index) {

    }

//    private class UploadCallBack implements UpLoadImgMgr.UpLoadCallBack{//回调方法
//        private int index;
//        UploadCallBack(int index){
//            this.index = index;
//        }
//        @Override
//        public void onSuccess(String name, String fileName ,String w,String h) {
//            list.get(index).setTxt(name);
//            list.get(index).setW(w);
//            list.get(index).setH(h);
//            richAdapter.notifyItemChanged(index);
//        }
//
//        @Override
//        public void onFail(String fileName) {
//            list.get(index).setTxt("error");
//            richAdapter.notifyItemChanged(index);
//        }
//    }


    @Override
    public void removeItem(int index) {
        list.get(index).setInputType(HIDDEN);
        int nextTxtIndex  = -1;
        int nextInputType= -1;
        int preTxtIndex = -1;
        int preInputType= -1;

        for(int i = index - 1;i >= 0 ; i--){
            if(list.get(i).getInputType() != HIDDEN){
                preTxtIndex = i;
                preInputType = list.get(i).getInputType();
                break;
            }
        }
        for(int j = index + 1;j < list.size() ; j++){
            if(list.get(j).getInputType() != HIDDEN){
                nextTxtIndex = j;
                nextInputType = list.get(j).getInputType();
                break;
            }
        }

        if(preInputType == TXT && nextInputType == TXT && preTxtIndex != -1 && nextTxtIndex != -1){
            String preTxt = list.get(preTxtIndex).getTxt();
            list.get(preTxtIndex).setTxt(preTxt+"\n"+list.get(nextTxtIndex).getTxt());

            list.get(nextTxtIndex).setInputType(HIDDEN);
            list.get(nextTxtIndex).setTxt("");
            richAdapter.notifyItemChanged(nextTxtIndex);
            richAdapter.notifyItemChanged(preTxtIndex);
            richAdapter.setRequestFocusText(preTxtIndex);
        }

        richAdapter.notifyItemChanged(index);
    }

    @Override
    public void tryUpLoadAgain(String upLoadPath,int position) {
        uploadImage("file://" + upLoadPath,position);//上传
    }

    @Override
    public void hasFocus(EditText textView, int positon) {
        if(mRecyclerView != null){
            mRecyclerView.smoothScrollToPosition(positon);
        }
        currentText = textView;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if(iconRecyclerView.getVisibility() == View.VISIBLE){
                iconRecyclerView.setVisibility(View.GONE);
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                return true;
            }
            return super.onKeyDown(keyCode, event);

        }else {
            return super.onKeyDown(keyCode, event);
        }

    }

    public void setRequestLine(int index,EditText et){
        focusIndex = index;
        focusView = et;
    }
    @Override
    public void setIcon(String imageName) {
        String s = null;


        if( TextUtils.isEmpty(list.get(focusIndex).getTxt())){
            s = list.get(focusIndex).getTxt() +imageName;
        }else {
            int indexText = focusView.getSelectionStart();
            String focusTxt = focusView.getText().toString();
            s =focusTxt.substring(0,indexText) + imageName+focusTxt.substring(indexText,focusTxt.length());
        }

        list.get(focusIndex).setTxt(s);



        SpannableString msp =new SpannableString(imageName);

        focusView.getText().insert(focusView.getSelectionStart(),msp);
    }
}
