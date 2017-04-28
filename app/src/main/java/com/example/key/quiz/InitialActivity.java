package com.example.key.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.key.quiz.database.AnswerDao;
import com.example.key.quiz.database.DaoSession;
import com.example.key.quiz.database.QuestionDao;
import com.example.key.quiz.database.QuizApplication;


public class InitialActivity extends AppCompatActivity {
    public static final long TYPE_QUESTION_1 = 1;
    public static final long TYPE_QUESTION_2 = 2;
    public static final long TYPE_QUESTION_3 = 3;

    public QuestionDao questionDao;
    public AnswerDao answerDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);
        // created daoSession  to access a database of questions and answers
        DaoSession daoSession = ((QuizApplication)getApplication()).getDaoSession();
        questionDao = daoSession.getQuestionDao();
        answerDao = daoSession.getAnswerDao();

        // ToDo replace downloading resource base method from a text field
        // add question ant answers to database
/**
            Question question1 = new Question();
            question1.setType(TYPE_QUESTION_1);
            question1.setQuestions("Як слід писати слово стоп(кран)?");
            question1.setRightAnswer(3L);
            questionDao.insert(question1);
            Answer answer1 = new Answer();
            answer1.setAnswers("Разом.");
            answer1.setQuestionId(question1.getId());
            answerDao.insert(answer1);
            Answer answer2 = new Answer();
            answer2.setAnswers("Окремо.");
            answer2.setQuestionId(question1.getId());
            answerDao.insert(answer2);
            Answer answer3 = new Answer();
            answer3.setAnswers("Через дефіс.");
            answer3.setQuestionId(question1.getId());
            answerDao.insert(answer3);

            Question question2 = new Question();
            question2.setType(TYPE_QUESTION_2);
            question2.setQuestions("Як створити найкращий додаток у цілому світі");
            question2.setRightAnswer(4L);
            questionDao.insert(question2);
            Answer answer4 = new Answer();
            answer4.setAnswers("Написати");
            answer4.setQuestionId(question2.getId());
            answerDao.insert(answer4);

            Question question3 = new Question();
            question3.setType(TYPE_QUESTION_3);
            question3.setQuestions("Вкажіть неправильно вжите слово");
            question3.setRightAnswer(1L);
            questionDao.insert(question3);
            Answer answer5 = new Answer();
            answer5.setAnswers("Я незнаю як правильно писати");
            answer5.setQuestionId(question3.getId());
            answerDao.insert(answer5);
*/
        Button startButton = (Button)findViewById(R.id.startQuizButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentTrialActivity = new Intent(InitialActivity.this, TrialActivity.class);
                startActivity(intentTrialActivity);
            }
        });
    }
}
