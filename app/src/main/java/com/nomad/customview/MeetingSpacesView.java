package com.nomad.customview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.CompoundButton;

import com.nomad.R;
import com.nomad.adapter.VenueListAdapter;
import com.nomad.databinding.MeetingSpaceItemBinding;
import com.nomad.model.MeetingSpace;
import com.nomad.model.SpanData;
import com.nomad.util.AppCommons;

import java.util.ArrayList;
import java.util.List;

public class MeetingSpacesView extends RecyclerView {

    private List<MeetingSpace> meetingSpaces = new ArrayList<>();
    public static final int ITEM_WITH_OUT_CHECKBOX = 100;
    public static final int ITEM_WITH_CHECKBOX = 101;
    private int itemType = ITEM_WITH_OUT_CHECKBOX;
    private MeetingSpaceCheckListener meetingSpaceCheckedListener;
    MeetingSpacesAdapter adapter;

    public MeetingSpacesView(@NonNull Context context) {
        super(context);
        init();
    }

    public MeetingSpacesView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MeetingSpacesView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {

        setNestedScrollingEnabled(false);
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        adapter = new MeetingSpacesAdapter();
        setAdapter(adapter);


    }
    public void addMeetingSpacesList(List<MeetingSpace> meetingSpaceList){
        adapter.addMeetingSpaceList(meetingSpaceList);
    }

    private class MeetingSpacesAdapter extends RecyclerView.Adapter<MeetingSpacesAdapter.MeetingSpaceItemHolder> {
        private boolean onBind;

        @NonNull
        @Override
        public MeetingSpaceItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MeetingSpaceItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.meeting_space_item, parent, false));
        }

        public void addMeetingSpaceList(List<MeetingSpace> meetingSpaceList){
            meetingSpaces.clear();
            meetingSpaces.addAll(meetingSpaceList);
            if (meetingSpaces.size() > 0){
                meetingSpaces.get(0).setSelected(true);
                meetingSpaces.get(0).setChecked(true);
            }
            notifyDataSetChanged();
        }
        @Override
        public void onBindViewHolder(@NonNull MeetingSpaceItemHolder holder, int position) {
            onBind = true;
            MeetingSpace meetingSpace = meetingSpaces.get(position);
            holder.binding.collapseToggleBtn.setSelected(meetingSpace.isSelected());
            holder.binding.meetingSpaceDetailLay.getLayoutParams().height = meetingSpace.isSelected() ? holder.meetingSpaceHeight : 0;
            holder.binding.meetingCheckBox.setChecked(meetingSpaces.get(position).isChecked());
            holder.binding.meetingSpaceChargeTv.setText(AppCommons.getSpannedString(
                    new SpanData("$"+meetingSpace.getHourlyCharge(), ContextCompat.getColor(getContext(),R.color.colorBlack),1.2f,true),
                    new SpanData(getContext().getString(R.string.per_hr), ContextCompat.getColor(getContext(),R.color.colorBlack),1f,false)
            ));
            holder.binding.nameTv.setText(meetingSpace.getTitle());
            holder.binding.typeTv.setText(meetingSpace.getType());

            holder.binding.capacityTv.setText(String.valueOf(meetingSpace.getCapacity()));


            holder.binding.serviceListView.setAmenities(meetingSpace.getServices());

            onBind = false;
        }


        @Override
        public int getItemCount() {
            return meetingSpaces.size();

        }

        class MeetingSpaceItemHolder extends RecyclerView.ViewHolder {
            private MeetingSpaceItemBinding binding;

            public MeetingSpaceItemHolder(@NonNull View itemView) {
                super(itemView);
                binding = DataBindingUtil.bind(itemView);
                setUpMeetingSpaceDetailAnimator();
                binding.meetingCheckBox.setVisibility(itemType == ITEM_WITH_CHECKBOX ? VISIBLE : GONE);
                binding.nameTv.setTypeface(itemType == ITEM_WITH_CHECKBOX ? "nomad_font_medium.ttf" : "nomad_font_bold.ttf");
                binding.nameTv.setAllCaps(itemType != ITEM_WITH_CHECKBOX);

                binding.collapseToggleBtn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MeetingSpace meetingSpace = meetingSpaces.get(getAdapterPosition());
                        meetingSpace.setSelected(!meetingSpace.isSelected());

                        if (meetingSpaceInteractionListener != null) {
                            if (meetingSpace.isSelected())
                                meetingSpaceInteractionListener.onMeetingSpaceFocus(binding.meetingSpaceCard.getTop());
                        }


                        binding.collapseToggleBtn.setSelected(meetingSpace.isSelected());

                        if (meetingSpace.isSelected())
                            binding.meetingSpaceDetailLay.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    meetingSpaceDetailAnimator.reverse();
                                }
                            }, 100);
                        else
                            meetingSpaceDetailAnimator.start();

                    }
                });
                binding.meetingCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (!onBind) {
                            for (MeetingSpace m : meetingSpaces)
                                m.setChecked(false);
                            meetingSpaces.get(getAdapterPosition()).setChecked(isChecked);
                            meetingSpaceCheckedListener.onMeetingSpaceChecked(getAdapterPosition(), isChecked);
                            notifyDataSetChanged();


                        }
                    }
                });
            }

            ValueAnimator meetingSpaceDetailAnimator;
            int meetingSpaceHeight = 0;

            private void setUpMeetingSpaceDetailAnimator() {
                meetingSpaceHeight = getResources().getDimensionPixelSize(R.dimen._170sdp);
                meetingSpaceDetailAnimator = ValueAnimator.ofInt(meetingSpaceHeight, 0);
                meetingSpaceDetailAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        int val = (Integer) valueAnimator.getAnimatedValue();
                        ViewGroup.LayoutParams layoutParams = binding.meetingSpaceDetailLay.getLayoutParams();
                        layoutParams.height = val;
                        binding.meetingSpaceDetailLay.setLayoutParams(layoutParams);
                    }
                });
                meetingSpaceDetailAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                meetingSpaceDetailAnimator.setDuration(300);
            }
        }
    }

    private MeetingSpaceInteractionListener meetingSpaceInteractionListener;

    public MeetingSpaceInteractionListener getMeetingSpaceInteractionListener() {
        return meetingSpaceInteractionListener;
    }

    public void setMeetingSpaceCheckedListener(MeetingSpaceCheckListener listener){
        this.meetingSpaceCheckedListener = listener;
    }

    public void setMeetingSpaceInteractionListener(MeetingSpaceInteractionListener meetingSpaceInteractionListener) {
        this.meetingSpaceInteractionListener = meetingSpaceInteractionListener;
    }

    public interface MeetingSpaceInteractionListener {
        void onMeetingSpaceFocus(int scrollY);

    }

    public interface MeetingSpaceCheckListener{
        void onMeetingSpaceChecked(int position, boolean isChecked);
    }


    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    private MeetingSpace getCheckedItem() {
        for (MeetingSpace m : meetingSpaces) {
            if (m.isChecked()) return m;
        }

        return null;
    }
}
