package com.nomad.customview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.nomad.R;
import com.nomad.model.OperationTime;
import com.nomad.model.OperationTimes;
import com.nomad.util.AnimationUtils;

import java.util.ArrayList;
import java.util.List;

public class OperationTimingsPopup extends FrameLayout {
    private RecyclerView operationTimingsRv;
    private ViewGroup target;
    private View anchor;
    private boolean isShowing = false;
    private List<OperationTimes> operationTimings = new ArrayList<>();

    public OperationTimingsPopup(@NonNull Context context, ViewGroup target, View anchor, List<OperationTimes> operationTimings) {
        super(context);
        this.anchor = anchor;
        this.target = target;
        setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite));
        setElevation(getResources().getDimensionPixelSize(R.dimen._5sdp));
        View view = LayoutInflater.from(context).inflate(R.layout.operation_timings_popup, this, false);
        addView(view);
        target.addView(this);
        setVisibility(INVISIBLE);
        operationTimingsRv = findViewById(R.id.operation_timings_rv);
        this.operationTimings = operationTimings;
        /*operationTimings.add(new OperationTimes());
        operationTimings.add(new OperationTimes());
        operationTimings.add(new OperationTimes());
        operationTimings.add(new OperationTimes());
        operationTimings.add(new OperationTimes());
        operationTimings.add(new OperationTimes());
        operationTimings.add(new OperationTimes());*/
        operationTimingsRv.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
        operationTimingsRv.setAdapter(new OperationTimingsAdapter());
        operationTimingsRv.setNestedScrollingEnabled(false);
    }

    public void show() {
        if(isShowing)return;
        isShowing = true;
        setVisibility(VISIBLE);
        AnimationUtils.enterReveal(this,false,null);
        int[] location = new int[2];
        anchor.getLocationOnScreen(location);
        setY(location[1] + anchor.getHeight());
        setX(location[0]);

    }

    public void hide() {
        if (!isShowing)return;
        isShowing = false;
        AnimationUtils.exitReveal(this,false,new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                setVisibility(GONE);
            }
        });


    }

    public void showHideToggle() {
        if (isShowing)
            hide();
        else
            show();
    }

    public void updatePosition() {
        if (!isShowing) return;
        int[] location = new int[2];
        anchor.getLocationOnScreen(location);
        setY(location[1] + anchor.getHeight());
        setX(location[0]);
    }

    public boolean isShowing() {
        return isShowing;
    }

    public void setShowing(boolean showing) {
        isShowing = showing;
    }

    public List<OperationTimes> getOperationTimings() {
        return operationTimings;
    }

    public void setOperationTimings(List<OperationTimes> operationTimings) {
        this.operationTimings = operationTimings;
    }

    private class OperationTimingsAdapter extends RecyclerView.Adapter<OperationTimingsAdapter.OperationTimeHolder> {

        @NonNull
        @Override
        public OperationTimeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new OperationTimeHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.operation_timings_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull OperationTimeHolder holder, int position) {
            //holder.bind();
            Typeface font = Typeface.createFromAsset(getContext().getAssets(), "font/nomad_font_bold.ttf");
            OperationTimes operationTimes = operationTimings.get(position);
            if(operationTimes.isOpen()){
                holder.timingTv.setText(String.format(getResources().getString(R.string.operation_times), operationTimes.getDayName(), operationTimes.getOpeningTime(), operationTimes.getClosingTime()));
            }
            else{
                holder.timingTv.setText(String.format(getResources().getString(R.string.operation_times), operationTimes.getDayName(),"Closed", ""));
                holder.timingTv.setTypeface(font);
            }
            if (operationTimes.isToday()){

                holder.timingTv.setTypeface(font);
            }

        }

        @Override
        public int getItemCount() {
            return operationTimings.size();
        }

        class OperationTimeHolder extends RecyclerView.ViewHolder {

            NMGTextView timingTv;

            public OperationTimeHolder(@NonNull View itemView) {
                super(itemView);
                timingTv = (NMGTextView) itemView;
            }

            private void bind() {
                itemView.setSelected(true);
            }
        }
    }

}
