package com.nomad.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.nomad.R;
import com.nomad.model.Amenity;
import com.nomad.util.Constants;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import static com.nomad.util.Constants.MAX_AMENITY_COUNT;

public class AmenitiesGridView extends FrameLayout {
    public AmenitiesGridView(@NonNull Context context) {
        super(context);
        init();
    }

    public AmenitiesGridView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AmenitiesGridView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public AmenitiesGridView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private RecyclerView amenitiesRv;
    private AmenitiesAdapter amenitiesAdapter;

    public void init() {
        amenitiesRv = (RecyclerView) LayoutInflater.from(getContext()).inflate(R.layout.amenities_default_view, this, false);
        addView(amenitiesRv);
        amenitiesRv.setLayoutManager(new GridLayoutManager(getContext(), Constants.AMENITY_GRID_COLUMNS, GridLayoutManager.VERTICAL, false));
        amenitiesAdapter=new AmenitiesAdapter();
        amenitiesRv.setAdapter(amenitiesAdapter);
    }

    public void setAmenities(List<Amenity> amenities)
    {
        amenitiesAdapter.setAmenities(amenities);
    }
    private class AmenitiesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<Amenity> amenities = new ArrayList<>();
        private int moreAmenityCount = 0;

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
            holder = new AmenityHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.amenity_grid_item_view, parent, false));

            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            ((AmenityHolder) holder).bind();
        }

        @Override
        public int getItemCount() {
          return amenities.size();
        }

        class AmenityHolder extends RecyclerView.ViewHolder {
            ImageView amenityIv;
            TextView amenityTv;

            public AmenityHolder(View itemView) {
                super(itemView);
                amenityIv = itemView.findViewById(R.id.amenity_iv);
                amenityTv = itemView.findViewById(R.id.amenity_tv);
            }

            public void bind() {
                ImageLoader.getInstance().displayImage(amenities.get(getAdapterPosition()).getIconURL(), amenityIv);
                amenityTv.setText(amenities.get(getAdapterPosition()).getName());
            }

        }

    }
}
