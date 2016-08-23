package com.empire.vince.crucom.stuff;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by VinceGee on 08/23/2016.
 */
public class VinceTextView extends TextView {

    public VinceTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public VinceTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VinceTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "Lato-Light.ttf");
            setTypeface(tf);
        }
    }

}
