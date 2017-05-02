package com.example.key.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.key.quiz.database.Answer;
import com.example.key.quiz.database.AnswerDao;
import com.example.key.quiz.database.DaoSession;
import com.example.key.quiz.database.Question;
import com.example.key.quiz.database.QuestionDao;
import com.example.key.quiz.database.QuizApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class InitialActivity extends AppCompatActivity {
    //ToDo we can improve the ability to process issues with different levels of difficulty
    public static final long TYPE_QUESTION_1 = 1;
   // public static final long TYPE_QUESTION_2 = 2;
   // public static final long TYPE_QUESTION_3 = 3;
    private Long mQuestionId;
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

        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(getAssets().open("quiz.txt"),"UTF-8"));

            String mLine;
            while ((mLine = bufferedReader.readLine()) != null){
                if(mLine.contains("?")){
                    Question question = new Question();
                    question.setType(TYPE_QUESTION_1);
                    question.setRightAnswer("");
                    question.setQuestions(mLine.substring(2));
                    questionDao.insert(question);
                    mQuestionId = question.getId();
                }else if (mLine.contains("-")){
                    Answer answer = new Answer();
                    answer.setAnswers(mLine.substring(2));
                    answer.setQuestionId(mQuestionId);
                    answerDao.insert(answer);
                }else if (mLine.contains("+")){
                    Answer answer = new Answer();
                    answer.setAnswers(mLine.substring(2));
                    answer.setQuestionId(mQuestionId);
                    answerDao.insert(answer);
                    Question question = questionDao.load(mQuestionId);
                    question.setRightAnswer(mLine.substring(2));
                    questionDao.insertOrReplace(question);
                }else if(mLine.contains("!")){
                    Question question = questionDao.load(mQuestionId);
                    question.setRightAnswer(mLine.substring(2));
                    questionDao.insertOrReplace(question);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
           if (bufferedReader != null){
               try {
                   bufferedReader.close();
               } catch (IOException e){
                   Toast.makeText(InitialActivity.this, "Введений варіант ",
                           Toast.LENGTH_SHORT).show();
               }
           }
        }

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
