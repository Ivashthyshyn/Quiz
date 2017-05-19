package com.example.key.quiz;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.key.quiz.database.DaoSession;
import com.example.key.quiz.database.Question;
import com.example.key.quiz.database.QuestionDao;
import com.example.key.quiz.database.QuizApplication;
import com.example.key.quiz.database.UserSuccess;
import com.example.key.quiz.database.UserSuccessDao;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.greendao.query.Query;

import java.util.List;

import static com.example.key.quiz.InitialActivity.DIFFICULTY_LEVEL;
import static com.example.key.quiz.InitialActivity.LEVEL_2;
import static com.example.key.quiz.InitialActivity.PREFS_NAME;

@EActivity
public class FinishActivity extends Activity implements View.OnClickListener {

    public QuestionDao questionDao;
    public UserSuccessDao userSuccessDao;
    public Query<Question> rightAnswerQuery;
    public Query<UserSuccess> userAnswerQuery;

    @ViewById(R.id.result)
    TextView rightAnswer;

    @ViewById(R.id.resultList)
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_finish);

        String mUserName = getIntent().getStringExtra("userName");
        long mDataQuiz = getIntent().getLongExtra("date",0);
        int mLevel = getIntent().getIntExtra("level",1);
        DaoSession daoSession = ((QuizApplication) getApplication()).getDaoSession();
        questionDao = daoSession.getQuestionDao();
        userSuccessDao = daoSession.getUserSuccessDao();
        rightAnswerQuery = questionDao.queryBuilder().where(QuestionDao.Properties.Level.eq(mLevel))
                .build();
        userAnswerQuery = userSuccessDao.queryBuilder().where(UserSuccessDao.Properties.DateAnswer.
                eq(mDataQuiz)).build();

        List<Question> listRightAnswer = rightAnswerQuery.list();
        List<UserSuccess> listUserAnswer = userAnswerQuery.list();
        rightAnswer.setText(FinishActivity.this.getResources().getString(R.string.results)+" "+mUserName);
        for (int i = 0; i < listUserAnswer.size();i++) {
            Button button = new Button(this);
            button.setOnClickListener(this);
            button.setPadding(10,5,10,5);
            button.setId((listRightAnswer.get(i).getId()).intValue());
            if (listRightAnswer.get(i).getRightAnswer().equals(listUserAnswer.get(i).getUserAnswer())) {
                //TODO need change
                button.setText("№"+ (i+1) +"  Правильно");
                linearLayout.addView(button);
                button.setBackgroundResource(R.drawable.rounded_button);

            } else {
                //TODO need change
                button.setText("№"+ (i+1) +"  Не правильно");
                linearLayout.addView(button);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams
                        (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                button.setBackgroundResource(R.drawable.button_background);
                layoutParams.setMargins(5,5,5,5);
                button.setLayoutParams(layoutParams);
            }

        }

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        prefs.edit().putInt(DIFFICULTY_LEVEL, LEVEL_2).apply();
    }

    @Override
    public void onClick(View button) {
        Question question = questionDao.load((long)button.getId());
        Toast.makeText(getApplication(),question.getQuestions()
                + "Правильна відповідь " + question.getRightAnswer(), Toast.LENGTH_SHORT).show();
    }
}
