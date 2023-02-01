package com.fugusama.mastermind;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.fugusama.mastermind.customviewobj.AnswerSlot;
import com.fugusama.mastermind.customviewobj.RecordBlock;

public class MainActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_frame);

        setupListener();
    }

    private void setupListener()
    {
        Button startButton = findViewById(R.id.start_button);
        startButton.setOnClickListener(view ->
        {
            setContentView(R.layout.game_frame);
            MainGameManager.forceReset();
        });
    }

    public void onSubmit(View view)
    {
        // check
        int[] guesses = AnswerSlot.getGuesses();
        int[] result = MainGameManager.makeGuess(guesses);

        if (result != null)
        {
            LinearLayout field = (LinearLayout) RecordBlock.getViewParent();
            View root = view.getRootView();
            if (field == null) {
                field = root.findViewById(R.id.record_view_field);
                RecordBlock.setViewParent(field);
            }

            RecordBlock block = new RecordBlock(field.getContext(), null, 0, guesses, result[0], result[1]);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, block.marginSpacing, 0, block.marginSpacing);
            field.addView(block, lp);

            view.setVisibility(View.INVISIBLE);
            AnswerSlot.reset();

            MainGameManager.refreshTrialIndicatorText(root);
            ((ScrollView) root.findViewById(R.id.scroll)).fullScroll(View.FOCUS_DOWN);
        }
        else
        {
            int title = MainGameManager.isWin() ? R.string.win_statement : R.string.lose_statement;
            showPopup(view, title, -1, R.string.play_again_button_text,
                    null,
                    MainGameManager::forceReset,
                    8,
                    0);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    public void onMenuButtonClicked(View view)
    {
        String tag = (String) view.getTag();
        if (tag.equalsIgnoreCase("hint"))
        {
            showPopup(view, R.string.hint_title, R.string.confirm_button_text, R.string.deny_button_text,
                    MainGameManager::showHint,
                    null,
                    0,
                    0);
        }
        else if (tag.equalsIgnoreCase("menu"))
        {
            showPopup(view, R.string.menu_title, R.string.rest_button_text, R.string.back_button_text,
                    () -> {
                        MainGameManager.forceReset();
                        MainGameManager.refreshTrialIndicatorText(view.getRootView());
                    },
                    null,
                    0,
                    0);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    public void showPopup(View view, int header, int firstBtn, int secBtn, Runnable firstRun, Runnable secRun, int firstVis, int secVis)
    {
        LayoutInflater inflater = getLayoutInflater();
        View popup = inflater.inflate(R.layout.popup, null);
        final PopupWindow popupWindow = new PopupWindow(popup,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                (int) getResources().getDimension(R.dimen.popup_height),
                true
            );
        final TextView title = popup.findViewById(R.id.menu_text);
        final Button firstButton = popup.findViewById(R.id.first_menu_button);
        final Button secondButton = popup.findViewById(R.id.second_menu_button);

        if (header != -1) title.setText(getResources().getText(header));
        if (firstBtn != -1) firstButton.setText(getResources().getText(firstBtn));
        if (secBtn != -1) secondButton.setText(getResources().getText(secBtn));

        firstButton.setVisibility(firstVis);
        secondButton.setVisibility(secVis);

        firstButton.setOnClickListener(view1 -> {
            if (firstRun != null) firstRun.run();
            popupWindow.dismiss();
        });
        secondButton.setOnClickListener(view1 -> {
            if (secRun != null) secRun.run();
            popupWindow.dismiss();
        });

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        popup.setOnTouchListener((v, event) -> {
            popupWindow.dismiss();
            return true;
        });
    }
}
