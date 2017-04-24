package com.example.key.quiz;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.key.quiz.database.QuestionDao;
import com.example.key.quiz.database.Repository;
import com.example.key.quiz.database.RepositoryDao;

import org.greenrobot.greendao.query.Query;


public class TrialActivity extends AppCompatActivity {
    private QuestionDao questionDao;
    private RepositoryDao repositoryDao;
    private Query<Repository> repositoryQuery;
    private RecyclerAdapter recyclerAdapter;
    // identifies the key questions and answers to them
    private long questionKey = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trial);

        Button nextButton = (Button)findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (questionKey < 5) {
                    questionKey = questionKey + 1;
                }
            }
        });

        Button backButton = (Button)findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (questionKey > 1) {
                    questionKey = questionKey - 1;
                }
            }
        });

    }








}
