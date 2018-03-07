package com.tlioylc.myricheditor;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;


import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.tlioylc.myricheditor.CommuntiyMessageEditActivity.HIDDEN;
import static com.tlioylc.myricheditor.CommuntiyMessageEditActivity.IMAGE;
import static com.tlioylc.myricheditor.CommuntiyMessageEditActivity.TXT;
import static com.tlioylc.myricheditor.CommuntiyMessageEditActivity.VIDEO;

/**
 * Created by Administrator on 2017/11/6.
 */

public class RichAdapter extends Adapter {
    private ArrayList<RichEditBean> list;
    private CustomImageView1.PicListRemove picListRemove;
    private CommuntiyMessageEditActivity mainActivity;
    private int requestFocusText = -1;
    private FocusViewListener focusViewListener;
    private final int TITLE = 1;
    private final int CONTENT = 2;

    public interface FocusViewListener{
        void hasFocus(EditText textView, int positon);
    }

    public RichAdapter(ArrayList<RichEditBean> list , CommuntiyMessageEditActivity picListRemove){
        this.list = list;
        this.picListRemove = picListRemove;
        this.mainActivity = picListRemove;
        this.focusViewListener = picListRemove;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TITLE){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.community_title_word_view,parent, false);
            return new TitleWordHolder(view);
        }else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.community_rich_word_holder,parent, false);
            return new RichWordHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof TitleWordHolder){
            ((TitleWordHolder)holder).updateView(list.get(position));
        }else {
            ((RichWordHolder)holder).updateView(list.get(position),position == list.size()-1,position);
        }
    }

    public void setRequestFocusText(int requestFocusText){
        this.requestFocusText = requestFocusText;
    }
    @Override
    public int getItemCount() {
        if(list == null)
            return 0;
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return TITLE;
        }else {
            return CONTENT;
        }

    }

    private class TitleWordHolder extends RecyclerView.ViewHolder {
        private EditText textView;
        private TextView textCount;
        private int position;
        private View contentView;
        private RichEditBean richEditBeanHolder;
        private String nameContent;

        public TitleWordHolder(View itemView) {
            super(itemView);
            textView = (EditText) itemView.findViewById(R.id.text);
            textCount = (TextView) itemView.findViewById(R.id.text_count);

            contentView =  itemView.findViewById(R.id.content_item);
            textView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus){
                        mainActivity.setInputVisibity(GONE);
                    }
                }
            });
            contentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //消耗掉点击事件，避免传递到底部
                }
            });
        }

        public void updateView(RichEditBean richEditBean ){
            this.richEditBeanHolder = richEditBean;
//            textView.requestFocus();
        }

    }
    private class RichWordHolder extends RecyclerView.ViewHolder{
        private CustomImageView1 imageView;
        private CustomVideoURLView customVideoURLView;
        private EditText  textView;
        private View contentView;
        private RichEditBean richEditBeanHolder;
        private int position;
        public RichWordHolder(View itemView) {
            super(itemView);
            imageView = (CustomImageView1) itemView.findViewById(R.id.image);
            customVideoURLView = (CustomVideoURLView) itemView.findViewById(R.id.video);
            textView = (EditText) itemView.findViewById(R.id.text);
            contentView =  itemView.findViewById(R.id.content_item);
            textView.addTextChangedListener(textChangeListener);
            contentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //消耗掉点击事件，避免传递到底部
                }
            });
        }

        private TextWatcher textChangeListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String content = textView.getText().toString();
                if(richEditBeanHolder!=null){
                    richEditBeanHolder.setTxt(content);
                }
            }
        };
        public void updateView(RichEditBean richEditBean ,final boolean isLast,final int position){
            this.richEditBeanHolder = richEditBean;
            this.position = position;
            switch (richEditBean.getInputType()){
                case TXT:
                    imageView.setVisibility(GONE);
                    customVideoURLView.setVisibility(GONE);
                    textView.setVisibility(View.VISIBLE);

//                    String html = Html.fromHtml(richEditBean.getTxt()).toString();

                    String html = richEditBean.getTxt();
//                    String html = "[daxiao]大花洒就离开丰厚的萨克奶粉[daxiao]大煞风景奥卡福[yun]";

                    textView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (hasFocus) {
                                mainActivity.setInputVisibity(VISIBLE);
                                focusViewListener.hasFocus(textView,position);
                                //通知main界面当前光标所在view；
                                mainActivity.setRequestLine(position,textView);
                                //删除某一行处理
                                textView.setOnKeyListener(new View.OnKeyListener() {
                                    @Override
                                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                                        if(keyCode == KeyEvent.KEYCODE_DEL){
                                            handleKeyDown();
                                        }
                                        return false;
                                    }
                                });

                            }
                            else {
                                textView.setOnKeyListener(null);
                            }
                        }
                    });

                    textView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            if (event.getAction() == MotionEvent.ACTION_UP) {
                                mainActivity.hideIconView();
                            }
                            return false;
                        }
                    });
                    textView.setText(html);
                    textView.setSelection(html.length());


                    if(isLast && position == 1 && TextUtils.isEmpty(html)){
                        textView.setHint("输入内容");
                    }else {
                        textView.setHint("");
                    }
                    if((requestFocusText != -1 && position == requestFocusText)   || (isLast )){
//                        && position != 1
                        if(position != 1){
                            textView.requestFocus();
                        }
                        focusViewListener.hasFocus(textView,position);
                    }
                    break;
                case IMAGE:
                    imageView.setVisibility(View.VISIBLE);
                    textView.setVisibility(GONE);
                    customVideoURLView.setVisibility(GONE);
                    imageView.setImagePath(richEditBean.getLocalImageUrl());
                    if(TextUtils.isEmpty(richEditBean.getTxt())){
                        imageView.initUpLoad();
                    }else if(richEditBean.getTxt().equals("error")){
                        imageView.uploadFail();
                    }else {
                        imageView.uploadSuccess();
                    }
                    imageView.setViewIndex(position);
                    imageView.setPicListRemove(picListRemove);
                    break;
                case VIDEO:
                    imageView.setVisibility(GONE);
                    textView.setVisibility(GONE);
                    customVideoURLView.setVisibility(VISIBLE);

                    customVideoURLView.updateView(richEditBean.getTxt(),position,picListRemove);
                    break;
                default:
                    imageView.setVisibility(GONE);
                    textView.setVisibility(GONE);
                    customVideoURLView.setVisibility(GONE);
                    break;
            }
        }


        private void handleKeyDown() {
            if(TextUtils.isEmpty(textView.getText().toString())) {
                int nextTxtIndex = -1;
                int preTxtIndex = -1;
                for (int i = position - 1; i >= 0; i--) {
                    if (list.get(i).getInputType() != HIDDEN) {
                        preTxtIndex = i;
                        break;
                    }
                }
                for (int j = position + 1; j < list.size(); j++) {
                    if (list.get(j).getInputType() != HIDDEN) {
                        nextTxtIndex = j;
                        break;
                    }
                }

                if (preTxtIndex == -1 && nextTxtIndex != -1) {
                    richEditBeanHolder.setInputType(HIDDEN);
                    richEditBeanHolder.setTxt("");
                    RichAdapter.this.notifyItemChanged(position);
                    requestFocusText = -1;
                }
            }
        }
    }
}
