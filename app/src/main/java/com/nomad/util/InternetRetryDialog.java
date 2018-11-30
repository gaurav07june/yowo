package com.nomad.util;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.nomad.R;


/**
 * Created by karan.kalsi on 5/3/2017.
 */
public class InternetRetryDialog {

    private MaterialDialog retryInternetDialog;
    private Context mContext;
    private Handler mHandler;

    public InternetRetryDialog(Context context, final RetryInternetListener retryInternetDialogCallback)
    {
        mHandler=new Handler();
        mContext=context;

        retryInternetDialog=  new MaterialDialog.Builder(context)
                .backgroundColorRes(R.color.colorWhite)
                .titleColorRes(R.color.colorGray1)
                .title(R.string.connection_error_msg)
                .positiveText(R.string.retry)
                .positiveColor(ContextCompat.getColor(context,R.color.colorPrimary))
                .negativeColor(ContextCompat.getColor(context,R.color.colorPrimary))
                .negativeText(R.string.exit)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        if (retryInternetDialogCallback!=null)
                        retryInternetDialogCallback.onRetry();
                        dismiss();

                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (retryInternetDialogCallback!=null)
                        retryInternetDialogCallback.onExit();
                        dismiss();
                    }
                })
               .build();
        retryInternetDialog.setCancelable(false);
    }


    public  void show()
    {

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (((Activity) mContext).hasWindowFocus()) {
                            retryInternetDialog.show();
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            },500);



    }

    public void dismiss()
    {

        try{
             if(retryInternetDialog.isShowing() &&  !((Activity) mContext).isFinishing() )
                retryInternetDialog.dismiss();

        }
        catch (Exception e)
        {

        }

    }
public interface RetryInternetListener {
    public void onRetry();
    public void onExit();
}
}
