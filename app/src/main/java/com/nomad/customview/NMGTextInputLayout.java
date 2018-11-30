package com.nomad.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.design.widget.TextInputLayout;
import android.util.AttributeSet;
import android.widget.TextView;

import com.nomad.R;

public class NMGTextInputLayout extends TextInputLayout {
    public NMGTextInputLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);


    }

    public NMGTextInputLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);

    }

    public NMGTextInputLayout(Context context) {
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
}
