package com.fugusama.mastermind;

import com.fugusama.mastermind.customviewobj.AnswerSlot;

import java.util.Random;

public class Game {

    private final int[] seq = new int[4];
    private final int[] colorCount = new int[4];
    private int count = 0, hintCount = 0;
    private boolean win = false;

    public Game()
    {
        Random rand = new Random();
        for (int i = 0; i < 4; i++)
        {
            int color = rand.nextInt(4);
            this.seq[i] = color;
            this.colorCount[color]++;
        }
    }

    public int[] makeGuess(int[] guess)
    {
        int correctColor = 0, correctPosColor = 0;
        int[] colorCount = new int[4];

        for (int i = 0; i < 4; i++)
        {
            colorCount[guess[i]]++;
        }

        for (int i = 0; i < 4; i++)
        {
            if (guess[i] == this.seq[i]) {
                correctPosColor++;
            }

            if (colorCount[i] <= this.colorCount[i]) {
                correctColor += colorCount[i];
            }
        }

        count++;
        if (correctPosColor == 4) win = true;

        return new int[] {correctPosColor, correctColor - correctPosColor};
    }

    public int getCount() {
        return count;
    }

    public boolean isWin() {
        return win;
    }

    public void showHint()
    {
        if (hintCount > 3) return;

        AnswerSlot slot = AnswerSlot.getSlots().get(hintCount);
        slot.setOccupant(seq[slot.getPosID()]);
        slot.invalidate();

        hintCount++;
    }
}
