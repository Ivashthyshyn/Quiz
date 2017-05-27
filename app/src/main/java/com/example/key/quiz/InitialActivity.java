package com.example.key.quiz;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.key.quiz.database.Answer;
import com.example.key.quiz.database.AnswerDao;
import com.example.key.quiz.database.DaoSession;
import com.example.key.quiz.database.Question;
import com.example.key.quiz.database.QuestionDao;
import com.example.key.quiz.database.QuizApplication;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.LongClick;
import org.androidannotations.annotations.ViewById;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;


@EActivity
public class InitialActivity extends AppCompatActivity {

    public static final  String PREFS_NAME = "MyPrefsFile";
    public static final int TYPE_QUESTION_0 = 0;
    public static final int TYPE_QUESTION_1 = 1;
    public static final int TYPE_QUESTION_2 = 2;
    public static final int TYPE_QUESTION_3 = 3;
    public static final int INT_POSITION_TYPE_QUESTION = 1;
    public static final char CHAR_MARKER_TYPE_QUESTION = '?';
    public static final int INT_POSITION_LEVEL_QUESTION = 1;
    public static final char CHAR_MARKER_LEVEL_QUESTION = 'L';
    public static final int WORK_POSITION = 2;
    public static final String DIFFICULTY_LEVEL = "difficulty_level";
    public static final int LEVEL_1 = 1;
    public static final int LEVEL_2 = 2;
    public static final int LEVEL_3 = 3;
    public static final int LEVEL_4 = 4;
    public static final int LEVEL_5 = 5;
    public static final int LEVEL_6 = 6;
    public static final int LEVEL_7 = 7;
    public static final int LEVEL_8 = 8;
    public static final int LEVEL_9 = 9;
    public static final int LEVEL_10 = 10;
    public final String PREF_VERSION_CODE_KEY = "version_code";
    public final int DOESNT_EXIST = -1;
    public int currentVersionCode = BuildConfig.VERSION_CODE;
    public QuestionDao questionDao;
    public AnswerDao answerDao;
    public SharedPreferences prefs;
    public  AlertDialog.Builder builder;
    public DialogFragment dialogFragment;
    private Context mContext = InitialActivity.this;
    private Long mQuestionId;
    private AlertDialog mAlert;
    private  EditText userNameInput;

    @ViewById(R.id.assistantImage)
    ImageView assistantImage;
    FragmentManager fragmentManager;


    @ViewById(R.id.button_training)
    Button buttonTraining;

