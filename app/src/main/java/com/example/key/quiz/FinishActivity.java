package com.example.key.quiz;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.key.quiz.database.Question;
import com.example.key.quiz.database.QuestionDao;
import com.example.key.quiz.database.UserSuccess;
import com.example.key.quiz.database.UserSuccessDao;

import org.greenrobot.greendao.query.Query;

import static com.example.key.quiz.InitialActivity.DIFFICULTY_LEVEL;
import static com.example.key.quiz.InitialActivity.LEVEL_2;
import static com.example.key.quiz.InitialActivity.PREFS_NAME;

public class FinishActivity extends AppCompatActivity {

    public QuestionDao questionDao;
    public UserSuccessDao userSuccessDao;
    public Query<Question> rightAnswerQuery;
    public Query<UserSuccess> userAnswerQuery;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
/** Need to finish

        String userName = getIntent().getStringExtra("userName");
        long dataQuiz = getIntent().getLongExtra("date",0);
        DaoSession daoSession = ((QuizApplication) getApplication()).getDaoSession();
        questionDao = daoSession.getQuestionDao();
        userSuccessDao = daoSession.getUserSuccessDao();
        userAnswerQuery = userSuccessDao.queryBuilder().where(UserSuccessDao.Properties.UserName.eq(userName),
                UserSuccessDao.Properties.DateAnswer.eq(dataQuiz)).build();

        List<UserSuccess> list = userAnswerQuery.list();
        TextView textView = (TextView)findViewById(R.id.result);
        textView.setText(list.get(1).getUserAnswer().toString());

*/
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        prefs.edit().putInt(DIFFICULTY_LEVEL, LEVEL_2).apply();
    }
}
