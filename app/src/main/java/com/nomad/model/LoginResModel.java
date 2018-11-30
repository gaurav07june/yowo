package com.nomad.model;

public class LoginResModel {
    private Profile profile;

    public Profile getProfile ()
    {
        return profile;
    }

    public void setProfile (Profile profile)
    {
        this.profile = profile;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [profile = "+profile+"]";
    }
}
