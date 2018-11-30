package com.nomad.model;

import java.util.List;

public class CitySuburbResModel {

    private List<City> cities;

    private List<Suburb> suburbs;

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    public List<Suburb> getSuburb() {
        return suburbs;
    }

    public void setSuburb(List<Suburb> suburbs) {
        this.suburbs = suburbs;
    }
}
