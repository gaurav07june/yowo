package com.nomad.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Toast;

import com.nomad.R;
import com.nomad.databinding.ActivityCmsBinding;
import com.nomad.model.CMSResponseModel;
import com.nomad.model.GenericResponseModel;
import com.nomad.retrofit.WebAPIHelper;
import com.nomad.util.AppConstants;

public class CMSActivity extends NetworkActivity {
    ActivityCmsBinding binding;
    Intent intent;
    private String cmsType;
    String titleText;
    String unencodedHtml = "";
    static final int CMS_REQ_CODE = 21;
    @Override
    public void setContentView() {
        transparentStatusBar();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cms);
    }

    @Override
    public void getExtras() {
        intent = getIntent();
        cmsType = intent.getStringExtra(AppConstants.EXTRA.CMS_TYPE);
    }

    @Override
    public void initViews() {

    }

    @Override
    public void setViews() {
        if (cmsType.equals(AppConstants.CONSTANTS.PRIVACY_POLICY)){
            titleText = getResources().getString(R.string.privacy_policy);
        }else if (cmsType.equals(AppConstants.CONSTANTS.ABOUT_US)){
            titleText = getResources().getString(R.string.about_us);
        }else if(cmsType.equals(AppConstants.CONSTANTS.TERMS_AND_CONDITION)){
            titleText = getResources().getString(R.string.term_condition);
        }
        binding.layToolbar.titleTv.setText(titleText);
        binding.webViewCms.getSettings().setJavaScriptEnabled(true);
        checkInternetAndHitApi(CMS_REQ_CODE);

    }

    @Override
    public <D> void onApiSuccess(GenericResponseModel<D> model, int request_code) {
        switch (request_code){
            case CMS_REQ_CODE:
                GenericResponseModel<CMSResponseModel> responseModel
                        = (GenericResponseModel<CMSResponseModel>) model;
                if (responseModel.getData() != null){
                    unencodedHtml = responseModel.getData().getCmsContent();
                    showCMSContent();
                }

        }

    }

    @Override
    public <D> void onApiFail(GenericResponseModel<D> model, int request_code) {
        if (model != null && model.getError() != null && model.getError().getMessage() != null) {
            Toast.makeText(this, model.getError().getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onApiException(Throwable t, int request_code) {

    }

    @Override
    public void hitApi(int request) {
        switch (request){
            case CMS_REQ_CODE:
                WebAPIHelper.getInstance().getCMSContent(new APICallback<CMSResponseModel>(CMS_REQ_CODE));

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    private void showCMSContent(){

        String encodedHtml = Base64.encodeToString(unencodedHtml.getBytes(),
                Base64.NO_PADDING);
        binding.webViewCms.loadData(encodedHtml, "text/html", "base64");

    }
}
