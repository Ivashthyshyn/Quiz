package com.example.key.quiz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.key.quiz.database.Answer;
import com.example.key.quiz.database.AnswerDao;
import com.example.key.quiz.database.DaoSession;
import com.example.key.quiz.database.Question;
import com.example.key.quiz.database.QuestionDao;
import com.example.key.quiz.database.QuizApplication;
import com.example.key.quiz.database.UserSuccess;
import com.example.key.quiz.database.UserSuccessDao;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.greendao.query.Query;

import java.util.Calendar;
import java.util.Date;

import static com.example.key.quiz.InitialActivity.DIFFICULTY_LEVEL;
import static com.example.key.quiz.InitialActivity.LEVEL_1;
import static com.example.key.quiz.InitialActivity.LEVEL_10;
import static com.example.key.quiz.InitialActivity.LEVEL_2;
import static com.example.key.quiz.InitialActivity.LEVEL_3;
import static com.example.key.quiz.InitialActivity.LEVEL_4;
import static com.example.key.quiz.InitialActivity.LEVEL_5;
import static com.example.key.quiz.InitialActivity.LEVEL_6;
import static com.example.key.quiz.InitialActivity.LEVEL_7;
import static com.example.key.quiz.InitialActivity.LEVEL_8;
import static com.example.key.quiz.InitialActivity.LEVEL_9;
import static com.example.key.quiz.InitialActivity.PREFS_NAME;
import static com.example.key.quiz.InitialActivity.TYPE_QUESTION;
import static com.example.key.quiz.InitialActivity.TYPE_QUESTION_0;
import static com.example.key.quiz.InitialActivity.TYPE_QUESTION_1;
import static com.example.key.quiz.InitialActivity.TYPE_QUESTION_2;
import static com.example.key.quiz.InitialActivity.TYPE_QUESTION_3;

@EActivity
public class TrialActivity extends AppCompatActivity implements Communicator{
    private static final String SAVE_QUESTION_NUMBER = "questionNumber";
    private static final String SAVE_USER_ANSWER = "userAnswer";
    private static final String SAVE_DATE_QUIZ = "dateQuiz";
    public static final String INTENT_NAME_VALUE = "userNameInput";
    public static final String INTENT_LEVEL_VALUE = "level";
    public static final String INTENT_DATE_VALUE = "date";
    public String userName;
    public QuestionDao questionDao;
    public AnswerDao answerDao;
    public UserSuccessDao userSuccessDao;
    public AssistantDialogFragment dialogFragment;
    public FragmentManager fragmentManager;
    public SelectorFragment selectorFragment;
    public Query<Answer> answerQuery;
    public Long dateLong;
    private int mQuestionNumber = 0;
    private int mNumberOfQuestion = 1;
    private String mRightAnswer;
    private String mData;
    private boolean mAssistantUser = false;
    private  SharedPreferences mPreferences;
    private int mLevelQuiz;
    private Query<Question> mQueryQuestion;
    private Long mQuestionId;
    private int mTypeQuestion;
    private Context mTrialContext = TrialActivity.this;
    private int mCounterTips = 0;
    private Bundle mSavedInstanceState = null;
    @ViewById (R.id.next_button)
    Button nextButton;

    @ViewById(R.id.back_button)
    Button beckButton;

