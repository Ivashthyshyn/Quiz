package com.example.key.quiz.Fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

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
    private LinearLayout linearLayout;
    private View.OnClickListener clickListener;
    private long answerId;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_cycle, container, false);
        linearLayout = (LinearLayout) fragment.findViewById(R.id.fragmentCycle);
        clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View button) {

               if (button.getId() == answerId){
                 button.setBackgroundColor(Color.GREEN);
                    Toast.makeText(getActivity(), "Відповідь првильна", Toast.LENGTH_SHORT).show();
                }else {
                   button.setBackgroundColor(Color.RED);
               }
            }
        };
        return fragment;
    }
        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            DaoSession daoSession = ((QuizApplication)getActivity().getApplication()).getDaoSession();
            questionDao = daoSession.getQuestionDao();
            Question question = questionDao.load(3L);
            answerId = question.getRightAnswer();
            answerDao = daoSession.getAnswerDao();
            Answer answer = answerDao.load(5L);
            String stringAnswer = answer.getAnswers();
            String [] words = stringAnswer.split(" ");
            for (int i = 0; i < words.length; i++ ){
                Button button = new Button(getActivity());
                button.setId(i);
                button.setText(words[i].toString());
                button.setOnClickListener(clickListener);
                linearLayout.addView(button);

            }

            // select answers with database
          //  repositoryQuery = answerDao.queryBuilder().where(RepositoryDao.Properties.UserRemoteId.eq(questionKey)).build();
    }


}
