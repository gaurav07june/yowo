package com.nomad.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.nomad.R;
import com.nomad.customview.LikeButton;
import com.nomad.databinding.VenueListItemBinding;
import com.nomad.model.Amenity;
import com.nomad.model.Venue;
import com.nomad.util.AppCommons;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class VenueListAdapter extends RecyclerView.Adapter<VenueListAdapter.VenueItemHolder> {

    private List<Venue> venueList = new ArrayList<>();
    Context context;
    VenueListListener venueListListener;
    public VenueListAdapter(Context context, VenueListListener venueListListener){
        this.venueListListener = venueListListener;
        this.context = context;
    }

    public void addVenues(List<Venue> venueList)
    {
        int start_position = this.venueList.size();
        this.venueList.addAll(venueList);
        notifyItemRangeInserted(start_position,this.venueList.size());
    }

    public void clear()
    {
        this.venueList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VenueItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VenueItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.venue_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull VenueItemHolder holder, int position) {
        //holder.bind();
        Venue venue = venueList.get(position);
        ImageLoader.getInstance().displayImage(venue.getMainCoverPicture(), holder.binding.venueCoverIv,
                AppCommons.getVenueImageLoadingOptions());
        holder.binding.amenitiesDefaultView.setAmenities(venue.getAmenities());
        holder.binding.venueDistanceTv.setText(String.format(context.getResources().getString(R.string.distance),Double.toString(venue.getDistance())));
        holder.binding.venueTitleTv.setText(venue.getVenueName());
        holder.binding.venueLocation.setText(venue.getVenueAddress());

        holder.binding.venueTimingsTv.setText(venue.getTodayTimings() != null ? venue.getTodayTimings() :
                context.getResources().getString(R.string.closed));
        holder.binding.addToFavBtn.setSelected(venue.isFavorite());

        holder.binding.checkInBtn.setVisibility(venue.isCheckedIn() ? View.GONE : View.VISIBLE);
        holder.binding.checkOutBtn.setVisibility(venue.isCheckedIn() ? View.VISIBLE : View.GONE);



    }

    @Override
    public int getItemCount() {
        return venueList.size();
    }


    class VenueItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        VenueListItemBinding binding;
        public VenueItemHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            binding= DataBindingUtil.bind(itemView);
            binding.addToFavBtn.setLikeButtonListener(new LikeButton.LikeButtonListener() {
                @Override
                public void onLikedButtonClicked() {
                    venueListListener.onFavSelected(getAdapterPosition());
                }
            });
            binding.checkInBtn.setOnClickListener(this);
            binding.checkOutBtn.setOnClickListener(this);
            binding.bookMeetingSpaceBtn.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.check_in_btn){
                venueListListener.onCheckIn(getAdapterPosition());
            }else if(v.getId() == R.id.check_out_btn){
                venueListListener.onCheckOut(getAdapterPosition());
            } else if (v.getId() == R.id.book_meeting_space_btn){
                venueListListener.onBookMeetingSpace(getAdapterPosition());
            }else{
                venueListListener.onItemSelected(getAdapterPosition());
            }
        }
    }

    public interface VenueListListener{
        void onFavSelected(int position);
        void onCheckIn(int position);
        void onBookMeetingSpace(int position);
        void onItemSelected(int position);
        void onCheckOut(int position);
    }

    public static final List<Amenity> getDummyAmenities() {

        List<Amenity>  dummyAmenities=new ArrayList<>();
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
    }

}
