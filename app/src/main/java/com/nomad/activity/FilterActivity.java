package com.nomad.activity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.nomad.R;
import com.nomad.databinding.ActivityFilterBinding;
import com.nomad.model.VenueFilterData;
import com.nomad.util.AppGlobalData;
import com.nomad.util.Constants;


import java.util.Calendar;


public class FilterActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private StringBuilder availableDays = new StringBuilder();
    private ActivityFilterBinding binding;
    private static final int DAY_FILTER = 1;
    private static final int HOUR_FILTER = 2;
    private static final int DAY_HOUR_TOGGLE_ANIM_DUR = 500;
    private int selectedDayHourFilter = DAY_FILTER;
    private String timeType;
    VenueFilterData venueFilterData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transparentStatusBar();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_filter);
        binding.toolbarLay.titleTv.setText(R.string.filters);
        binding.dayFilterBtn.setOnClickListener(this);
        binding.hourFilterBtn.setOnClickListener(this);
        binding.toolbarLay.backBtn.setOnClickListener(this);
        binding.lnrLayStartTime.setOnClickListener(this);
        binding.lnrLayEndTime.setOnClickListener(this);
        binding.applyBtn.setOnClickListener(this);
        binding.resetBtn.setOnClickListener(this);
        binding.day0.setOnCheckedChangeListener(this);
        binding.day1.setOnCheckedChangeListener(this);
        binding.day2.setOnCheckedChangeListener(this);
        binding.day3.setOnCheckedChangeListener(this);
        binding.day4.setOnCheckedChangeListener(this);
        binding.day5.setOnCheckedChangeListener(this);
        binding.day6.setOnCheckedChangeListener(this);
        binding.checkBoxFavourite.setOnCheckedChangeListener(this);
        binding.meetingSpaceAvailCb.setOnCheckedChangeListener(this);
        binding.applyBtn.setOnClickListener(this);
        binding.resetBtn.setOnClickListener(this);
        toggleDayHourFilter(DAY_FILTER);
        setViews();
    }

    private void setViews(){
        venueFilterData = AppGlobalData.getInstance().getLoggedInCustomer().getFilterData();

        binding.checkBoxFavourite.setChecked(venueFilterData.isFavourite());
        binding.day0.setChecked(venueFilterData.isMondaySelected());
        binding.day1.setChecked(venueFilterData.isTuesdaySelected());
        binding.day2.setChecked(venueFilterData.isWednesdaySelected());
        binding.day3.setChecked(venueFilterData.isThursdaySelected());
        binding.day4.setChecked(venueFilterData.isFridaySelected());
        binding.day5.setChecked(venueFilterData.isSaturdaySelected());
        binding.day6.setChecked(venueFilterData.isSundaySelected());
        binding.meetingSpaceAvailCb.setChecked(venueFilterData.isMeetingSpaceAvailable());
        binding.txtStartTime.setText(venueFilterData.getStartTime());
        binding.txtEndTime.setText(venueFilterData.getEndTime());


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.day_filter_btn:
                toggleDayHourFilter(DAY_FILTER);
                break;
            case R.id.hour_filter_btn:
                toggleDayHourFilter(HOUR_FILTER);
                break;
            case R.id.back_btn:
                onBackPressed();
                break;
            case R.id.lnrLayStartTime:
                timeType = "start_time";
                openTimePicker(timeType);
                break;
            case R.id.lnrLayEndTime:
                timeType = "end_type";
                openTimePicker(timeType);
                break;
            case R.id.apply_btn:
                doApplyFilterTask();
                break;
            case R.id.reset_btn:
                doResetTask();
                break;
        }
    }

    private void doResetTask(){

        venueFilterData.invalidateFilter();
        setViews();
        AppGlobalData.getInstance().getLoggedInCustomer().setFilterSelected(false);
        AppGlobalData.getInstance().getLoggedInCustomer().setFilterData(venueFilterData);
        AppGlobalData.getInstance().saveToPrefs(this);
        Intent intent = new Intent();
        intent.putExtra(Constants.EXTRA.FILTER_DATA, venueFilterData);
        setResult(RESULT_OK, intent);
        finish();

    }

    private void doApplyFilterTask(){
        if (isFilterValid()){
            AppGlobalData.getInstance().getLoggedInCustomer().setFilterSelected(true);
            AppGlobalData.getInstance().getLoggedInCustomer().setFilterData(venueFilterData);
            AppGlobalData.getInstance().saveToPrefs(this);
            Intent intent = new Intent();
            intent.putExtra(Constants.EXTRA.FILTER_DATA, venueFilterData);
            /*intent.putExtra(Constants.EXTRA.IS_FAVOURITE, binding.checkBoxFavourite.isChecked());
            intent.putExtra(Constants.EXTRA.MEETING_SPACE_AVAILABLE, binding.meetingSpaceAvailCb.isChecked());
            intent.putExtra(Constants.EXTRA.START_TIME, binding.txtStartTime.getText().toString().trim());
            intent.putExtra(Constants.EXTRA.END_TIME, binding.txtEndTime.getText().toString().trim());*/
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    private boolean isFilterValid(){
        String startTime = binding.txtStartTime.getText().toString().trim();
        String endTime = binding.txtEndTime.getText().toString().trim();
        if (!startTime.equals("") && !startTime.equals("")){
            if (!compareTimeInterval(startTime, endTime)){
                Toast.makeText(this, getResources().getString(R.string.start_time_end_time_error), Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        venueFilterData.setStartTime(startTime);
        venueFilterData.setEndTime(endTime);
        return true;
    }
    private boolean compareTimeInterval(String startTime, String endTime){

        int startHour = Integer.parseInt(startTime.split(":")[0]);
        int startMinute = Integer.parseInt(startTime.split(":")[1]);

        int endHour = Integer.parseInt(endTime.split(":")[0]);
        int endMinute = Integer.parseInt(endTime.split(":")[1]);

        if(startHour > endHour){
            return false;
        }
        if (startHour == endHour){
            if (startMinute >= endMinute){
                return false;
            }
        }
        return true;
    }
    private void openTimePicker(final String timeType){
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(FilterActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                if (timeType.equals("start_time")){
                    binding.txtStartTime.setText((selectedHour < 10 ? "0"+selectedHour : selectedHour) + ":" + (selectedMinute < 10 ? "0"+selectedMinute : selectedMinute));
                }else{
                    binding.txtEndTime.setText((selectedHour < 10 ? "0"+selectedHour : selectedHour) + ":" + (selectedMinute < 10 ? "0"+selectedMinute : selectedMinute));
                }

            }
        }, hour, minute, true);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    private void toggleDayHourFilter(int filter) {
        switch (filter) {
            case DAY_FILTER:
                ((TransitionDrawable) binding.dayFilterBtn.getBackground()).startTransition(DAY_HOUR_TOGGLE_ANIM_DUR);
                ((TransitionDrawable) binding.hourFilterBtn.getBackground()).resetTransition();
                binding.dayFilterBtn.setSelected(true);
                binding.hourFilterBtn.setSelected(false);
                binding.dayFilter.setVisibility(View.VISIBLE);
                binding.hoursFilter.setVisibility(View.GONE);
                break;
            case HOUR_FILTER:
                ((TransitionDrawable) binding.dayFilterBtn.getBackground()).resetTransition();
                ((TransitionDrawable) binding.hourFilterBtn.getBackground()).startTransition(DAY_HOUR_TOGGLE_ANIM_DUR);
                binding.dayFilterBtn.setSelected(false);
                binding.hourFilterBtn.setSelected(true);
                binding.dayFilter.setVisibility(View.GONE);
                binding.hoursFilter.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.day_0:
                venueFilterData.setMondaySelected(isChecked);
                //updateAvailableDays(2, isChecked);
                break;
            case R.id.day_1:
                venueFilterData.setTuesdaySelected(isChecked);
                //updateAvailableDays(3, isChecked);
                break;
            case R.id.day_2:
                venueFilterData.setWednesdaySelected(isChecked);
                //updateAvailableDays(4, isChecked);
                break;
            case R.id.day_3:
                venueFilterData.setThursdaySelected(isChecked);
                //updateAvailableDays(5, isChecked);
                break;
            case R.id.day_4:
                venueFilterData.setFridaySelected(isChecked);
                //updateAvailableDays(6, isChecked);
                break;
            case R.id.day_5:
                venueFilterData.setSaturdaySelected(isChecked);
                //updateAvailableDays(7, isChecked);
                break;
            case R.id.day_6:
                venueFilterData.setSundaySelected(isChecked);
                //updateAvailableDays(1, isChecked);
                break;
            case R.id.checkBoxFavourite:
                venueFilterData.setFavourite(isChecked);
                break;
            case R.id.meeting_space_avail_cb:
                venueFilterData.setMeetingSpaceAvailable(isChecked);
                break;

        }
    }

    private void updateAvailableDays(int dayId, boolean isChecked) {
    }
}
