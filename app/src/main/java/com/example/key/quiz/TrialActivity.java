package com.example.key.quiz;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.key.quiz.database.DaoSession;
import com.example.key.quiz.database.QuestionDao;
import com.example.key.quiz.database.Repository;
import com.example.key.quiz.database.RepositoryDao;

import org.greenrobot.greendao.query.Query;

import java.util.List;


public class TrialActivity extends AppCompatActivity {
    private QuestionDao questionDao;
    private RepositoryDao repositoryDao;
    private Query<Repository> repositoryQuery;
    private RecyclerAdapter recyclerAdapter;
    // identifies the key questions and answers to them
    private long questionKey = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trial);

        Button nextButton = (Button)findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (questionKey < 5) {
                    questionKey = questionKey + 1;
                    setUpViews();
                }
            }
        });

        Button backButton = (Button)findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (questionKey > 1) {
                    questionKey = questionKey - 1;
                    setUpViews();
                }
            }
        });

        DaoSession daoSession = ((QuizApplication) getApplication()).getDaoSession();
        questionDao = daoSession.getQuestionDao();
        repositoryDao = daoSession.getRepositoryDao();

        setUpViews();

    }

    private void setUpViews() {
        TextView textQuestion = (TextView)findViewById(R.id.question);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        //noinspection ConstantConditions
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerAdapter = new RecyclerAdapter(answerClickListener);
        recyclerView.setAdapter(recyclerAdapter);
        textQuestion.setText(questionDao.load(questionKey).getContent());
        // select answers with database
        repositoryQuery = repositoryDao.queryBuilder().where(RepositoryDao.Properties.UserRemoteId.eq(questionKey)).build();
        updateNotes();
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
            Toast.makeText(TrialActivity.this, "Ви натиснули кнопку " + position,
                    Toast.LENGTH_LONG).show();
        }

    };
}
