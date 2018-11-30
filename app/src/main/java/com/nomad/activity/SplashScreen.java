package com.nomad.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;

import com.nomad.R;
import com.nomad.model.GenericResponseModel;

public class SplashScreen extends NetworkActivity {

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transparentStatusNavBar();
        setContentView(R.layout.activity_splash_screen);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreen.this,OnBoardingActivity.class));
                finish();
            }
        },1000);
    }

    @Override
    public void setContentView() {

    }

    @Override
    public void getExtras() {

    }

    @Override
    public void initViews() {

    }

    @Override
    public void setViews() {

    }

    @Override
    public <D> void onApiSuccess(GenericResponseModel<D> model, int request_code) {

    }

    @Override
    public <D> void onApiFail(GenericResponseModel<D> model, int request_code) {

    }

    @Override
    public void onApiException(Throwable t, int request_code) {

    }

    @Override
    public void hitApi(int request) {

    }
}