    @ViewById(R.id.startQuizButton)
    Button startQuizButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_initial);
        checkFirstRun();


    }

   @Click(R.id.startQuizButton)
   void startQuizButtonWasClicked(){
       goToTrialActivity(TYPE_QUESTION_0);
   }


    private void goToTrialActivity(int TYPE_QUESTION) {
        Intent intentTrialActivity = new Intent(InitialActivity.this, TrialActivity_.class);
        intentTrialActivity.putExtra("TYPE_QUESTION",TYPE_QUESTION);
        startActivity(intentTrialActivity);
    }

    @Click(R.id.button_training)
    void trainingButtonWasClicked() {
        builder = new AlertDialog.Builder(InitialActivity.this);
        LinearLayout customDialog = (LinearLayout) getLayoutInflater()
                .inflate(R.layout.custom_dialog, (LinearLayout)findViewById(R.id.custom_select_dialog));
        builder.setView(customDialog);
        Button buttonOrthography = (Button)customDialog.findViewById(R.id.buttonOrthography);
        buttonOrthography.setText(mContext.getResources().getString(R.string.orthography));
        buttonOrthography.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToTrialActivity(TYPE_QUESTION_1);
                mAlert.cancel();
            }
        });
        Button buttonPurity = (Button)customDialog.findViewById(R.id.buttonPurity);
        buttonPurity.setText(mContext.getResources().getString(R.string.purity_of_language));
        buttonPurity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToTrialActivity(TYPE_QUESTION_2);
                mAlert.cancel();
            }
        });
        Button buttonLoanwords = (Button)customDialog.findViewById(R.id.buttonLoanwords);
        buttonLoanwords.setText(mContext.getResources().getString(R.string.loanwords));
        buttonLoanwords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToTrialActivity(TYPE_QUESTION_3);
                mAlert.cancel();
            }
        });
        mAlert = builder.create();
        mAlert.show();
    }

    @Background
    void loadingThread(){
        DaoSession daoSession = ((QuizApplication)getApplication()).getDaoSession();
        questionDao = daoSession.getQuestionDao();
        answerDao = daoSession.getAnswerDao();

        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(getAssets().open("quiz.txt"),"UTF-8"));

            String mLine;
            while ((mLine = bufferedReader.readLine()) != null){
                if(mLine.startsWith("?")){
                    Question question = new Question();
                    question.setType(Character.getNumericValue
                            (mLine.charAt(mLine.indexOf(CHAR_MARKER_TYPE_QUESTION) + INT_POSITION_TYPE_QUESTION)));
                    question.setLevel(Character.getNumericValue
                            (mLine.charAt(mLine.indexOf(CHAR_MARKER_LEVEL_QUESTION) + INT_POSITION_LEVEL_QUESTION)));
                    question.setRightAnswer("");
                    question.setQuestions(mLine.substring(mLine.indexOf(CHAR_MARKER_LEVEL_QUESTION) + WORK_POSITION));
                    questionDao.insert(question);
                    mQuestionId = question.getId();
                }else if (mLine.startsWith("-")){
                    Answer answer = new Answer();
                    answer.setAnswers(mLine.substring(2));
                    answer.setQuestionId(mQuestionId);
                    answerDao.insert(answer);
                }else if (mLine.startsWith("+")){
                    Answer answer = new Answer();
                    answer.setAnswers(mLine.substring(2));
                    answer.setQuestionId(mQuestionId);
                    answerDao.insert(answer);
                    Question question = questionDao.load(mQuestionId);
                    question.setRightAnswer(mLine.substring(2));
                    questionDao.insertOrReplace(question);
                }else if(mLine.startsWith("!")){
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

        prefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).apply();
    }

    /**
     * This checks the application on first launch and logic to determine various options
     */
    private void checkFirstRun() {

        // Get saved version code
        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int savedVersionCode = prefs.getInt(PREF_VERSION_CODE_KEY, DOESNT_EXIST);

        // Check for first run or upgrade
        if (currentVersionCode == savedVersionCode) {

            // This is just a normal run
            // Update the shared preferences with the current version code
            prefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).apply();
            goAnimation();

        } else if (savedVersionCode == DOESNT_EXIST) {
            // This a thread for load quiz.txt to quiz.assistantText
            loadingThread();
            // creating dialogue for user input his name
           showStartDialog();


        // TODO This is an upgrade
        //} else if (currentVersionCode > savedVersionCode) {
        }

    }

    private void goAnimation() {
        assistantImage.setVisibility(View.VISIBLE);
        buttonTraining.setVisibility(View.VISIBLE);
        startQuizButton.setVisibility(View.VISIBLE);
        Animation button1Visible = AnimationUtils.loadAnimation(this, R.anim.button_tread_anim);
        buttonTraining.startAnimation(button1Visible);
        Animation button2Visible = AnimationUtils.loadAnimation(this, R.anim.button_start_anim);
        startQuizButton.startAnimation(button2Visible);


    }


    @Click(R.id.assistantImage)
    void assistantWasClicked(){
        showAssistantDialog(mContext.getResources().getString(R.string.settings_question));
    }

    private void showAssistantDialog(String textDialog) {
        fragmentManager = getSupportFragmentManager();
        if(dialogFragment != null) {
            fragmentManager.beginTransaction().remove(dialogFragment).commit();
        }
        dialogFragment = new DialogFragment_();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        fragmentTransaction.add(R.id.container, dialogFragment);
        fragmentTransaction.commit();
        dialogFragment.setAssistantTalk(textDialog);

        autoOff(dialogFragment);
    }

    @Background(delay=2000)
    void autoOff(DialogFragment dialogFragment) {
       fragmentManager.beginTransaction().remove(dialogFragment).commit();
    }

    @LongClick(R.id.assistantImage)
    void assistantWasLongClicked() {
        builder = new AlertDialog.Builder(InitialActivity.this);
        LinearLayout customSettingsDialog = (LinearLayout) getLayoutInflater()
                .inflate(R.layout.custom_settings_dialog, (LinearLayout) findViewById(R.id.custom_settings));
        builder.setView(customSettingsDialog);
        Button buttonNewUser = (Button) customSettingsDialog.findViewById(R.id.button_new_user);
        buttonNewUser.setText(mContext.getResources().getString(R.string.orthography));
        buttonNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStartDialog();
                mAlert.cancel();
            }
        });
        Button buttonChangeUser = (Button) customSettingsDialog.findViewById(R.id.button_change_user);
        buttonChangeUser.setText(mContext.getResources().getString(R.string.purity_of_language));
        buttonChangeUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAlert.cancel();
            }
        });
        Button buttonCklear = (Button) customSettingsDialog.findViewById(R.id.button_cklear);
        buttonCklear.setText(mContext.getResources().getString(R.string.loanwords));
        buttonCklear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToTrialActivity(TYPE_QUESTION_3);
                mAlert.cancel();
            }
        });

        mAlert = builder.create();
        mAlert.show();
    }

    //   SettingsFragment settingsFragment = new SettingsFragment();
     //  getFragmentManager().beginTransaction().add(R.id.container, settingsFragment).commit();

        void showStartDialog(){
        builder = new AlertDialog.Builder(this);
        userNameInput = new EditText(this);
        userNameInput.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        View customTitle = getLayoutInflater().inflate(R.layout.custom_title,(LinearLayout)findViewById(R.id.customTitle));
        builder.setCustomTitle(customTitle);
        builder.setView(userNameInput);
        builder.setPositiveButton(mContext.getResources().getString(R.string.ready),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Set<String> user_name = new HashSet<String>();
                        user_name.add(userNameInput.getText().toString());
                        user_name.add(",e,e");
                        user_name.add("lfkd");
                        prefs.edit().putStringSet("userName1",user_name).apply();
                        prefs.edit().putString("userName", userNameInput.getText().toString()).apply();
                        prefs.edit().putInt(DIFFICULTY_LEVEL, LEVEL_1).apply();
                        dialog.cancel();
                        assistantImage.setVisibility(View.VISIBLE);
                        Animation korovkaAnim = AnimationUtils.loadAnimation(mContext, R.anim.button_tread_anim);
                        assistantImage.startAnimation(korovkaAnim);
                        goAnimation();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }



}
