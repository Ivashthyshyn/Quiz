package com.example.key.quiz;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;


public class TrialActivity extends AppCompatActivity {

    private QuestionDao mQuestionDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trial);
        DaoSession daoSession = ((QuizApplication)getApplication()).getDaoSession();
        mQuestionDao = daoSession.getQuestionDao();
        //fill database Question
        for (int i = 0; i < 10; i++) {
            Question question = new Question();
            question.setContent("Як створити найкращий додаток у цілому світі");
            mQuestionDao.insert(question);

        }
        TextView questionText = (TextView)findViewById(R.id.question);

    }
}
