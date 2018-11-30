package com.nomad.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.opengl.Visibility;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.nomad.R;
import com.nomad.customview.LikeButton;
import com.nomad.customview.NMGButton;
import com.nomad.customview.NMGTextView;
import com.nomad.databinding.MyVenueListItemBinding;
import com.nomad.model.Venue;
import com.nomad.util.AppCommons;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class MyVenueListAdapter extends RecyclerView.Adapter<MyVenueListAdapter.MyVenueItemHolder> {
    private List<Venue> favoriteList = new ArrayList<>();
    private List<Venue> pastLogList = new ArrayList<>();
    private int viewType = FAVOURITE_VIEW;
    private static final int FAVOURITE_VIEW = 1;
    private static final int PAST_LOG_VIEW = 2;
    Context context;
    int lastPosition = -1;
    private boolean isPastLogView = false;
    FavouriteListListener favouriteListListener;

    public void addPastLog(List<Venue> pastLogList) {
        viewType = PAST_LOG_VIEW;
        int start_position = this.pastLogList.size();
        this.pastLogList.addAll(pastLogList);
        notifyItemRangeInserted(start_position, this.pastLogList.size());
    }

    public void addFavourite(List<Venue> favoriteList) {
        viewType = FAVOURITE_VIEW;
        int start_position = this.favoriteList.size();
        this.favoriteList.addAll(favoriteList);
        notifyItemRangeInserted(start_position, this.favoriteList.size());
    }

    public void setFavouriteListListener(FavouriteListListener favouriteListListener) {
        this.favouriteListListener = favouriteListListener;
    }

    public MyVenueListAdapter(Context context) {
        this.context = context;
    }


    public void clear() {
        this.favoriteList.clear();
        this.pastLogList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyVenueItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyVenueItemHolder(LayoutInflater.from(
                parent.getContext()).inflate(R.layout.my_venue_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyVenueItemHolder holder, int position) {

        // setAnimation(holder.itemView, position);
        if (viewType == PAST_LOG_VIEW) {
            updatePastLogView(holder, position);
        } else if (viewType == FAVOURITE_VIEW) {
            updateFavouriteView(holder, position);
        }

    }

    private void updatePastLogView(MyVenueItemHolder holder, int position) {
        Venue pastlog = pastLogList.get(position);

        ImageLoader.getInstance().displayImage(pastlog.getMainCoverPicture(), holder.binding.venueCoverIv, AppCommons.getVenueImageLoadingOptions());
        holder.binding.venueDistanceTv.setText(String.format(context.getString(R.string.distance), Double.toString(pastlog.getDistance())));
        holder.binding.addToFavBtn.setSelected(pastlog.isFavorite());
        holder.binding.venueTitleTv.setText(pastlog.getVenueName());
        holder.binding.venueLocation.setText(pastlog.getVenueAddress());
        holder.binding.venueTimingsTv.setText(pastlog.getTodayTimings());
        holder.binding.amenitiesDefaultView.setAmenities(pastlog.getAmenities());
        holder.binding.lnrLayCheckIn.setVisibility(View.GONE);

    }

    private void updateFavouriteView(MyVenueItemHolder holder, int position) {
        Venue favorite = favoriteList.get(position);
        ImageLoader.getInstance().displayImage(favorite.getMainCoverPicture(), holder.binding.venueCoverIv, AppCommons.getVenueImageLoadingOptions());
        holder.binding.venueDistanceTv.setText(String.format(context.getString(R.string.distance), Double.toString(favorite.getDistance())));
        holder.binding.addToFavBtn.setSelected(favorite.isFavorite());
        holder.binding.venueTitleTv.setText(favorite.getVenueName());
        holder.binding.venueLocation.setText(favorite.getVenueAddress());
        holder.binding.venueTimingsTv.setText(favorite.getTodayTimings());
        holder.binding.amenitiesDefaultView.setAmenities(favorite.getAmenities());
        holder.binding.lnrLayCheckIn.setVisibility(View.VISIBLE);
        holder.binding.checkInBtn.setVisibility(favorite.isCheckedIn() ? View.GONE : View.VISIBLE);
        holder.binding.checkOutBtn.setVisibility(favorite.isCheckedIn() ? View.VISIBLE : View.GONE);


    }

    private void toggleCheckInButton(NMGTextView button, boolean isCheckedIn){

    }

    @Override
    public int getItemCount() {
        int size = 0;
        if (viewType == FAVOURITE_VIEW) {
            size = favoriteList.size();
        } else {
            size = pastLogList.size();
        }
        return size;
    }


    class MyVenueItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        MyVenueListItemBinding binding;

        public MyVenueItemHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            binding.checkInBtn.setOnClickListener(this);
            binding.checkOutBtn.setOnClickListener(this);
            binding.bookMeetingSpaceBtn.setOnClickListener(this);
            binding.addToFavBtn.setLikeButtonListener(new LikeButton.LikeButtonListener() {
                @Override
                public void onLikedButtonClicked() {
                    favouriteListListener.onFavouriteClicked(getAdapterPosition(), viewType);
                }
            });

        }


        public void clearAnimation() {
            binding.cardView.clearAnimation();
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.check_in_btn:
                    favouriteListListener.onCheckIn(getAdapterPosition(), viewType);
                    break;

                case R.id.check_out_btn:
                    favouriteListListener.onCheckOut(getAdapterPosition(), viewType);
                    break;

                case R.id.book_meeting_space_btn:
                    favouriteListListener.onBookMeetingSpace(getAdapterPosition(), viewType);
                    break;
                case R.id.add_to_fav_btn:
                    favouriteListListener.onFavouriteClicked(getAdapterPosition(), viewType);
                    break;
            }
        }
    }

    /*public List<Amenity> getDummyAmenities() {
        List<Amenity> dummyAmenities = new ArrayList<>();
        Amenity amenity1 = new Amenity();
        amenity1.setIconURL("http://newmediaguru.co/nomad/4x/icon1.png");
        Amenity amenity2 = new Amenity();
        amenity2.setIconURL("http://newmediaguru.co/nomad/4x/icon2.png");
        Amenity amenity3 = new Amenity();
        amenity3.setIconURL("http://newmediaguru.co/nomad/4x/icon3.png");
        Amenity amenity4 = new Amenity();
        amenity4.setIconURL("http://newmediaguru.co/nomad/4x/icon4.png");
        Amenity amenity5 = new Amenity();
        amenity5.setIconURL("http://newmediaguru.co/nomad/4x/icon5.png");
        Amenity amenity6 = new Amenity();
        amenity6.setIconURL("http://newmediaguru.co/nomad/4x/icon6.png");
        Amenity amenity7 = new Amenity();
        amenity7.setIconURL("http://newmediaguru.co/nomad/4x/icon7.png");
        Amenity amenity8 = new Amenity();
        amenity8.setIconURL("http://newmediaguru.co/nomad/4x/icon8.png");
        dummyAmenities.add(amenity1);
        dummyAmenities.add(amenity2);
        dummyAmenities.add(amenity3);
        dummyAmenities.add(amenity4);
        dummyAmenities.add(amenity5);
        dummyAmenities.add(amenity6);
        dummyAmenities.add(amenity7);
        dummyAmenities.add(amenity8);
        return dummyAmenities;
    }*/

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.item_animation_slide_from_right);
            viewToAnimate.setAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull MyVenueItemHolder holder) {
        holder.clearAnimation();
    }

    public interface FavouriteListListener {
        void onCheckIn(int position, int viewType);
        void onCheckOut(int position, int viewType);

        void onBookMeetingSpace(int position, int viewType);

        void onFavouriteClicked(int position, int viewType);
    }
}

