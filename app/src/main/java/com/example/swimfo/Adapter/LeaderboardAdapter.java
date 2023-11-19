package com.example.swimfo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.swimfo.Model.LeaderBoardModel;
import com.example.swimfo.R;

import java.util.List;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.LeaderboardViewHolder> {

    private Context context;
    private List<LeaderBoardModel> leadList;

    public LeaderboardAdapter(Context context, List<LeaderBoardModel> leadList) {
        this.context = context;
        this.leadList = leadList;
    }


    
    @Override
    public LeaderboardViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.leaderboard_item, parent, false);
        return new LeaderboardViewHolder(view);
    }

    @Override
    public void onBindViewHolder( LeaderboardViewHolder holder, int position) {
        LeaderBoardModel leaderBoardModel = leadList.get(position);
        holder.bind(leaderBoardModel, position);

    }

    @Override
    public int getItemCount() {
        return leadList.size();
    }

    public class LeaderboardViewHolder extends RecyclerView.ViewHolder {
        private TextView rank,name,date,score;


        public LeaderboardViewHolder( View itemView) {
            super(itemView);
            rank = itemView.findViewById(R.id.rank);
            name = itemView.findViewById(R.id.name);
            date = itemView.findViewById(R.id.date);
            score = itemView.findViewById(R.id.score);



        }

        public void bind(LeaderBoardModel leaderBoardModel, int position) {
            rank.setText(String.valueOf(position+1));
            name.setText(leaderBoardModel.getDisplayName());
            score.setText(leaderBoardModel.getScore());
            date.setText(leaderBoardModel.getDate());
        }
    }
}

