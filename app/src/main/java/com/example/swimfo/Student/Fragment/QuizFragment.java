package com.example.swimfo.Student.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
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

import com.example.swimfo.Student.Quiz.QuizContainer;
import com.example.swimfo.R;
import com.example.swimfo.Student.Quiz.StartQuiz;
import com.example.swimfo.Teacher.adapter.MyInterface;
import com.example.swimfo.Teacher.adapter.QuizListAdapter;
import com.example.swimfo.Teacher.model.QuizListModel;
import com.example.swimfo.Teacher.quiz.AddQuiz;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class QuizFragment extends Fragment implements MyInterface {
    Button start;

    private View view;
    private RecyclerView recyclerView;
    private QuizListAdapter quizListAdapter;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_quiz, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        progressBar = view.findViewById(R.id.progressBar);
        //initialize adapter
        quizListAdapter = new QuizListAdapter(getContext(), quizListModels, this);

        //set up data
        quizListData();

        //set up recycler view
        recyclerView.setAdapter(quizListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


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
        getContext().startActivity(new Intent(getContext(), StartQuiz.class)
                .putExtra("title", quizListModels.get(pos).getName()));




    }
}