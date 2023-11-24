package com.example.swimfo.Student.Quiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.swimfo.R;
import com.example.swimfo.Student.Fragment.QuizFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class QuizInstruction extends Fragment {

     private String quizname;
     private View view;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_quiz_instruction, container, false);

        Button start_quiz = view.findViewById(R.id.start_quiz);


        start_quiz.setOnClickListener(v -> startActivity(new Intent(getContext(), QuizFragment.class)));




        return view;
    }


    private void Section() {



    }

}