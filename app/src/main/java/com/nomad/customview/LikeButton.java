package com.nomad.customview;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.nomad.R;

public class LikeButton extends FloatingActionButton implements View.OnTouchListener,View.OnClickListener, Animation.AnimationListener {
    LikeButtonListener listener = null;
    Animation scaleUp = AnimationUtils.loadAnimation(getContext(), R.anim.item_animation_scale_up);
    Animation scaleDown = AnimationUtils.loadAnimation(getContext(), R.anim.item_animation_scale_down);

    private boolean isSelected = false;
    private static int CLICK_DELAY_TIME=300;

    public LikeButton(Context context) {
        super(context);
        setListener();

    }

    public LikeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setListener();
    }

    public LikeButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setListener();
    }
    private void setListener(){
        scaleUp.setAnimationListener(this);
        this.setOnTouchListener(this);
        this.setOnClickListener(this);
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public void setSelected(boolean selected) {
        isSelected = selected;
        this.setImageDrawable(getContext().getDrawable( isSelected ? R.drawable.ic_favorites_nav : R.drawable.ic_favorites_border));
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getActionMasked();

        switch (action){
            case MotionEvent.ACTION_UP:
                this.startAnimation(scaleUp);
                break;
            case MotionEvent.ACTION_DOWN:
                this.startAnimation(scaleDown);
                break;
            case MotionEvent.ACTION_MOVE:
                this.startAnimation(scaleUp);
                break;
        }
        return false;

    }


    public void setLikeButtonListener(LikeButtonListener listener){
        this.listener = listener;
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {


    }
    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    @Override
    public void onClick(View v) {
        v.postDelayed(new Runnable() {
            @Override
            public void run() {
                setSelected(!isSelected);
                if (listener != null){
                    listener.onLikedButtonClicked();
                }
            }
        },CLICK_DELAY_TIME);


    }

    public interface LikeButtonListener{
        void onLikedButtonClicked();
    }
}
