package com.example.swimfo.Teacher.section;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.swimfo.unorganized.Adapter.AvailableStudentAdapter;
import com.example.swimfo.R;
import com.example.swimfo.unorganized.Model.StudentModel;
import com.example.swimfo.Teacher.AddStudent;
import com.example.swimfo.Teacher.TeacherMain;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StudentList extends AppCompatActivity {
    String sectionName ;
    String sectionId;

    //RecycleView
    RecyclerView recyclerView;
    AvailableStudentAdapter availableStudentAdapter;

    FirebaseDatabase rootNode;
    DatabaseReference reference;
    FirebaseAuth firebaseAuth;
    ArrayList<StudentModel> list;
    TextView secTv;

    ImageView back ;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        // Inside the StudentList class
        Intent intent = getIntent();

        if (intent != null) {
             sectionName = intent.getStringExtra("section");
             sectionId = intent.getStringExtra("id");
        }else{
            Toast.makeText(this, "Some Error Occurred", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
//        Toast.makeText(this, sectionId, Toast.LENGTH_SHORT).show();

        firebaseAuth = FirebaseAuth.getInstance();

        rootNode = FirebaseDatabase.getInstance();
        recyclerView = findViewById(R.id.recycle_view_av);
        secTv = findViewById(R.id.sectionTv);
        secTv.setText(sectionName);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();


        availableStudentAdapter = new AvailableStudentAdapter(this,list);
        availableStudentAdapter.setHasStableIds(true);
        recyclerView.setAdapter(availableStudentAdapter);
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        String uid = currentUser.getUid();

        reference = rootNode.getReference("studentlist");

        Query query = reference.orderByChild("sectionId").equalTo(sectionId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot snapshot) {
                list.clear();
                if (snapshot.hasChildren()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        StudentModel studentModel = dataSnapshot.getValue(StudentModel.class);
                        list.add(studentModel);
                    }
                } else {
                    Toast.makeText(StudentList.this, "No Students Available", Toast.LENGTH_SHORT).show();
                }
                availableStudentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled( DatabaseError error) {
                // Handle database error
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(StudentList.this, TeacherMain.class);

        startActivity(intent);
        super.onBackPressed();
    }

    public void ClickAdd(View view) {
        Intent intent = new Intent(StudentList.this, AddStudent.class);
        intent.putExtra("section", sectionName);
        intent.putExtra("id", sectionId);
        startActivity(intent);
    }

    public void LeaderBoard(View view) {
        Intent intent = new Intent(StudentList.this, Leaderboard.class);
        intent.putExtra("section", sectionName);
        intent.putExtra("id", sectionId);
        startActivity(intent);
    }
}