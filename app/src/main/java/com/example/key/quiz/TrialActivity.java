package com.example.key.quiz;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.key.quiz.database.Answer;
import com.example.key.quiz.database.AnswerDao;
import com.example.key.quiz.database.DaoSession;
import com.example.key.quiz.database.Question;
import com.example.key.quiz.database.QuestionDao;
import com.example.key.quiz.database.QuizApplication;

import org.greenrobot.greendao.query.Query;


public class TrialActivity extends AppCompatActivity implements Communicator{
    public QuestionDao questionDao;
    public AnswerDao answerDao;
    public TextView textQuestion;
    public FragmentManager fragmentManager;
    public SelectorFragment fragmentButton;
    public Query<Answer> answerQuery;
    private long mQuestionId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trial);
        // create daoSession to access the database
        DaoSession daoSession = ((QuizApplication) getApplication()).getDaoSession();
        questionDao = daoSession.getQuestionDao();
        answerDao = daoSession.getAnswerDao();
        textQuestion = (TextView)findViewById(R.id.textQuestion);
        fragmentManager = getSupportFragmentManager();
        fragmentButton = (SelectorFragment) fragmentManager.findFragmentById(R.id.fragment_button);
        updateFragment();


        Button nextButton = (Button)findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mQuestionId < 10) {
                    mQuestionId = mQuestionId + 1;
                    updateFragment();
                }
            }
        });
        Button backButton = (Button)findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mQuestionId > 1) {
                    mQuestionId = mQuestionId - 1;
                    updateFragment();
                }
            }
        });
    }
    private void updateFragment() {
        Question question = questionDao.load(mQuestionId);
        textQuestion.setText(question.getQuestions());
        answerQuery = answerDao.queryBuilder().where(AnswerDao.Properties.QuestionId
                .eq(question.getId())).build();
            fragmentButton.loadAnswer(answerQuery);
    }


    @Override
    public void processingUserAnswer(String data) {

    }
}
