package com.fugusama.mastermind.customviewobj.Listener;

import android.content.ClipDescription;
import android.view.DragEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import com.fugusama.mastermind.R;
import com.fugusama.mastermind.customviewobj.AnswerSlot;

public class DragListener implements View.OnDragListener {

    final static DragListener instance = new DragListener();

    @Override
    public boolean onDrag(View view, DragEvent e) {
        switch (e.getAction())
        {
            case DragEvent.ACTION_DRAG_STARTED:
                if (e.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN))
                {
                    // add rotation animation
                    float centre = view.getResources().getDimension(R.dimen.answerSlot) / 2F;

                    RotateAnimation animation = new RotateAnimation(0, 360, centre, centre);
                    animation.setInterpolator(new LinearInterpolator());
                    animation.setDuration(3000);
                    animation.setRepeatCount(Animation.INFINITE);

                    for (AnswerSlot slot : AnswerSlot.getSlots())
                    {
                        slot.setAnimated(true);
                        slot.startAnimation(animation);

                        slot.invalidate();
                    }
                    return true;
                }
                return false;
            case DragEvent.ACTION_DROP:
                if (view instanceof AnswerSlot)
                {
                    AnswerSlot slot = (AnswerSlot) view;
                    if (slot.isValidDropPos(e.getX(), e.getY()))
                    {
                        String label = (String) e.getClipData().getItemAt(0).getText();
                        slot.setAnimated(false);
                        slot.setOccupant(Integer.parseInt(label));

                        slot.invalidate();
                        return true;
                    }
                    return false;
                }
                return false;
            case DragEvent.ACTION_DRAG_ENDED:
                for (AnswerSlot slot : AnswerSlot.getSlots())
                {
                    slot.setAnimated(false);
                    slot.clearAnimation();

                    slot.invalidate();
                }
                return true;
            default:
                return false;
        }
    }

    public static DragListener getInstance() {
        return instance;
    }
}
