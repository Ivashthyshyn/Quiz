package com.example.key.quiz.Fragments;


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
import android.widget.Toast;

import com.example.key.quiz.R;
import com.example.key.quiz.database.Answer;

import org.greenrobot.greendao.query.Query;

/**
 * Created by Key on 24.04.2017.
 * This fragment is provided for the treatment of type 3 Questions
 */

public class ButtonFragment extends Fragment {
    public RadioGroup radioGroup;
    private String mAnswer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_cycle, container, false);
        radioGroup = (RadioGroup) fragment.findViewById(R.id.fragmentCycle);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == 1L){
                    Toast.makeText(getContext(), "Правильна відповідь", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return fragment;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String[] words = mAnswer.split(" ");
        for (int i = 0; i < words.length; i++) {
            RadioButton button = new RadioButton(getActivity());
            button.setId(i);
            button.setText(words[i]);
            button.setTextSize(14);
            button.setButtonDrawable(R.drawable.rounded_button);
            button.setBackgroundResource(R.drawable.radiobutton_selector);
            button.setPadding(10,0,10,0);
            button.setGravity(Gravity.CENTER);
            radioGroup.addView(button);

        }
    }

        public void loadAnswer(Query<Answer> answer) {
          mAnswer = answer.list().get(0).getAnswers();

        }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
}
