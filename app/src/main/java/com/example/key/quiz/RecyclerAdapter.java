package com.example.key.quiz;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.key.quiz.database.Answer;

import java.util.ArrayList;
import java.util.List;



public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.AnswerViewHolder> {
    /**
     * List for answer
     */

    private List<Answer> dataset;

    private AnswerClickListener clickListener;

    public interface AnswerClickListener {
        void onAnswerClick(long answer);
    }


    /**
     * ViewHolder instance for multiple display  timeText, titleText, mySection, and information myUrl
     * also containing listener events are pressing
     */
    static class AnswerViewHolder extends RecyclerView.ViewHolder {

        public TextView text;


        public AnswerViewHolder(final View itemView, final AnswerClickListener clickListener) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.text_position);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickListener != null) {
                        clickListener.onAnswerClick(getAdapterPosition());
                        text.setBackgroundColor(Color.GREEN);
                    }
                }
            });
        }
    }

    public RecyclerAdapter(AnswerClickListener clickListener) {
        this.clickListener = clickListener;
        this.dataset = new ArrayList<Answer>();
    }

    public void setNotes(@NonNull List<Answer> notes) {
        dataset = notes;
        notifyDataSetChanged();
    }

    public Answer getRepository(int position) {
        return dataset.get(position);
    }

    @Override
    public RecyclerAdapter.AnswerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new AnswerViewHolder(view, clickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.AnswerViewHolder holder, int position) {
        Answer answer = dataset.get(position);
        holder.text.setText(answer.getAnswers());
    }
    @Override
    public int getItemCount () {
        return dataset.size();
    }
}