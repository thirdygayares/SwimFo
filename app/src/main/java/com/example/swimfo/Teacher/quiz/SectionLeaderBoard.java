package com.example.swimfo.Teacher.quiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.swimfo.R;
import com.example.swimfo.Teacher.quiz.QuizSectionAdapter;
import com.example.swimfo.unorganized.Model.SectionModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SectionLeaderBoard extends AppCompatActivity {


    //RecycleView
    RecyclerView recyclerView;
    QuizSectionAdapter quizSectionAdapter;
    Button logout;

    FirebaseDatabase rootNode;
    DatabaseReference reference;
    FirebaseAuth firebaseAuth;
    ArrayList<SectionModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_quiz_section_leader_board);


        firebaseAuth = FirebaseAuth.getInstance();

        rootNode = FirebaseDatabase.getInstance();
        recyclerView = findViewById(R.id.recycle_view_av);



        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();


        Intent intent = getIntent();
        String title = intent.getStringExtra("title");

        quizSectionAdapter = new QuizSectionAdapter(this,list, title);
        quizSectionAdapter.setHasStableIds(true);
        recyclerView.setAdapter(quizSectionAdapter);
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        String uid = currentUser.getUid();

        reference = rootNode.getReference("users/").child("sections").child("4smAFPenKKda95RNnHshnIaUHUV2");

        reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                list.clear();
                if (snapshot.hasChildren()){
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        SectionModel classModel = dataSnapshot.getValue(SectionModel.class);
                        list.add(classModel);
                    }

                }else {
                    Toast.makeText(getApplicationContext(), "No Section Available", Toast.LENGTH_SHORT).show();
                }
                quizSectionAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    }
}