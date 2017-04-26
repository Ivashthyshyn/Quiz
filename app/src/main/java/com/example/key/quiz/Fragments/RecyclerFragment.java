package com.example.key.quiz.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.key.quiz.QuizApplication;
import com.example.key.quiz.R;
import com.example.key.quiz.RecyclerAdapter;
import com.example.key.quiz.database.Answer;
import com.example.key.quiz.database.AnswerDao;
import com.example.key.quiz.database.DaoSession;
import com.example.key.quiz.database.Question;
import com.example.key.quiz.database.QuestionDao;

import org.greenrobot.greendao.query.Query;

import java.util.List;


/**
 * Created by Key on 24.04.2017.
 */

public class RecyclerFragment extends Fragment {
    public TextView textQuestion;
    public RecyclerView recyclerView;
    private AnswerDao answerDao;
    private QuestionDao questionDao;
    private Query<Answer> answerQuery;
    private RecyclerAdapter recyclerAdapter;
    private long questionKey = 1;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View fragmentRecycler = inflater.inflate(R.layout.fragment_recycleer,container,false);
        recyclerView = (RecyclerView)fragmentRecycler.findViewById(R.id.recycler_view);
        setUpViews();
        return fragmentRecycler;


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DaoSession daoSession = ((QuizApplication)getActivity().getApplication()).getDaoSession();
        answerDao = daoSession.getAnswerDao();
        questionDao = daoSession.getQuestionDao();
        // select answers with database
        answerQuery = answerDao.queryBuilder().where(AnswerDao.Properties.RemoutCommunicationId.eq(questionKey)).build();
        updateNotes();
    }

    private void setUpViews() {

        //noinspection ConstantConditions
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerAdapter = new RecyclerAdapter(answerClickListener);
        recyclerView.setAdapter(recyclerAdapter);

    }
    private void updateNotes() {
        List<Answer> answer = answerQuery.list();
        recyclerAdapter.setNotes(answer);
    }

    RecyclerAdapter.AnswerClickListener answerClickListener = new  RecyclerAdapter.AnswerClickListener() {
        @Override
        public void onAnswerClick(long position) {
            Question question = questionDao.load(1L);
            long answerId = question.getRightAnswer();
            if (position + 1 == answerId){
                Toast.makeText(getActivity(),"Відповідь првильна",Toast.LENGTH_SHORT).show();
            }

        }

    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
}
