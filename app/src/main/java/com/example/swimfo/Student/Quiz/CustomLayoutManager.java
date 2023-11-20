package com.example.swimfo.Student.Quiz;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;

public class CustomLayoutManager extends LinearLayoutManager {
    private boolean isScrollEnabled = true;

    public CustomLayoutManager(Context context) {
        super(context);
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically() {
        // Similarly for canScrollHorizontally if needed
        return isScrollEnabled && super.canScrollVertically();
    }
}