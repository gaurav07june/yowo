package com.nomad.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.widget.TextView;

import com.nomad.R;

public class NMGTextView extends AppCompatTextView {
    public NMGTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);


    }

    public NMGTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);

    }

    public NMGTextView(Context context) {
        super(context);
        init(null);
    }
    private void init(AttributeSet attrs) {

        if (attrs!=null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.NMGTextView);
            String fontName = a.getString(R.styleable.NMGTextView_fontName);
            if (fontName!=null) {
                Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), "font/" +fontName);
                if(myTypeface!=null)
                    setTypeface(myTypeface);
            }
            a.recycle();
        }
    }

    public void setTypeface(String typeface)
    {
            if (typeface!=null) {
                Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), "font/" +typeface);
                if(myTypeface!=null)
                    setTypeface(myTypeface);
            }
    }
}