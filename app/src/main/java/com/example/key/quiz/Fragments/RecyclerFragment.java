package com.example.key.quiz.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.key.quiz.R;
import com.example.key.quiz.RecyclerAdapter;
import com.example.key.quiz.database.Answer;

import org.greenrobot.greendao.query.Query;

import java.util.List;


/**
 * Created by Key on 24.04.2017.
 * This fragment is provided for the treatment of type 1 Questions
 */

public class RecyclerFragment extends Fragment {
    public RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;
    private List<Answer> answer;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View fragmentRecycler = inflater.inflate(R.layout.fragment_recycleer,container,false);
        recyclerView = (RecyclerView)fragmentRecycler.findViewById(R.id.recycler_view);
        setUpViews();
        recyclerAdapter.setAnswers(answer);
        return fragmentRecycler;


    }


    public void loadAnswer(Query<Answer> answerQuery) {

        answer = answerQuery.list();

    }

    private void setUpViews() {

        //noinspection ConstantConditions
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerAdapter = new RecyclerAdapter(answerClickListener);
        recyclerView.setAdapter(recyclerAdapter);

    }

    RecyclerAdapter.AnswerClickListener answerClickListener = new  RecyclerAdapter.AnswerClickListener() {
        @Override
        public void onAnswerClick(long position) {

                Toast.makeText(getActivity(),"Відповідь првильна" + position,Toast.LENGTH_SHORT).show();


        }

    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
}
