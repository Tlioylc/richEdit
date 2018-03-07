package com.tlioylc.myricheditor;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2017/12/5.
 */

public class DispatchTouchRecyclerView extends RecyclerView {
    public DispatchTouchRecyclerView(Context context) {
        super(context);
    }

    public DispatchTouchRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DispatchTouchRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {

        boolean inter = super.onInterceptTouchEvent(e);

        return inter;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        boolean inter =super.onTouchEvent(e);

        if(inter && getScrollState()== SCROLL_STATE_IDLE){
            return false;
        }
        return inter;
    }
}
