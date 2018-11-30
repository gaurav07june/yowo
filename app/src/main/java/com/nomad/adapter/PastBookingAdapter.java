package com.nomad.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nomad.R;
import com.nomad.databinding.MyBookingsItemDefaultBinding;
import com.nomad.databinding.MyBookingsItemLargeBinding;
import com.nomad.databinding.PastBookingsItemLargeBinding;
import com.nomad.model.BookingsGeneralModel;

import java.util.ArrayList;
import java.util.List;

public class PastBookingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    final int DEFAUT_VIEW_TYPE = 101;
    final int LARGE_VIEW_TYPE = 102;
    int selectedPosition = 0;

    PastBookingListener pastBookingListener;

    private List<BookingsGeneralModel> pastBookingList = new ArrayList<>();

    public void addBookingList(List<BookingsGeneralModel> upcomingBookingList)
    {
        int start_position = this.pastBookingList.size();
        this.pastBookingList.addAll(upcomingBookingList);
        notifyItemRangeInserted(start_position,this.pastBookingList.size());
    }

    public void setPastBookingListener(PastBookingListener pastBookingListener){
        this.pastBookingListener = pastBookingListener;
    }

    public void clear()
    {
        this.pastBookingList.clear();
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
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.past_bookings_item_large,parent,false);
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
        holder.myBookingsItemDefaultBinding.cardViewBookingDetailButton.setVisibility(View.GONE);

    }
    public void setLargeView(LargeItemHolder holder , int selectedPosition){
        holder.pastBookingsItemLargeBinding.cardViewBookingDetailButton.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return pastBookingList.size();
    }


    class LargeItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        PastBookingsItemLargeBinding pastBookingsItemLargeBinding;

        public LargeItemHolder(View itemView) {
            super(itemView);
            pastBookingsItemLargeBinding = DataBindingUtil.bind(itemView);
            itemView.setOnClickListener(this);
            pastBookingsItemLargeBinding.imgSubmenuButton.setOnClickListener(this);
            pastBookingsItemLargeBinding.mapBtn.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.imgSubmenuButton){
                pastBookingsItemLargeBinding.cardViewBookingDetailButton.setVisibility(View.VISIBLE);
                pastBookingsItemLargeBinding.cardViewBookingDetailButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pastBookingListener.onSubmenuClick(getAdapterPosition());
                        pastBookingsItemLargeBinding.cardViewBookingDetailButton.setVisibility(View.GONE);
                    }
                });
                //pastBookingListener.onSubmenuClick(getAdapterPosition());
            }else if (v.getId() == R.id.map_btn){
                pastBookingsItemLargeBinding.cardViewBookingDetailButton.setVisibility(View.GONE);
                pastBookingListener.onMapButtonClicked(getAdapterPosition());
            }else {
                pastBookingsItemLargeBinding.cardViewBookingDetailButton.setVisibility(View.GONE);
                notifyItemChanged(selectedPosition);
                selectedPosition = getAdapterPosition();
                notifyItemChanged(selectedPosition);

                pastBookingListener.onItemClick(getAdapterPosition());
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
                myBookingsItemDefaultBinding.cardViewBookingDetailButton.setVisibility(View.VISIBLE);
                myBookingsItemDefaultBinding.cardViewBookingDetailButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myBookingsItemDefaultBinding.cardViewBookingDetailButton.setVisibility(View.GONE);
                        pastBookingListener.onSubmenuClick(getAdapterPosition());

                    }
                });
                //
            }else{
                myBookingsItemDefaultBinding.cardViewBookingDetailButton.setVisibility(View.GONE);
                notifyItemChanged(selectedPosition);
                selectedPosition = getAdapterPosition();
                notifyItemChanged(selectedPosition);
                pastBookingListener.onItemClick(getAdapterPosition());
            }
        }
    }


    @Override
    public int getItemViewType(int position) {
        return selectedPosition == position ? LARGE_VIEW_TYPE : DEFAUT_VIEW_TYPE;
    }
    public interface PastBookingListener{
        void onItemClick(int position);
        void onSubmenuClick(int position);
        void onMapButtonClicked(int position);
    }
}
