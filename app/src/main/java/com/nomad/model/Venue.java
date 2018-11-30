package com.nomad.model;

import java.io.Serializable;
import java.util.List;
import java.util.Observable;

public class Venue extends Observable implements Serializable {

    private String logoImage;

    private List<Amenity> amenities;

    private String todayTimings;

    private String venueAddress;

    private double distance;

    private boolean isMeetingSpaceAvailable;

    private boolean isCheckedIn;

    private boolean isBookingLive;

    private boolean isFavorite;

    private String closingTime;

    private String openingTime;

    private double lattitude;

    private double longitude;

    private String venueName;

    private String mainCoverPicture;

    private int venueId;

    private String type;

    public boolean isMeetingSpaceAvailable() {
        return isMeetingSpaceAvailable;
    }

    public void setMeetingSpaceAvailable(boolean meetingSpaceAvailable) {
        isMeetingSpaceAvailable = meetingSpaceAvailable;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLogoImage ()
    {
        return logoImage;
    }

    public void setLogoImage (String logoImage)
    {
        this.logoImage = logoImage;
    }

    public String getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(String openingTime) {
        this.openingTime = openingTime;
    }

    public String getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(String closingTime) {
        this.closingTime = closingTime;
    }

    public String getTodayTimings ()
    {
        return todayTimings;
    }

    public void setTodayTimings (String todayTimings)
    {
        this.todayTimings = todayTimings;
    }

    public String getVenueAddress ()
    {
        return venueAddress;
    }

    public void setVenueAddress (String venueAddress)
    {
        this.venueAddress = venueAddress;
    }

    public List<Amenity> getAmenities() {
        return amenities;
    }

    public void setAmenities(List<Amenity> amenities) {
        this.amenities = amenities;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public boolean isCheckedIn() {
        return isCheckedIn;
    }

    public void setCheckedIn(boolean checkedIn) {
        isCheckedIn = checkedIn;
        setChanged();
        notifyObservers(checkedIn);
    }

    public boolean isBookingLive() {
        return isBookingLive;
    }

    public void setBookingLive(boolean bookingLive) {
        isBookingLive = bookingLive;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        this.isFavorite = favorite;
        this.setChanged();
        notifyObservers(favorite);
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

    public String getVenueName ()
    {
        return venueName;
    }

    public void setVenueName (String venueName)
    {
        this.venueName = venueName;
    }

    public String getMainCoverPicture ()
    {
        return mainCoverPicture;
    }

    public void setMainCoverPicture (String mainCoverPicture)
    {
        this.mainCoverPicture = mainCoverPicture;
    }



    @Override
    public String toString()
    {
        return "ClassPojo [logoImage = "+logoImage+", amenities = "+amenities+", todayTimings = "+todayTimings+", venueAddress = "+venueAddress+", distance = "+distance+", isCheckedIn = "+isCheckedIn+", isBookingLive = "+isBookingLive+", isFavorite = "+isFavorite+", lattitude = "+lattitude+", longitude = "+longitude+", venueName = "+venueName+", mainCoverPicture = "+mainCoverPicture+", venueId = "+ venueId +"]";
    }
}
