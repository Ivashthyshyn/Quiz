package com.example.key.quiz;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    public static final  String PREFS_NAME = "MyPrefsFile";
    public static final int TYPE_QUESTION_0 = 0;
    public static final int TYPE_QUESTION_1 = 1;
    public static final int TYPE_QUESTION_2 = 2;
    public static final int TYPE_QUESTION_3 = 3;
    public static final String DIFFICULTY_LEVEL = "difficulty_level";
    public static final int LEVEL_1 = 10;
    public static final int LEVEL_2 = 20;
    public QuestionDao questionDao;
    public AnswerDao answerDao;
    public SharedPreferences prefs;
    public  AlertDialog.Builder builder;
    private Context mContext = InitialActivity.this;
    private Long mQuestionId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);
        Button trainingButton = (Button)findViewById(R.id.button_training);
        trainingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] mItemsName ={mContext.getResources().getString(R.string.orthography),
                        mContext.getResources().getString(R.string.purity_of_language),
                        mContext.getResources().getString(R.string.loanwords)};
                builder = new AlertDialog.Builder(InitialActivity.this);
                builder.setTitle( mContext.getResources().getString(R.string.question_of_section));
                builder.setItems(mItemsName, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                       switch (item){
                           case 0:
                               goToTrialActivity(TYPE_QUESTION_1);
                               dialog.cancel();
                               break;
                           case 1:
                               goToTrialActivity(TYPE_QUESTION_2);
                               dialog.cancel();
                               break;
                           case 2:
                               goToTrialActivity(TYPE_QUESTION_3);
                               dialog.cancel();
                               break;
                       }
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });
        checkFirstRun();
        Button startButton = (Button)findViewById(R.id.startQuizButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToTrialActivity(TYPE_QUESTION_0);
            }
        });
    }

    private void goToTrialActivity(int TYPE_QUESTION) {
        Intent intentTrialActivity = new Intent(InitialActivity.this, TrialActivity.class);
        intentTrialActivity.putExtra("TYPE_QUESTION",TYPE_QUESTION);
        startActivity(intentTrialActivity);
    }

    /**
     * This checks the application on first launch and logic to determine various options
     */
    private void checkFirstRun() {

        final String PREF_VERSION_CODE_KEY = "version_code";
        final int DOESNT_EXIST = -1;

        // Get current version code
        int currentVersionCode = BuildConfig.VERSION_CODE;

        // Get saved version code
        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int savedVersionCode = prefs.getInt(PREF_VERSION_CODE_KEY, DOESNT_EXIST);

        // Check for first run or upgrade
        if (currentVersionCode == savedVersionCode) {

            // This is just a normal run
            return;

        } else if (savedVersionCode == DOESNT_EXIST) {
            // This a thread for load quiz.txt to quiz.db
            Thread loadingThread = new Thread(new Runnable() {
                @Override
                public void run() {
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
                                Toast.makeText(InitialActivity.this, mContext.getResources().getString(R.string.warning_masage),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            });
            loadingThread.start();
            // creating dialogue for user input his name
            builder = new AlertDialog.Builder(this);
            final EditText userNameInput = new EditText(this);

            builder.setMessage(mContext.getResources().getString(R.string.assistant_heloo));
            builder.setView(userNameInput);
            builder.setPositiveButton(mContext.getResources().getString(R.string.ready),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            prefs.edit().putString("userName", userNameInput.getText().toString()).apply();
                            prefs.edit().putInt(DIFFICULTY_LEVEL, LEVEL_1).apply();
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
            // TODO This is an upgrade
        //} else if (currentVersionCode > savedVersionCode) {


        }

        // Update the shared preferences with the current version code
        prefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).apply();
    }
}