    @ViewById(R.id.textQuestion)
    TextView textQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSavedInstanceState = savedInstanceState;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_trial);
        if (savedInstanceState != null) {
            mQuestionNumber = savedInstanceState.getInt(SAVE_QUESTION_NUMBER, 0);
            mData = savedInstanceState.getString(SAVE_USER_ANSWER, "");
            dateLong = savedInstanceState.getLong(SAVE_DATE_QUIZ, 0);
        }else {
            getDateQuiz();
        }
        DaoSession daoSession = ((QuizApplication) getApplication()).getDaoSession();
        questionDao = daoSession.getQuestionDao();
        answerDao = daoSession.getAnswerDao();
        userSuccessDao = daoSession.getUserSuccessDao();
        fragmentManager = getSupportFragmentManager();
        selectorFragment = (SelectorFragment) fragmentManager.findFragmentById(R.id.fragment_selector);

        // get data with InitialActivity
        mTypeQuestion = getIntent().getIntExtra(TYPE_QUESTION,0);
        switch (mTypeQuestion){
            case TYPE_QUESTION_0:
                changeLevelQuiz();
               mQueryQuestion = questionDao.queryBuilder()
                       .where(QuestionDao.Properties.Level.eq(mLevelQuiz)).build();
                nextButton.setVisibility(View.INVISIBLE);
                beckButton.setVisibility(View.INVISIBLE);
                updateFragment();
                break;
            case TYPE_QUESTION_1:
                mQueryQuestion = questionDao.queryBuilder()
                        .where(QuestionDao.Properties.Type.eq(TYPE_QUESTION_1)).build();
                mAssistantUser = true;
                nextButton.setVisibility(View.VISIBLE);
                beckButton.setVisibility(View.VISIBLE);
                updateFragment();
                break;
            case TYPE_QUESTION_2:
                mQueryQuestion = questionDao.queryBuilder()
                        .where(QuestionDao.Properties.Type.eq(TYPE_QUESTION_2)).build();
                mAssistantUser = true;
                nextButton.setVisibility(View.VISIBLE);
                beckButton.setVisibility(View.VISIBLE);
                updateFragment();
                break;
            case TYPE_QUESTION_3:
                mQueryQuestion = questionDao.queryBuilder()
                        .where(QuestionDao.Properties.Type.eq(TYPE_QUESTION_3)).build();
                mAssistantUser = true;
                nextButton.setVisibility(View.VISIBLE);
                beckButton.setVisibility(View.VISIBLE);
                updateFragment();
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVE_QUESTION_NUMBER, mQuestionNumber);
        outState.putString(SAVE_USER_ANSWER, mData);
        outState.putLong(SAVE_DATE_QUIZ, dateLong);
    }

    @Click(R.id.next_button)
    void nextButtonWasClicked(){
        if (mTypeQuestion == TYPE_QUESTION_0) {
            nextButton.setVisibility(View.INVISIBLE);
            saveUserAnswer();
        }
        if (mQuestionNumber < mNumberOfQuestion - 1) {
            mQuestionNumber = mQuestionNumber + 1;
            updateFragment();
        }else if (mQuestionNumber == mNumberOfQuestion - 1 && mTypeQuestion != TYPE_QUESTION_0){
            mQuestionNumber = 0;
            updateFragment();
        }else {
            Intent intentFinishActivity = new Intent(TrialActivity.this,FinishActivity_.class);
            intentFinishActivity.putExtra(INTENT_NAME_VALUE,userName);
            intentFinishActivity.putExtra(INTENT_DATE_VALUE, dateLong);
            intentFinishActivity.putExtra(INTENT_LEVEL_VALUE, mLevelQuiz);
            startActivity(intentFinishActivity);
        }
    }
    @Click(R.id.back_button)
    void backButtonWasClicked(){
        if (mQuestionNumber > 1) {
            mQuestionNumber = mQuestionNumber - 1;
            updateFragment();
        }else
            mQuestionNumber = mNumberOfQuestion - 1;
            updateFragment();
    }
    /**
     * This get DIFFICULTY_LEVEL in SharedPreferences
     * and sort question in accordance with the level of difficulty
     */
    private void changeLevelQuiz() {
        mPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        mLevelQuiz = mPreferences.getInt(DIFFICULTY_LEVEL,mLevelQuiz);
        if (mSavedInstanceState == null) {
            switch (mLevelQuiz) {
                case LEVEL_1:
                    showAssistantDialog(mTrialContext.getResources().getString(R.string.entry_level_1));
                    break;
                case LEVEL_2:
                    showAssistantDialog(mTrialContext.getResources().getString(R.string.entry_level_2));
                    break;
                case LEVEL_3:
                    showAssistantDialog(mTrialContext.getResources().getString(R.string.entry_level_3));
                    break;
                case LEVEL_4:
                    showAssistantDialog(mTrialContext.getResources().getString(R.string.entry_level_4));
                    break;
                case LEVEL_5:
                    showAssistantDialog(mTrialContext.getResources().getString(R.string.entry_level_5));
                    break;
                case LEVEL_6:
                    showAssistantDialog(mTrialContext.getResources().getString(R.string.entry_level_6));
                    break;
                case LEVEL_7:
                    showAssistantDialog(mTrialContext.getResources().getString(R.string.entry_level_7));
                    break;
                case LEVEL_8:
                    showAssistantDialog(mTrialContext.getResources().getString(R.string.entry_level_8));
                    break;
                case LEVEL_9:
                    showAssistantDialog(mTrialContext.getResources().getString(R.string.entry_level_9));
                    break;
                case LEVEL_10:
                    showAssistantDialog(mTrialContext.getResources().getString(R.string.entry_level_10));
                    break;
            }
        }
    }

    /**
     * This updates fragment for each new question and loads the correct answer
     */
    private void updateFragment() {
        mNumberOfQuestion = mQueryQuestion.list().size();
        Question question = mQueryQuestion.list().get(mQuestionNumber);
        mRightAnswer = question.getRightAnswer();
        mQuestionId = question.getId();
        textQuestion.setText(question.getQuestions());
        answerQuery = answerDao.queryBuilder().where(AnswerDao.Properties.QuestionId
                .eq(mQuestionId)).build();
            selectorFragment.loadAnswer(answerQuery);
    }

    void saveUserAnswer() {
        if (mData == null) {
            mData = "not available";
        }
        mPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        userName = mPreferences.getString("userName", "");
        UserSuccess userSuccess = new UserSuccess();
        userSuccess.setId((long) mQuestionNumber);
        userSuccess.setUserAnswer(mData);
        userSuccess.setUserName(userName);
        userSuccess.setUserQuestionId(mQuestionId);
        userSuccess.setDateAnswer(dateLong);
        userSuccessDao.insertOrReplace(userSuccess);
    }
    /**
     * This handles the user response
     * @param data is a String user answer
     */
    @Override
    public void processingUserAnswer(String data) {
        nextButton.setVisibility(View.VISIBLE);
        mData = data;

        if (mRightAnswer.toLowerCase().equals(data.toLowerCase()) & mAssistantUser) {
            showAssistantDialog(mTrialContext.getResources().getString(R.string.right_anser));
        } else if (mAssistantUser) {
            showAssistantDialog(mTrialContext.getResources().getString(R.string.false_answer));
        }
    }
    /**
     * This date implementation quiz for saving users results in database
     */
    public void getDateQuiz() {
        Calendar c = Calendar.getInstance();
        Date date  = c.getTime();
        dateLong = date.getTime();
    }

    @Click(R.id.assistantImage)
    void assistantWasClicked(){
        if (mCounterTips == 0 & !mAssistantUser) {
            showAssistantDialog(mTrialContext.getResources().getString(R.string.right_anser) +"  (" + mRightAnswer + ")");
        }else if (!mAssistantUser){
            showAssistantDialog(mTrialContext.getResources().getString(R.string.no_tips));
        }else{
            showAssistantDialog(mTrialContext.getResources().getString(R.string.exit_the_training_mode));
        }
        mCounterTips++;

    }

    private void showAssistantDialog(String textDialog) {
        fragmentManager = getSupportFragmentManager();
        if(dialogFragment != null) {
            fragmentManager.beginTransaction().remove(dialogFragment).commit();
        }
            dialogFragment = new AssistantDialogFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager
                    .beginTransaction();
            fragmentTransaction.add(R.id.container, dialogFragment);
            fragmentTransaction.commit();
            dialogFragment.setAssistantTalk(textDialog);
            autoOff(dialogFragment);

    }
    @Background(delay=4000)
    void autoOff(AssistantDialogFragment fragment) {
        if (fragment != null) {
            fragment.dismiss();
        }
    }
}
