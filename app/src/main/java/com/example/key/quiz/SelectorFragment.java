package com.example.key.quiz;


import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.key.quiz.database.Answer;

import org.androidannotations.annotations.EFragment;
import org.greenrobot.greendao.query.Query;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Key on 24.04.2017.
 * This fragment reflects choices for all types of questions
 */
@EFragment
public class SelectorFragment extends Fragment implements RadioGroup.OnCheckedChangeListener {
    public RadioGroup radioGroup1;
    public RadioGroup radioGroup2;
    public EditText userAnswer;
    private Communicator mCommunicator;
    private boolean changeGroupFlag = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_selector, container, false);
        radioGroup1 = (RadioGroup) fragment.findViewById(R.id.radioGroup1);
        radioGroup1.setOnCheckedChangeListener(this);
        radioGroup2 = (RadioGroup) fragment.findViewById(R.id.radioGroup2);
        radioGroup2.setOnCheckedChangeListener(this);
        return fragment;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCommunicator = (Communicator)getActivity();
    }

    /**
     * This field displays the user's choice, depending on the type of questions
     * @param answer is a list of answers from database
     */
    public void loadAnswer(Query<Answer> answer) {
        List<Answer> mAnswerList = answer.list();
        // type of questions unanswered
        if (mAnswerList.size() == 0) {
            radioGroup1.removeAllViews();
            radioGroup2.removeAllViews();
            userAnswer = new EditText(getContext());
            userAnswer.setHint(R.string.enter_your_version);
            userAnswer.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
            radioGroup1.addView(userAnswer);
            userAnswer.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN &&
                            (keyCode == KeyEvent.KEYCODE_ENTER)) {
                            mCommunicator.processingUserAnswer(userAnswer.getText().toString());
                        return true;
                    }
                    return false;
                }
            });
        } else if (mAnswerList.size() == 1 & mAnswerList.get(0).getAnswers() != null) {
            radioGroup1.removeAllViews();
            radioGroup2.removeAllViews();
            radioGroup1.setOrientation(LinearLayout.HORIZONTAL);
            radioGroup2.setOrientation(LinearLayout.HORIZONTAL);
            String mAnswer = mAnswerList.get(0).getAnswers();
            List<String> words = Arrays.asList(mAnswer.split(" "));
            //type with one answer
            for (int i = 0; i < words.size(); i++) {
                RadioButton button = new RadioButton(getActivity());
                button.setId(i);
                button.setText(words.get(i));
                button.setTextSize(14);
                button.setButtonDrawable(R.drawable.rounded_button);
                button.setBackgroundResource(R.drawable.radiobutton_selector);
                button.setPadding(10, 10, 10, 10);
                button.setTextSize(20);
                button.setGravity(Gravity.CENTER);
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    radioGroup1.addView(button);
                } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    if (i <= words.size()/2){
                        radioGroup1.addView(button);
                    }else{
                        radioGroup2.addView(button);
                    }
                }
            }
            //type with many answers
        } else if (mAnswerList.size() > 1) {
            radioGroup1.removeAllViews();
                radioGroup1.setOrientation(LinearLayout.VERTICAL);
            for (int i = 0; i < mAnswerList.size(); i++) {
                RadioButton button = new RadioButton(getActivity());
                button.setId(i);
                button.setText(mAnswerList.get(i).getAnswers());
                button.setTextSize(14);
                button.setButtonDrawable(R.drawable.rounded_button);
                button.setBackgroundResource(R.drawable.radiobutton_selector);
                button.setPadding(10, 10, 10, 10);
                button.setGravity(Gravity.CENTER);
                button.setTextSize(20);
                radioGroup1.addView(button);
            }
        }
    }

    /**
     *This method saves all data and settings fragment when changing screen orientation
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    /**
     *This listener processes the event-touch buttons for both groups in the style of radio groups
     * and send userAnswer to TrialActivity using the Communicator
     * @param group a group in which there were changes
     * @param checkedId is  resource ID radio button press
     */
    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

        if (group != null && changeGroupFlag == false) {
            if (group == radioGroup1) {
                changeGroupFlag =true;
                RadioButton pressedButton = (RadioButton) group.findViewById(checkedId);
                mCommunicator.processingUserAnswer(pressedButton.getText().toString());
                radioGroup2.clearCheck();
                changeGroupFlag = false;

            } else if (group == radioGroup2)  {
                changeGroupFlag =true;
                RadioButton pressedButton = (RadioButton) group.findViewById(checkedId);
                mCommunicator.processingUserAnswer(pressedButton.getText().toString());
                radioGroup1.clearCheck();
                changeGroupFlag = false;
            }
        }
    }
}