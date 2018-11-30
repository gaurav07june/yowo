package com.nomad.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.nomad.R;
import com.nomad.databinding.ActivityChangePasswordBinding;
import com.nomad.model.GenericResponseModel;
import com.nomad.model.UpdatePasswordReqModel;
import com.nomad.retrofit.WebAPIHelper;
import com.nomad.util.AppGlobalData;

public class ChangePasswordActivity extends NetworkActivity implements View.OnClickListener {
    ActivityChangePasswordBinding binding;
    static final int UPDATE_PASSWORD_REQ_CODE = 99;
    @Override
    public void setContentView() {
        transparentStatusBar();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_password);
    }

    @Override
    public void getExtras() {

    }
    @Override
    public void initViews() {
        binding.layToolbar.titleTv.setText(getResources().getString(R.string.change_password));
    }
    @Override
    public void setViews() {
        binding.btnUpdate.setOnClickListener(this);
        binding.layToolbar.backBtn.setOnClickListener(this);
    }
    @Override
    public <D> void onApiSuccess(GenericResponseModel<D> model, int request_code) {
        switch (request_code){
            case UPDATE_PASSWORD_REQ_CODE:
                if (model != null){
                    Toast.makeText(this, model.getMessage(), Toast.LENGTH_SHORT).show();
                    /*AppGlobalData.getInstance().logOut(this);
                    startActivity(new Intent(this, SignInActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();*/
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

    }

    @Override
    public void hitApi(int request) {
        switch (request){
            case UPDATE_PASSWORD_REQ_CODE:
                UpdatePasswordReqModel reqModel = new UpdatePasswordReqModel();
                reqModel.setUserId(AppGlobalData.getInstance().getLoggedInCustomer().getId());
                reqModel.setOldPassword(binding.edtOldPassword.getText().toString().trim());
                reqModel.setNewPassword(binding.edtNewPassword.getText().toString().trim());
                WebAPIHelper.getInstance().updatePassword(reqModel, new APICallback<Object>(UPDATE_PASSWORD_REQ_CODE));
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
            case R.id.btnUpdate:
                if (isValidForm()){
                    //doChangPassword
                    checkInternetAndHitApi(UPDATE_PASSWORD_REQ_CODE);
                }
                break;
            case R.id.back_btn:
                super.onBackPressed();
                break;
        }

    }

    private boolean isValidForm(){
        if (binding.edtOldPassword.getText().toString().trim().isEmpty()){
            Toast.makeText(this, getResources().getString(R.string.old_password_empty_error), Toast.LENGTH_SHORT).show();
            binding.edtOldPassword.requestFocus();
            return false;
        }
        if (binding.edtNewPassword.getText().toString().trim().isEmpty()){
            Toast.makeText(this, getResources().getString(R.string.new_password_empty_error), Toast.LENGTH_SHORT).show();
            binding.edtNewPassword.requestFocus();
            return false;
        }
        if (binding.edtConfNewPassword.getText().toString().trim().isEmpty()){
            Toast.makeText(this, getResources().getString(R.string.confirm_password_empty_error), Toast.LENGTH_SHORT).show();
            binding.edtConfNewPassword.requestFocus();
            return false;
        }
        if (!(binding.edtNewPassword.getText().toString().trim().equals(binding.edtConfNewPassword.getText().toString().trim()))){
            Toast.makeText(this, getResources().getString(R.string.password_mismatch_error), Toast.LENGTH_SHORT).show();
            binding.edtNewPassword.setText("");
            binding.edtConfNewPassword.setText("");
            binding.edtNewPassword.requestFocus();
            return false;
        }

        return true;
    }
}
