package com.nomad.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;

import com.nomad.R;
import com.nomad.model.SpanData;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class AppCommons {

    private AppCommons() {

    }

    public static final String formatTimeValue(int value) {
        String stringValue = String.valueOf(value);
        if (stringValue.length() == 1)
            return "0" + stringValue;
        else
            return stringValue;
    }

    public static final List<String> generatePersonCountList(int count) {
        List<String> personCountList = new ArrayList<>();
        for (int i = 1; i <= count; i++)
            personCountList.add(String.valueOf(i));
        return personCountList;
    }

    public static final List<String> generatePersonCountList(int count, boolean fromZero) {
        List<String> personCountList = new ArrayList<>();
        int start = fromZero ? 0 : 1;
        for (int i = start; i <= count; i++)
            personCountList.add(String.valueOf(i));
        return personCountList;
    }


    public static final SpannableString getSpannedString(SpanData... spanDataArr) {
        if (spanDataArr == null) return null;
        int cur_index = 0;
        int span_string_length = 0;
        SpannableString content = new SpannableString(getAppendedString(spanDataArr));
        for (final SpanData spanData : spanDataArr) {
            span_string_length += spanData.getString().length();
            content.setSpan(new ForegroundColorSpan(spanData.getColor()), cur_index, span_string_length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            content.setSpan(new RelativeSizeSpan(spanData.getRelativeSize()), cur_index, span_string_length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (spanData.isMakeBold())
                content.setSpan(new StyleSpan(Typeface.BOLD), cur_index, span_string_length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (spanData.getOnClickListener() != null) {
                content.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        Log.d("SpanData", "on click");
                        spanData.getOnClickListener().onClick(widget);
                    }

                    public void updateDrawState(TextPaint ds) {
                        ds.setUnderlineText(false);
                    }
                }, cur_index, span_string_length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            cur_index = span_string_length;
        }

        return content;


    }

    public static final String getAppendedString(SpanData... spanDataArr) {
        String spanString = "";

        for (SpanData spanData : spanDataArr) {
            spanString += spanData.getString();
        }
        return spanString;
    }

    public static final DisplayImageOptions getAmenitiesImageLoadingOptions() {
        return new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.ic_placeholder_amenities)
                .showImageOnFail(R.drawable.ic_placeholder_amenities)
                .showImageOnLoading(R.drawable.ic_placeholder_amenities)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
    }
    public static final DisplayImageOptions getVenueImageLoadingOptions() {
        return new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.ic_placeholder_venue)
                .showImageOnFail(R.drawable.ic_placeholder_venue)
                .showImageOnLoading(R.drawable.ic_placeholder_venue)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
    }

public static final String getAppDir(Context context)
{
    PackageManager m = context.getPackageManager();
    String s = context.getPackageName();
    try {
        PackageInfo p = m.getPackageInfo(s, 0);
        s = p.applicationInfo.dataDir;
    } catch (PackageManager.NameNotFoundException e) {
    }
    return s;
}
    public static boolean isValidEmail(String email){
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null) {
            return false;
        }
        return pat.matcher(email).matches();
    }

    public static String getBookingDateTimeFormate(String dateString){
        SimpleDateFormat simpleDateFormatInput = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat simpleDateFormatOutput = new SimpleDateFormat("yyyy/MM/dd hh:mm a");
        Date date = null;
        try {
            date = simpleDateFormatInput.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return simpleDateFormatOutput.format(date);
    }

    public static void clearImageCache(String imageUrl){
        DiskCacheUtils.removeFromCache(imageUrl, ImageLoader.getInstance().getDiskCache());
        MemoryCacheUtils.removeFromCache(imageUrl, ImageLoader.getInstance().getMemoryCache());
    }

    public static boolean isValidCheckInTime(String openingTime, String closingTime){

        return true;
    }
}
