package com.example.key.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.key.quiz.database.DaoSession;
import com.example.key.quiz.database.QuestionDao;
import com.example.key.quiz.database.RepositoryDao;

public class InitialActivity extends AppCompatActivity {

    private QuestionDao mQuestionDao;
    private RepositoryDao mRepositoryDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);

        DaoSession daoSession = ((QuizApplication)getApplication()).getDaoSession();
        mQuestionDao = daoSession.getQuestionDao();
        mRepositoryDao = daoSession.getRepositoryDao();

        //fill database Question
/**
        Question question = new Question();
        question.setContent("Як створити найкращий додаток у цілому світі");
        question.setRemoteId(1L);
        mQuestionDao.insertOrReplace(question);

        List<Repository> repositoryList = question.getRepositories();
        Repository newRepository = new Repository();
        newRepository.setAnswer("Я не впевнений що знаю");
        newRepository.setUserRemoteId(question.getRemoteId());
        mRepositoryDao.insert(newRepository);
        Repository newRepository1 = new Repository();
        newRepository1.setAnswer("Знаю але не скажу");
        newRepository1.setUserRemoteId(question.getRemoteId());
        repositoryList.add(newRepository1);
        mRepositoryDao.insert(newRepository1);


*/
        Button startButton = (Button)findViewById(R.id.button1);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InitialActivity.this, TrialActivity.class);
                startActivity(intent);
            }
        });
    }
}
