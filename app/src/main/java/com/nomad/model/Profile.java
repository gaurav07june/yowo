package com.nomad.model;

import android.graphics.Bitmap;


import java.io.Serializable;
import java.util.Observable;

public class Profile extends Observable implements Serializable {

    private int id;

    private String filePath;

    private boolean isProfileChanged = false;

    private String lastName;

    private String subscriptionExpiryDate;

    private String accessToken;

    private String email;

    private String createdAt;

    private CheckedInVenue checkedInVenue;

    private String avatar;

    private String firstName;

    private boolean isTrialActive;

    private String hasFavorites;

    private String firebaseRegId;

    private String registeredVia;

    private boolean notificationEnabled = false;

    private City city;

    private Suburb suburb;

    private VenueFilterData filterData;

    private boolean isFilterSelected;


    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public boolean isProfileChanged() {
        return isProfileChanged;
    }

    public void setProfileChanged(boolean profileChanged) {
        isProfileChanged = profileChanged;
        this.setChanged();
        notifyObservers(profileChanged);
    }

    public boolean isFilterSelected() {
        return isFilterSelected;
    }

    public void setFilterSelected(boolean filterSelected) {
        isFilterSelected = filterSelected;
    }

    public VenueFilterData getFilterData() {
        if (filterData == null){
            return new VenueFilterData();
        }else{
            return filterData;
        }

    }

    public Suburb getSuburb() {
        return suburb;
    }

    public void setSuburb(Suburb suburb) {
        this.suburb = suburb;
    }

    public void setFilterData(VenueFilterData filterData) {
        this.filterData = filterData;
    }

    public int getId ()
    {
        return id;
    }

    public void setId (int id)
    {
        this.id = id;
    }

    public String getLastName ()
    {
        return lastName;
    }

    public void setLastName (String lastName)
    {
        this.lastName = lastName;

    }

    public String getSubscriptionExpiryDate ()
    {
        return subscriptionExpiryDate;
    }

    public void setSubscriptionExpiryDate (String subscriptionExpiryDate)
    {
        this.subscriptionExpiryDate = subscriptionExpiryDate;
    }

    public String getAccessToken ()
    {
        return accessToken;
    }

    public String getRegisteredVia() {
        return registeredVia;
    }

    public void setRegisteredVia(String registeredVia) {
        this.registeredVia = registeredVia;
    }

    public boolean isTrialActive() {
        return isTrialActive;
    }

    public void setTrialActive(boolean trialActive) {
        isTrialActive = trialActive;
    }

    public boolean isNotificationEnabled() {
        return notificationEnabled;
    }

    public void setNotificationEnabled(boolean notificationEnabled) {
        this.notificationEnabled = notificationEnabled;
    }


    public String getFirebaseRegId() {
        return firebaseRegId;
    }

    public void setFirebaseRegId(String firebaseRegId) {
        this.firebaseRegId = firebaseRegId;
    }

    public void setAccessToken (String accessToken)
    {
        this.accessToken = accessToken;
    }

    public String getEmail ()
    {
        return email;
    }

    public void setEmail (String email)
    {
        this.email = email;
    }

    public String getCreatedAt ()
    {
        return createdAt;
    }

    public void setCreatedAt (String createdAt)
    {
        this.createdAt = createdAt;
    }

    public CheckedInVenue getCheckedInVenue ()
    {
        return checkedInVenue;
    }

    public void setCheckedInVenue (CheckedInVenue checkedInVenue)
    {
        this.checkedInVenue = checkedInVenue;
    }

    public String getAvatar ()
    {
        return avatar;
    }

    public void setAvatar (String avatar)
    {
        this.avatar = avatar;

    }

    public String getFirstName ()
    {
        return firstName;
    }

    public void setFirstName (String firstName)
    {
        this.firstName = firstName;

    }

    public boolean getIsTrialActive ()
    {
        return isTrialActive;
    }

    public void setIsTrialActive (boolean isTrialActive)
    {
        this.isTrialActive = isTrialActive;
    }

    public String getHasFavorites ()
    {
        return hasFavorites;
    }

    public void setHasFavorites (String hasFavorites)
    {
        this.hasFavorites = hasFavorites;
    }

    public City getCity ()
    {
        return city;
    }

    public void setCity (City city)
    {
        this.city = city;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", lastName = "+lastName+", subscriptionExpiryDate = "+subscriptionExpiryDate+", accessToken = "+accessToken+", email = "+email+", createdAt = "+createdAt+", checkedInVenue = "+checkedInVenue+", avatar = "+avatar+", firstName = "+firstName+", isTrialActive = "+isTrialActive+", hasFavorites = "+hasFavorites+", city = "+city+"]";
    }
}
