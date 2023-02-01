package com.fugusama.mastermind;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.fugusama.mastermind.customviewobj.AnswerSlot;
import com.fugusama.mastermind.customviewobj.RecordBlock;

public class MainGameManager {

    private static Game currentGame = null;

    public static boolean hasGame()
    {
        return currentGame == null;
    }

    public static int[] makeGuess(int[] guess)
    {
        int[] result = currentGame.makeGuess(guess);
        if (result[0] == 4 || currentGame.getCount() >= 10)
        {
            return null;
        }

        return result;
    }

    public static int mapToColor(int id)
    {
        switch (id)
        {
            case 0:
                return R.color.blue_ball;
            case 1:
                return R.color.static_text;
            case 2:
                return R.color.yellow_ball;
            case 3:
                return R.color.green_ball;
            default:
                return 0;
        }
    }

    public static int mapToID(int color)
    {
        return 0;
    }

    @SuppressLint("SetTextI18n")
    public static void refreshTrialIndicatorText(View root)
    {
        TextView counter = root.getRootView().findViewById(R.id.Counter);
        counter.setText("Trials Remaining: " + (10 - currentGame.getCount()));
    }

    public static void forceReset()
    {
        if (currentGame != null) {
            AnswerSlot.reset();
            ViewGroup vg = RecordBlock.getViewParent();
            if (vg != null) vg.removeAllViews();
        }

        currentGame = new Game();
    }

    public static boolean isNewGame()
    {
        return currentGame.getCount() == 0;
    }

    public static void showHint() {
        currentGame.showHint();
    }

    public static boolean isWin()
    {
        return currentGame.isWin();
    }
}
