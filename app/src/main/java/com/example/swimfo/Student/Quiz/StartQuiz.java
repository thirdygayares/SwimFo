package com.example.swimfo.Student.Quiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uzairiqbal.circulartimerview.CircularTimerListener;
import com.uzairiqbal.circulartimerview.CircularTimerView;
import com.uzairiqbal.circulartimerview.TimeFormatEnum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
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
    private int correctAnswers = 0;
    private int wrongAnswers = 0;
    private int unanswered = 0;

    private Date startTime;
    private Date endTime;
    CircularTimerView progressTimerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_quiz);

        recyclerView = findViewById(R.id.recyclerView);
        btnNext = findViewById(R.id.btnNext);
        progressBar = findViewById(R.id.progressBar);
        progressTimerView = findViewById(R.id.progress_circular);

        questionQuizListAdapter = new QuestionQuizListAdapter(this, quizStudentModels);

        progressTimerView.setCircularTimerListener(new CircularTimerListener() {
            @Override
            public String updateDataOnTick(long remainingTimeInMs) {
                return String.valueOf((int)Math.ceil((remainingTimeInMs / 1000.f)));
            }

            @Override
            public void onTimerFinished() {
//            Toast.makeText(QuizContainer.this, "FINISHED", Toast.LENGTH_SHORT).show();
                progressTimerView.setPrefix("");
                progressTimerView.setSuffix("");
                progressTimerView.setText("FINISHED THANKS!");

                    if (quizStudentModels.get(nextPosition).getAnswer().equals("")){
                        Toast.makeText(StartQuiz.this, "Missed", Toast.LENGTH_SHORT).show();
                     }

                     nextMethod();


            }
        }, 30, TimeFormatEnum.SECONDS, 10);

        progressTimerView.startTimer();


        //start Time
        startTime = new Date(System.currentTimeMillis());
        Log.d("Start Time", String.valueOf(startTime));


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
            if(quizStudentModels.get(nextPosition).getAnswer().equals("")) {
                Toast.makeText(this, "Please enter an answer", Toast.LENGTH_SHORT).show();
                return;
            }
            nextMethod();
        });
    }

    private void nextMethod() {

        if(quizStudentModels.get(nextPosition).getAnswer().equals(correctAnswerList.get(nextPosition).getAnswer())) {
            correctAnswers++;
            Toast.makeText(this, "Correct", Toast.LENGTH_SHORT).show();
        } else if(quizStudentModels.get(nextPosition).getAnswer().equals("")){
            unanswered++;

        } else if(!quizStudentModels.get(nextPosition).getAnswer().equals(correctAnswerList.get(nextPosition).getAnswer())){
            Toast.makeText(this, "Wrong", Toast.LENGTH_SHORT).show();
            wrongAnswers++;
        }


        if (nextPosition == quizStudentModels.size()-1) {
            // Handle share the answers
            SaveAnswer();

            return;
        }

        if (nextPosition == quizStudentModels.size()-2) {
            // Handle end of quiz
            btnNext.setText("Finish");
        }


        progressTimerView.startTimer();


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

    private void SaveAnswer() {
        progressBar.setVisibility(View.VISIBLE);

        //fetch some user data
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        String name = user.getDisplayName();

        //start Time
        endTime = new Date(System.currentTimeMillis());
        Log.d("End  Time", String.valueOf(endTime));

        long elapsedMilliseconds = endTime.getTime() - startTime.getTime();
        int elapsedSeconds = (int) (elapsedMilliseconds / 1000);
        int elapsedMinutes = elapsedSeconds / 60;
        int elapsedHours = elapsedMinutes / 60;
        elapsedSeconds %= 60;
        elapsedMinutes %= 60;

        String elapsedTimeString = String.format("%02d:%02d:%02d", elapsedHours, elapsedMinutes, elapsedSeconds);
        Log.d("Total Time", String.valueOf(elapsedTimeString));

        HashMap<String, Object> hashMap = new HashMap<>();
        for (int i = 0; i < quizStudentModels.size(); i++) {
            hashMap.put("question" + (i + 1), quizStudentModels.get(i).getAnswer());
        }

        HashMap<String, Object> hashMap2 = new HashMap<>();
        hashMap2.put("correct", correctAnswers);
        hashMap2.put("wrong", wrongAnswers);
        hashMap2.put("unanswered", unanswered);
        hashMap2.put("time", elapsedTimeString);
        hashMap2.put("date", new Date().toString());
        hashMap2.put("quizname", getIntent().getStringExtra("title"));
        hashMap2.put("answers", hashMap);
        hashMap2.put("studentName", name);

        FirebaseDatabase.getInstance().getReference("QuizResult")
                .child(getIntent().getStringExtra("title"))
                .child(getIntent().getStringExtra("sectionId"))
                .child(uid)
                .updateChildren(hashMap2);


        Intent intent = new Intent(StartQuiz.this, Finish.class);
        intent.putExtra("correct", correctAnswers);
        intent.putExtra("wrong", wrongAnswers);
        intent.putExtra("unanswered", unanswered);
        startActivity(intent);
        finishAffinity();
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


    //get Data

}