package com.bignerdranch.android.geoquiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    private static final String EXTRA_ANSWER_IS_TRUE = "com.bingnerdranch.android.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN = "com.bingnerdranch.android.geoquiz.answer_shown";
    private static final String EXTRA_COUNT_TIPS = "com.bingnerdranch.android.geoquiz.answer.count_tips";
    private static final String KEY_CHEATING = "key_cheating";
    private static final String KEY_COUNT_TIPS = "count_tips";

    private TextView mAnswerTextView;
    private Button mShowAnswerButton;
    private TextView mCountTipsTextView;

    private int mCountTips;

    private boolean mAnswerIsTrue;
    private boolean mIsCheating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);
        mCountTips = getIntent().getIntExtra(EXTRA_COUNT_TIPS, 3);

        if(savedInstanceState != null)
        {
            mIsCheating = savedInstanceState.getBoolean(KEY_CHEATING);
            mCountTips = savedInstanceState.getInt(KEY_COUNT_TIPS);
        }

        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        mCountTipsTextView = (TextView) findViewById(R.id.api_text_view);
        blockShowAnswer();

        mAnswerTextView = (TextView) findViewById(R.id.answer_text_view);
        mShowAnswerButton = (Button) findViewById(R.id.show_answer_button);
        mShowAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAnswerIsTrue){
                    mAnswerTextView.setText(R.string.true_button);
                    mIsCheating = true;
                }
                else {
                    mAnswerTextView.setText(R.string.false_button);
                    mIsCheating = true;
                }
                mCountTips--;
                setAnswerShownResult();
                blockShowAnswer();
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    int cx = mShowAnswerButton.getWidth() / 2;
                    int cy = mShowAnswerButton.getHeight() / 2;
                    float radius = mShowAnswerButton.getWidth();
                    Animator anim = ViewAnimationUtils.createCircularReveal(mShowAnswerButton, cx, cy, radius, 0);
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            mShowAnswerButton.setVisibility(View.INVISIBLE);
                        }
                    });
                    anim.start();
                }
                else {
                    mShowAnswerButton.setVisibility(View.INVISIBLE);
                }

            }
        });



    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean(KEY_CHEATING, mIsCheating);
        savedInstanceState.putInt(KEY_COUNT_TIPS, mCountTips);
    }

    public static Intent newIntent(Context packageContext, boolean answerIsTrue, int countTips){
        Intent intent = new Intent(packageContext, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        intent.putExtra(EXTRA_COUNT_TIPS, countTips);
        return intent;
    }

    public static boolean wasAnswerShown(Intent result){

        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }

    public static int getCountTips(Intent result){

        return result.getIntExtra(EXTRA_COUNT_TIPS, 3);
    }

    private void setAnswerShownResult(){
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, mIsCheating);
        data.putExtra(EXTRA_COUNT_TIPS, mCountTips);
        setResult(RESULT_OK, data);
    }

    private void blockShowAnswer(){
        if(mCountTips <= 0){
            mShowAnswerButton.setVisibility(View.GONE);
        }
        mCountTipsTextView.setText(String.format("Count of tips: %d", mCountTips));
    }
}
