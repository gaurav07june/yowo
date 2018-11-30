package com.nomad.model;

import java.util.List;

public class MyVenueResponseModel {
    private List<Venue> favorite;

    private List<Venue> pastlog;

    public List<Venue> getFavorite() {
        return favorite;
    }

    public void setFavorite(List<Venue> favorite) {
        this.favorite = favorite;
    }

    public List<Venue> getPastlog() {
        return pastlog;
    }

    public void setPastlog(List<Venue> pastlog) {
        this.pastlog = pastlog;
    }
}
