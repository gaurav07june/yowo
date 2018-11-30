package com.nomad.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.nomad.R;
import com.nomad.customview.CateringPackageView;
import com.nomad.databinding.ActivityConfirmBookingBinding;
import com.nomad.model.GenericResponseModel;
import com.nomad.model.SpanData;
import com.nomad.util.AppCommons;
import com.nomad.util.BookingConfirmedDialog;

public class ConfirmBookingActivity extends NetworkActivity implements View.OnClickListener{

    private ActivityConfirmBookingBinding binding;
    private BookingConfirmedDialog bookingConfirmedDialog;
    @Override
    public void setContentView() {

        binding = DataBindingUtil.setContentView(this,R.layout.activity_confirm_booking);
    }

    @Override
    public void getExtras() {

    }

    @Override
    public void initViews() {
        bookingConfirmedDialog = new BookingConfirmedDialog(this,true);
    }

    @Override
    public void setViews() {
    binding.cateringPackagesView.setItemType(CateringPackageView.ItemType.NO_OF_PERSON_INPUT);
    binding.cateringPackagesView.setTotalPersons(4);
    binding.amountToBePaidTv.setText(AppCommons.getSpannedString(
                new SpanData(getString(R.string.amount_to_be_paid), ContextCompat.getColor(this,R.color.colorBlack),1f,false),
                new SpanData("$180", ContextCompat.getColor(this,R.color.colorBlack),1.5f,false)
        ));

    binding.confirmBtn.setOnClickListener(this);
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

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.confirm_btn:
            bookingConfirmedDialog.show();
            break;
        }
    }
}
