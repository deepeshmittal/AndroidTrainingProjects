package com.bignerdranch.android.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private Button mCheatButton;
    private TextView mQuestionTextView;
    private static final String TAG = "QuizActivity";
    private static final int REQUEST_CODE_CHEAT = 0;
    private boolean mIsCheater;

    private Question[] mQuestionBank = new Question[] {
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };
    private int mCurrentIndex = 0;
    private static final String KEY_INDEX = "index";
    private int mAnsweredQuestion = 0;
    private static final String ANSWERED_COUNT = "count";
    private int[] mQuestionAnswered = new int[mQuestionBank.length];
    private static final String QUESTION_MAP = "questionMap";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mAnsweredQuestion = savedInstanceState.getInt(ANSWERED_COUNT, 0);
            mQuestionAnswered = savedInstanceState.getIntArray(QUESTION_MAP);
        }

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);


        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int question = mQuestionBank[mCurrentIndex].getTestResId();
                mTrueButton.setEnabled(false);
                mFalseButton.setEnabled(false);
                mCheatButton.setEnabled(false);
                mAnsweredQuestion++;
                checkAnswer(true);
                displayResultToast();
            }
        });
        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View v) {
                int question = mQuestionBank[mCurrentIndex].getTestResId();
                mTrueButton.setEnabled(false);
                mFalseButton.setEnabled(false);
                mCheatButton.setEnabled(false);
                mAnsweredQuestion++;
                checkAnswer(false);
                displayResultToast();
            }
        });

        mNextButton = (Button) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                mIsCheater = false;
                updateQuestion();
            }
        });

        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent intent = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
            }
        });
        updateQuestion();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return; }
        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return; }
            mIsCheater = CheatActivity.wasAnswerShown(data);
            if(mIsCheater) {
                mCheatButton.setEnabled(false);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putInt(ANSWERED_COUNT, mAnsweredQuestion);
        savedInstanceState.putIntArray(QUESTION_MAP, mQuestionAnswered);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }
    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTestResId();
        mQuestionTextView.setText(question);

        if (mQuestionAnswered[mCurrentIndex] > 0) {
            mTrueButton.setEnabled(false);
            mFalseButton.setEnabled(false);
            mCheatButton.setEnabled(false);
        } else {
            mTrueButton.setEnabled(true);
            mFalseButton.setEnabled(true);
            mCheatButton.setEnabled(true);
        }
        displayResultToast();
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResId = 0;
        if (mIsCheater) {
            messageResId = R.string.judgment_toast;
            mQuestionAnswered[mCurrentIndex] = 2;
        } else {
            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
                mQuestionAnswered[mCurrentIndex] = 1;
            } else {
                messageResId = R.string.incorrect_toast;
                mQuestionAnswered[mCurrentIndex] = 2;
            }
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
                .show();
    }

    private void displayResultToast() {
        if (mAnsweredQuestion == mQuestionBank.length) {
            int correctAnswer = 0;

            for (int i = 0; i < mQuestionAnswered.length; i++) {
                if (mQuestionAnswered[i] == 1) {
                    correctAnswer++;
                }
            }

            float score = ((float)correctAnswer / mQuestionAnswered.length ) * 100;

            Toast.makeText(this, "Your Score is " + score + "%", Toast.LENGTH_SHORT)
                    .show();
        }
    }
}
