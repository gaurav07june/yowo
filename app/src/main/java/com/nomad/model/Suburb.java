package com.nomad.model;

import java.io.Serializable;
import java.util.Observable;

public class Suburb extends Observable implements Serializable{
    private int id;
    private String name;
    private String type;

    public int getId() {
        return id;
    }

    public void setId(int id)
    {

        this.id = id;
        setChanged();
        notifyObservers(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
