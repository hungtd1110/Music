package com.example.lyrics_lib;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * Created by 29028 on 2017/6/6.
 */

public class GradienTextView extends AppCompatTextView {
    private int originalColor, changeColor;
    private Paint originalPaint, changePaint;
    private float textSize;
    private Orientation orientation = Orientation.LEFT_TO_RIGHT;
    private float mCurrentProgress;
    ValueAnimator animator;

    public GradienTextView(Context context) {
        super(context);
    }

    public GradienTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GradienTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float middle = mCurrentProgress * getWidth();
        
        if(orientation==Orientation.LEFT_TO_RIGHT){

            clipRect(canvas,0,middle,changePaint);
            clipRect(canvas,middle,getWidth(),originalPaint);

        }else if(orientation==Orientation.INNER_TO_OUTER){

            clipRect(canvas,getWidth()-middle,middle,changePaint);
            clipRect(canvas,middle,getWidth()-middle,originalPaint);

        }else if(orientation==Orientation.RIGHT_TO_LEFT){

            clipRect(canvas,getWidth()-middle,getWidth(),changePaint);
            clipRect(canvas,0,getWidth()-middle,originalPaint);

        }else if(orientation==Orientation.RIGHT_TO_LEFT_FROM_NONE){

            clipRect(canvas,getWidth()-middle,getWidth(),changePaint);
            clipRect(canvas,getWidth(),getWidth()-middle,originalPaint);

        }else if(orientation==Orientation.LEFT_TO_RIGHT_FORME_NONE){
            clipRect(canvas,0,middle,changePaint);
            clipRect(canvas,middle,0,originalPaint);

        }else if (orientation==Orientation.RESET) {
            clipRect(canvas,0,middle,originalPaint);
            clipRect(canvas,middle,0,originalPaint);
        }
    }
    private void clipRect(Canvas canvas,float start,float region,Paint paint){
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();
        windowManager.getDefaultDisplay().getSize(size);
        int widthScreen = size.x;
        float widthText = paint.measureText(getText().toString());

        float x = (widthScreen - widthText) / 2;
        float y = canvas.getHeight() - getPaddingBottom() - 2;

        canvas.save();
        canvas.clipRect(start,0,region,getHeight());
        canvas.drawText(getText().toString(),x,y,paint);
        canvas.restore();
    }

    public void setOrientation(Orientation orientation){
        this.orientation = orientation;
    }

    public void setCurrentProgress(float currentProgress){
        mCurrentProgress = currentProgress;
        invalidate();
    }

    public void start(final Orientation orientation,long duration){
        if (animator != null) {
            animator.cancel();
        }
        animator=ValueAnimator.ofFloat(0,1);
        animator.setDuration(duration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value= (float) animation.getAnimatedValue();
                setOrientation(orientation);
                setCurrentProgress(value);
            }
        });
        animator.start();
    }

    public void reset(){
        if (animator != null) {
            animator.cancel();
        }
        animator=ValueAnimator.ofFloat(0,1);
        animator.setDuration(0);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value= (float) animation.getAnimatedValue();
                setOrientation(Orientation.RESET);
                setCurrentProgress(value);
            }
        });
        animator.start();
    }

    public void setOriginalColor(int color) {
        originalColor = color;
    }

    public void setChangeColor(int color) {
        changeColor = color;
    }

    public void setTextSize(float size) {
        textSize = size;
    }

    public void init() {
        originalPaint = getPaintByColor(originalColor);
        changePaint = getPaintByColor(changeColor);
    }

    private Paint getPaintByColor(int color){
        Paint paint=new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(color);
        paint.setTextSize(textSize);
//        paint.setTypeface(Typeface.DEFAULT_BOLD);
        return paint;
    }
}
