package com.babach.geoquiz;

/**
 * Created by sbaba on 20-Oct-18.
 */

public class Question {
    private int mTextResId;
    private boolean mAnswerTrue;
    private Boolean userAnswer;

    public Question(int mTextResId, boolean mAnswerTrue) {
        this.mTextResId = mTextResId;
        this.mAnswerTrue = mAnswerTrue;
    }

    public int getTextResId() {
        return mTextResId;
    }

    public void setTextResId(int textResId) {
        mTextResId = textResId;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }

    public void setUserAnswer(boolean userAnswerParam) {
        this.userAnswer = userAnswerParam;
    }

    public Boolean getUserAnswer() {
        return userAnswer;
    }
}
