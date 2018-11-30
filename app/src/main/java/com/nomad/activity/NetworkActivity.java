package com.nomad.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.nomad.R;
import com.nomad.model.GenericResponseModel;
import com.nomad.util.AppGlobalData;
import com.nomad.util.CircularProgressDialog;
import com.nomad.util.Constants;
import com.nomad.util.InternetCheck;
import com.nomad.util.InternetRetryDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class NetworkActivity extends BaseActivity {
    public abstract void setContentView();
    public abstract void getExtras();
    public abstract void initViews();
    public abstract void setViews();

    public abstract <D> void onApiSuccess(GenericResponseModel<D> model, int request_code);

    public abstract <D> void onApiFail(GenericResponseModel<D> model, int request_code);



    public abstract void onApiException(Throwable t, int request_code);

    public abstract void hitApi(int request);

    public CircularProgressDialog progressDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog=new CircularProgressDialog(this, false);
        getExtras();
        setContentView();
        initViews();
        setViews();

    }
    public void checkInternetAndHitApi(final int request_code) {
        progressDialog.show();
        new InternetCheck(this).isInternetConnectionAvailable(new InternetCheck.InternetCheckListener() {

            @Override
            public void onComplete(boolean connected) {
                if (connected)
                    hitApi(request_code);
                else {
                    new InternetRetryDialog(NetworkActivity.this, new InternetRetryDialog.RetryInternetListener() {
                        @Override
                        public void onRetry() {
                            checkInternetAndHitApi(request_code);
                        }
                        @Override
                        public void onExit() {
                            progressDialog.show();
                            ActivityCompat.finishAffinity(NetworkActivity.this);
                        }
                    }).show();
                }
            }
        });
    }
    public void checkInternetAndHitApiWithoutLoader(final int request_code) {

        new InternetCheck(this).isInternetConnectionAvailable(new InternetCheck.InternetCheckListener() {

            @Override
            public void onComplete(boolean connected) {
                if (connected)
                    hitApi(request_code);
                else {
                    new InternetRetryDialog(NetworkActivity.this, new InternetRetryDialog.RetryInternetListener() {
                        @Override
                        public void onRetry() {
                            checkInternetAndHitApiWithoutLoader(request_code);
                        }
                        @Override
                        public void onExit() {
                            progressDialog.show();
                            ActivityCompat.finishAffinity(NetworkActivity.this);
                        }
                    }).show();
                }
            }
        });
    }

    public class APICallback<T> implements Callback<GenericResponseModel<T>>
    {
        private int request_code=0;
        public APICallback(int request_code)
        {
            this.request_code=request_code;
        }


        @Override
        public void onResponse(Call<GenericResponseModel<T>> call, Response<GenericResponseModel<T>> response) {


            if(progressDialog.isShowing())progressDialog.dismiss();
            if (response.code() == Constants.API.TOKEN_EXPIRED_CODE){
                AppGlobalData.getInstance().logOut(NetworkActivity.this);
                startActivity(new Intent(NetworkActivity.this, SignInActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                Toast.makeText(NetworkActivity.this, "Your session has expired.", Toast.LENGTH_SHORT).show();
                finish();
            }else if (response.body()==null)
            {
                onApiFail(null,request_code);
                return;
            }
            else{
                boolean status = response.body().getStatus();
                String msg =response.body().getMessage();

                if(status)
                {
                    onApiSuccess(response.body(),request_code);
                }
                else
                {
                    onApiFail(response.body(),request_code);
                }
            }
        }
        @Override
        public void onFailure(Call<GenericResponseModel<T>> call, Throwable t) {
            if(progressDialog.isShowing())progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), R.string.check_internet_connection_msg,Toast.LENGTH_SHORT).show();
            onApiException(t,request_code);
        }
    }

    public class VoidAPICallback implements Callback<Void>
    {
        private int request_code=0;
        public VoidAPICallback(int request_code)
        {
            this.request_code=request_code;
        }


        @Override
        public void onResponse(Call<Void> call, Response<Void> response) {


            if(progressDialog.isShowing())progressDialog.dismiss();
            if (response.code() == Constants.API.TOKEN_EXPIRED_CODE){
                AppGlobalData.getInstance().logOut(NetworkActivity.this);
                startActivity(new Intent(NetworkActivity.this, SignInActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                Toast.makeText(NetworkActivity.this, "Your session has expired.", Toast.LENGTH_SHORT).show();
                finish();
            }else if (response.code() == Constants.API.TOKEN_SUCCESS_CODE){
                onApiSuccess(null, request_code);
            }else{
                onApiFail(null, request_code);
            }
        }
        @Override
        public void onFailure(Call<Void> call, Throwable t) {
            if(progressDialog.isShowing())progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), R.string.check_internet_connection_msg,Toast.LENGTH_SHORT).show();
            onApiException(t,request_code);
        }
    }
}
