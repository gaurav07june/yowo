package com.nomad.model;

import android.os.Parcelable;

import java.io.Serializable;

public class PagerData implements Serializable {
    int image;
    String text;
    public PagerData(int image, String text){
        this.image = image;
        this.text = text;
    }

    public int getImage() {
        return image;
    }


    public String getText() {
        return text;
    }

}
