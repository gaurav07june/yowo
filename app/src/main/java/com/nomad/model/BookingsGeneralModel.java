package com.nomad.model;

public class BookingsGeneralModel {
    private MeetingSpace meetingSpace;

    private int bookingId;

    private String logoImage;

    private String bookingDate;

    private double totalCharge;

    private String venueAddress;

    private double distance;

    private boolean isBookingLive;

    private boolean isCheckedIn;

    private double lattitude;

    private double longitude;

    private String checkedInTime;

    private String checkedOutTime;

    private int venueId;

    private String venueName;

    private String mainCoverPicture;

    private int bookingStatus;

    public MeetingSpace getMeetingSpace() {
        return meetingSpace;
    }

    public void setMeetingSpace(MeetingSpace meetingSpace) {
        this.meetingSpace = meetingSpace;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public String getLogoImage() {
        return logoImage;
    }

    public void setLogoImage(String logoImage) {
        this.logoImage = logoImage;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public double getTotalCharge() {
        return totalCharge;
    }

    public void setTotalCharge(double totalCharge) {
        this.totalCharge = totalCharge;
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

    public String getCheckedInTime() {
        return checkedInTime;
    }

    public void setCheckedInTime(String checkedInTime) {
        this.checkedInTime = checkedInTime;
    }

    public String getCheckedOutTime() {
        return checkedOutTime;
    }

    public void setCheckedOutTime(String checkedOutTime) {
        this.checkedOutTime = checkedOutTime;
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

    public int getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(int bookingStatus) {
        this.bookingStatus = bookingStatus;
    }
}
