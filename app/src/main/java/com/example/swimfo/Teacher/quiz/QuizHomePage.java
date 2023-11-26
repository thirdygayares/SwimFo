package com.example.swimfo.Teacher.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.swimfo.R;
import com.example.swimfo.Student.Quiz.QuizInstruction;
import com.example.swimfo.Teacher.adapter.MyInterface;
import com.example.swimfo.Teacher.adapter.QuizListAdapter;
import com.example.swimfo.Teacher.model.QuizListModel;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class QuizHomePage extends Fragment implements MyInterface {

    private View view;
    private RecyclerView recyclerView;
    private QuizListAdapter quizListAdapter;
    private List<QuizListModel> quizListModels = new ArrayList<>();
    private ImageView btnAddQuiz;
    private ProgressBar progressBar;


    @Override
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.teacher_fragment_quiz_homepage, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        btnAddQuiz = view.findViewById(R.id.btnAddQuiz);
        progressBar = view.findViewById(R.id.progressBar);
        //initialize adapter
        quizListAdapter = new QuizListAdapter(getContext(), quizListModels, this);

        //set up data
        quizListData();

        //set up recycler view
        recyclerView.setAdapter(quizListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //btn add quiz
        btnAddQuiz.setOnClickListener(view -> {
            AddQuiz addQuiz = new AddQuiz();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_content, addQuiz).commit();
        });

        return view;
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
        if (categories.equals("delete")){
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference quizzesRef = database.getReference("Quiz");
            quizzesRef.child(quizListModels.get(pos).getName()).removeValue();
            quizListModels.remove(pos);
            quizListAdapter.notifyDataSetChanged();
            Toast.makeText(getContext(), "Quiz deleted", Toast.LENGTH_SHORT).show();
        }
        else {
            getContext().startActivity(new Intent(getContext(), SectionLeaderBoard.class)
                    .putExtra("title", quizListModels.get(pos).getName()));
        }
    }
}
