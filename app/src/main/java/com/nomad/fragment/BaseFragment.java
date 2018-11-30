package com.nomad.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.nomad.R;
import com.nomad.activity.NetworkActivity;
import com.nomad.activity.SignInActivity;
import com.nomad.model.GenericResponseModel;
import com.nomad.util.AppGlobalData;
import com.nomad.util.Constants;
import com.nomad.util.InternetCheck;
import com.nomad.util.InternetRetryDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by karan.kalsi on 4/20/2017.
 */
public abstract class BaseFragment extends Fragment {


    public abstract <D> void onApiSuccess(GenericResponseModel<D> model, int request_code);
    public abstract <D> void onApiFail(GenericResponseModel<D> model, int request_code);
    public abstract  void onApiException(Throwable t,int request_code);
    public abstract  void setViews();
    public abstract  void hitApi(int request);

    public class APICallback<T> implements Callback<GenericResponseModel<T>>

    {



        private int request_code=0;
        public APICallback(int request_code)
        {
            this.request_code=request_code;
        }


        @Override
        public void onResponse(Call<GenericResponseModel<T>> call, Response<GenericResponseModel<T>> response) {
            if (getActivity() == null) return;
            if (((NetworkActivity) getActivity()).progressDialog.isShowing())
                ((NetworkActivity) getActivity()).progressDialog.dismiss();
            if (response.code() == Constants.API.TOKEN_EXPIRED_CODE){

                AppGlobalData.getInstance().logOut(getActivity());
                startActivity(new Intent(getActivity(), SignInActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                Toast.makeText(getActivity(), "Your session has expired.", Toast.LENGTH_SHORT).show();
                getActivity().finish();

            }
            else if(response.body()==null)
            {
                onApiFail(null,request_code);
                return;
            }else{
                boolean status = response.body().getStatus();

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
           try
           {
               if(  ((NetworkActivity)getActivity()).progressDialog.isShowing())
                   ((NetworkActivity)getActivity()).progressDialog.dismiss();
               if (getContext()!=null)
               {
                   Toast.makeText(getContext(), R.string.check_internet_connection_msg,Toast.LENGTH_SHORT).show();
               }
               onApiException(t,request_code);
           }
           catch (Exception e)
           {

           }
        }
    }

    public class SilentAPICallback<T> implements Callback<GenericResponseModel<T>>

    {
        private int request_code=0;
        public SilentAPICallback(int request_code)
        {
            this.request_code=request_code;
        }

        @Override
        public void onResponse(Call<GenericResponseModel<T>> call, Response<GenericResponseModel<T>> response) {
            if (getActivity() == null) return;

            if (response.code() == Constants.API.TOKEN_EXPIRED_CODE){

                AppGlobalData.getInstance().logOut(getActivity());
                startActivity(new Intent(getActivity(), SignInActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                Toast.makeText(getActivity(), "Your session has expired.", Toast.LENGTH_SHORT).show();
                getActivity().finish();

            }
            else if(response.body()==null)
            {
                onApiFail(null,request_code);
                return;
            }else{
                boolean status = response.body().getStatus();

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
            try
            {

                if (getContext()!=null)
                {
                    Toast.makeText(getContext(), R.string.check_internet_connection_msg,Toast.LENGTH_SHORT).show();
                }
                onApiException(t,request_code);
            }
            catch (Exception e)
            {

            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setViews();
    }


    public void checkInternetAndHitApiWithoutLoader(final int request_code)
    {



        new InternetCheck(getActivity()).isInternetConnectionAvailable(new InternetCheck.InternetCheckListener() {

            @Override
            public void onComplete(boolean connected) {
                if (connected)
                    hitApi(request_code);
                else {

                    if (getActivity()==null)return;
                    new InternetRetryDialog(getActivity(), new InternetRetryDialog.RetryInternetListener() {
                        @Override
                        public void onRetry() {

                            checkInternetAndHitApiWithoutLoader(request_code);
                        }

                        @Override
                        public void onExit() {

                            ActivityCompat.finishAffinity(getActivity());
                        }
                    }).show();
                }

            }
        });
    }


    public void checkInternetAndHitApi(final int request_code)
    {
       ((NetworkActivity)getActivity()).progressDialog.show();
        new InternetCheck(getActivity()).isInternetConnectionAvailable(new InternetCheck.InternetCheckListener() {

            @Override
            public void onComplete(boolean connected) {
                if (connected)
                    hitApi(request_code);
                else {
                    ((NetworkActivity)getActivity()).progressDialog.dismiss();
                    new InternetRetryDialog(getActivity(), new InternetRetryDialog.RetryInternetListener() {
                        @Override
                        public void onRetry() {

                            checkInternetAndHitApi(request_code);
                        }

                        @Override
                        public void onExit() {
                            //((NetworkActivity)getActivity()).progressDialog.dismiss();
                            ActivityCompat.finishAffinity(getActivity());
                        }
                    }).show();
                }

            }
        });
    }

    public void dismissProgressDialog()
    {

        try{
            if (getActivity()==null)return;
          ((NetworkActivity)getActivity()).progressDialog.dismiss();
        }
    catch (Exception e)
    {

    }

    }


    public class VoidAPICallback implements Callback

    {



        private int request_code=0;
        public VoidAPICallback(int request_code)
        {
            this.request_code=request_code;
        }


        @Override
        public void onResponse(Call call, Response response) {
            if (getActivity() == null) return;
            if (((NetworkActivity) getActivity()).progressDialog.isShowing())
                ((NetworkActivity) getActivity()).progressDialog.dismiss();
            if (response.code() == Constants.API.TOKEN_EXPIRED_CODE){

                AppGlobalData.getInstance().logOut(getActivity());
                startActivity(new Intent(getActivity(), SignInActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                Toast.makeText(getActivity(), "Your session has expired.", Toast.LENGTH_SHORT).show();
                getActivity().finish();

            }else if (response.code() == Constants.API.TOKEN_SUCCESS_CODE){
                onApiSuccess(null, request_code);
            }else{
                onApiFail(null, request_code);
            }

        }
        @Override
        public void onFailure(Call call, Throwable t) {
            try
            {
                if(  ((NetworkActivity)getActivity()).progressDialog.isShowing())
                    ((NetworkActivity)getActivity()).progressDialog.dismiss();
                if (getContext()!=null)
                {
                    Toast.makeText(getContext(), R.string.check_internet_connection_msg,Toast.LENGTH_SHORT).show();
                }
                onApiException(t,request_code);
            }
            catch (Exception e)
            {

            }
        }
    }
}
