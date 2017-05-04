package com.example.key.quiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.key.quiz.database.Answer;
import com.example.key.quiz.database.AnswerDao;
import com.example.key.quiz.database.DaoSession;
import com.example.key.quiz.database.Question;
import com.example.key.quiz.database.QuestionDao;
import com.example.key.quiz.database.QuizApplication;
import com.example.key.quiz.database.UserSuccess;
import com.example.key.quiz.database.UserSuccessDao;

import org.greenrobot.greendao.query.Query;

import java.util.Calendar;
import java.util.Date;


public class TrialActivity extends AppCompatActivity implements Communicator{
    public QuestionDao questionDao;
    public AnswerDao answerDao;
    public TextView textQuestion;
    public FragmentManager fragmentManager;
    public SelectorFragment fragmentButton;
    public Query<Answer> answerQuery;
    private long mQuestionId = 1;
    private String mRightAnswer;
    public UserSuccessDao userSuccessDao;
    public Long dateInt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trial);
        getDateQuiz();


        // intent use for a userName with InitialActivity
        Intent intent = getIntent();

        // create daoSession to access the database
        DaoSession daoSession = ((QuizApplication) getApplication()).getDaoSession();
        questionDao = daoSession.getQuestionDao();
        answerDao = daoSession.getAnswerDao();
        userSuccessDao = daoSession.getUserSuccessDao();
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

    /**
     * this updates fragment for each new question and loads the correct answer
     */
    private void updateFragment() {
        Question question = questionDao.load(mQuestionId);
        mRightAnswer = question.getRightAnswer();
        textQuestion.setText(question.getQuestions());
        answerQuery = answerDao.queryBuilder().where(AnswerDao.Properties.QuestionId
                .eq(question.getId())).build();
            fragmentButton.loadAnswer(answerQuery);
    }

    /**
     * This handles the user response
     * @param data is a String user answer
     */
    @Override
    public void processingUserAnswer(String data) {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        String userName = preferences.getString("userName","");
        UserSuccess userSuccess = new UserSuccess();
        userSuccess.setUserAnswer(data);
        userSuccess.setUserName(userName);
        userSuccess.setQuestionId(mQuestionId);
        userSuccess.setDateAnswer(dateInt);
        userSuccessDao.insertOrReplace(userSuccess);

        if (mRightAnswer.equals(data)){
               Toast.makeText(TrialActivity.this,"Це правильна відповідь",Toast.LENGTH_SHORT).show();
           }else {
               Toast.makeText(TrialActivity.this, " Ой це не зовсім правильно",Toast.LENGTH_SHORT).show();
           
        }
    }

    public void getDateQuiz() {
        Calendar c = Calendar.getInstance();
        Date date  = c.getTime();
        dateInt = date.getTime();
    }
}
