package com.nomad.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nomad.R;
import com.nomad.databinding.SavedCardExpandedLayoutBinding;
import com.nomad.databinding.SavedCardNomalLayoutBinding;

import com.nomad.model.Card;

import java.util.List;

public class SavedCardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int DEFAULT_VIEW = 1;
    private final int EXPANDED_VIEW = 2;
    private int selectedPosition = 0;
    List<Card> cardList;
    CardListener listener;

    public void addCardList(List<Card> cardList){
        int start_position = this.cardList.size();
        this.cardList.addAll(cardList);
        notifyItemRangeInserted(start_position,this.cardList.size());
    }
    public void clear()
    {
        this.cardList.clear();
        notifyDataSetChanged();
    }

    public void setCardListener(CardListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType){
            case DEFAULT_VIEW:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_card_nomal_layout,parent,false);

                return new SmallItemHolder(view);
            case EXPANDED_VIEW:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_card_expanded_layout,parent,false);
                return new LargeItemHolder(view);
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_card_nomal_layout,parent,false);
                return new SmallItemHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()){
            case DEFAULT_VIEW:
                SavedCardAdapter.SmallItemHolder smallItemHolder = (SavedCardAdapter.SmallItemHolder)holder;
                setDefautView(smallItemHolder, position);
                break;
            case EXPANDED_VIEW:
                SavedCardAdapter.LargeItemHolder largeItemHolder = (SavedCardAdapter.LargeItemHolder)holder;
                setLargeView(largeItemHolder, position);
                break;
            default:
                break;
        }
    }

    public void setDefautView(SavedCardAdapter.SmallItemHolder holder, int selectedPosition){
        //holder.savedCardNomalLayoutBinding.cardViewBookingDetailButton.setVisibility(View.GONE);

    }
    public void setLargeView(SavedCardAdapter.LargeItemHolder holder , int selectedPosition){
        //holder.myBookingsItemLargeBinding.cardViewBookingDetailButton.setVisibility(View.GONE);

    }
    class LargeItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        SavedCardExpandedLayoutBinding savedCardExpandedLayoutBinding;
        public LargeItemHolder(View itemView) {
            super(itemView);
            savedCardExpandedLayoutBinding = DataBindingUtil.bind(itemView);
            itemView.setOnClickListener(this);
            savedCardExpandedLayoutBinding.imgDelete.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.imgDelete) {
                listener.onCardDelete(getAdapterPosition());
            }
        }
    }

    class SmallItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        SavedCardNomalLayoutBinding savedCardNomalLayoutBinding;

        public SmallItemHolder(View itemView) {
            super(itemView);
            savedCardNomalLayoutBinding = DataBindingUtil.bind(itemView);
            itemView.setOnClickListener(this);
            savedCardNomalLayoutBinding.imgDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.imgDelete) {
                listener.onCardDelete(getAdapterPosition());
            }else{
                notifyItemChanged(getAdapterPosition());
                selectedPosition = getAdapterPosition();
                notifyItemChanged(selectedPosition);
            }
        }
    }
    @Override
    public int getItemCount() {
        return 4;
    }
    @Override
    public int getItemViewType(int position) {
        return position == selectedPosition ? EXPANDED_VIEW : DEFAULT_VIEW;
    }
    public interface CardListener{
        void onCardDelete(int position);
        void onCardSelect(int position);
    }
}
