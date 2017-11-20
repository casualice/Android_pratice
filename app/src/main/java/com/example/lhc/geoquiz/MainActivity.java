package com.example.lhc.geoquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button mAnswerButton;
    private Button mAddButton;
    private ArrayList<Question> mQuestionBank = new ArrayList<>();

    public MainActivity(){
        mQuestionBank.add(new Question(R.string.question_anstralia,true));
        mQuestionBank.add(new Question(R.string.question_ocean,true));
        mQuestionBank.add(new Question(R.string.question_mideast,false));
        mQuestionBank.add(new Question(R.string.question_africa,false));
        mQuestionBank.add(new Question(R.string.question_americas,true));
        mQuestionBank.add(new Question(R.string.question_asia,true));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //获取AddActivity传来的Question对象
        if(this.getIntent().getExtras()!=null) {
            Bundle bundle = this.getIntent().getExtras();
            Question Q = (Question) bundle.getSerializable("Q");
            mQuestionBank.add(Q);
        }
		//点击答题按钮，将MainActivity中的问题列表传入QuizActivity
        mAnswerButton =  (Button) findViewById(R.id.answer_button);
        mAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,QuizActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("QB",mQuestionBank);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
		//跳转至新增题目
        mAddButton = (Button)findViewById(R.id.add_button);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,AddActivity.class);
                startActivity(intent);
            }
        });
    }
}
