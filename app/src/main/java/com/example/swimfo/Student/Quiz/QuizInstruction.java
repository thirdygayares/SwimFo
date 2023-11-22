package com.example.swimfo.Student.Quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.swimfo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class QuizInstruction extends AppCompatActivity {
    String quizname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_instruction);

        Button start_quiz = findViewById(R.id.start_quiz);

        Intent intent = getIntent();

        if(intent.getStringExtra("title") != null){
            quizname = intent.getStringExtra("title");
        }


        start_quiz.setOnClickListener(v -> {

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String uid = user.getUid();

            FirebaseDatabase db = FirebaseDatabase.getInstance();
            DatabaseReference ref1 = db.getReference("studentlist");

            ref1.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override public void onDataChange(DataSnapshot snapshot)

                {
                    if (snapshot.exists()) {
                        String sectionId = snapshot.child("sectionId").getValue(String.class);

                        if (quizname == null) {
                            Toast.makeText(getApplicationContext(), "Quiz not found", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        startActivity(new Intent(getApplicationContext(), StartQuiz.class)
                                .putExtra("title", quizname).putExtra("sectionId", sectionId));
                    } else {
                        // Handle the case where the user data doesn't exist
                        Toast.makeText(QuizInstruction.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Handle any database errors here
                }
            });


        });





    }


    private void Section() {



    }

}