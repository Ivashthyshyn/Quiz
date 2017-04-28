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

import com.example.key.quiz.R;
import com.example.key.quiz.database.Answer;

import org.greenrobot.greendao.query.Query;

/**
 * Created by Key on 24.04.2017.
 * This fragment is provided for the treatment of type 2 Questions
 */

public class EditFragment extends Fragment {
    private EditText editText;
    private String mAnswer;
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


                    if (answerText.equals(mAnswer)){
                        Toast.makeText(getActivity(),"Відповідь првильна",Toast.LENGTH_SHORT).show();
                    }

                    return true;
                }
                return false;
            }
            
        });
        return editFragment;
    }

    public void loadAnswer(Query<Answer> answer){
        mAnswer = answer.list().get(0).toString();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
}
