package com.example.swimfo.Teacher.quiz;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.swimfo.R;
import com.example.swimfo.Teacher.section.StudentList;
import com.example.swimfo.unorganized.Model.SectionModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class QuizSectionAdapter extends RecyclerView.Adapter<QuizSectionAdapter.MyViewHolder> {

    Context context;
    ArrayList<SectionModel> list;


    FirebaseDatabase rootNode;
    DatabaseReference studentRef,adminRef;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    String title;

    public QuizSectionAdapter(Context context, ArrayList<SectionModel>list, String title) {
        this.context = context;
        this.list = list;
        this.title = title;
    }

    
    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_quiz_leaderboard_section,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder( MyViewHolder holder, int position) {
        SectionModel sectionModel = list.get(position);
        FirebaseUser currentuser = firebaseAuth.getCurrentUser();
        String uid = currentuser.getUid();


        rootNode = FirebaseDatabase.getInstance();


        holder.mySectionTv.setText(sectionModel.getSectionName());
        holder.go.setOnClickListener(v->{
            Intent intent = new Intent(context, StudentLeaderboard.class);
            intent.putExtra("section", list.get(position).getSectionName());
            intent.putExtra("id", list.get(position).getId());
            intent.putExtra("title", title);

            context.startActivity(intent);
        });
        holder.cv.setOnClickListener(v->{
            Intent intent = new Intent(context, StudentLeaderboard.class);
            intent.putExtra("section", list.get(position).getSectionName());
            intent.putExtra("id", list.get(position).getId());
            intent.putExtra("title", title);
            context.startActivity(intent);
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView go;
        CardView cv;
        TextView mySectionTv;
     //   View parentLayout;
        public MyViewHolder( View itemView) {
            super(itemView);

            go = itemView.findViewById(R.id.go);
            cv = itemView.findViewById(R.id.section);
            mySectionTv = itemView.findViewById(R.id.section_name);
           // parentLayout = itemView.findViewById(android.R.id.content);
        }
    }

    @Override
    public long getItemId(int position) {
        return (position);
    }

    @Override
    public int getItemViewType(int position) {
        return (position);
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
    }
}
