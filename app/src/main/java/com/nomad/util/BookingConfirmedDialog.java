package com.nomad.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

import com.nomad.R;


/**
 * Created by karan.kalsi on 4/7/2017.
 */
public class BookingConfirmedDialog {
    private Dialog dialog;
    public BookingConfirmedDialog(Context context, boolean isCancellable)
    {
        dialog = new Dialog(context, android.R.style.Theme_Light);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.booking_confirmed_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(isCancellable);
    }
public void show()
{
    try{
        if(dialog==null )return;
        dialog.show();
    }
    catch (Exception e)
    {
    }

}
    public void dismiss()
    {
        try{
        if(dialog==null )return;
        dialog.dismiss();
    }
    catch (Exception e)
    {

    }
    }

    public boolean isShowing()
    {
        return dialog!=null && dialog.isShowing();
    }
}
