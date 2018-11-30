package com.nomad.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.nomad.R;
import com.nomad.databinding.ActivitySettingBinding;
import com.nomad.model.GenericResponseModel;
import com.nomad.model.NotiSettingReqModel;
import com.nomad.model.NotificationStatusResModel;
import com.nomad.retrofit.WebAPIHelper;
import com.nomad.util.AppGlobalData;
import com.nomad.util.Constants;

public class SettingActivity extends NetworkActivity implements View.OnClickListener {
    ActivitySettingBinding binding;
    boolean isPushNotification = false;
    boolean isEmailNotification = false;
    private static final int NOTIFICATION_SETTING_REQ_CODE = 55;
    private static final int GET_NOTIFICATION_STATUS_REQ_CODE = 11;
    NotiSettingReqModel reqModel = new NotiSettingReqModel();
    @Override
    public void setContentView() {
        transparentStatusBar();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting);

    }

    @Override
    public void getExtras() {

    }

    @Override
    public void initViews() {

    }

    @Override
    public void setViews() {
        binding.layToolbar.titleTv.setText(getResources().getString(R.string.notifiaction_setting));
        binding.imgToggleOn.setOnClickListener(this);
        binding.imgToggleOff.setOnClickListener(this);
        binding.rltLayChangePassword.setOnClickListener(this);
        binding.imgPushToggleOff.setOnClickListener(this);
        binding.imgPushToggleOn.setOnClickListener(this);
        binding.layToolbar.backBtn.setOnClickListener(this);
        checkInternetAndHitApi(GET_NOTIFICATION_STATUS_REQ_CODE);
    }

    @Override
    public <D> void onApiSuccess(GenericResponseModel<D> model, int request_code) {
        switch (request_code){
            case NOTIFICATION_SETTING_REQ_CODE:
                Toast.makeText(this, model.getMessage(), Toast.LENGTH_SHORT).show();
                if (reqModel.getNotificationType().equals(Constants.EXTRA.EMAIL_NOTI_TYPE)){
                    isEmailNotification = !isEmailNotification;
                }else{
                    isPushNotification = !isPushNotification;
                }

                updateToggleButton();

                break;
            case GET_NOTIFICATION_STATUS_REQ_CODE:
                if (model.getData() != null){
                    GenericResponseModel<NotificationStatusResModel> resModel = (GenericResponseModel<NotificationStatusResModel>) model;
                    isEmailNotification = resModel.getData().isEmailNotification();
                    isPushNotification = resModel.getData().isPushNotification();
                    updateToggleButton();
                    }
                break;
        }

    }

    @Override
    public <D> void onApiFail(GenericResponseModel<D> model, int request_code) {
        Toast.makeText(this, model.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onApiException(Throwable t, int request_code) {
        Toast.makeText(this, t.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hitApi(int request) {
        switch (request){
            case GET_NOTIFICATION_STATUS_REQ_CODE:
                WebAPIHelper.getInstance().getNotificationStatus(new APICallback<NotificationStatusResModel>(GET_NOTIFICATION_STATUS_REQ_CODE),
                        AppGlobalData.getInstance().getLoggedInCustomer().getId());
                break;
            case NOTIFICATION_SETTING_REQ_CODE:
                WebAPIHelper.getInstance().updateNotificationStatus(reqModel, new APICallback<Object>(NOTIFICATION_SETTING_REQ_CODE));
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgToggleOn:
                reqModel.setNotification(false);
                reqModel.setNotificationType(Constants.EXTRA.EMAIL_NOTI_TYPE);
                reqModel.setUserId(AppGlobalData.getInstance().getLoggedInCustomer().getId());
                checkInternetAndHitApi(NOTIFICATION_SETTING_REQ_CODE);
                break;
            case R.id.imgToggleOff:
                reqModel.setNotification(true);
                reqModel.setNotificationType(Constants.EXTRA.EMAIL_NOTI_TYPE);
                reqModel.setUserId(AppGlobalData.getInstance().getLoggedInCustomer().getId());
                checkInternetAndHitApi(NOTIFICATION_SETTING_REQ_CODE);
                break;
            case R.id.imgPushToggleOn:
                reqModel.setNotification(false);
                reqModel.setNotificationType(Constants.EXTRA.PUSH_NOTI_TYPE);
                reqModel.setUserId(AppGlobalData.getInstance().getLoggedInCustomer().getId());
                checkInternetAndHitApi(NOTIFICATION_SETTING_REQ_CODE);
                break;
            case R.id.imgPushToggleOff:
                reqModel.setNotification(true);
                reqModel.setNotificationType(Constants.EXTRA.PUSH_NOTI_TYPE);
                reqModel.setUserId(AppGlobalData.getInstance().getLoggedInCustomer().getId());
                checkInternetAndHitApi(NOTIFICATION_SETTING_REQ_CODE);
                break;
            case R.id.rltLayChangePassword:
                startActivity(new Intent(this, ChangePasswordActivity.class));
                break;
            case R.id.back_btn:
                super.onBackPressed();
                break;
        }
    }
    private void updateToggleButton(){
        if (isEmailNotification){
            binding.imgToggleOn.setVisibility(View.VISIBLE);
            binding.imgToggleOff.setVisibility(View.GONE);
        }else{
            binding.imgToggleOff.setVisibility(View.VISIBLE);
            binding.imgToggleOn.setVisibility(View.GONE);
        }

        if (isPushNotification){
            binding.imgPushToggleOn.setVisibility(View.VISIBLE);
            binding.imgPushToggleOff.setVisibility(View.GONE);
        }else{
            binding.imgPushToggleOff.setVisibility(View.VISIBLE);
            binding.imgPushToggleOn.setVisibility(View.GONE);
        }
    }
}
