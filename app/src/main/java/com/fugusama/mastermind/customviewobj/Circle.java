package com.fugusama.mastermind.customviewobj;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.Nullable;
import com.fugusama.mastermind.R;

public class Circle extends View {

    protected Paint paint;
    protected int color, posID, strokeWidth, strokeColor, size = 100;
    protected boolean hasStroke = false;

    public Circle(Context context)
    {
        this(context, null);
    }

    public Circle(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public Circle(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray arr = context.getTheme().obtainStyledAttributes(attrs, R.styleable.Circle, 0, 0);

        try {
            color = arr.getColor(R.styleable.Circle_circleColor, 0xff000000);
            posID = arr.getInteger(R.styleable.Circle_posID, 0);
        } finally {
            arr.recycle();
        }

        paint = new Paint();
        paint.setColor(color);
    }

    public void setColor(int color)
    {
        this.color = color;
        paint.setColor(color);
    }

    public void setStroke(int width, int color)
    {
        this.hasStroke = true;
        this.strokeWidth = width;
        this.strokeColor = color;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getPaddingLeft() + size + getPaddingRight(), getPaddingTop() + size + getPaddingBottom());
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        if (canvas != null)
        {
            paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(size / 2F, size / 2F, size / 2.2F, paint);

            if (hasStroke) {
                paint.setStyle(Paint.Style.STROKE);
                paint.setColor(strokeColor);
                paint.setStrokeWidth(strokeWidth);
                canvas.drawCircle(size / 2F, size / 2F, size / 2.2F, paint);
            }
        }
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getPosID() {
        return posID;
    }
}
