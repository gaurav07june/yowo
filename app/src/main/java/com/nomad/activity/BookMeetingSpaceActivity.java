package com.nomad.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.nomad.R;
import com.nomad.customview.MeetingSpacesView;
import com.nomad.databinding.ActivityBookMeetingSpaceBinding;
import com.nomad.model.CheckAvailabilityResModel;
import com.nomad.model.GenericResponseModel;
import com.nomad.model.MeetingSpace;
import com.nomad.model.MeetingSpacesResModel;
import com.nomad.model.OperationTime;
import com.nomad.model.OperationTimes;
import com.nomad.model.SpanData;
import com.nomad.model.SpecialDates;
import com.nomad.model.Venuedetail;
import com.nomad.retrofit.WebAPIHelper;
import com.nomad.util.AppCommons;
import com.nomad.util.Constants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.nomad.util.AppCommons.formatTimeValue;
import static com.nomad.util.AppCommons.generatePersonCountList;

public class BookMeetingSpaceActivity extends NetworkActivity implements View.OnClickListener, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private ActivityBookMeetingSpaceBinding binding;
    private TimePickerDialog timePickerDialog;
    private TimePickerType selectedTimePickerType = TimePickerType.NONE;
    private DatePickerDialog datePickerDialog;
    private static final int BOOK_MEETING_SPACE_REQ_CODE = 1;
    private List<MeetingSpace> meetingSpaceList = new ArrayList<>();
    private List<OperationTimes> operationTimesList = new ArrayList<>();
    private List<SpecialDates> specialDatesList = new ArrayList<>();

    private static final int CHECK_AVAILABILITY_REQ_CODE = 2;
    private SpecialDates specialDates = null;

    private String selectedDate = null;
    OperationTimes operationTimes = null;
    private String selectedDateParam = "";
    private String startTimeParam = "";
    private String endTimeParam = "";
    private Map<String, String> availabilityQueryMap = new HashMap<>();


    Venuedetail venuedetail;

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = getString(R.string.date_format, formatTimeValue(dayOfMonth), formatTimeValue(month + 1), formatTimeValue(year));
        selectedDate =getString(R.string.special_date_format, formatTimeValue(year),  formatTimeValue(month + 1), formatTimeValue(dayOfMonth));
        selectedDateParam = getString(R.string.date_format, formatTimeValue(month+1),  formatTimeValue(dayOfMonth), formatTimeValue(year));
        SpecialDates specialDates = isSpecialDate();
        if (specialDates == null){
            Calendar c = Calendar.getInstance();
            c.set(year + 1900, month, dayOfMonth - 1);


            int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

            switch (dayOfWeek){
                case Calendar.SUNDAY:
                    operationTimes = operationTimesList.get(0);

                    break;
                case Calendar.MONDAY:
                    operationTimes = operationTimesList.get(1);
                    break;
                case Calendar.TUESDAY:
                    operationTimes = operationTimesList.get(2);
                    break;
                case Calendar.WEDNESDAY:
                    operationTimes = operationTimesList.get(3);
                    break;
                case Calendar.THURSDAY:
                    operationTimes = operationTimesList.get(4);
                    break;
                case Calendar.FRIDAY:
                    operationTimes = operationTimesList.get(5);
                    break;
                case Calendar.SATURDAY:
                    operationTimes = operationTimesList.get(6);
                    break;
            }

            if (operationTimes != null ){
                if (operationTimes.isOpen()){
                    binding.lnrLayStartTime.setVisibility(View.VISIBLE);
                    binding.lnrLayEndTime.setVisibility(View.VISIBLE);
                    binding.txtStartTime.setText("Start Time ("+operationTimes.getOpeningTime()+")");
                    binding.txtEndTime.setText("End Time ("+operationTimes.getClosingTime()+")");

                    binding.startTimePicker.setText(getFormatedTime(operationTimes.getOpeningTime()));
                    binding.endTimePicker.setText(getFormatedTime(operationTimes.getClosingTime()));

                    binding.specialDateMessageTv.setText("Open");
                }else{
                    binding.lnrLayStartTime.setVisibility(View.GONE);
                    binding.lnrLayEndTime.setVisibility(View.GONE);
                    binding.specialDateMessageTv.setText("Closed");
                }

            }
        }
        else if(!specialDates.isClosed()){
            binding.lnrLayStartTime.setVisibility(View.VISIBLE);
            binding.lnrLayEndTime.setVisibility(View.VISIBLE);
            binding.txtStartTime.setText("Start Time ("+specialDates.getOpeningTime()+")");
            binding.txtEndTime.setText("Start Time ("+specialDates.getClosingTime()+")");
            binding.startTimePicker.setText(getFormatedTime(specialDates.getOpeningTime()));
            binding.endTimePicker.setText(getFormatedTime(specialDates.getClosingTime()));

            binding.specialDateMessageTv.setText("Special date : Open");

        }else{
            binding.lnrLayStartTime.setVisibility(View.GONE);
            binding.lnrLayEndTime.setVisibility(View.GONE);
            binding.specialDateMessageTv.setText("Special date : Closed");
        }


        startTimeParam =  getParamTimeFormate(binding.startTimePicker.getText().toString().trim());
        endTimeParam = getParamTimeFormate(binding.endTimePicker.getText().toString().trim());
        binding.datePickerTv.setText(date);
    }

    private String getFormatedTime(String time){
        String fomatedTime = "";
        if (time != null){
            String meridian = time.split(" ")[1];
            String timeValue = time.split(" ")[0];
            String hour = timeValue.split(":")[0];
            String minute = timeValue.split(":")[1];

            if (meridian.equalsIgnoreCase("PM")){
                    hour = String.valueOf(Integer.parseInt(hour)+ 12);
            }
            fomatedTime = hour+":"+minute;

        }
        return fomatedTime;
    }

    private SpecialDates isSpecialDate(){
        if (specialDatesList.size() > 0){
            for (SpecialDates specialDates : specialDatesList){
                String specialDateString = specialDates.getDate().split("T")[0];
                Log.e("Nomad", specialDateString+" : "+selectedDate);
                if (selectedDate.equals(specialDateString)){
                    return specialDates;
                }
            }
        }
        return null;
    }

    private enum TimePickerType {START, END, NONE}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_book_meeting_space);
    }

    @Override
    public void getExtras() {
        venuedetail =(Venuedetail) getIntent().getSerializableExtra(Constants.EXTRA.VENUE_DETAIL_EXTRA);
        if (venuedetail != null){
            operationTimesList = venuedetail.getOperationTimes();
            specialDatesList = venuedetail.getSpecialDates();
        }

    }

    @Override
    public void initViews() {
        timePickerDialog = new TimePickerDialog(this, this, 0, 0, true);
        datePickerDialog = new DatePickerDialog(this, this, 2018, 12, 24);

    }

    @Override
    public void setViews() {
        checkInternetAndHitApi(BOOK_MEETING_SPACE_REQ_CODE);
        binding.lnrLayDateTimeSelect.setVisibility(View.VISIBLE);

        binding.personCountSpinner.setEditable(false);
        binding.meetingSpaceView.setMeetingSpaceCheckedListener(new MeetingSpacesView.MeetingSpaceCheckListener() {
            @Override
            public void onMeetingSpaceChecked(int position, boolean isChecked) {
                if (isChecked){
                    availabilityQueryMap.put(Constants.PARAMS.MEETING_ROOM_ID, String.valueOf(meetingSpaceList.get(position).getId()));
                }

                updatePersonsDropDown(position, isChecked);
            }
        });

        binding.personCountSpinner.setText("0");
        binding.startTimePicker.setOnClickListener(this);
        binding.endTimePicker.setOnClickListener(this);
        binding.datePickerTv.setOnClickListener(this);
        binding.backBtn.setOnClickListener(this);
        binding.continueBtn.setOnClickListener(this);
        binding.checkAgainBtn.setOnClickListener(this);
        binding.btnCheckAvailability.setOnClickListener(this);

        binding.meetingSpaceAvailNoteLay.setVisibility(View.GONE);


    }
    private void updateView(){
        binding.meetingSpaceView.setItemType(MeetingSpacesView.ITEM_WITH_CHECKBOX);
        binding.meetingSpaceView.addMeetingSpacesList(meetingSpaceList);
        if (meetingSpaceList.size() > 0){
            availabilityQueryMap.put(Constants.PARAMS.MEETING_ROOM_ID, String.valueOf(meetingSpaceList.get(0).getId()));
            binding.personCountSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                    generatePersonCountList(meetingSpaceList.get(0).getCapacity())));
        }

    }
    private void updatePersonsDropDown(int position, boolean isChecked){
        binding.personCountSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                generatePersonCountList(isChecked ? meetingSpaceList.get(position).getCapacity() : 0)));
        binding.lnrLayDateTimeSelect.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        //binding.personCountSpinner.setText("0");
    }


    @Override
    public <D> void onApiSuccess(GenericResponseModel<D> model, int request_code) {
        switch (request_code){
            case BOOK_MEETING_SPACE_REQ_CODE:
                if (model.getData() != null){
                    GenericResponseModel<MeetingSpacesResModel> resModel = (GenericResponseModel<MeetingSpacesResModel>) model;
                    meetingSpaceList = resModel.getData().getMeetingRooms();
                    updateView();

                }
                break;
            case CHECK_AVAILABILITY_REQ_CODE:
                if (model.getData() != null){
                    GenericResponseModel<CheckAvailabilityResModel> resModel =
                            (GenericResponseModel<CheckAvailabilityResModel>) model;
                    binding.meetingSpaceAvailNoteLay.setVisibility(View.VISIBLE);
                    if (resModel.getData().isAvailable()){
                        binding.meetingSpaceAvailNoteTv.setText(AppCommons.getSpannedString(
                                new SpanData(getString(R.string.yes_meeting_space_is), ContextCompat.getColor(this,R.color.colorBlack),1f,false),
                                new SpanData(getString(R.string.available), ContextCompat.getColor(this,R.color.colorPrimary),1f,true)
                        ));
                        binding.continueBtn.setVisibility(View.VISIBLE);

                    }else{
                        binding.meetingSpaceAvailNoteTv.setText(AppCommons.getSpannedString(
                                new SpanData(getString(R.string.no_meeting_space_is), ContextCompat.getColor(this,R.color.colorBlack),1f,false),
                                new SpanData(getString(R.string.not_available), ContextCompat.getColor(this,R.color.colorPrimary),1f,true)
                        ));
                        binding.continueBtn.setVisibility(View.GONE);
                    }
                }
                break;
        }
    }

    @Override
    public <D> void onApiFail(GenericResponseModel<D> model, int request_code) {

    }

    @Override
    public void onApiException(Throwable t, int request_code) {
        Toast.makeText(this, t.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hitApi(int request) {
        switch (request){
            case BOOK_MEETING_SPACE_REQ_CODE:
                WebAPIHelper.getInstance().getMeetingSpaces(new APICallback<MeetingSpacesResModel>(BOOK_MEETING_SPACE_REQ_CODE), venuedetail.getVenueId());
                break;
            case CHECK_AVAILABILITY_REQ_CODE:
                WebAPIHelper.getInstance().checkMeetingRoomAvailability(new APICallback<CheckAvailabilityResModel>(CHECK_AVAILABILITY_REQ_CODE),
                        availabilityQueryMap);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        int[] hourMinutes;
        switch (v.getId()) {

            case R.id.start_time_picker:
                if (isSelectedDateFilled()){
                    hourMinutes = getHoursMinutes(binding.startTimePicker.getText().toString());
                    timePickerDialog.updateTime(hourMinutes[0], hourMinutes[1]);
                    selectedTimePickerType = TimePickerType.START;
                    timePickerDialog.show();
                }

                break;
            case R.id.end_time_picker:
                if (isSelectedDateFilled()){
                    hourMinutes = getHoursMinutes(binding.endTimePicker.getText().toString());
                    timePickerDialog.updateTime(hourMinutes[0], hourMinutes[1]);
                    selectedTimePickerType = TimePickerType.END;
                    timePickerDialog.show();

                }
                break;

            case R.id.date_picker_tv:
                datePickerDialog.show();
                break;
            case R.id.continue_btn:
                startActivity(new Intent(this,ConfirmBookingActivity.class));
                break;
            case R.id.back_btn:
                onBackPressed();
                break;

            case R.id.btnCheckAvailability:
                if (isFormValid()){
                    availabilityQueryMap.put(Constants.PARAMS.BOOKING_DATE, selectedDateParam);
                    availabilityQueryMap.put(Constants.PARAMS.BOOKING_START_TIME, startTimeParam);
                    availabilityQueryMap.put(Constants.PARAMS.BOOKING_END_TIME, endTimeParam);
                    checkInternetAndHitApi(CHECK_AVAILABILITY_REQ_CODE);
                }

                break;
        }
    }

    private boolean isFormValid(){
        if (binding.datePickerTv.getText().toString().trim().isEmpty()){
            Toast.makeText(this, getResources().getString(R.string.date_empty_error), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.startTimePicker.getText().toString().trim().isEmpty()){
            Toast.makeText(this, getResources().getString(R.string.start_time_empty_error), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.endTimePicker.getText().toString().trim().isEmpty()){
            Toast.makeText(this, getResources().getString(R.string.end_time_empty_error), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean isSelectedDateFilled(){
        if (binding.datePickerTv.getText().toString().trim().isEmpty()){
            Toast.makeText(this, getString(R.string.date_empty_error), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        switch (selectedTimePickerType) {
            case START:
                if (isStartTimeValid(hourOfDay, minute)){
                    binding.startTimePicker.setText(getString(R.string.hours_minutes_format, formatTimeValue(hourOfDay), formatTimeValue(minute)));
                    startTimeParam = getParamTimeFormate(binding.startTimePicker.getText().toString().trim());
                }

                else
                    Toast.makeText(getApplicationContext(), R.string.start_time_not_valid_error, Toast.LENGTH_SHORT).show();
                break;
            case END:
                if (isEndTimeValid(hourOfDay, minute)){
                    binding.endTimePicker.setText(getString(R.string.hours_minutes_format, formatTimeValue(hourOfDay), formatTimeValue(minute)));
                    endTimeParam = getParamTimeFormate(binding.endTimePicker.getText().toString().trim());
                }

                else
                    Toast.makeText(getApplicationContext(), R.string.end_time_not_valid_error, Toast.LENGTH_SHORT).show();
                break;
        }

    }

    private String getParamTimeFormate(String timeString){
        int hour = Integer.parseInt(timeString.split(":")[0]);
        int minute = Integer.parseInt(timeString.split(":")[1]);
        String ampm = "";

        if (hour < 12){
            ampm = "AM";
        }else if (hour > 12){
            ampm = "PM";
            hour = (hour - 12);
        }else{
            ampm = "PM";
        }
        return (hour < 10 ? "0"+hour : String.valueOf(hour))+":"+(minute < 10 ? "0"+minute : String.valueOf(minute))+" "+ampm;
    }

    public boolean isStartTimeValid(int startHours, int startMinutes) {

        try {
            String openingTime;
            int openTimeValue = 0, startTimeValue, endTimeValue;
            int openingHour, openingMinute;
            int endHours = Integer.valueOf(binding.endTimePicker.getText().toString().split(":")[0]);
            int endMinutes = Integer.valueOf(binding.endTimePicker.getText().toString().split(":")[1]);
            startTimeValue = startHours * 60 + startMinutes;
            endTimeValue = endHours * 60 + endMinutes;
            if (operationTimes != null){
                openingTime = getFormatedTime(operationTimes.getOpeningTime());
                openingHour = Integer.valueOf(openingTime.split(":")[0]);
                openingMinute = Integer.valueOf(openingTime.split(":")[1]);
                openTimeValue = openingHour * 60 + openingMinute;
            }
            

            return ((startTimeValue < endTimeValue) && (startTimeValue >= openTimeValue));

        } catch (Exception e) {

        }
        return false;
    }

    public boolean isEndTimeValid(int endHours, int endMinutes) {

        try {
            String closingTime;
            int closingHour, closingMinute;
            int closingTimeValue = 0, startTimeValue, endTimeValue;
            int startHours = Integer.valueOf(binding.startTimePicker.getText().toString().split(":")[0]);
            int startMinutes = Integer.valueOf(binding.startTimePicker.getText().toString().split(":")[1]);
            startTimeValue = startHours * 60 + startMinutes;
            endTimeValue = endHours * 60 + endMinutes;
            if (operationTimes != null){
                closingTime = getFormatedTime(operationTimes.getClosingTime());
                closingHour = Integer.valueOf(closingTime.split(":")[0]);
                closingMinute = Integer.valueOf(closingTime.split(":")[1]);
                closingTimeValue = closingHour * 60 + closingMinute;
                
            }
            return ((endTimeValue > startTimeValue) && (endTimeValue <= closingTimeValue));

        } catch (Exception e) {

        }
        return false;
    }

    private int[] getHoursMinutes(String time) {
        int[] hoursMinutes = new int[2];
        try {
            hoursMinutes[0] = Integer.valueOf(time.split(":")[0]);
            hoursMinutes[1] = Integer.valueOf(time.split(":")[1]);
        } catch (Exception e) {

        }
        return hoursMinutes;
    }
}
