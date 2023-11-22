package com.example.swimfo.Teacher.quiz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.swimfo.R;
import com.example.swimfo.Teacher.quiz.StudentLeaderBoardModel;

import java.util.List;

public class StudentLeaderboardAdapter extends RecyclerView.Adapter<StudentLeaderboardAdapter.LeaderboardViewHolder> {

    private Context context;
    private List<StudentLeaderBoardModel> leadList;

    public StudentLeaderboardAdapter(Context context, List<StudentLeaderBoardModel> leadList) {
        this.context = context;
        this.leadList = leadList;
    }


    
    @Override
    public LeaderboardViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_leaderboard_student, parent, false);
        return new LeaderboardViewHolder(view);
    }

    @Override
    public void onBindViewHolder( LeaderboardViewHolder holder, int position) {
        StudentLeaderBoardModel leaderBoardModel = leadList.get(position);
        holder.bind(leaderBoardModel, position);

    }

    @Override
    public int getItemCount() {
        return leadList.size();
    }

    public class LeaderboardViewHolder extends RecyclerView.ViewHolder {
        private TextView rank,name,date,score, time;


        public LeaderboardViewHolder( View itemView) {
            super(itemView);
            rank = itemView.findViewById(R.id.rank);
            name = itemView.findViewById(R.id.name);
            date = itemView.findViewById(R.id.date);
            score = itemView.findViewById(R.id.score);
            time = itemView.findViewById(R.id.time);


        }

        public void bind(StudentLeaderBoardModel leaderBoardModel, int position) {
            rank.setText(String.valueOf(position+1));
            name.setText(leaderBoardModel.getDisplayName());
            score.setText(String.valueOf(leaderBoardModel.getScore()));
            date.setText(leaderBoardModel.getDate());
            time.setText(leaderBoardModel.getTime());
        }
    }
}

