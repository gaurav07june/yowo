package com.nomad.util;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import com.nomad.R;
import com.nomad.model.SpanData;


/**
 * Created by karan.kalsi on 4/7/2017.
 */
public class SinglePersonCheckInDialog {
    private Dialog dialog;
//    private SinglePersonCheckInDialogBinding binding;
    public SinglePersonCheckInDialog(Context context, boolean isCancellable)
    {
//        dialog = new Dialog(context, android.R.style.Theme_Light);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        View view = LayoutInflater.from(context).inflate(R.layout.single_person_check_in_dialog,null);
//        dialog.setContentView(view);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        dialog.setCancelable(isCancellable);
//        binding = DataBindingUtil.bind(view);
//        binding.youAreCheckedInTv.setText(AppCommons.getSpannedString(
//                new SpanData(context.getString(R.string.you_are_checked_in_msg_pre), ContextCompat.getColor(context,R.color.colorGray),1,false),
//                new SpanData(context.getString(R.string.you_are_checked_in_msg_post),ContextCompat.getColor(context,R.color.colorBlack),1,false)));
//        binding.wifiNameTv.setText(AppCommons.getSpannedString(
//                new SpanData(context.getString(R.string.the_wifi_network_name_is),ContextCompat.getColor(context,R.color.colorGray),1,false),
//                new SpanData("InfectedRouter",ContextCompat.getColor(context,R.color.colorPrimary),1,false)
//        ));
//        binding.wifiPasswordTv.setText(AppCommons.getSpannedString(
//                new SpanData(context.getString(R.string.your_wifi_password),ContextCompat.getColor(context,R.color.colorGray),1,false),
//                new SpanData("12345678",ContextCompat.getColor(context,R.color.colorPrimary),1,false)
//        ));
//
//        binding.checkInDiscountMsgTv.setText(AppCommons.getSpannedString(
//                new SpanData(context.getString(R.string.check_in_discount_pre_msg),ContextCompat.getColor(context,R.color.colorGray),1,false),
//                new SpanData("15% discount",ContextCompat.getColor(context,R.color.colorPrimary),1,false),
//                new SpanData(context.getString(R.string.check_in_discount_post_msg),ContextCompat.getColor(context,R.color.colorGray),1,false)
//        ));
    }
public void show()
{
//    try{
//        if(dialog==null )return;
//        dialog.show();
//    }
//    catch (Exception e)
//    {
//    }

}
    public void dismiss()
    {
//        try{
//        if(dialog==null )return;
//        dialog.dismiss();
//    }
//    catch (Exception e)
//    {
//
//    }
    }

    public boolean isShowing()
    {
        return false;
//        return dialog!=null && dialog.isShowing();
    }
}
