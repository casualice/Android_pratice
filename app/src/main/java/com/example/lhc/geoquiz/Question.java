package com.example.lhc.geoquiz;


import java.io.Serializable;

public class Question implements Serializable {
    private int mTextResId;
    private String mText;
    private boolean mAnswerTrue;
    private boolean IfTrue = false;

    public Question(int TextResId,boolean anserTrue){
        mTextResId=TextResId;
        mAnswerTrue=anserTrue;
    }

    public Question(String text,boolean anserTrue){
        mText=text;
        mAnswerTrue=anserTrue;
    }

    public String getText(){
        return mText;
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

    public boolean getIfTrue(){
        return IfTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }

    public void setIfTrue(boolean chooseTrue){
        IfTrue = chooseTrue;
    }
}
