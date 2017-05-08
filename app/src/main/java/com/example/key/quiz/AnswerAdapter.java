package com.example.key.quiz;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.example.key.quiz.database.Question;

import java.util.List;

/**
 * Created by Key on 06.05.2017.
 */

public class AnswerAdapter extends ArrayAdapter<Question> {


    public AnswerAdapter(Context context, List<Question> questionList ) {
        super(context, 0, questionList);
    }
}

