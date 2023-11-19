package com.example.swimfo.game;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class NonScrollableScrollView extends ScrollView {

    public NonScrollableScrollView(Context context) {
        super(context);
    }

    public NonScrollableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NonScrollableScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;  // Prevent touch events
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;  // Prevent touch events
    }
}

