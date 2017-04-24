package com.example.key.quiz;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.key.quiz.database.DaoSession;
import com.example.key.quiz.database.QuestionDao;


public class TrialActivity extends AppCompatActivity {
    private QuestionDao questionDao;
    private TextView textQuestion;
    private FragmentManager fragmentManager;
    private FragmentRecycler fragmentRecycler;
    private FragmentEdit fragmentEdit;
    private FrameLayout container;
    // identifies the key questions and answers to them
    // тимчасова змінна
    private long questionKey = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trial);
        // create daosession to access the database
        DaoSession daoSession = ((QuizApplication) getApplication()).getDaoSession();
        questionDao = daoSession.getQuestionDao();
        // container fragments
        container = (FrameLayout)findViewById(R.id.container);
        textQuestion = (TextView)findViewById(R.id.textQuestion);
        fragmentRecycler = new FragmentRecycler();
        fragmentEdit = new FragmentEdit();
        fragmentManager = getSupportFragmentManager();


            FragmentTransaction fragmentTransaction = fragmentManager
                    .beginTransaction();
            fragmentTransaction.add(R.id.container, fragmentRecycler);
            fragmentTransaction.commit();


        Button nextButton = (Button)findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (questionKey < 5) {
                    questionKey = questionKey + 1;
                    FragmentTransaction fragmentTransaction = fragmentManager
                            .beginTransaction();
                    fragmentTransaction.replace(R.id.container, fragmentRecycler);
                    fragmentTransaction.commit();
                }
            }
        });

        Button backButton = (Button)findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (questionKey > 1) {
                    questionKey = questionKey - 1;
                    FragmentTransaction fragmentTransaction = fragmentManager
                            .beginTransaction();
                    fragmentTransaction.replace(R.id.container, fragmentEdit);
                    fragmentTransaction.commit();
                }
            }
        });

    }








}
