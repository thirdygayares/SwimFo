package com.example.swimfo.Student.Quiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.swimfo.Finish;
import com.example.swimfo.R;
import com.example.swimfo.Student.adapter.QuestionQuizListAdapter;
import com.example.swimfo.Student.model.QuizStudentModel;
import com.example.swimfo.Teacher.model.QuestionQuizModel;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StartQuiz extends AppCompatActivity {
    private List<QuestionQuizModel> quizStudentModels = new ArrayList<>();
    private List<QuestionQuizModel> correctAnswerList = new ArrayList<>();

    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private MaterialButton btnNext;
    private QuestionQuizListAdapter questionQuizListAdapter;
    private CustomLayoutManager layoutManager;

    private int nextPosition = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_quiz);

        recyclerView = findViewById(R.id.recyclerView);
        btnNext = findViewById(R.id.btnNext);
        progressBar = findViewById(R.id.progressBar);


        questionQuizListAdapter = new QuestionQuizListAdapter(this, quizStudentModels);

        Intent intent = getIntent();
        if(intent.getStringExtra("title") != null){
            String quizname = intent.getStringExtra("title");
            quizQueztionData(quizname);

        }

        recyclerView.setAdapter(questionQuizListAdapter);
         layoutManager = new CustomLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        layoutManager.setScrollEnabled(false);


        btnNext.setOnClickListener(v -> {
            nextMethod();
        });
    }

    private void nextMethod() {




        if (nextPosition == quizStudentModels.size()-1) {
            // Handle end of quiz
           startActivity(new Intent(StartQuiz.this, Finish.class));
           finish();
            return;
        }

        if (nextPosition == quizStudentModels.size()-2) {
            // Handle end of quiz
            btnNext.setText("Finish");
        }

        layoutManager.setScrollEnabled(true);
         nextPosition = getCurrentPosition() + 1;
        recyclerView.smoothScrollToPosition(nextPosition);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    layoutManager.setScrollEnabled(false);
                    recyclerView.removeOnScrollListener(this); // Remove the listener
                }
            }
        });

    }


    private int getCurrentPosition() {
        // Your logic to get the current position
        return nextPosition++; // Placeholder return
    }

    private void quizQueztionData(String quizname){
        progressBar.setVisibility(View.VISIBLE);
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        // Assume FirebaseDatabase instance has been initialized as 'database'
        DatabaseReference quizRef = database.getReference("Quiz");

// Query the database for the quiz with the name "Quiz 3: Swimming Stroke"
        quizRef.orderByChild("quizname").equalTo(quizname)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            // Assuming 'questions' is a child node of your quiz
                            DataSnapshot questionsSnapshot = snapshot.child("questions");

                            for (DataSnapshot questionSnapshot : questionsSnapshot.getChildren()) {
                                String questionText = questionSnapshot.child("question").getValue(String.class);
                                String correctAnswer = questionSnapshot.child("correct").getValue(String.class);
                                String type = questionSnapshot.child("type").getValue(String.class);

                                QuestionQuizModel.QuestionType questionType =
                                        "MULTIPLE_CHOICE".equals(type) ? QuestionQuizModel.QuestionType.MULTIPLE_CHOICE :
                                                QuestionQuizModel.QuestionType.IDENTIFICATION;

                                // For multiple choice, get the options
                                List<String> options = new ArrayList<>();
                                if (questionType == QuestionQuizModel.QuestionType.MULTIPLE_CHOICE) {
                                    for (DataSnapshot optionSnapshot : questionSnapshot.child("options").getChildren()) {
                                        options.add(optionSnapshot.getValue(String.class));
                                    }
                                }

                                // Create a new QuestionQuizModel object and add it to your list
                                quizStudentModels.add(new QuestionQuizModel(
                                        questionType,
                                        questionText,
                                        options.isEmpty() ? null : options,
                                        ""
                                ));


                                // Add the correct answer to your list
                                correctAnswerList.add(new QuestionQuizModel(
                                        questionType,
                                        questionText,
                                        options.isEmpty() ? null : options,
                                        correctAnswer
                                ));


                            }

                            questionQuizListAdapter.notifyDataSetChanged();
                            progressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle possible errors.
                        Toast.makeText(StartQuiz.this, "Error: " + databaseError, Toast.LENGTH_SHORT).show();
                    }
                });

    }


    private void quizQuestionData2() {
        // Add two multiple choice questions
        quizStudentModels.add(new QuestionQuizModel(
                QuestionQuizModel.QuestionType.MULTIPLE_CHOICE,
                "What is the capital of France?",
                Arrays.asList("Paris", "London", "Berlin", "Rome"),
                "Paris"
        ));

        quizStudentModels.add(new QuestionQuizModel(
                QuestionQuizModel.QuestionType.MULTIPLE_CHOICE,
                "Which gas is most prevalent in the Earth's atmosphere?",
                Arrays.asList("Oxygen", "Hydrogen", "Carbon Dioxide", "Nitrogen"),
                "Nitrogen"
        ));

// Add three identification questions
        quizStudentModels.add(new QuestionQuizModel(
                QuestionQuizModel.QuestionType.IDENTIFICATION,
                "What is the process by which plants make their food using sunlight called?",
                null,
                "Photosynthesis"
        ));

        quizStudentModels.add(new QuestionQuizModel(
                QuestionQuizModel.QuestionType.IDENTIFICATION,
                "What is the term for the amount of matter in an object?",
                null,
                "Mass"
        ));

        quizStudentModels.add(new QuestionQuizModel(
                QuestionQuizModel.QuestionType.IDENTIFICATION,
                "What is the name of the largest ocean on Earth?",
                null,
                "Pacific Ocean"
        ));

    }

}