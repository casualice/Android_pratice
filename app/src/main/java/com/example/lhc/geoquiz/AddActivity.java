package com.example.lhc.geoquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class AddActivity extends AppCompatActivity {

    Button mSubmitButton;
    EditText mEditText;
    RadioGroup mGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        mEditText = (EditText)findViewById(R.id.editText);
        mGroup = (RadioGroup)findViewById(R.id.Answer_choose);
        mSubmitButton = (Button)findViewById(R.id.submit_button);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String question = mEditText.getText().toString();
                boolean answer = true;
				//获取被选择的RadioButton
                for (int i = 0;i<mGroup.getChildCount();i++){
                    RadioButton rb = (RadioButton)mGroup.getChildAt(i);
                    if(rb.isChecked()&&i==0)
                        answer = true;
                    else if (rb.isChecked()&&i==1)
                        answer = false;
                }
				//产生新的Question对象并传递
                Question Q = new Question(question,answer);
                Intent intent = new Intent(AddActivity.this,MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Q",Q);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
