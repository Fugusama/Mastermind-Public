package com.fugusama.mastermind.customviewobj;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import androidx.core.content.ContextCompat;
import com.fugusama.mastermind.MainGameManager;
import com.fugusama.mastermind.R;

public class RecordBlock extends LinearLayout {
    final int height = (int) getResources().getDimension(R.dimen.record_block_height);
    final int recordSize = (int) (getResources().getDimension(R.dimen.colorPad) - 5);
    public final int marginSpacing = (int) getResources().getDimension(R.dimen.record_block_layout_margin);

    static ViewGroup parent = null;

    int[] ans;
    int countBlack, countWhite;

    public RecordBlock(Context context) {
        this(context, null);
    }

    public RecordBlock(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public RecordBlock(Context context, AttributeSet attrs, int defStyleAttr)
    {
        this(context, attrs, defStyleAttr, new int[4], 0, 0);
    }

    public RecordBlock(Context context, AttributeSet attrs, int defStyleAttr, int[] ans, int countBlack, int countWhite)
    {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.RecordBlock,
                0,
                0
        );

        this.ans = ans;
        this.countBlack = countBlack;
        this.countWhite = countWhite;

        this.setOrientation(HORIZONTAL);
        this.setGravity(Gravity.CENTER);

        final LinearLayout.LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(marginSpacing, marginSpacing, marginSpacing, marginSpacing);

        for (int i = 0; i < 4; i++)
        {
            final Circle circle = createCircle(context, recordSize, MainGameManager.mapToColor(ans[i]));
            this.addView(circle, lp);

            circle.invalidate();
        }

        final TableLayout table = iniResult(context);
        this.addView(table, lp);

        this.setPadding(marginSpacing, 0, marginSpacing, 0);
        this.setBackground(getResources().getDrawable(R.drawable.record_shadow));
        this.setElevation(marginSpacing);
    }

    private TableLayout iniResult(Context context)
    {
        final TableLayout table = new TableLayout(context);
        final TableLayout.LayoutParams tlp = new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tlp.setMargins(0, 8, 0, 8);

        for (int i = 0; i < 2; i++)
        {
            final TableRow row = new TableRow(context);
            row.setLayoutParams(tlp);
            row.setGravity(Gravity.CENTER);
            for (int j = 0; j < 2; j++)
            {
                final Circle circle = createCircle(context, recordSize / 3, R.color.gray_ball);
                circle.setStroke(5, ContextCompat.getColor(context, R.color.static_text));
                circle.setPadding(8, 0, 8, 0);

                if (countBlack > 0)
                {
                    circle.setColor(0xff000000);
                    countBlack--;
                }
                else if (countWhite > 0)
                {
                    circle.setColor(0xffffffff);
                    countWhite--;
                }

                row.addView(circle);
                circle.invalidate();
            }
            table.addView(row, tlp);
        }

        return table;
    }

    private Circle createCircle(Context context, int size, int color)
    {
        final Circle circle = new Circle(context);
        circle.setColor(ContextCompat.getColor(context, color));
        circle.setSize(size);

        return circle;
    }

    public static ViewGroup getViewParent() {
        return parent;
    }

    public static void setViewParent(ViewGroup parent) {
        RecordBlock.parent = parent;
    }
}
