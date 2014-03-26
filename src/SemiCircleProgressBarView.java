package com.example.progressbar;

import com.example.progressbar.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

public class SemiCircleProgressBarView extends View
{

    private Path mClippingPath;
    private Context mContext;
    private Bitmap mBitmap;
    private float mPivotX;
    private float mPivotY;

    public SemiCircleProgressBarView(Context context) {
        super(context);
        mContext = context;
        initilizeImage();
    }

    public SemiCircleProgressBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initilizeImage();
    }

    private void initilizeImage() {
        mClippingPath = new Path();

        //Top left coordinates of image. Give appropriate values depending on the position you wnat image to be placed
        mPivotX = 200;
        mPivotY = 50;

        //Adjust the image size to support different screen sizes
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.green_round);
        int imageWidth = (int) (getScreenGridUnit() * 10);
        int imageHeight = (int) (getScreenGridUnit() * 10);
    //    mBitmap = Bitmap.createScaledBitmap(bitmap, imageWidth, imageHeight, false);
       mBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), false);
    }

    public void setClipping(float progress) {

        //Convert the progress in range of 0 to 100 to angle in range of 0 180. Easy math.
        float angle = (progress * 280) / 100;
        mClippingPath.reset();
        //Define a rectangle containing the image
        RectF oval = new RectF(mPivotX, mPivotY, mPivotX + mBitmap.getWidth(), mPivotY + mBitmap.getHeight());
        //Move the current position to center of rect
        mClippingPath.moveTo(oval.centerX(), oval.centerY());
        //Draw an arc from center to given angle
        mClippingPath.addArc(oval, 220, angle);
        //Draw a line from end of arc to center
        mClippingPath.lineTo(oval.centerX(), oval.centerY());
        //Redraw the canvas
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //Clip the canvas
        canvas.clipPath(mClippingPath);
        canvas.drawBitmap(mBitmap, mPivotX, mPivotY, null);

    }

    private float getScreenGridUnit()
    {
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity)mContext).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels / 32;
    }

}
