package com.nomad.customview;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.nomad.R;

public class PickImageDialog extends Dialog implements View.OnClickListener{
    Activity activity;
    NMGButton btnCamera, btnGallery;
    PickImageListener pickImageListener;

    public PickImageDialog(Activity activity, PickImageListener pickImageListener) {
        super(activity);
        this.pickImageListener = pickImageListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.take_pic_dialog);
        btnCamera = findViewById(R.id.btnCamera);
        btnGallery = findViewById(R.id.btnGallery);
        btnCamera.setOnClickListener(this);
        btnGallery.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnCamera:
                pickImageListener.onCameraClick();
                break;
            case R.id.btnGallery:
                pickImageListener.onGalleryClick();
                break;
        }

    }

    public interface PickImageListener{
        void onCameraClick();
        void onGalleryClick();
    }

}
