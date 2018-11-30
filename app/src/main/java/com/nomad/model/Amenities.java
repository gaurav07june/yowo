package com.nomad.model;

public class Amenities {
    private String name;

    private String iconURL;

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getIconURL ()
    {
        return iconURL;
    }

    public void setIconURL (String iconURL)
    {
        this.iconURL = iconURL;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [name = "+name+", iconURL = "+iconURL+"]";
    }
}
