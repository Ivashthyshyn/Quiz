package com.example.key.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.key.quiz.database.AnswerDao;
import com.example.key.quiz.database.DaoSession;
import com.example.key.quiz.database.QuestionDao;

public class InitialActivity extends AppCompatActivity {

    private QuestionDao mQuestionDao;
    private AnswerDao mAnswerDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);

        DaoSession daoSession = ((QuizApplication)getApplication()).getDaoSession();
        mQuestionDao = daoSession.getQuestionDao();
        mAnswerDao = daoSession.getAnswerDao();

        //fill database Question
/**
        Question question1 = new Question();
        question1.setId(1L);
        question1.setType(1L);
        question1.setQuestions("Як слід писати слово стоп(кран)?");
        question1.setRightAnswer(3L);
        question1.setCommunicationId(1L);
        mQuestionDao.insert(question1);
        Answer mAnsver1 = new Answer();
        mAnsver1.setId(1L);
        mAnsver1.setAnswers("Разом.");
        mAnsver1.setRemoutCommunicationId(question1.getCommunicationId());
        mAnswerDao.insert(mAnsver1);
        Answer mAnsver2 = new Answer();
        mAnsver2.setId(2L);
        mAnsver2.setAnswers("Окркмо.");
        mAnsver2.setRemoutCommunicationId(question1.getCommunicationId());
        mAnswerDao.insert(mAnsver2);
        Answer mAnsver3 = new Answer();
        mAnsver3.setId(3L);
        mAnsver3.setAnswers("Через дефіс.");
        mAnsver3.setRemoutCommunicationId(question1.getCommunicationId());
        mAnswerDao.insert(mAnsver3);

        Question question2 = new Question();
        question2.setId(2L);
        question2.setType(2L);
        question2.setQuestions("Як створити найкращий додаток у цілому світі");
        question2.setRightAnswer(4L);
        question2.setCommunicationId(2L);
        mQuestionDao.insert(question2);
        Answer mAnsver4 = new Answer();
        mAnsver4.setId(4L);
        mAnsver4.setAnswers("Написати");
        mAnsver4.setRemoutCommunicationId(question2.getCommunicationId());
        mAnswerDao.insert(mAnsver4);

        Question question3 = new Question();
        question3.setId(3L);
        question3.setType(3L);
        question3.setQuestions("Вкажіть неправильно вжите слово");
        question3.setRightAnswer(1L);
        question3.setCommunicationId(3L);
        mQuestionDao.insert(question3);
        Answer mAnsver5 = new Answer();
        mAnsver5.setId(5L);
        mAnsver5.setAnswers("Я незнаю як правильно писати");
        mAnsver5.setRemoutCommunicationId(question3.getCommunicationId());
        mAnswerDao.insert(mAnsver5);
*/





        Button startButton = (Button)findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InitialActivity.this, TrialActivity.class);
                startActivity(intent);
            }
        });
    }
}
