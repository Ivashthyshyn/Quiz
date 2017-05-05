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

import static com.example.key.quiz.InitialActivity.DIFFICULTY_LEVEL;
import static com.example.key.quiz.InitialActivity.PREFS_NAME;
import static com.example.key.quiz.InitialActivity.TYPE_QUESTION_0;
import static com.example.key.quiz.InitialActivity.TYPE_QUESTION_1;
import static com.example.key.quiz.InitialActivity.TYPE_QUESTION_2;
import static com.example.key.quiz.InitialActivity.TYPE_QUESTION_3;


public class TrialActivity extends AppCompatActivity implements Communicator{
    public String userName;
    public QuestionDao questionDao;
    public AnswerDao answerDao;
    public UserSuccessDao userSuccessDao;
    public TextView textQuestion;
    public FragmentManager fragmentManager;
    public SelectorFragment fragmentButton;
    public Query<Answer> answerQuery;
    public Long dateLong;
    private long mQuestionId = 1;
    private int mCVTType = 1;
    private String mRightAnswer;
    private String mData;
    private boolean mAssistantUser = false;
    private int TYPE_QUESTION;
    private  SharedPreferences mPreferences;
    private int mLevelQuiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trial);
        getDateQuiz();
        // get data with InitialActivity
        final Intent intent = getIntent();
        TYPE_QUESTION = intent.getIntExtra("TYPE_QUESTION",0);
        switch ( TYPE_QUESTION){
            case TYPE_QUESTION_0:
                mQuestionId = 1;
                mCVTType = 1;
                mAssistantUser = false;
                TYPE_QUESTION = 1;
                break;
            case TYPE_QUESTION_1:
                mQuestionId = 1;
                mCVTType = 3;
                mAssistantUser = true;
                break;
            case TYPE_QUESTION_2:
                mQuestionId = 2;
                mCVTType = 3;
                mAssistantUser = true;
                break;
            case TYPE_QUESTION_3:
                mQuestionId = 3;
                mCVTType = 3;
                mAssistantUser = true;
                break;
        }
        changeLevelQuiz();
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
                if (mQuestionId < mLevelQuiz) {
                    mQuestionId = mQuestionId + mCVTType;
                    updateFragment();
                }else {
                    Intent intentFinishActivity = new Intent(TrialActivity.this,FinishActivity.class);
                    intentFinishActivity.putExtra("userName",userName);
                    intentFinishActivity.putExtra("date", dateLong);
                    startActivity(intentFinishActivity);
                }
            }
        });
        Button backButton = (Button)findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mQuestionId > TYPE_QUESTION) {
                    mQuestionId = mQuestionId - mCVTType;
                    updateFragment();
                }else {
                    mQuestionId = TYPE_QUESTION;
                    updateFragment();
                }
            }
        });
    }

    /**
     * This get DIFFICULTY_LEVEL in SharedPreferences
     * and sort question in accordance with the level of difficulty
     */
    private void changeLevelQuiz() {
        mPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        mLevelQuiz = mPreferences.getInt(DIFFICULTY_LEVEL,mLevelQuiz);
        mQuestionId = (mLevelQuiz - 10)+ mQuestionId;
    }

    /**
     * This updates fragment for each new question and loads the correct answer
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

        mData = data;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                mPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                userName = mPreferences.getString("userName", "");
                UserSuccess userSuccess = new UserSuccess();
                userSuccess.setId(mQuestionId);
                userSuccess.setUserAnswer(mData);
                userSuccess.setUserName(userName);
                userSuccess.setQuestionId(mQuestionId);
                userSuccess.setDateAnswer(dateLong);
                userSuccessDao.insertOrReplace(userSuccess);
            }
        });
       thread.start();
        // ToDo need create Dialog Assistant and his logic
        if (mRightAnswer.equals(data) & mAssistantUser) {
            Toast.makeText(TrialActivity.this, "Це правильна відповідь", Toast.LENGTH_SHORT).show();
        } else if (mAssistantUser) {
            Toast.makeText(TrialActivity.this, " Ой це не зовсім правильно", Toast.LENGTH_SHORT).show();

        }
    }

    /**
     * This date implementation quiz for saving users results in database
     */
    public void getDateQuiz() {
        Calendar c = Calendar.getInstance();
        Date date  = c.getTime();
        dateLong = date.getTime();
    }



}
