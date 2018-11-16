package com.bignerdranch.android.draganddraw;

import android.graphics.PointF;

public class Box {

    private PointF mOrigin;
    private PointF mCurrent;
    private int mColor;


    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        mColor = color;
    }


    public Box(PointF origin, int c) {
        mOrigin = origin;
        mCurrent = origin;
        mColor = c;
    }
    public PointF getCurrent() {
        return mCurrent;
    }
    public void setCurrent(PointF current) {
        mCurrent = current;
    }
    public PointF getOrigin() {
        return mOrigin;
    }
}
