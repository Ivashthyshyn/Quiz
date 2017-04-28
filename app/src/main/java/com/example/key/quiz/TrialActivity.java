package com.example.key.quiz;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.key.quiz.Fragments.ButtonFragment;
import com.example.key.quiz.Fragments.EditFragment;
import com.example.key.quiz.Fragments.RecyclerFragment;
import com.example.key.quiz.database.Answer;
import com.example.key.quiz.database.AnswerDao;
import com.example.key.quiz.database.DaoSession;
import com.example.key.quiz.database.Question;
import com.example.key.quiz.database.QuestionDao;
import com.example.key.quiz.database.QuizApplication;

import org.greenrobot.greendao.query.Query;

import static com.example.key.quiz.InitialActivity.TYPE_QUESTION_1;
import static com.example.key.quiz.InitialActivity.TYPE_QUESTION_2;
import static com.example.key.quiz.InitialActivity.TYPE_QUESTION_3;


public class TrialActivity extends AppCompatActivity {
    public QuestionDao questionDao;
    public AnswerDao answerDao;
    public TextView textQuestion;
    public FragmentManager fragmentManager;
    public RecyclerFragment fragmentRecycler;
    public ButtonFragment buttonFragment;
    public EditFragment fragmentEdit;
    public Query<Answer> answerQuery;
    private long mQuestionId = 1;
    private long mQuestionType;
    private FragmentTransaction mFragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trial);
        // create daoSession to access the database
        DaoSession daoSession = ((QuizApplication) getApplication()).getDaoSession();
        questionDao = daoSession.getQuestionDao();
        answerDao = daoSession.getAnswerDao();
        textQuestion = (TextView)findViewById(R.id.textQuestion);
        fragmentRecycler = new RecyclerFragment();
        fragmentEdit = new EditFragment();
        buttonFragment = new ButtonFragment();
        fragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            Question question = questionDao.load(mQuestionId);
            mQuestionType = question.getType();
            textQuestion.setText(question.getQuestions());
            answerQuery = answerDao.queryBuilder().where(AnswerDao.Properties.QuestionId.eq(question.getId())).build();
            mFragmentTransaction = fragmentManager.beginTransaction();
            mFragmentTransaction.add(R.id.container, fragmentRecycler);
            mFragmentTransaction.commit();
            fragmentRecycler.loadAnswer(answerQuery);
        }

        Question question = questionDao.load(mQuestionId);
        mQuestionType = question.getType();
        textQuestion.setText(question.getQuestions());


        Button nextButton = (Button)findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mQuestionId < 3) {
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
        mQuestionType = question.getType();
        textQuestion.setText(question.getQuestions());
        answerQuery = answerDao.queryBuilder().where(AnswerDao.Properties.QuestionId
                .eq(question.getId())).build();


        if(mQuestionType == TYPE_QUESTION_1 ){
            mFragmentTransaction = fragmentManager.beginTransaction();
            mFragmentTransaction.replace(R.id.container, fragmentRecycler);
            mFragmentTransaction.commit();
            fragmentRecycler.loadAnswer(answerQuery);
        }else if (mQuestionType == TYPE_QUESTION_2){
            mFragmentTransaction = fragmentManager.beginTransaction();
            mFragmentTransaction.replace(R.id.container, fragmentEdit);
            mFragmentTransaction.commit();
            fragmentEdit.loadAnswer(answerQuery);
        }else if (mQuestionType == TYPE_QUESTION_3){
            mFragmentTransaction = fragmentManager.beginTransaction();
            mFragmentTransaction.replace(R.id.container, buttonFragment);
            mFragmentTransaction.commit();
            buttonFragment.loadAnswer(answerQuery);
        }
    }


}
