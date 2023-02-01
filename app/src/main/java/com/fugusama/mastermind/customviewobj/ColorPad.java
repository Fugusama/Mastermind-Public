package com.fugusama.mastermind.customviewobj;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.Nullable;
import com.fugusama.mastermind.R;
import com.fugusama.mastermind.customviewobj.Listener.DragListener;

public class ColorPad extends Circle {

    public ColorPad(Context context)
    {
        this(context, null);
    }

    public ColorPad(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    @SuppressLint("ClickableViewAccessibility")
    public ColorPad(Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);

        size = (int) getResources().getDimension(R.dimen.colorPad);

        // onDrag
        this.setOnTouchListener((view, e) ->
        {
            if (e.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData.Item item = new ClipData.Item(String.valueOf(posID));
                ClipData dragData = new ClipData(
                        "COLOR_BALL_POS",
                        new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN},
                        item
                );
                final View.DragShadowBuilder shadowBuilder = new BallShadowBuilder(this);

                view.startDragAndDrop(
                        dragData,
                        shadowBuilder,
                        null,
                        0
                );

                return true;
            }

            return false;
        });

        this.setOnDragListener(DragListener.getInstance());
    }

    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    @Override
    public void onDraw(Canvas canvas) {
        if (canvas != null) {
            canvas.drawCircle(getHeight() / 2, getWidth() / 2, getWidth() / 2, paint);
        }
    }

    private static class BallShadowBuilder extends DragShadowBuilder {
        private static ColorPad shadow;

        public BallShadowBuilder(ColorPad colorPad) {
            super(colorPad);

            shadow = colorPad;
        }

        @Override
        public void onProvideShadowMetrics(Point size,Point touch)
        {
            int width = getView().getWidth();
            int height = getView().getHeight();

            size.set(width, height);

            touch.set(width / 2, height / 2);
        }

        @Override
        public void onDrawShadow(Canvas canvas) {
            shadow.draw(canvas);
        }
    }
}
