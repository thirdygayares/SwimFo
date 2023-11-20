package com.example.swimfo.Teacher.coteacher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.swimfo.Teacher.adapter.CoTeacherListAdapter;
import com.example.swimfo.Teacher.model.CoTeacherListModel;
import com.example.swimfo.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CoTeacherMain extends Fragment {

    private View view;

    //RecycleView
    RecyclerView recyclerView;
    CoTeacherListAdapter coTeacherListAdapter;

    FirebaseDatabase rootNode;
    DatabaseReference reference;
    FirebaseAuth firebaseAuth;
    ArrayList<CoTeacherListModel> list;
    TextView secTv;
    private MaterialButton addBtn;

    private ProgressBar progressBar;
    ImageView back ;
    @Override
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.teacher_fragment_co_teacher_main, container, false);
        addBtn = view.findViewById(R.id.addBtn);
        progressBar = view.findViewById(R.id.progressBar);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddCoTeacher.class);
                startActivity(intent);
            }
        });


        firebaseAuth = FirebaseAuth.getInstance();

        rootNode = FirebaseDatabase.getInstance();
        recyclerView = view.findViewById(R.id.recycle_view_av);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        reference = rootNode.getReference("coteacherlist");
        // Assuming 'reference' is already pointing to the correct database reference.
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                list.clear();
                if (snapshot.hasChildren()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        CoTeacherListModel studentModel = dataSnapshot.getValue(CoTeacherListModel.class);
                        list.add(studentModel);
                    }
                } else {
                    Toast.makeText(getContext(), "No Teacher Available", Toast.LENGTH_SHORT).show();
                }
                coTeacherListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle database error
                Toast.makeText(getContext(), "No Teacher Available", Toast.LENGTH_SHORT).show();
            }
        });




        list = new ArrayList<>();

        coTeacherListAdapter = new CoTeacherListAdapter(getContext(),list);
        coTeacherListAdapter.setHasStableIds(true);
        recyclerView.setAdapter(coTeacherListAdapter);

        return view;
    }
}