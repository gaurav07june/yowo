package com.nomad.model;

public class CateringPackage {

    private transient int selectedPersons;

    private int id;
    private String title;
    private String description;
    private double perPersonCharge;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPerPersonCharge() {
        return perPersonCharge;
    }

    public void setPerPersonCharge(double perPersonCharge) {
        this.perPersonCharge = perPersonCharge;
    }

    public int getSelectedPersons() {
        return selectedPersons;
    }

    public void setSelectedPersons(int selectedPersons) {
        this.selectedPersons = selectedPersons;
    }
}
