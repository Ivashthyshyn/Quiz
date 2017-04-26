package com.example.key.quiz.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.key.quiz.QuizApplication;
import com.example.key.quiz.R;
import com.example.key.quiz.database.AnswerDao;
import com.example.key.quiz.database.DaoSession;
import com.example.key.quiz.database.Question;
import com.example.key.quiz.database.QuestionDao;

/**
 * Created by Key on 24.04.2017.
 */

public class EditFragment extends Fragment {
    private EditText editText;
    private QuestionDao questionDao;
    private AnswerDao answerDao;
    private long answerId;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View editFragment = inflater.inflate(R.layout.fragment_edit, container, false);
        editText = (EditText)editFragment.findViewById(R.id.editText);
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN &&
                        (keyCode == KeyEvent.KEYCODE_ENTER))
                {
                    String answerText = editText.getText().toString();


                    if (answerText.equals(answerDao.load(answerId).getAnswers())){
                        Toast.makeText(getActivity(),"Відповідь првильна",Toast.LENGTH_SHORT).show();
                    }

                    return true;
                }
                return false;
            }
            
        });
        return editFragment;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DaoSession daoSession = ((QuizApplication) getActivity().getApplication()).getDaoSession();
        questionDao = daoSession.getQuestionDao();
        Question question = questionDao.load(2L);
        answerId = question.getRightAnswer();
        answerDao = daoSession.getAnswerDao();

    }
}
