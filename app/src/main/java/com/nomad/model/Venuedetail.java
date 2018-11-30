package com.nomad.model;

import java.io.Serializable;
import java.util.List;

public class Venuedetail implements Serializable {
    private List<SpecialDates> specialDates;

    private String contactNumber;

    private String logoImage;

    private List<Amenity> amenities;

    private String todayTimings;

    private List<OperationTimes> operationTimes;

    private String minDateForBooking;

    private List<String> otherImages;

    private String venueAddress;

    private double distance;

    private boolean isBookingLive;

    private boolean isCheckedIn;

    private String closingTime;

    private boolean isFavorite;

    private double lattitude;

    private double longitude;

    private int venueId;

    private String venueName;

    private String mainCoverPicture;

    private String openingTime;

    private boolean isMeetingSpaceAvailable;


    public List<SpecialDates> getSpecialDates() {
        return specialDates;
    }

    public void setSpecialDates(List<SpecialDates> specialDates) {
        this.specialDates = specialDates;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getLogoImage() {
        return logoImage;
    }

    public void setLogoImage(String logoImage) {
        this.logoImage = logoImage;
    }

    public List<Amenity> getAmenities() {
        return amenities;
    }

    public void setAmenities(List<Amenity> amenities) {
        this.amenities = amenities;
    }

    public String getTodayTimings() {
        return todayTimings;
    }

    public void setTodayTimings(String todayTimings) {
        this.todayTimings = todayTimings;
    }

    public List<OperationTimes> getOperationTimes() {
        return operationTimes;
    }

    public void setOperationTimes(List<OperationTimes> operationTimes) {
        this.operationTimes = operationTimes;
    }

    public String getMinDateForBooking() {
        return minDateForBooking;
    }

    public void setMinDateForBooking(String minDateForBooking) {
        this.minDateForBooking = minDateForBooking;
    }

    public List<String> getOtherImages() {
        return otherImages;
    }

    public void setOtherImages(List<String> otherImages) {
        this.otherImages = otherImages;
    }

    public String getVenueAddress() {
        return venueAddress;
    }

    public void setVenueAddress(String venueAddress) {
        this.venueAddress = venueAddress;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public boolean isBookingLive() {
        return isBookingLive;
    }

    public void setBookingLive(boolean bookingLive) {
        isBookingLive = bookingLive;
    }

    public boolean isCheckedIn() {
        return isCheckedIn;
    }

    public void setCheckedIn(boolean checkedIn) {
        isCheckedIn = checkedIn;
    }

    public String getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(String closingTime) {
        this.closingTime = closingTime;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public double getLattitude() {
        return lattitude;
    }

    public void setLattitude(double lattitude) {
        this.lattitude = lattitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getVenueId() {
        return venueId;
    }

    public void setVenueId(int venueId) {
        this.venueId = venueId;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public String getMainCoverPicture() {
        return mainCoverPicture;
    }

    public void setMainCoverPicture(String mainCoverPicture) {
        this.mainCoverPicture = mainCoverPicture;
    }

    public String getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(String openingTime) {
        this.openingTime = openingTime;
    }

    public boolean isMeetingSpaceAvailable() {
        return isMeetingSpaceAvailable;
    }

    public void setMeetingSpaceAvailable(boolean meetingSpaceAvailable) {
        isMeetingSpaceAvailable = meetingSpaceAvailable;
    }
}
