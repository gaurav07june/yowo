package com.nomad.model;

import java.util.List;

public class VenueListingResModel {
    private PageData pageData;

    private List<Venue> venues;

    public PageData getPageData ()
    {
        return pageData;
    }

    public void setPageData (PageData pageData)
    {
        this.pageData = pageData;
    }

    public List<Venue> getVenues() {
        return venues;
    }

    public void setVenues(List<Venue> venues) {
        this.venues = venues;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [pageData = "+pageData+", venues = "+venues+"]";
    }
}
