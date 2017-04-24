package com.example.key.quiz;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.key.quiz.database.Repository;

import java.util.ArrayList;
import java.util.List;



public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.AnswerViewHolder> {
    /**
     * List for answer
     */

    private List<Repository> dataset;

    private AnswerClickListener clickListener;

    public interface AnswerClickListener {
        void onAnswerClick(int position);
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
                        text.setBackgroundColor(Color.RED);
                    }
                }
            });
        }
    }

    public RecyclerAdapter(AnswerClickListener clickListener) {
        this.clickListener = clickListener;
        this.dataset = new ArrayList<Repository>();
    }

    public void setNotes(@NonNull List<Repository> notes) {
        dataset = notes;
        notifyDataSetChanged();
    }

    public Repository getRepository(int position) {
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
        Repository repository = dataset.get(position);
        holder.text.setText(repository.getAnswer());
    }
    @Override
    public int getItemCount () {
        return dataset.size();
    }
}