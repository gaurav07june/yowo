package com.nomad.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;

public class AnimationUtils {


    public static final void enterReveal(View view,boolean fromCenter,AnimatorListenerAdapter animationEndListener) {
        int cx = view.getMeasuredWidth() / 2;
        int cy = view.getMeasuredHeight() / 2;
        if (!fromCenter) {
            cx = 0;
            cy = 0;
        }
        float finalRadius = Math.max(view.getWidth(), view.getHeight()) * 1.2f;
        Animator anim =
                ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);
        if (animationEndListener!=null)
        anim.addListener(animationEndListener);
        anim.setDuration(Constants.REVEAL_ANIM_DURATION);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.start();
    }

    public static final void exitReveal(View view,boolean fromCenter,AnimatorListenerAdapter animationEndListener) {

        int cx = view.getMeasuredWidth() / 2;
        int cy = view.getMeasuredHeight() / 2;
        if (!fromCenter) {
            cx = 0;
            cy = 0;
        }
        float finalRadius = Math.max(view.getWidth(), view.getHeight()) * 1.2f;
        Animator anim =
                ViewAnimationUtils.createCircularReveal(view, cx, cy, finalRadius, 0);
        if (animationEndListener!=null)
            anim.addListener(animationEndListener);

        anim.setDuration(Constants.REVEAL_ANIM_DURATION);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.start();
    }
}
