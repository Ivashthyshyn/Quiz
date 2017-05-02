package com.example.key.quiz;


import android.os.Bundle;
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
import android.widget.Toast;

import com.example.key.quiz.database.Answer;

import org.greenrobot.greendao.query.Query;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Key on 24.04.2017.
 * This fragment reflects choices for all types of questions
 */

public class SelectorFragment extends Fragment implements View.OnClickListener {
    public RadioGroup radioGroup;
      public EditText userAnswer;
    private Communicator mCommunicator;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_selector, container, false);
        radioGroup = (RadioGroup) fragment.findViewById(R.id.fragmentCycle);
        return fragment;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCommunicator = (Communicator)getActivity();
    }

    public void loadAnswer(Query<Answer> answer) {
        List<Answer> mAnswerList = answer.list();
        if (mAnswerList.size() == 0) {
            radioGroup.removeAllViews();
            userAnswer = new EditText(getContext());
            userAnswer.setHint(R.string.enter_your_version);
            userAnswer.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
            radioGroup.addView(userAnswer);
            userAnswer.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN &&
                            (keyCode == KeyEvent.KEYCODE_ENTER)) {
                            Toast.makeText(getActivity(), "Введений варіант "+ (userAnswer.getText().toString()),
                                    Toast.LENGTH_SHORT).show();
                            mCommunicator.processingUserAnswer(userAnswer.getText().toString());

                        return true;
                    }
                    return false;
                }
            });
        } else if (mAnswerList.size() == 1 & mAnswerList.get(0).getAnswers() != null) {
            radioGroup.removeAllViews();
            radioGroup.setOrientation(LinearLayout.HORIZONTAL);
            String mAnswer = mAnswerList.get(0).getAnswers();
            List<String> words = Arrays.asList(mAnswer.split(" "));

            for (int i = 0; i < words.size(); i++) {
                RadioButton button = new RadioButton(getActivity());
                button.setId(i);
                button.setText(words.get(i));
                button.setTextSize(14);
                button.setButtonDrawable(R.drawable.rounded_button);
                button.setBackgroundResource(R.drawable.radiobutton_selector);
                button.setPadding(10, 0, 10, 0);
                button.setGravity(Gravity.CENTER);
                button.setOnClickListener(this);
                radioGroup.addView(button);
            }
        } else if (mAnswerList.size() > 1) {
            radioGroup.removeAllViews();
                radioGroup.setOrientation(LinearLayout.VERTICAL);
            for (int i = 0; i < mAnswerList.size(); i++) {
                RadioButton button = new RadioButton(getActivity());
                button.setId(i);
                button.setText(mAnswerList.get(i).getAnswers());
                button.setTextSize(14);
                button.setButtonDrawable(R.drawable.rounded_button);
                button.setBackgroundResource(R.drawable.radiobutton_selector);
                button.setPadding(10, 10, 10, 10);
                button.setGravity(Gravity.CENTER);
                button.setOnClickListener(this);
                radioGroup.addView(button);
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onClick(View radioButton) {
        RadioButton pressedButton = (RadioButton)radioButton;
        mCommunicator.processingUserAnswer(pressedButton.getText().toString());
        Toast.makeText(getContext(), "Вибрана відповідь " + (pressedButton.getText().toString()) , Toast.LENGTH_SHORT).show();
    }
}