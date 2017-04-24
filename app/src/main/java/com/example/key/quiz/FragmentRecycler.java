package com.example.key.quiz;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.key.quiz.database.DaoSession;
import com.example.key.quiz.database.QuestionDao;
import com.example.key.quiz.database.Repository;
import com.example.key.quiz.database.RepositoryDao;

import org.greenrobot.greendao.query.Query;

import java.util.List;


/**
 * Created by Key on 24.04.2017.
 */

public class FragmentRecycler extends Fragment {
    public TextView textQuestion;
    public RecyclerView recyclerView;
    private QuestionDao questionDao;
    private RepositoryDao repositoryDao;
    private Query<Repository> repositoryQuery;
    private RecyclerAdapter recyclerAdapter;
    private long questionKey = 1;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View fragmentRecycler = inflater.inflate(R.layout.fragment_recycleer,container,false);
        textQuestion = (TextView)fragmentRecycler.findViewById(R.id.text_question);
        recyclerView = (RecyclerView)fragmentRecycler.findViewById(R.id.recycler_view);
        setUpViews();
        return fragmentRecycler;


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DaoSession daoSession = ((QuizApplication)getActivity().getApplication()).getDaoSession();
        questionDao = daoSession.getQuestionDao();
        repositoryDao = daoSession.getRepositoryDao();
        textQuestion.setText(questionDao.load(questionKey).getContent());
        // select answers with database
        repositoryQuery = repositoryDao.queryBuilder().where(RepositoryDao.Properties.UserRemoteId.eq(questionKey)).build();
        updateNotes();
    }

    private void setUpViews() {

        //noinspection ConstantConditions
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerAdapter = new RecyclerAdapter(answerClickListener);
        recyclerView.setAdapter(recyclerAdapter);
      //
    }
    private void updateNotes() {
        List<Repository> answer = repositoryQuery.list();
        recyclerAdapter.setNotes(answer);
    }

    RecyclerAdapter.AnswerClickListener answerClickListener = new  RecyclerAdapter.AnswerClickListener() {
        @Override
        public void onAnswerClick(int position) {
            Repository note = recyclerAdapter.getRepository(position);
            Long noteId = note.getId();
            // action for ButtonClick


        }

    };
}
