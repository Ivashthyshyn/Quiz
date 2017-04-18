package com.example.key.quiz;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;




public class TrialActivity extends AppCompatActivity {

    private QuestionDao mQuestionDao;
    private RepositoryDao mRepositoryDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trial);
        DaoSession daoSession = ((QuizApplication)getApplication()).getDaoSession();
        mQuestionDao = daoSession.getQuestionDao();
        mRepositoryDao = daoSession.getRepositoryDao();

        //fill database Question

            Question question = new Question();
            question.setContent("Як створити найкращий додаток у цілому світі");
            question.setRemoteId(1L);
            mQuestionDao.insert(question);
            Repository repository = new Repository();
            repository.setAnswer("Я не знаю");
        repository.setUserRemoteId(1L);
            mRepositoryDao.insert(repository);
        TextView questionText = (TextView)findViewById(R.id.question);
        Long questionId = question.getId();
        questionText.setText(question.getContent());
    }
}
