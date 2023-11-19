package com.example.swimfo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.swimfo.Student.Quiz.QuizContainer;
import com.example.swimfo.Model.QuizModel;
import com.example.swimfo.R;

import java.util.List;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.QuizViewHolder> {

    private Context context;
    private List<QuizModel> quizList;
    private OnItemClickListener onItemClickListener;

    public QuizAdapter(Context context, List<QuizModel> quizList) {
        this.context = context;
        this.quizList = quizList;
    }

    public interface OnItemClickListener {
        void onItemClick(String quizNumber);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    
    @Override
    public QuizViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quiz, parent, false);
        return new QuizViewHolder(view);
    }

    @Override
    public void onBindViewHolder( QuizViewHolder holder, int position) {
        QuizModel quiz = quizList.get(position);
        holder.bind(quiz);
        holder.quizNumberTextView.setOnClickListener(v->{
            Intent intent = new Intent(v.getContext(), QuizContainer.class);
            intent.putExtra("quiznum",quizList.get(position).getQuizNumber() );
            v.getContext().startActivity(intent);
        });
        holder.klik.setOnClickListener(v->{
            Intent intent = new Intent(v.getContext(), QuizContainer.class);
            intent.putExtra("quiznum",quizList.get(position).getQuizNumber() );
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return quizList.size();
    }

    public class QuizViewHolder extends RecyclerView.ViewHolder {
        private TextView quizNumberTextView;
        private LinearLayout klik;

        public QuizViewHolder( View itemView) {
            super(itemView);
            quizNumberTextView = itemView.findViewById(R.id.quizNumberTextView);
            klik = itemView.findViewById(R.id.klik);



        }

        public void bind(QuizModel quiz) {
            quizNumberTextView.setText(quiz.getQuizNumber());
        }
    }
}

