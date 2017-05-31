package com.example.key.quiz;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.key.quiz.database.DaoSession;
import com.example.key.quiz.database.Question;
import com.example.key.quiz.database.QuestionDao;
import com.example.key.quiz.database.QuizApplication;
import com.example.key.quiz.database.UserSuccess;
import com.example.key.quiz.database.UserSuccessDao;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.greendao.query.Query;

import java.util.List;

import static com.example.key.quiz.InitialActivity.DIFFICULTY_LEVEL;
import static com.example.key.quiz.InitialActivity.PREFS_NAME;
import static com.example.key.quiz.TrialActivity.INTENT_DATE_VALUE;
import static com.example.key.quiz.TrialActivity.INTENT_LEVEL_VALUE;

@EActivity
public class FinishActivity extends Activity implements View.OnClickListener {

    public QuestionDao questionDao;
    public UserSuccessDao userSuccessDao;
    public Query<Question> rightAnswerQuery;
    public Query<UserSuccess> userAnswerQuery;
    public AlertDialog finishAlertDialog;
    private int mCounterWrongAnswers = 0;

    private int mLevel;

    @ViewById(R.id.resultList)
    LinearLayout linearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_finish);

        //String mUserName = getIntent().getStringExtra(INTENT_NAME_VALUE);

        long mDataQuiz = getIntent().getLongExtra(INTENT_DATE_VALUE, 0);
        mLevel = getIntent().getIntExtra(INTENT_LEVEL_VALUE, 1);
        DaoSession daoSession = ((QuizApplication) getApplication()).getDaoSession();
        questionDao = daoSession.getQuestionDao();
        userSuccessDao = daoSession.getUserSuccessDao();
        rightAnswerQuery = questionDao.queryBuilder().where(QuestionDao.Properties.Level.eq(mLevel))
                .build();
        userAnswerQuery = userSuccessDao.queryBuilder().where(UserSuccessDao.Properties.DateAnswer.
                eq(mDataQuiz)).build();

        List<Question> listRightAnswer = rightAnswerQuery.list();
        List<UserSuccess> listUserAnswer = userAnswerQuery.list();
        for (int i = 0; i < listUserAnswer.size();i++) {
            Button button = new Button(this);
            button.setOnClickListener(this);
            button.setPadding(10,5,10,5);
            button.setId((listRightAnswer.get(i).getId()).intValue());
            if ( listUserAnswer.get(i).getUserAnswer().toLowerCase().equals(listRightAnswer.get(i)
                    .getRightAnswer().toLowerCase())) {
                button.setText("№"+ (i+1) + "  " + getApplication().getResources().getString(R.string.right));
                linearLayout.addView(button);
                button.setBackgroundResource(R.drawable.rounded_button);

            } else {
                mCounterWrongAnswers++;
                button.setText("№"+ (i+1) + "  " + getApplication().getResources().getString(R.string.not_right));
                linearLayout.addView(button);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams
                        (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                button.setBackgroundResource(R.drawable.button_background);
                layoutParams.setMargins(5,5,5,5);
                button.setLayoutParams(layoutParams);
            }

        }
        if (savedInstanceState == null) {
            showFinishAlertDialog();
        }
    }

    private void showFinishAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(FinishActivity.this);
        LinearLayout customDialog = (LinearLayout) getLayoutInflater()
                .inflate(R.layout.custom_finish_dialog, (LinearLayout)findViewById(R.id.custom_finish_dialog_root));
        TextView textGreeting = (TextView)customDialog.findViewById(R.id.textGreetings);

        ImageView medal = (ImageView)customDialog.findViewById(R.id.medal);
        medal.setBackgroundResource(R.drawable.ic_medal);
        Button buttonOk = (Button)customDialog.findViewById(R.id.buttonOk);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMainActivity();
            }
        });
        Button buttonViewResult = (Button)customDialog.findViewById(R.id.buttonViewResult);
        buttonViewResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finishAlertDialog.cancel();
            }
        });
        if(mCounterWrongAnswers == 0) {
            textGreeting.setText(getApplication().getResources().getString(R.string.best_resolt));
        }else if (mCounterWrongAnswers == 1){
            textGreeting.setText(getApplication().getResources().getString(R.string.one_error));
        }else if (mCounterWrongAnswers == 2){
            textGreeting.setText(getApplication().getResources().getString(R.string.two_errors));
        }else {
            textGreeting.setText(getApplication().getResources().getString(R.string.more_errors));
        }
        builder.setView(customDialog);
        finishAlertDialog = builder.create();
        finishAlertDialog.show();
    }

    private void goToMainActivity() {
        if (mLevel != 9) {
            SharedPreferences mPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            mPrefs.edit().putInt(DIFFICULTY_LEVEL, mLevel + 1).apply();
            Intent intentInitial = new Intent(FinishActivity.this, InitialActivity_.class);
            startActivity(intentInitial);
        }
    }

    @Override
    public void onClick(View button) {
        Question question = questionDao.load((long)button.getId());
        Toast.makeText(getApplication(),question.getQuestions()
                + "Правильна відповідь " + question.getRightAnswer(), Toast.LENGTH_LONG).show();
    }

    @Click(R.id.button_back_to_main_activity)
    public void floatingButtonWasClicked(){
        goToMainActivity();
    }
}
