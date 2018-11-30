package com.nomad.adapter;

import android.support.annotation.NonNull;
import android.support.transition.ChangeBounds;
import android.support.transition.ChangeClipBounds;
import android.support.transition.Scene;
import android.support.transition.Transition;
import android.support.transition.TransitionManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import com.nomad.R;

public class MyBookingsAdapter extends RecyclerView.Adapter<MyBookingsAdapter.MyBookingsViewHolder> {
    final int DEFAULT_TYPE = 1;
    final int DETAIL_TYPE = 2;
    int selectedPosition =0;

    @NonNull
    @Override
    public MyBookingsViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType){
            case DEFAULT_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_bookings_item_default, parent, false);
                break;
            case DETAIL_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_bookings_item_large, parent, false);
                break;


        }


        return new MyBookingsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyBookingsViewHolder holder, final int position) {

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyItemChanged(selectedPosition);
                selectedPosition = position;
                notifyItemChanged(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class MyBookingsViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        TextView venueName;
        TextView venueLocation;
        TextView venueTimings;
        View venueDetailLay;

        public MyBookingsViewHolder(View itemView) {
            super(itemView);
            this.itemView=itemView;
            venueName = itemView.findViewById(R.id.venue_name);
            venueLocation = itemView.findViewById(R.id.venue_location);
            venueTimings = itemView.findViewById(R.id.venue_timings);
            venueDetailLay = itemView.findViewById(R.id.venue_detail_lay);
        }


    }

    @Override
    public int getItemViewType(int position) {

        return (position == selectedPosition ? DETAIL_TYPE : DEFAULT_TYPE);
    }

}
