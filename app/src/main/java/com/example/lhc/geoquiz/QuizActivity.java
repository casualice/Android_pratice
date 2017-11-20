package com.example.lhc.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.nfc.Tag;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class QuizActivity extends AppCompatActivity {

//    private Question[] mQuestionBank=new Question[]{
//            new Question(R.string.question_anstralia,true),
//            new Question(R.string.question_ocean,true),
//            new Question(R.string.question_mideast,false),
//            new Question(R.string.question_africa,false),
//            new Question(R.string.question_americas,true),
//            new Question(R.string.question_asia,true),
//    };

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mCheatButton;
    private ImageButton mNextButton;
    private ImageButton mPrevButton;
    private TextView mQuestionTextview;
    private TextView mQuestionNum;
    private TextView mTrueNumberMessage;
    private int mCurrentIndex=0;
    //private int mQuestionnum=mQuestionBank.length;
    private int mTrueNumber = 0;
    private boolean mIsCheater;
    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";//设备旋转后保持信息
    private static final String NUM_INDEX = "num_index";//记录正确数信息
    private static final String CLICK_INDEX = "Click_index";
    private boolean clickable = true;//用于在最后一题时防止多次计算正确题数
    private static final int REQUEST_CODE_CHEAT=0;//记录是否作弊
    private ArrayList<Question> mQuestionbank= new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG,"onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);

        if(savedInstanceState!=null){
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX,0);
            mTrueNumber = savedInstanceState.getInt(NUM_INDEX,0);
            clickable = savedInstanceState.getBoolean(CLICK_INDEX,true);
        }

        mQuestionTextview = (TextView)findViewById(R.id.question_text_view);
        mQuestionNum = (TextView)findViewById(R.id.num_message);
        mTrueNumberMessage = (TextView)findViewById(R.id.true_number);
		//获取MainActivity传来的Intent
        if(this.getIntent().getExtras()!=null) {
            Bundle bundle = this.getIntent().getExtras();
            mQuestionbank = (ArrayList<Question>) bundle.getSerializable("QB");
        }

        mCheatButton = (Button)findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                boolean answerIsTrue = mQuestionbank.get(mCurrentIndex).isAnswerTrue();
                Intent intent = CheatActivity.newIntent(QuizActivity.this,answerIsTrue);
                startActivityForResult(intent,REQUEST_CODE_CHEAT);
            }
        });

        mTrueButton = (Button)findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(mCurrentIndex+1==mQuestionbank.size()){
                    checkAnswer(true);
                    mTrueNumberMessage.setText(String.format("你共回答对了%d题",mTrueNumber));
                }
                else {
                    checkAnswer(true);
                }
            }
        });

        mFalseButton=(Button)findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(mCurrentIndex+1==mQuestionbank.size()){
                    checkAnswer(false);
                    mTrueNumberMessage.setText(String.format("你共回答对了%d题",mTrueNumber));
                }
                else {
                    checkAnswer(false);
                }
            }
        });

        //切换至下一题
        mNextButton=(ImageButton)findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mCurrentIndex+1<mQuestionbank.size()){
                    mCurrentIndex = mCurrentIndex+1;
                    mIsCheater = false;
                    updateQuestion();
                }
                if(mCurrentIndex+1==mQuestionbank.size()){
                    mCurrentIndex=mQuestionbank.size()-1;
                }
            }
        });

        //切换至上一题
        mPrevButton=(ImageButton)findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(mCurrentIndex-1>=0){
                    mCurrentIndex = mCurrentIndex-1;
                    updateQuestion();
                }
                else {

                }
            }
        });
        updateQuestion();
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data){
        if(resultCode != Activity.RESULT_OK){
            return;
        }

        if(requestCode == REQUEST_CODE_CHEAT){
            if(data == null){
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG,"onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX,mCurrentIndex);
        savedInstanceState.putInt(NUM_INDEX,mTrueNumber);
        savedInstanceState.putBoolean(CLICK_INDEX,clickable);
    }

    private void updateQuestion(){
		//Question在初始化时有通过String与ID初始化，新增题目中生成的Question是由String初始化的
        if(mQuestionbank.get(mCurrentIndex).getText() == null) {
            int question = mQuestionbank.get(mCurrentIndex).getTextResId();
            mQuestionTextview.setText(question);
            mQuestionNum.setText(String.format("共%d道题，当前第%d题", mQuestionbank.size(), mCurrentIndex + 1));
        }
        else {
            String question = mQuestionbank.get(mCurrentIndex).getText();
            mQuestionTextview.setText(question);
            mQuestionNum.setText(String.format("共%d道题，当前第%d题", mQuestionbank.size(), mCurrentIndex + 1));
        }
    }

    private void checkAnswer(boolean userPressedTrue){
        boolean answerIsTrue = mQuestionbank.get(mCurrentIndex).isAnswerTrue();

        int messageResId = 0;
        if(mIsCheater){
            messageResId=R.string.judgment_toast;
        }else {
            if (userPressedTrue == answerIsTrue) {
                mQuestionbank.get(mCurrentIndex).setIfTrue(true);
                messageResId = R.string.correct_toast;
            } else {
                mQuestionbank.get(mCurrentIndex).setIfTrue(false);
                messageResId = R.string.incorrect_toast;
            }
            Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
            if (mCurrentIndex + 1 == mQuestionbank.size() && clickable == true) {
                for (int i = 0; i < mQuestionbank.size(); i++) {
                    if (mQuestionbank.get(i).getIfTrue() == true) {
                        mTrueNumber = mTrueNumber + 1;
                    }
                }
                clickable = false;
            }
        }
    }
}
