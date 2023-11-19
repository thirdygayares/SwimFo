package com.example.swimfo.Teacher.section;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.swimfo.Model.LeaderBoardModel;
import com.example.swimfo.Adapter.LeaderboardAdapter;
import com.example.swimfo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Leaderboard extends AppCompatActivity {
    ArrayList<LeaderBoardModel> list;

    FirebaseAuth fAuth;
    int count = 1;

    FirebaseDatabase db;
    DatabaseReference databaseReference;
    LeaderboardAdapter adapter;
    RecyclerView recyclerView;

    String sectionName ;
    String sectionId;

    ImageView back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        // Inside the StudentList class
        Intent intent = getIntent();

        if (intent != null) {
            sectionName = intent.getStringExtra("section");
            sectionId = intent.getStringExtra("id");
        }else{
            Toast.makeText(this, "Some Error Occurred", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }

        recyclerView = findViewById(R.id.recycle_view_av);
        back = findViewById(R.id.back);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference("LeaderBoard/1");

        Query query = databaseReference.orderByChild("sectionId");

        // Create an empty list to hold quiz data
        list = new ArrayList<>();

        // Initialize your custom adapter
        adapter = new LeaderboardAdapter(this, list);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Add a ValueEventListener to fetch leaderboard data from Firebase
        query.equalTo(sectionId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String q = snapshot.getKey();
                    String sc = snapshot.child("score").getValue(String.class);
                    String nm = snapshot.child("displayName").getValue(String.class);
                    String dt = snapshot.child("date").getValue(String.class);
                    LeaderBoardModel lboard = new LeaderBoardModel(nm,sectionId, sc,dt);

                    list.add(lboard);

                }
                // Sort the list based on the score (in descending order)
                Collections.sort(list, new Comparator<LeaderBoardModel>() {
                    @Override
                    public int compare(LeaderBoardModel o1, LeaderBoardModel o2) {
                        // Convert scores to integers and compare in reverse order
                        int score1 = Integer.parseInt(o1.getScore());
                        int score2 = Integer.parseInt(o2.getScore());
                        return Integer.compare(score2, score1); // Sort in descending order
                    }
                });
                Toast.makeText(Leaderboard.this, list.toString(), Toast.LENGTH_SHORT).show();

                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database error
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Leaderboard.this, MainTeacher.class);
        startActivity(intent);
        finish();
    }
}