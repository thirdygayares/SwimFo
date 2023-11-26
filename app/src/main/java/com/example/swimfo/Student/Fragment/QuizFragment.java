package com.example.swimfo.Student.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.swimfo.Finish;
import com.example.swimfo.Student.Quiz.QuizContainer;
import com.example.swimfo.R;
import com.example.swimfo.Student.Quiz.QuizInstruction;
import com.example.swimfo.Student.Quiz.StartQuiz;
import com.example.swimfo.Student.adapter.QuizListStudentAdapter;
import com.example.swimfo.Teacher.adapter.MyInterface;
import com.example.swimfo.Teacher.adapter.QuizListAdapter;
import com.example.swimfo.Teacher.model.QuizListModel;
import com.example.swimfo.Teacher.quiz.AddQuiz;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class QuizFragment extends AppCompatActivity implements MyInterface {
    Button start;

    private RecyclerView recyclerView;
    private QuizListStudentAdapter quizListAdapter;
    private List<QuizListModel> quizListModels = new ArrayList<>();
    private ProgressBar progressBar;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public QuizFragment() {
        // Required empty public constructor
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_quiz);

        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        //initialize adapter
        quizListAdapter = new QuizListStudentAdapter(this, quizListModels, this);

        //set up data
        quizListData();

        //set up recycler view
        recyclerView.setAdapter(quizListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


    }

    private void quizListData() {
        progressBar.setVisibility(View.VISIBLE);
        // Get a reference to the quizzes in the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference quizzesRef = database.getReference("Quiz");

        // Attach a listener to read the data
        quizzesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Clear the existing data (to avoid duplicates)
                quizListModels.clear();

                // Iterate through all the children in the snapshot
                for (DataSnapshot quizSnapshot : dataSnapshot.getChildren()) {
                    // Convert the snapshot into your QuizListModel class
                    QuizListModel quiz = quizSnapshot.getValue(QuizListModel.class);

                    // Assuming QuizListModel has a constructor that matches the fields
                    if (quiz != null) {
                        quizListModels.add(quiz);
                    }
                }

                progressBar.setVisibility(View.GONE);
                // Notify the adapter that the data set has changed to refresh the RecyclerView
                quizListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Failed to read value, handle the error
                Log.w("TAG", "Failed to read value.", databaseError.toException());
            }
        });
    }


    @Override
    public void onItemClick(int pos, String categories) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref1 = db.getReference("studentlist");

        ref1.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override public void onDataChange(DataSnapshot snapshot)

            {
                if (snapshot.exists()) {
                    String sectionId = snapshot.child("sectionId").getValue(String.class);

                    check(sectionId, pos, uid);
                } else {
                    // Handle the case where the user data doesn't exist
                    Toast.makeText(getApplicationContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle any database errors here
            }
        });






    }

    private void check(String sectionId, int pos, String uid) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref1 = db.getReference("QuizResult").child(quizListModels.get(pos).getName()).child(sectionId).child(uid);

        ref1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override public void onDataChange(DataSnapshot snapshot)

            {
                if (snapshot.exists()) {

                    int correctAnswers = snapshot.child("correct").getValue(Integer.class);
                    int wrongAnswers = snapshot.child("wrong").getValue(Integer.class);
                    int unanswered = snapshot.child("unanswered").getValue(Integer.class);


                    Intent intent = new Intent(getApplicationContext(), Finish.class);
                    intent.putExtra("correct", correctAnswers);
                    intent.putExtra("wrong", wrongAnswers);
                    intent.putExtra("unanswered", unanswered);
                    startActivity(intent);

                } else {
                    // Handle the case where the user data doesn't exist
                    getApplicationContext().startActivity(new Intent(getApplicationContext(), StartQuiz.class)
                            .putExtra("title", quizListModels.get(pos).getName()).putExtra("sectionId", sectionId).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle any database errors here
            }
        });

    }
}