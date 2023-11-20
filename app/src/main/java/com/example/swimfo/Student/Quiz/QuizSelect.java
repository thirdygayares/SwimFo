package com.example.swimfo.Student.Quiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.swimfo.unorganized.Adapter.QuizAdapter;
import com.example.swimfo.unorganized.Model.QuizModel;
import com.example.swimfo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class QuizSelect extends AppCompatActivity {
    private RecyclerView quizRecyclerView;
    private DatabaseReference quizDatabaseRef;
    List<QuizModel> quizList;
    QuizAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_select);

        quizRecyclerView = findViewById(R.id.quizRecyclerView);
        quizRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        quizDatabaseRef = FirebaseDatabase.getInstance().getReference("Quizzes");
        // Query the quizzes from Firebase
        Query query = quizDatabaseRef.orderByKey();

        // Create an empty list to hold quiz data
       quizList = new ArrayList<>();

        // Initialize your custom adapter
       adapter = new QuizAdapter(this, quizList);




        // Add a ValueEventListener to fetch quiz data from Firebase
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                quizList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String q = snapshot.getKey();
                    QuizModel quiz = new QuizModel(q);

                    quizList.add(quiz);

                }
                Toast.makeText(QuizSelect.this, quizList.toString(), Toast.LENGTH_SHORT).show();

                adapter.notifyDataSetChanged();
                quizRecyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database error
            }
        });

    }
 
}