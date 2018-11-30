package com.nomad.model;

public class VenueDetailResModel {
    private Venuedetail venuedetail;

    public Venuedetail getVenuedetail ()
    {
        return venuedetail;
    }

    public void setVenuedetail (Venuedetail venuedetail)
    {
        this.venuedetail = venuedetail;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [venuedetail = "+venuedetail+"]";
    }
}
