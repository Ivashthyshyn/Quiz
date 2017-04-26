package com.example.key.quiz.Fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.key.quiz.QuizApplication;
import com.example.key.quiz.R;
import com.example.key.quiz.database.Answer;
import com.example.key.quiz.database.AnswerDao;
import com.example.key.quiz.database.DaoSession;
import com.example.key.quiz.database.Question;
import com.example.key.quiz.database.QuestionDao;

/**
 * Created by Key on 24.04.2017.
 */

public class ButtonFragment extends Fragment {
    private AnswerDao answerDao;
    private QuestionDao questionDao;
    private RadioGroup radioGroup;
    private long answerId;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_cycle, container, false);
        radioGroup = (RadioGroup) fragment.findViewById(R.id.fragmentCycle);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

            }
        });
        return fragment;
    }
        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            DaoSession daoSession = ((QuizApplication) getActivity().getApplication()).getDaoSession();
            questionDao = daoSession.getQuestionDao();
            Question question = questionDao.load(3L);
            answerId = question.getRightAnswer();
            answerDao = daoSession.getAnswerDao();
            Answer answer = answerDao.load(5L);
            String stringAnswer = answer.getAnswers();
            String[] words = stringAnswer.split(" ");
            for (int i = 0; i < words.length; i++) {
                RadioButton button = new RadioButton(getActivity());
                button.setId(i);
                button.setText(words[i].toString());
                button.setButtonDrawable(R.drawable.radiobutton_selector);
                button.setBackgroundColor(Color.GREEN);
                button.setGravity(Gravity.CENTER);
                button.setPadding(5,0,5,0);
                radioGroup.addView(button);

            }
        }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
}
