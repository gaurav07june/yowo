package com.nomad.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.nomad.R;
import com.nomad.databinding.MyBookingsItemDefaultBinding;
import com.nomad.databinding.MyBookingsItemLargeBinding;
import com.nomad.databinding.VenueListItemBinding;
import com.nomad.model.Amenity;
import com.nomad.model.BookingsGeneralModel;
import com.nomad.model.Venue;
import com.nomad.util.AppCommons;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class UpcomingBookingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    final int DEFAUT_VIEW_TYPE = 101;
    final int LARGE_VIEW_TYPE = 102;

    int selectedPosition = 0;
    Context context;

    UpcomingBookingListener upcomingBookingListener;
    public UpcomingBookingAdapter(Context context){
        this.context = context;
    }

    private List<BookingsGeneralModel> upcomingBookingList = new ArrayList<>();

    public void addBookingList(List<BookingsGeneralModel> upcomingBookingList)
    {
        int start_position = this.upcomingBookingList.size();
        this.upcomingBookingList.addAll(upcomingBookingList);
        notifyItemRangeInserted(start_position,this.upcomingBookingList.size());
    }

    public void setUpcomingBookingListener(UpcomingBookingListener upcomingBookingListener){
        this.upcomingBookingListener = upcomingBookingListener;
    }

    public void clear()
    {
        this.upcomingBookingList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType){
            case DEFAUT_VIEW_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_bookings_item_default,parent,false);
                return new SmallItemHolder(view);

            case LARGE_VIEW_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_bookings_item_large,parent,false);
                return new LargeItemHolder(view);

            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_bookings_item_default,parent,false);
                return new SmallItemHolder(view);

        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
       switch (holder.getItemViewType()){
           case DEFAUT_VIEW_TYPE:
               SmallItemHolder smallItemHolder = (SmallItemHolder)holder;
               setDefautView(smallItemHolder, position);
               break;
           case LARGE_VIEW_TYPE:
               LargeItemHolder largeItemHolder = (LargeItemHolder)holder;
               setLargeView(largeItemHolder, position);
               break;
           default:

               break;
       }
    }
    public void setDefautView(SmallItemHolder holder, int selectedPosition){
        BookingsGeneralModel model = upcomingBookingList.get(selectedPosition);
        holder.myBookingsItemDefaultBinding.cardViewBookingDetailButton.setVisibility(View.GONE);

        holder.myBookingsItemDefaultBinding.venueName.setText(model.getVenueName());
        holder.myBookingsItemDefaultBinding.venueLocation.setText(model.getVenueAddress());
        holder.myBookingsItemDefaultBinding.venueTimings.setText(AppCommons.getBookingDateTimeFormate(model.getBookingDate()));
        holder.myBookingsItemDefaultBinding.txtDistance.setText(String.format(context.getString(R.string.distance),
                String.valueOf(model.getDistance())));
        ImageLoader.getInstance().displayImage(model.getLogoImage(), holder.myBookingsItemDefaultBinding.imgVenueLogo);


    }
    public void setLargeView(LargeItemHolder holder , int selectedPosition){
        BookingsGeneralModel model = upcomingBookingList.get(selectedPosition);
        holder.myBookingsItemLargeBinding.cardViewBookingDetailButton.setVisibility(View.GONE);
        ImageLoader.getInstance().displayImage(model.getLogoImage(), holder.myBookingsItemLargeBinding.imgVenueLogo);
        holder.myBookingsItemLargeBinding.txtVenueName.setText(model.getVenueName());
        holder.myBookingsItemLargeBinding.txtDistance.setText(String.format(context.getResources().getString(R.string.distance),
                String.valueOf(model.getDistance())));
        holder.myBookingsItemLargeBinding.venueLocation.setText(model.getVenueAddress());
        if (model.getMeetingSpace() != null){
            holder.myBookingsItemLargeBinding.txtMeetingSpaceDetail.setText(model.getMeetingSpace().getTitle());
            if (model.getMeetingSpace().getServices() != null){
                List<Amenity> serviceList = model.getMeetingSpace().getServices();

                if (serviceList.size() > 0){
                    if (!serviceList.get(0).getIconURL().equals("") && serviceList.get(0).getIconURL() != null){
                        ImageLoader.getInstance().displayImage(serviceList.get(0).getIconURL(), holder.myBookingsItemLargeBinding.imgAmenityOne);
                    }

                }
                if (serviceList.size() > 1){
                    if (!serviceList.get(1).getIconURL().equals("") && serviceList.get(1).getIconURL() != null) {
                        ImageLoader.getInstance().displayImage(serviceList.get(1).getIconURL(), holder.myBookingsItemLargeBinding.imgAmenityTwo);
                    }
                }
            }
        }

        holder.myBookingsItemLargeBinding.txtVenueTime.setText(AppCommons.getBookingDateTimeFormate(model.getBookingDate()));
        holder.myBookingsItemLargeBinding.txtBillAmount.setText(String.format(context.getString(R.string.bill_amount),
                String.valueOf(model.getTotalCharge())));


    }

    @Override
    public int getItemCount() {
        return upcomingBookingList.size();
    }


    class LargeItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        MyBookingsItemLargeBinding myBookingsItemLargeBinding;

        public LargeItemHolder(View itemView) {
            super(itemView);
            myBookingsItemLargeBinding = DataBindingUtil.bind(itemView);
            itemView.setOnClickListener(this);
            myBookingsItemLargeBinding.imgSubmenuButton.setOnClickListener(this);
            myBookingsItemLargeBinding.btnCheckIn.setOnClickListener(this);
            myBookingsItemLargeBinding.btnCheckOut.setOnClickListener(this);
            myBookingsItemLargeBinding.mapBtn.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.imgSubmenuButton){
                /*PopupMenu popupMenu = new PopupMenu(context, myBookingsItemLargeBinding.imgSubmenuButton);
                popupMenu.getMenuInflater().inflate(R.menu.book_detail_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.bookingDetail){
                            Toast.makeText(context, "booking detail clicked", Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                });
                popupMenu.show();*/
                myBookingsItemLargeBinding.cardViewBookingDetailButton.setVisibility(View.VISIBLE);
                myBookingsItemLargeBinding.cardViewBookingDetailButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        upcomingBookingListener.onSubmenuClick(getAdapterPosition());
                        myBookingsItemLargeBinding.cardViewBookingDetailButton.setVisibility(View.GONE);
                    }
                });
                //upcomingBookingListener.onSubmenuClick(getAdapterPosition());
            }else if (v.getId() == R.id.btnCheckIn){
                myBookingsItemLargeBinding.cardViewBookingDetailButton.setVisibility(View.GONE);
                upcomingBookingListener.onCheckIn(getAdapterPosition());
            }else if (v.getId() == R.id.btnCheckOut){
                myBookingsItemLargeBinding.cardViewBookingDetailButton.setVisibility(View.GONE);
                upcomingBookingListener.onCheckOut(getAdapterPosition());
            }else if (v.getId() == R.id.map_btn){
                myBookingsItemLargeBinding.cardViewBookingDetailButton.setVisibility(View.GONE);
                upcomingBookingListener.onMapButtonClicked(getAdapterPosition());
            }else {
                myBookingsItemLargeBinding.cardViewBookingDetailButton.setVisibility(View.GONE);
                notifyItemChanged(selectedPosition);
                selectedPosition = getAdapterPosition();
                notifyItemChanged(selectedPosition);
                upcomingBookingListener.onItemClick(getAdapterPosition());
            }
        }
    }

    class SmallItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        MyBookingsItemDefaultBinding myBookingsItemDefaultBinding;

        public SmallItemHolder(View itemView) {
            super(itemView);
            myBookingsItemDefaultBinding = DataBindingUtil.bind(itemView);
            itemView.setOnClickListener(this);
            myBookingsItemDefaultBinding.imgSubmenuButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.imgSubmenuButton){
                /*PopupMenu popupMenu = new PopupMenu(context, myBookingsItemDefaultBinding.imgSubmenuButton);
                popupMenu.getMenuInflater().inflate(R.menu.book_detail_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.bookingDetail){
                            Toast.makeText(context, "booking detail clicked", Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                });
                popupMenu.show();*/
                myBookingsItemDefaultBinding.cardViewBookingDetailButton.setVisibility(View.VISIBLE);
                myBookingsItemDefaultBinding.cardViewBookingDetailButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myBookingsItemDefaultBinding.cardViewBookingDetailButton.setVisibility(View.GONE);
                        upcomingBookingListener.onSubmenuClick(getAdapterPosition());
                    }
                });

            }else{
                myBookingsItemDefaultBinding.cardViewBookingDetailButton.setVisibility(View.GONE);
                notifyItemChanged(selectedPosition);
                selectedPosition = getAdapterPosition();
                notifyItemChanged(selectedPosition);
                upcomingBookingListener.onItemClick(getAdapterPosition());
            }
        }
    }


    @Override
    public int getItemViewType(int position) {
        return selectedPosition == position ? LARGE_VIEW_TYPE : DEFAUT_VIEW_TYPE;
    }
    public interface UpcomingBookingListener{
        void onItemClick(int position);
        void onSubmenuClick(int position);
        void onCheckIn(int position);
        void onCheckOut(int position);
        void onMapButtonClicked(int position);
    }
}
