package com.example.key.quiz;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.key.quiz.Fragments.ButtonFragment;
import com.example.key.quiz.Fragments.EditFragment;
import com.example.key.quiz.Fragments.RecyclerFragment;
import com.example.key.quiz.database.DaoSession;
import com.example.key.quiz.database.Question;
import com.example.key.quiz.database.QuestionDao;


public class TrialActivity extends AppCompatActivity {
    private QuestionDao questionDao;
    private TextView textQuestion;
    private FragmentManager fragmentManager;
    private RecyclerFragment fragmentRecycler;
    private ButtonFragment buttonFragment;
    private EditFragment fragmentEdit;
    private FrameLayout container;
    FragmentTransaction fragmentTransaction;
    // identifies the key questions and answers to them
    // тимчасова змінна
    private long questionId = 1;
    private long questionType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trial);
        // create daosession to access the database
        DaoSession daoSession = ((QuizApplication) getApplication()).getDaoSession();
        questionDao = daoSession.getQuestionDao();
        container = (FrameLayout)findViewById(R.id.container);

        fragmentRecycler = new RecyclerFragment();
        fragmentEdit = new EditFragment();
        buttonFragment = new ButtonFragment();
        fragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            // при первом запуске программы
            fragmentTransaction = fragmentManager.beginTransaction();
            // добавляем в контейнер при помощи метода add()
            fragmentTransaction.add(R.id.container, fragmentRecycler);
            fragmentTransaction.commit();
        }


        updateFragment();


        Button nextButton = (Button)findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (questionId < 3) {
                    questionId = questionId + 1;
                    updateFragment();
                }
            }
        });

        Button backButton = (Button)findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (questionId > 1) {
                    questionId = questionId - 1;
                    updateFragment();
                }
            }
        });

    }

    private void updateFragment() {
        Question question = questionDao.load(questionId);
        questionType = question.getType();
        textQuestion = (TextView)findViewById(R.id.textQuestion);
        textQuestion.setText(question.getQuestions());


        if(questionType == 1 ){
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, fragmentRecycler);
            fragmentTransaction.commit();
        }else if (questionType == 2){
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, fragmentEdit);
            fragmentTransaction.commit();
        }else if (questionType == 3){
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, buttonFragment);
            fragmentTransaction.commit();
        }
    }


}
