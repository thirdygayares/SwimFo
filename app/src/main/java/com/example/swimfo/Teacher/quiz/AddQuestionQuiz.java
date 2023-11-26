package com.example.swimfo.Teacher.quiz;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.swimfo.R;
import com.example.swimfo.Teacher.adapter.QuestionQuizListAdapter;
import com.example.swimfo.Teacher.model.QuestionModel;
import com.example.swimfo.Teacher.model.QuestionQuizModel;
import com.example.swimfo.Teacher.model.QuizModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class AddQuestionQuiz extends Fragment {
    private View view;

    private RecyclerView recyclerView;
    public  QuestionQuizListAdapter questionQuizListAdapter;
    public  List<QuestionQuizModel> questionQuizModels = new ArrayList<>();
    private MaterialButton btnAddMultipleChoiceQuestion, btnAddIdenticationQuestion, btnAddQuiz;
    private Spinner spinner;
    private TextView txtQuizTitle;
    private ProgressBar progressBar;
    private String quizTitle, date;
    @Override
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.teacher_quiz_add_question_quiz, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        btnAddMultipleChoiceQuestion = view.findViewById(R.id.btnAddMultipleChoiceQuestion);
        btnAddIdenticationQuestion = view.findViewById(R.id.btnAddIdenticationQuestion);
        btnAddQuiz = view.findViewById(R.id.btnAddQuiz);
        spinner = view.findViewById(R.id.spinner);
        txtQuizTitle = view.findViewById(R.id.txtQuizTitle);
        progressBar = view.findViewById(R.id.progressBar);

        //set Quiz Title
        Bundle bundle = this.getArguments();
        quizTitle = bundle.getString("title");
        date = bundle.getString("date");
        txtQuizTitle.setText(quizTitle);



        //adapter
        questionQuizListAdapter = new QuestionQuizListAdapter(getContext(), questionQuizModels);

        //initialize data
        //quizQuestionData();

        //when user click add question
        btnAddMultipleChoiceQuestion.setOnClickListener(v -> addQuestion(0));
        btnAddIdenticationQuestion.setOnClickListener(v -> addQuestion(1));
        btnAddQuiz.setOnClickListener(v -> addQuiz());

        recyclerView.setAdapter(questionQuizListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //recyclerView.setHasFixedSize(true);
        return view;
    }

    private void addQuiz() {
        if (questionQuizModels.isEmpty()) {
            Toast.makeText(getContext(), "Please complete the question", Toast.LENGTH_SHORT).show();
        } else {
            if (questionQuizModels.get(questionQuizModels.size() - 1).getQuestionText().isEmpty() ||
                    questionQuizModels.get(questionQuizModels.size() - 1).getAnswer().isEmpty()) {
                Toast.makeText(getContext(), "All fields are required", Toast.LENGTH_SHORT).show();
            } else {
                progressBar.setVisibility(View.VISIBLE);

                QuizModel newQuiz = new QuizModel();
                newQuiz.createdby = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                newQuiz.created = ServerValue.TIMESTAMP; // Directly assign ServerValue.TIMESTAMP
                newQuiz.items = questionQuizModels.size();
                newQuiz.quizname = quizTitle;
                newQuiz.deadline = date; // Make sure 'date' is in the correct format or a timestamp

                // Add questions dynamically from the questionQuizModels list
                newQuiz.questions = new HashMap<>();
                for (int i = 0; i < questionQuizModels.size(); i++) {
                    QuestionModel questionModel = new QuestionModel();
                    questionModel.type = String.valueOf(questionQuizModels.get(i).getType());
                    questionModel.question = questionQuizModels.get(i).getQuestionText();

                    // Check if it's a multiple-choice question and set options
                    if (questionQuizModels.get(i).getType() == QuestionQuizModel.QuestionType.MULTIPLE_CHOICE) {
                        questionModel.options = new HashMap<>();
                        List<String> options = questionQuizModels.get(i).getOptions();
                        for (int j = 0; j < options.size(); j++) {
                            questionModel.options.put(String.valueOf(j + 1), options.get(j));
                        }
                    }
                    questionModel.correct = questionQuizModels.get(i).getAnswer();

                    newQuiz.questions.put("question" + (i + 1), questionModel);
                }

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference quizzesRef = database.getReference("Quiz");

                quizzesRef.child(quizTitle).setValue(newQuiz).addOnSuccessListener(aVoid -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Quiz Added", Toast.LENGTH_SHORT).show();
                    // Navigate to the QuizHomePage
                    navigateToQuizHomePage();
                }).addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Something Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        }
    }

    private void addQuestion(int btn) {
        if (questionQuizModels.isEmpty()) {
            setUp(btn);
        } else {
            if (questionQuizModels.get(questionQuizModels.size() - 1).getQuestionText().isEmpty() || questionQuizModels.get(questionQuizModels.size()-1).getAnswer() == "") {
                Toast.makeText(getContext(),"All fields Are Required", Toast.LENGTH_SHORT).show();
            } else {
                setUp(btn);
            }
        }
    }

    private void setUp(int btn) {

        if (btn == 0) {
            questionQuizModels.add(new QuestionQuizModel(
                    QuestionQuizModel.QuestionType.MULTIPLE_CHOICE,
                    "",
                    Arrays.asList("", "", "", ""),
                    ""
            ));
            questionQuizListAdapter.notifyItemInserted(questionQuizModels.size() - 1);
        } else if (btn == 1) {
            questionQuizModels.add(new QuestionQuizModel(
                    QuestionQuizModel.QuestionType.IDENTIFICATION,
                    "",
                    null,
                    ""
            ));
            questionQuizListAdapter.notifyItemInserted(questionQuizModels.size() - 1);
        }
    }

    private void quizQuestionData() {
        // Add two multiple choice questions
        questionQuizModels.add(new QuestionQuizModel(
                QuestionQuizModel.QuestionType.MULTIPLE_CHOICE,
                "What is the capital of France?",
                Arrays.asList("Paris", "London", "Berlin", "Rome"),
                "Paris"
        ));

        questionQuizModels.add(new QuestionQuizModel(
                QuestionQuizModel.QuestionType.MULTIPLE_CHOICE,
                "Which gas is most prevalent in the Earth's atmosphere?",
                Arrays.asList("Oxygen", "Hydrogen", "Carbon Dioxide", "Nitrogen"),
                "Nitrogen"
        ));

// Add three identification questions
        questionQuizModels.add(new QuestionQuizModel(
                QuestionQuizModel.QuestionType.IDENTIFICATION,
                "What is the process by which plants make their food using sunlight called?",
                null,
                "Photosynthesis"
        ));

        questionQuizModels.add(new QuestionQuizModel(
                QuestionQuizModel.QuestionType.IDENTIFICATION,
                "What is the term for the amount of matter in an object?",
                null,
                "Mass"
        ));

        questionQuizModels.add(new QuestionQuizModel(
                QuestionQuizModel.QuestionType.IDENTIFICATION,
                "What is the name of the largest ocean on Earth?",
                null,
                "Pacific Ocean"
        ));

    }


    private void navigateToQuizHomePage() {
        QuizHomePage quizHomePage = new QuizHomePage();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_content, quizHomePage).commit();
    }


}