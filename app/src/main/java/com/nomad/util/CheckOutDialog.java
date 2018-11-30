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
import com.nomad.databinding.CheckOutDialogBinding;
import com.nomad.model.SpanData;


/**
 * Created by karan.kalsi on 4/7/2017.
 */
public class CheckOutDialog {
//    private Dialog dialog;
//    private CheckOutDialogBinding binding;
//    public CheckOutDialog(Context context, boolean isCancellable)
//    {
//        dialog = new Dialog(context, android.R.style.Theme_Light);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        View view = LayoutInflater.from(context).inflate(R.layout.check_out_dialog,null);
//        dialog.setContentView(view);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        dialog.setCancelable(isCancellable);
//        binding = DataBindingUtil.bind(view);
//        binding.checkedOutMsgTv.setText(AppCommons.getSpannedString(
//                new SpanData(context.getString(R.string.you_are_checked_out_msg_pre), ContextCompat.getColor(context,R.color.colorGray),1,false),
//                new SpanData(context.getString(R.string.you_are_checked_out_msg_mid),ContextCompat.getColor(context,R.color.colorBlack),1,false),
//                new SpanData(context.getString(R.string.you_are_checked_out_msg_post),ContextCompat.getColor(context,R.color.colorGray),1,false)
//        ));
//
//    }
//public void show()
//{
//    try{
//        if(dialog==null )return;
//        dialog.show();
//    }
//    catch (Exception e)
//    {
//    }
//
//}
//    public void dismiss()
//    {
//        try{
//        if(dialog==null )return;
//        dialog.dismiss();
//    }
//    catch (Exception e)
//    {
//
//    }
//    }
//
//    public boolean isShowing()
//    {
//        return dialog!=null && dialog.isShowing();
//    }
}
