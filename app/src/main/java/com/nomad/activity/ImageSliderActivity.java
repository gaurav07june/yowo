package com.nomad.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.View;

import com.nomad.R;
import com.nomad.customview.CoverImagesPager;
import com.nomad.customview.NMGCircularViewPager;
import com.nomad.util.Constants;

public class ImageSliderActivity extends BaseActivity {

    private CoverImagesPager imagePager;
    private int startImageIndex=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportPostponeEnterTransition();
        transparentStatusBar();
        setContentView(R.layout.activity_image_slider);

        imagePager = findViewById(R.id.image_pager);
        imagePager.setZoomable(true);
        startImageIndex=getIntent().getExtras().getInt(Constants.EXTRA.COVER_IMAGE_POSITION);
        imagePager.setCoverImages(getIntent().getExtras().getStringArrayList(Constants.EXTRA.COVER_IMAGES));
        imagePager.setCurrentItem(startImageIndex);

    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK,new Intent().putExtra(Constants.EXTRA.COVER_IMAGE_POSITION,imagePager.getCurrentItem()));
        super.onBackPressed();

    }
}
