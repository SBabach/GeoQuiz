package com.babach.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.nio.file.Files;

public class QuizActivity extends AppCompatActivity {

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private Button mPreviousButton;
    private Button mCheatButton;
    private TextView mRemainingCheats;
    private ImageButton mNextImgButton;
    private ImageButton mPreviousImgButton;
    private int mCheatTokens = 3;
    private static final String KEY_INDEX = "index";
    private static final int REQUEST_CODE_CHEAT = 0;

    private TextView mQuestionTextView;

    private Question[] mQuestions = new Question[]
            {
                    new Question(R.string.question_sofia, true),
                    new Question(R.string.question_oceans, true),
                    new Question(R.string.question_mideast, false),
                    new Question(R.string.question_africa, false),
                    new Question(R.string.question_americas, true),
                    new Question(R.string.question_asia, true),
            };

    private int mCurrentIndex = 0;
    private boolean mIsCheater;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(QuizActivity.class.getSimpleName(), "OnCreate(Bundle) called");

        setContentView(R.layout.activity_quiz);

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
        }

        mQuestionTextView = findViewById(R.id.question_text_view);

        setTitle(R.string.app_name);
        mTrueButton = (Button) findViewById(R.id.true_button);
        mFalseButton = (Button) findViewById(R.id.false_button);
        mNextButton = findViewById(R.id.next_button);
        mPreviousButton = findViewById(R.id.previous_button);
        mCheatButton = findViewById(R.id.cheat_button);
        mRemainingCheats = findViewById(R.id.remaining_cheats);
        mRemainingCheats.setText(String.format("(%d)", mCheatTokens));


        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestions.length;
                updateQuestion();
            }
        });

        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
                mCurrentIndex = (mCurrentIndex + 1) % mQuestions.length;
                updateQuestion();
            }
        });

        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
                mCurrentIndex = (mCurrentIndex + 1) % mQuestions.length;
                updateQuestion();
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestions.length;
                mIsCheater = false;
                updateQuestion();
            }
        });

        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex - 1) % mQuestions.length;
                updateQuestion();
            }
        });


        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean answerIsTrue = mQuestions[mCurrentIndex].isAnswerTrue();
                Intent intent = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
            }
        });


        updateQuestion();
    }


    private void updateQuestion() {
        Question nextQuestion = mQuestions[mCurrentIndex];
        int question = nextQuestion.getTextResId();
        mQuestionTextView.setText(question);
        boolean enableButtons = !(nextQuestion.getUserAnswer() != null);
        mTrueButton.setClickable(enableButtons);
        mFalseButton.setClickable(enableButtons);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(QuizActivity.class.getSimpleName(), "onSaveInstanceState() called");
        outState.putInt(KEY_INDEX, mCurrentIndex);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
            if(mIsCheater &&(--mCheatTokens)==0)
            {
                mCheatButton.setVisibility(View.INVISIBLE);
            }
            mRemainingCheats.setText(String.format("(%d)", mCheatTokens));
        }
    }


    private void checkAnswer(boolean userPressedTrue) {

        if(mIsCheater)
        {
            Toast.makeText(this, "You Cheated!", Toast.LENGTH_SHORT).show();
            return;
        }
        mQuestions[mCurrentIndex].setUserAnswer(userPressedTrue);

        boolean allAnswered = true;
        for (int i = 0; i < mQuestions.length; i++) {
            allAnswered &= mQuestions[i].getUserAnswer() != null;
        }

        if (allAnswered) {
            int correctAnswers = 0;
            for (int i = 0; i < mQuestions.length; i++) {
                Question question = mQuestions[i];
                correctAnswers = question.getUserAnswer().booleanValue() == question.isAnswerTrue() ? ++correctAnswers : correctAnswers;
                Log.i(QuizActivity.class.getSimpleName(), "Question N" + i + "user answer:" + question.getUserAnswer() + "| correct answer:" + question.isAnswerTrue());
            }

            double successRate = ((double) correctAnswers / (double) mQuestions.length) * 100;
            String message = "Your result is " + successRate + "%";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(QuizActivity.class.getSimpleName(), "OnStart() called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(QuizActivity.class.getSimpleName(), "OnStop() called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(QuizActivity.class.getSimpleName(), "OnDestroy() called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(QuizActivity.class.getSimpleName(), "OnPause() called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(QuizActivity.class.getSimpleName(), "OnResume() called");
    }
}
