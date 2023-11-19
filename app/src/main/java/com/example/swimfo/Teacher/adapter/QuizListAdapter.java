package com.example.swimfo.Teacher.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.swimfo.R;
import com.example.swimfo.Teacher.model.QuizListModel;

import java.util.List;

public class QuizListAdapter extends RecyclerView.Adapter<QuizListAdapter.MyViewHolder> {

    public final MyInterface myInterfaces;
    Context context;
    List<QuizListModel> quizListModels;

    public QuizListAdapter(Context context, List<QuizListModel> quizListModels, MyInterface myInterfaces){
        this.context = context;
        this.quizListModels = quizListModels;
        this.myInterfaces = myInterfaces;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType  ) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.items_quiz, parent, false);

        return new MyViewHolder(view, myInterfaces);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.txtQuizTitle.setText(quizListModels.get(position).getName());
            holder.txtQuizItems.setText(quizListModels.get(position).getItems() + " Items");
            holder.txtQuizDeadline.setText("Deadline: " + quizListModels.get(position).getDeadline());
    }

    @Override
    public int getItemCount() {

        return quizListModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{


        TextView txtQuizTitle, txtQuizItems, txtQuizDeadline    ;


        public MyViewHolder(@NonNull View itemView, MyInterface myInterfaces) {
            super(itemView);

            txtQuizTitle = itemView.findViewById(R.id.txtQuizTitle);
            txtQuizItems = itemView.findViewById(R.id.txtQuizItems);
            txtQuizDeadline = itemView.findViewById(R.id.txtQuizDeadline);


            itemView.setOnClickListener(view -> {
                if(myInterfaces != null ){
                    int pos = getAdapterPosition();
                    if(pos!= RecyclerView.NO_POSITION){
                        myInterfaces.onItemClick(pos, "about");
                    }

                }
            });
        }
    }


}
