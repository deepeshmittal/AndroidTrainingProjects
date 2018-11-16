package com.bignerdranch.android.draganddraw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BoxDrawingView extends View {
    private static final String TAG = "BoxDrawingView";
    private Box mCurrentBox;
    private List<Box> mBoxen = new ArrayList<>();
    private Paint mBoxPaint;
    private Paint mBackgroundPaint;

    // Used when creating the view in code
    public BoxDrawingView(Context context) {
        this(context, null);
        Log.d(TAG, "BoxDrawingView context");
    }
    // Used when inflating the view from XML
    public BoxDrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.d(TAG, "BoxDrawingView XML");
        // Paint the boxes a nice semitransparent red (ARGB)
        mBoxPaint = new Paint();

        // Paint the background off-white
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(0xfff8efe0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        PointF current = new PointF(event.getX(), event.getY());
        String action = "";
        Log.d(TAG, "onTouchEvent" + event.getAction());

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                action = "ACTION_DOWN";
                Random rnd = new Random();
                int r = rnd.nextInt(128) + 128; // 128 ... 255
                int g = rnd.nextInt(128) + 128; // 128 ... 255
                int b = rnd.nextInt(128) + 128; // 128 ... 255

                int c = Color.rgb(r,g,b);

                // Reset drawing state
                mCurrentBox = new Box(current, c);
                mBoxen.add(mCurrentBox);
                break;
            case MotionEvent.ACTION_MOVE:
                action = "ACTION_MOVE";
                if (mCurrentBox != null) {
                    mCurrentBox.setCurrent(current);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                action = "ACTION_UP";
                mCurrentBox = null;
                break;
            case MotionEvent.ACTION_CANCEL:
                action = "ACTION_CANCEL";
                mCurrentBox = null;
                break; }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Fill the background
        Log.d(TAG, "onDraw" + mBoxen.size());
        canvas.drawPaint(mBackgroundPaint);
        for (Box box : mBoxen) {
            float left = Math.min(box.getOrigin().x, box.getCurrent().x);
            float right = Math.max(box.getOrigin().x, box.getCurrent().x);
            float top = Math.min(box.getOrigin().y, box.getCurrent().y);
            float bottom = Math.max(box.getOrigin().y, box.getCurrent().y);
            Log.d(TAG, " at left=" + left +
                    ", right=" + right + ", top=" + top + ", bottom=" + bottom);
            //canvas.drawRect(left, top, right, bottom, mBoxPaint);
            //canvas.drawCircle(box.getCurrent().x, box.getCurrent().y, 2, mBoxPaint);
            mBoxPaint.setColor(box.getColor());
            mBoxPaint.setAlpha(80);
            canvas.drawOval(left, top, right, bottom, mBoxPaint);
        }
    }
}
