package com.nomad.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.tompee.circularviewpager.CircularViewPager;

public class NMGCircularViewPager extends CircularViewPager {
    public NMGCircularViewPager(Context context) {
        super(context);
    }

    public NMGCircularViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            return super.onTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
