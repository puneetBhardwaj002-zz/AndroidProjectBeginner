package com.example.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class PieChart extends View {
    Paint mPaint;
    Rect mRect;
    static int mSquareColor;
    static int mPadding=0;
    public PieChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }
    private void init(@Nullable AttributeSet set){
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRect = new Rect();
        if(set == null){
            return;
        }
        TypedArray typedArray = getContext().obtainStyledAttributes(set,R.styleable.PieChart);
        mSquareColor = typedArray.getColor(R.styleable.PieChart_square_color,Color.RED);
        mPaint.setColor(mSquareColor);
        typedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mRect.left = mPadding;
        mRect.right = getWidth() - mPadding;
        mRect.top = mPadding;
        mRect.bottom = getHeight() - mPadding;
        canvas.drawRect(mRect,mPaint);
    }

    public void swapColor(){
        mPaint.setColor(mPaint.getColor() == mSquareColor ? Color.GREEN : mSquareColor);
        postInvalidate();
    }

    public void customPaddingUp(int padding){
        mPadding += padding;
        postInvalidate();
    }
    public void customPaddingDown(int padding){
        mPadding -= padding;
        postInvalidate();
    }
}
