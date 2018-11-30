package com.nomad.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.nomad.R;
import com.nomad.adapter.MyVenueListAdapter;
import com.nomad.model.Amenities;
import com.nomad.model.Amenity;
import com.nomad.util.AppCommons;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import static com.nomad.util.AppConstants.MAX_AMENITY_COUNT;

public class AmenitiesDefaultView extends FrameLayout {
    public AmenitiesDefaultView(@NonNull Context context) {
        super(context);
        init();
    }

    public AmenitiesDefaultView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AmenitiesDefaultView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public AmenitiesDefaultView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private RecyclerView amenitiesRv;
    private AmenitiesAdapter amenitiesAdapter;

    public void init() {
        amenitiesRv = (RecyclerView) LayoutInflater.from(getContext()).inflate(R.layout.amenities_default_view, this, false);
        addView(amenitiesRv);
        amenitiesRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        amenitiesAdapter=new AmenitiesAdapter();
        amenitiesRv.setAdapter(amenitiesAdapter);
    }

    public void setAmenities(List<Amenity> amenities)
    {
        amenitiesAdapter.setAmenities(amenities);
    }

    private class AmenitiesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private static final int AMENITY_VIEW = 100;
        private static final int MORE_COUNT_VIEW = 101;
        private int lastPosition = -1;
        private List<Amenity> amenities = new ArrayList<>();
        private int moreAmenityCount = 0;
        Context context = null;

        public void setAmenities(List<Amenity> amenities) {

            moreAmenityCount = 0;
            this.amenities.clear();
            if (amenities.size() > MAX_AMENITY_COUNT) {
                this.amenities.addAll(amenities.subList(0, MAX_AMENITY_COUNT));
                moreAmenityCount = amenities.size() - MAX_AMENITY_COUNT;
            } else {
                this.amenities.addAll(amenities);
            }

            notifyDataSetChanged();
        }


        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder holder;
            switch (viewType) {
                case AMENITY_VIEW:
                    holder = new AmenityHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.amenity_default_item_view, parent, false));
                    break;
                case MORE_COUNT_VIEW:
                    holder = new MoreCountHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.more_amenity_count_view, parent, false));
                    break;
                default:
                    holder = new AmenityHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.amenity_default_item_view, parent, false));

            }
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
          if (holder instanceof AmenityHolder){
              ((AmenityHolder) holder).bind();
              ImageLoader.getInstance().displayImage(amenities.get(position).getIconURL(),((AmenityHolder) holder).amenityIv,
                      AppCommons.getAmenitiesImageLoadingOptions());
          }


          else
          if (holder instanceof MoreCountHolder)
              ((MoreCountHolder) holder).bind();
          if (context != null){
              setAnimation(holder.itemView, position);
          }


        }
        private void setAnimation(View viewToAnimate, int position) {
            if (position > lastPosition) {
                Animation animation = AnimationUtils.loadAnimation(context, R.anim.item_animation_slide_from_right);
                viewToAnimate.setAnimation(animation);
                lastPosition = position;
            }
        }

        /*@Override
        public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
            holder.clearAnimation();
        }*/

        @Override
        public int getItemViewType(int position) {
            return position > MAX_AMENITY_COUNT-1 ? MORE_COUNT_VIEW : AMENITY_VIEW;
        }

        @Override
        public int getItemCount() {
            return moreAmenityCount > 0 ? amenities.size() + 1 : amenities.size();
        }

        class AmenityHolder extends RecyclerView.ViewHolder {
            ImageView amenityIv;

            public AmenityHolder(View itemView) {
                super(itemView);
                amenityIv = itemView.findViewById(R.id.amenity_iv);
            }

            public void bind() {
                //amenityIv.setImageDrawable(getResources().getDrawable(R.drawable.alert));
               // ImageLoader.getInstance().displayImage(,amenityIv);
            }

        }

        class MoreCountHolder extends RecyclerView.ViewHolder {

            TextView moreAmenityCountTv;

            public MoreCountHolder(View itemView) {
                super(itemView);
                moreAmenityCountTv = itemView.findViewById(R.id.more_amenity_count_tv);
            }

            public void bind() {
                moreAmenityCountTv.setText(String.format("+%d", moreAmenityCount));
            }
        }
    }

}
