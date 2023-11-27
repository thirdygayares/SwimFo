package com.example.swimfo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.swimfo.Student.Fragment.QuizFragment;
import com.example.swimfo.Student.Quiz.StartQuiz;
import com.example.swimfo.Student.adapter.QuestionQuizListAdapter;
import com.example.swimfo.Student.adapter.QuestionQuizPreviewListAdapter;
import com.example.swimfo.Student.model.QuestionQuizPreviewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Finish extends AppCompatActivity {
    TextView score, wronh,missed;
    String quizName, sectionId;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    private List<QuestionQuizPreviewModel> quizStudentModels = new ArrayList<>();
    private QuestionQuizPreviewListAdapter questionQuizPreviewListAdapter;

    private List<QuestionQuizPreviewModel> correctAnswerList = new ArrayList<>();
    private static Map<String, String> userAnswersMap = new HashMap<>(); // To store user's answers

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
        score = findViewById(R.id.score);
        wronh = findViewById(R.id.wscore);
        missed = findViewById(R.id.mscore);
        recyclerView = findViewById(R.id.recyclerView);
        Integer points =  getIntent().getIntExtra("correct", 0);
        Integer wpoints =  getIntent().getIntExtra("wrong", 0);
        Integer mpoints =  getIntent().getIntExtra("unanswered", 0);
        sectionId = getIntent().getStringExtra("sectionId");
        quizName = getIntent().getStringExtra("quizName");
        progressBar = findViewById(R.id.progressBar);


        score.setText(String.valueOf(points));
        wronh.setText(String.valueOf(wpoints));
        missed.setText(String.valueOf(mpoints));

        questionQuizPreviewListAdapter = new QuestionQuizPreviewListAdapter(this, quizStudentModels);

        setData();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(questionQuizPreviewListAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
    }

    private void setData() {
        progressBar.setVisibility(View.VISIBLE);
        FirebaseDatabase database = FirebaseDatabase.getInstance();


        // Fetch user answers and store them in a map
        DatabaseReference answerRef = database.getReference("QuizResult").child(quizName).child(sectionId).child(FirebaseAuth.getInstance().getUid()).child("answers");
        answerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Store each answer in the map with the questionId as the key
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String questionId = snapshot.getKey();
                    String userAnswer = snapshot.getValue(String.class);
                    userAnswersMap.put(questionId, userAnswer);
                }


                Log.d("TAG", "questionId: " + userAnswersMap);


                progressBar.setVisibility(View.GONE);


                // Assume FirebaseDatabase instance has been initialized as 'database'
                DatabaseReference quizRef = database.getReference("Quiz");

                // Query the database for the quiz with the name "Quiz 3: Swimming Stroke"
                quizRef.orderByChild("quizname").equalTo(quizName)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    // Assuming 'questions' is a child node of your quiz
                                    DataSnapshot questionsSnapshot = snapshot.child("questions");

                                    for (DataSnapshot questionSnapshot : questionsSnapshot.getChildren()) {
                                        String questionId = questionSnapshot.getKey();
                                        String questionText = questionSnapshot.child("question").getValue(String.class);
                                        String correctAnswer = questionSnapshot.child("correct").getValue(String.class);
                                        String type = questionSnapshot.child("type").getValue(String.class);

                                        QuestionQuizPreviewModel.QuestionType questionType =
                                                "MULTIPLE_CHOICE".equals(type) ? QuestionQuizPreviewModel.QuestionType.MULTIPLE_CHOICE :
                                                        QuestionQuizPreviewModel.QuestionType.IDENTIFICATION;

                                        // For multiple choice, get the options
                                        List<String> options = new ArrayList<>();
                                        if (questionType == QuestionQuizPreviewModel.QuestionType.MULTIPLE_CHOICE) {
                                            for (DataSnapshot optionSnapshot : questionSnapshot.child("options").getChildren()) {
                                                options.add(optionSnapshot.getValue(String.class));
                                            }
                                        }



                                        // Create a new QuestionQuizPreviewModel object and add it to your list
                                        quizStudentModels.add(new QuestionQuizPreviewModel(
                                                questionId,
                                                questionType,
                                                questionText,
                                                options.isEmpty() ? null : options,
                                                userAnswersMap.get(questionId)
                                        ));

                                        Log.d("USERANSWERMAP", "questionId: " + questionId);
                                        Log.d("USERANSWERMAP", "userAnswer: " + userAnswersMap.get(questionId));
                                        Log.d("TAG", "questionIds: " + userAnswersMap);

                                        // Add the correct answer to your list
                                        correctAnswerList.add(new QuestionQuizPreviewModel(
                                                questionId,
                                                questionType,
                                                questionText,
                                                options.isEmpty() ? null : options,
                                                correctAnswer
                                        ));

                                    }


                                    questionQuizPreviewListAdapter.notifyDataSetChanged();
                                    progressBar.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                // Handle possible errors.
                                Toast.makeText(Finish.this, "Error: " + databaseError, Toast.LENGTH_SHORT).show();
                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors
            }
        });




    }

    public void Finish(View view) {
        Intent intent = new Intent(Finish.this, QuizFragment.class);
        startActivity(intent);
        finishAffinity();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Finish.this, QuizFragment.class);
        startActivity(intent);
        finishAffinity();
    }
}