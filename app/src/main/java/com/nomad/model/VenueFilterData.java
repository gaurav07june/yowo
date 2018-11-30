package com.nomad.model;

import java.io.Serializable;

public class VenueFilterData implements Serializable {

    private boolean isFavourite = false;
    private boolean isMeetingSpaceAvailable = false;
    private boolean isMondaySelected = false;
    private boolean isTuesdaySelected = false;
    private boolean isWednesdaySelected = false;
    private boolean isThursdaySelected = false;
    private boolean isFridaySelected = false;
    private boolean isSaturdaySelected = false;
    private boolean isSundaySelected = false;
    private String startTime = "";
    private String endTime = "";

    public void invalidateFilter(){
        setFavourite(false);
        setMeetingSpaceAvailable(false);
        setMondaySelected(false);
        setTuesdaySelected(false);
        setWednesdaySelected(false);
        setThursdaySelected(false);
        setThursdaySelected(false);
        setFridaySelected(false);
        setSaturdaySelected(false);
        setSundaySelected(false);
        setStartTime("");
        setEndTime("");
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    public boolean isMeetingSpaceAvailable() {
        return isMeetingSpaceAvailable;
    }

    public void setMeetingSpaceAvailable(boolean meetingSpaceAvailable) {
        isMeetingSpaceAvailable = meetingSpaceAvailable;
    }

    public boolean isMondaySelected() {
        return isMondaySelected;
    }

    public void setMondaySelected(boolean mondaySelected) {
        isMondaySelected = mondaySelected;
    }

    public boolean isTuesdaySelected() {
        return isTuesdaySelected;
    }

    public void setTuesdaySelected(boolean tuesdaySelected) {
        isTuesdaySelected = tuesdaySelected;
    }

    public boolean isWednesdaySelected() {
        return isWednesdaySelected;
    }

    public void setWednesdaySelected(boolean wednesdaySelected) {
        isWednesdaySelected = wednesdaySelected;
    }

    public boolean isThursdaySelected() {
        return isThursdaySelected;
    }

    public void setThursdaySelected(boolean thursdaySelected) {
        isThursdaySelected = thursdaySelected;
    }

    public boolean isFridaySelected() {
        return isFridaySelected;
    }

    public void setFridaySelected(boolean fridaySelected) {
        isFridaySelected = fridaySelected;
    }

    public boolean isSaturdaySelected() {
        return isSaturdaySelected;
    }

    public void setSaturdaySelected(boolean saturdaySelected) {
        isSaturdaySelected = saturdaySelected;
    }

    public boolean isSundaySelected() {
        return isSundaySelected;
    }

    public void setSundaySelected(boolean sundaySelected) {
        isSundaySelected = sundaySelected;
    }
}
