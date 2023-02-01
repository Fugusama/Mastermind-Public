package com.fugusama.mastermind.customviewobj;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import com.fugusama.mastermind.MainGameManager;
import com.fugusama.mastermind.R;
import com.fugusama.mastermind.customviewobj.Listener.DragListener;

import java.util.ArrayList;

public class AnswerSlot extends Circle {

    static final ArrayList<AnswerSlot> slots = new ArrayList<>();
    static int filled = 0;
    boolean animated;
    int occupant = -1;

    final float mainSize = getResources().getDimension(R.dimen.answerSlot_main);
    final float offset = getResources().getDimension(R.dimen.answerSlot_offset);

    DashPathEffect innerDashPathEffect = new DashPathEffect(new float[] {20F, 15F}, 0);
    DashPathEffect outerDashPathEffect = new DashPathEffect(new float[] {20, 0, 20, 20}, 0);

    public AnswerSlot(Context context)
    {
        this(context, null);
    }

    public AnswerSlot(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    @SuppressLint("ClickableViewAccessibility")
    public AnswerSlot(Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);

        size = (int) getResources().getDimension(R.dimen.answerSlot);
        animated = false;

        this.setOnDragListener(DragListener.getInstance());
        this.setOnTouchListener((view, e) ->
        {
            if (e.getAction() == MotionEvent.ACTION_DOWN && isValidDropPos(e.getX(), e.getY())) {
                if (occupant != -1) {
                    setOccupant(-1);
                    view.invalidate();

                    getRootView().findViewById(R.id.submit_button).setVisibility(INVISIBLE);

                    return true;
                }
            }
            return false;
        });

        slots.add(this);
    }

    public boolean isValidDropPos(float x, float y)
    {
        return  Math.pow(x - size / 2F, 2) + Math.pow(y - size / 2F, 2) <= Math.pow((mainSize + offset) / 3, 2);
    }

    public void setAnimated(boolean state)
    {
        this.animated = state;
    }

    public void setOccupant(int id)
    {
        if (occupant == -1 && id != -1) filled++;
        else if (id == -1) filled--;

        this.occupant = id;
        int rawColor = occupant == -1 ? R.color.answer_slot : MainGameManager.mapToColor(id);
        this.color = ContextCompat.getColor(getContext(), rawColor);
        this.paint.setColor(this.color);

        if (AnswerSlot.isFull()) getRootView().findViewById(R.id.submit_button).setVisibility(View.VISIBLE);
    }

    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    @Override
    public void onDraw(Canvas canvas)
    {
     //   super.onDraw(canvas);

        if (canvas != null) {

            float offset = 0;
            if (animated) offset += this.offset;

            paint.setStyle(Paint.Style.STROKE);
            if (occupant == -1 || animated) paint.setPathEffect(outerDashPathEffect);
            else paint.setPathEffect(null);
            paint.setStrokeWidth(8);
            canvas.drawCircle(getHeight() / 2, getWidth() / 2, (mainSize + offset) / 2.2F, paint);

            if (occupant == -1) paint.setPathEffect(innerDashPathEffect);
            else
            {
                paint.setPathEffect(null);
                paint.setStyle(Paint.Style.FILL);
            }
            paint.setStrokeWidth(6);
            canvas.drawCircle(getHeight() / 2F, getWidth() / 2F, (mainSize + offset) / 3F, paint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int size = (int)getResources().getDimension(R.dimen.answerSlot);
        setMeasuredDimension(size, size);
    }

    public static ArrayList<AnswerSlot> getSlots() { return slots; }

    public static boolean isFull()
    {
/*        int filled = 0;
        for (AnswerSlot slot : slots)
        {
            if (slot.occupant != -1) filled++;
        }*/
        return filled == 4;
    }

    public static int[] getGuesses()
    {
        int[] guesses = new int[4];
        for (AnswerSlot slot : slots)
        {
            guesses[slot.posID] = slot.occupant;
        }

        return guesses;
    }

    public static void reset()
    {
        for (AnswerSlot slot : slots)
        {
            slot.setOccupant(-1);
            slot.invalidate();
        }

        filled = 0;
    }
}
