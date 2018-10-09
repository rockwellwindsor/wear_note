package com.example.android.windsordesignstudio.wear_note_1.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by Rockwell Windsor Rice on 10/9/18.
 */
public class LoraWearEditTextView extends EditText {

    public LoraWearEditTextView(Context context) {
        super(context);
        init();
    }

    public LoraWearEditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoraWearEditTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public LoraWearEditTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Lora-Regular.ttf");
        setTypeface(tf, 1);
    }
}
