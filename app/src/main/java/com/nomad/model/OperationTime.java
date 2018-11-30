package com.nomad.model;

public class OperationTime
{
    private String closingTime;

    private String isToday;

    private String dayName;

    private String openingTime;

    private String isOpen;

    public String getClosingTime ()
    {
        return closingTime;
    }

    public void setClosingTime (String closingTime)
    {
        this.closingTime = closingTime;
    }

    public String getIsToday ()
    {
        return isToday;
    }

    public void setIsToday (String isToday)
    {
        this.isToday = isToday;
    }

    public String getDayName ()
    {
        return dayName;
    }

    public void setDayName (String dayName)
    {
        this.dayName = dayName;
    }

    public String getOpeningTime ()
    {
        return openingTime;
    }

    public void setOpeningTime (String openingTime)
    {
        this.openingTime = openingTime;
    }

    public String getIsOpen ()
    {
        return isOpen;
    }

    public void setIsOpen (String isOpen)
    {
        this.isOpen = isOpen;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [closingTime = "+closingTime+", isToday = "+isToday+", dayName = "+dayName+", openingTime = "+openingTime+", isOpen = "+isOpen+"]";
    }
}
	