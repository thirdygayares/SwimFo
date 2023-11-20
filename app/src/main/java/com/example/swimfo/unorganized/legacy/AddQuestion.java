package com.example.swimfo.unorganized.legacy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.swimfo.Modelclass;
import com.example.swimfo.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddQuestion extends Fragment {
    private View view;
    ImageView quizImg,back;
    EditText question,c1,c2,c3,c4;
    Spinner answer,category;
    Button add;

    public static String correctAns;

    private FirebaseDatabase root = FirebaseDatabase.getInstance();
    String mcategory;
    private DatabaseReference reference;
    @Override
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.activity_add_question, container, false);

        Intent getData = getActivity().getIntent();
        mcategory = getData.getStringExtra("category");



        answer = view.findViewById(R.id.ans);

        add  = view.findViewById(R.id.addQuiz);
        question = view.findViewById(R.id.question);
        c1 = view.findViewById(R.id.c1);
        c2 = view.findViewById(R.id.c2);
        c3  = view.findViewById(R.id.c3);
        c4  = view.findViewById(R.id.c4);

        ArrayAdapter<CharSequence> ans = ArrayAdapter.createFromResource(getContext(),R.array.answer, android.R.layout.simple_spinner_item);
        ans.setDropDownViewResource(android.R.layout.simple_spinner_item);
        answer.setAdapter(ans);

        add.setOnClickListener(view -> {
            String myQuest = question.getText().toString();
            String mC1 = c1.getText().toString();
            String mC2 = c2.getText().toString();
            String mC3 = c3.getText().toString();
            String mC4 = c4.getText().toString();
            String mAns = answer.getSelectedItem().toString();

            if (mC1.isEmpty() || mC2.isEmpty() || mC3.isEmpty() || mC4.isEmpty() || myQuest.isEmpty()){
                Toast.makeText(getContext(), "Fill up fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (mAns.equalsIgnoreCase("Choice 1")){
                correctAns = mC1;
            }else if (mAns.equalsIgnoreCase("Choice 2")){
                correctAns = mC2;
            }else if(mAns.equalsIgnoreCase("Choice 3")){
                correctAns = mC3;
            }else if(mAns.equalsIgnoreCase("Choice 4")){
                correctAns = mC4;
            }


            if(!TextUtils.isEmpty(myQuest) && !TextUtils.isEmpty(mC1)  && !TextUtils.isEmpty(mC2)  && !TextUtils.isEmpty(mC3) && !TextUtils.isEmpty(mC4)){
                uploadInfo(myQuest,mC1,mC2,mC3,mC4,correctAns,mcategory);
            }else {
                Toast.makeText(getContext(),"Complete fields",Toast.LENGTH_SHORT).show();
            }

        });

        return view;
    }

    private void uploadInfo(String myQuest, String mC1, String mC2, String mC3, String mC4, String mAns, String category) {

        reference = root.getReference("NewQuiz");
        Modelclass quiz = new Modelclass(myQuest,mC1,mC2,mC3,mC4,"1",mAns,"1");

        reference.child(category).push().setValue(quiz);
        c1.setText("");
        c2.setText("");
        c3.setText("");
        c4.setText("");
        question.setText("");

        Toast.makeText(getContext(), "Success, You can add another question.", Toast.LENGTH_SHORT).show();

    }

}