package com.nomad.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.nomad.R;

import com.nomad.databinding.ActivitySignBinding;
import com.nomad.model.GenericResponseModel;

import com.nomad.model.LoginReqModel;
import com.nomad.model.LoginResModel;
import com.nomad.retrofit.WebAPIHelper;
import com.nomad.socialLoginUtil.FacebookLoginUtil;
import com.nomad.socialLoginUtil.GoogleLoginUtil;
import com.nomad.util.AppCommons;
import com.nomad.util.AppGlobalData;
import com.nomad.util.SoftKeyUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

public class SignInActivity extends NetworkActivity implements GoogleLoginUtil.GoogleSignInListener, FacebookLoginUtil.FacebookLoginListener {

    ActivitySignBinding binding;
    GoogleLoginUtil googleLoginUtil;
    FacebookLoginUtil facebookLoginUtil;
    List<String> googleSinginResultList;
    private static final String EMAIL = "email";
    private static final int SIGN_IN_API_REQ_CODE = 101;
    private static final int GOOGLE_SIGN_IN_REQ_CODE = 701;
    LoginReqModel loginReqModel = new LoginReqModel();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        transparentStatusBar();
    }


    @Override
    public void setContentView() {
        binding= DataBindingUtil.setContentView(this, R.layout.activity_sign);
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.nomad",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        googleLoginUtil = new GoogleLoginUtil(this, this);
        facebookLoginUtil = new FacebookLoginUtil(this, this);
    }

    @Override
    public void getExtras() {

    }

    @Override
    public void initViews() {

    }

    @Override
    public void setViews() {
        if (AppGlobalData.getInstance().setFromPrefs(this)){
            //user is already logged in . take him to dashboard
            startActivity(new Intent(SignInActivity.this, Dashboard.class));
            finish();
            return;
        }

    }

    @Override
    public <D> void onApiSuccess(GenericResponseModel<D> model, int request_code) {
        try{
            switch (request_code){
                case SIGN_IN_API_REQ_CODE:
                    GenericResponseModel<LoginResModel> responseModel =
                            (GenericResponseModel<LoginResModel>) model;
                    Toast.makeText(this, responseModel.getData().getProfile().getAccessToken(), Toast.LENGTH_SHORT).show();
                    if (responseModel.getData().getProfile() != null){
                        AppGlobalData.getInstance().setLoggedInCustomer(responseModel.getData().getProfile());
                        AppGlobalData.getInstance().setToken(responseModel.getData().getProfile().getAccessToken());
                        AppGlobalData.getInstance().setGuest(false);
                        AppGlobalData.getInstance().getLoggedInCustomer().setProfileChanged(false);
                        AppGlobalData.getInstance().saveToPrefs(getApplicationContext());
                        Toast.makeText(this, AppGlobalData.getInstance().getToken(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, Dashboard.class));
                        finish();
                    }
            }
        }catch (Exception e){

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
            case SIGN_IN_API_REQ_CODE:

                WebAPIHelper.getInstance().doSignin(loginReqModel, new APICallback<LoginResModel>(SIGN_IN_API_REQ_CODE));

                break;
        }

    }
    public void onOuterLayClicked(View view) {

        SoftKeyUtil.hideSoftKeyboard(this);
    }

    public void onSignIn(View view) {
        if (isFormValid()){
            loginReqModel.setRegisteredVia("normal");
            loginReqModel.setLoginId(binding.edtUsername.getText().toString().trim());

            loginReqModel.setPassword(binding.edtPassword.getText().toString().trim());
            checkInternetAndHitApi(SIGN_IN_API_REQ_CODE);
        }
    }

    public void onFacebookClicked(View view) {
        //Toast.makeText(this, "Login to facebook account", Toast.LENGTH_SHORT).show();
        facebookLoginUtil.loginWithFacebook(Arrays.asList("public_profile"));
    }

    public void onGoogleClicked(View view) {

        //Toast.makeText(this, "Login to google account", Toast.LENGTH_SHORT).show();
        startActivityForResult(googleLoginUtil.getGoogleSignInIntent(), GOOGLE_SIGN_IN_REQ_CODE);
    }

    @Override
    public void onLogoutFromGoogleComplete() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        facebookLoginUtil.getCallbackManager().onActivityResult(requestCode, resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_SIGN_IN_REQ_CODE){
            googleSinginResultList = googleLoginUtil.getGoogleSignInResult(data);
            if (googleSinginResultList.size()>2)
            {

                loginReqModel.setRegisteredVia("google");
                loginReqModel.setLoginId(googleSinginResultList.get(2));
                checkInternetAndHitApi(SIGN_IN_API_REQ_CODE);
                //Toast.makeText(this, googleSinginResultList.get(2), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onFacebookLoginSuccess(String email, String userId) {
        loginReqModel.setSocialId(userId);
        loginReqModel.setLoginId(email);
        loginReqModel.setRegisteredVia("facebook");
        //Toast.makeText(this, email+"userId "+userId, Toast.LENGTH_SHORT).show();
        checkInternetAndHitApi(SIGN_IN_API_REQ_CODE);

    }

    @Override
    public void onFacebookLoginCancel() {
        Toast.makeText(this, "facebook login cancelled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFacebookLoginError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFacebookLogoutCompleted() {

    }
    private boolean isFormValid(){
        if (binding.edtUsername.getText().toString().trim().isEmpty()){
           Toast.makeText(this, getResources().getString(R.string.username_empty_error), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!AppCommons.isValidEmail(binding.edtUsername.getText().toString().trim())){
            Toast.makeText(this, getResources().getString(R.string.email_invalid_error), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (binding.edtPassword.getText().toString().trim().isEmpty()){
            Toast.makeText(this, getResources().getString(R.string.password_empty_error), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
