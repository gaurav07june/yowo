package com.nomad.customview;


import android.app.Dialog;
import android.content.Context;

import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;

import com.nomad.R;

import com.nomad.model.CheckedInVenue;
import com.nomad.util.AppCommons;
import com.nostra13.universalimageloader.core.ImageLoader;

public class VenueCheckoutDialog extends Dialog implements View.OnClickListener {
    Context context;
    CheckedInVenue checkedInVenue;
    NMGTextView txtVenueTitle, txtVenueLocation, txtCheckoutButton, txtCheckedInDate, txtCheckedInTime;
    ImageView imgVenueLogo;
    VenueCheckoutDialogListener listener;
    public VenueCheckoutDialog(Context context, CheckedInVenue checkedInVenue) {
        super(context);
        this.context = context;
        this.checkedInVenue = checkedInVenue;
    }

    public void setCheckoutListener(VenueCheckoutDialogListener listener){
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.venue_check_out_dialog_layout);
        txtVenueTitle = findViewById(R.id.venue_title_tv);
        txtVenueLocation = findViewById(R.id.venue_location);
        txtCheckoutButton = findViewById(R.id.check_out_btn);
        imgVenueLogo = findViewById(R.id.venue_cover_iv);
        txtCheckedInDate = findViewById(R.id.venue_timings_tv);
        txtCheckedInTime = findViewById(R.id.txtStartTimeValue);
        ImageLoader.getInstance().displayImage(checkedInVenue.getLogoImage(), imgVenueLogo);
        txtVenueTitle.setText(checkedInVenue.getVenueName());
        txtVenueLocation.setText(checkedInVenue.getVenueAddress());
        txtCheckedInDate.setText(AppCommons.getBookingDateTimeFormate(checkedInVenue.getCheckInDate()));
        txtCheckedInTime.setText(checkedInVenue.getStartTime());
        txtCheckoutButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.check_out_btn:
                this.listener.onVenueCheckout(checkedInVenue.getCheckInId());
                break;
        }
    }

    public interface VenueCheckoutDialogListener{
        void onVenueCheckout(int id);
    }
}
