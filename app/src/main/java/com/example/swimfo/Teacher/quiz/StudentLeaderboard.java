package com.example.swimfo.Teacher.quiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.swimfo.R;
import com.example.swimfo.Teacher.AddStudent;
import com.example.swimfo.Teacher.TeacherMain;
import com.example.swimfo.Teacher.adapter.AvailableStudentAdapter;
import com.example.swimfo.Teacher.section.Leaderboard;
import com.example.swimfo.Teacher.quiz.StudentLeaderboard;
import com.example.swimfo.Teacher.section.MainTeacher;
import com.example.swimfo.unorganized.Adapter.LeaderboardAdapter;
import com.example.swimfo.unorganized.Model.LeaderBoardModel;
import com.example.swimfo.unorganized.Model.StudentModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class StudentLeaderboard extends AppCompatActivity {
    String sectionName ;
    String sectionId;
    String title;

    ArrayList<StudentLeaderBoardModel> list;

    FirebaseAuth fAuth;
    int count = 1;

    FirebaseDatabase db;
    DatabaseReference databaseReference;
    StudentLeaderboardAdapter adapter;
    RecyclerView recyclerView;


    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_leaderboard);


        // Inside the StudentLeaderboard class
        Intent intent = getIntent();

        if (intent != null) {
            sectionName = intent.getStringExtra("section");
            sectionId = intent.getStringExtra("id");
            title = intent.getStringExtra("title");

        }else{
            Toast.makeText(this, "Some Error Occurred", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
        recyclerView = findViewById(R.id.recycle_view_av);
        back = findViewById(R.id.back);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference("QuizResult").child(title).child(sectionId);

        Query query = databaseReference.orderByChild("sectionId");

        // Create an empty list to hold quiz data
        list = new ArrayList<>();

        // Initialize your custom adapter
        adapter = new StudentLeaderboardAdapter(this, list);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Add a ValueEventListener to fetch leaderboard data from Firebase
        query.equalTo(sectionId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String q = snapshot.getKey();
                    Integer sc = snapshot.child("correct").getValue(Integer.class);
                    String nm = snapshot.child("studentName").getValue(String.class);
                    String dt = snapshot.child("date").getValue(String.class);
                    String time = snapshot.child("time").getValue(String.class);


                    StudentLeaderBoardModel lboard = new StudentLeaderBoardModel(nm,sectionId, sc,getDate(dt), time);

                    list.add(lboard);

                }
                // Sort the list based on the score (in descending order)
                Collections.sort(list, new Comparator<StudentLeaderBoardModel>() {
                    @Override
                    public int compare(StudentLeaderBoardModel o1, StudentLeaderBoardModel o2) {
                        // Convert scores to integers and compare in reverse order
                      o1.getScore();

                        return Integer.compare( o2.getScore(),  o1.getScore()); // Sort in descending order
                    }
                });

                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database error
            }
        });
    }

    public String getDate(String dates){
        String formattedDate = "";
        SimpleDateFormat originalFormat = new SimpleDateFormat("E MMM dd HH:mm:ss 'GMT'Z yyyy");
        SimpleDateFormat desiredFormat = new SimpleDateFormat("MMM dd yyyy");

        try {
            Date date = originalFormat.parse(dates);
             formattedDate = desiredFormat.format(date);
            System.out.println("Converted Date: " + formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return formattedDate;
    }






}