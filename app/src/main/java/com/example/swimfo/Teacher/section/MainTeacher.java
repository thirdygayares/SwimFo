package com.example.swimfo.Teacher.section;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.swimfo.Adapter.AvailableClassAdapter;
import com.example.swimfo.LoginActivity;
import com.example.swimfo.Model.SectionModel;
import com.example.swimfo.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainTeacher extends Fragment {
    View view;

    //RecycleView
    RecyclerView recyclerView;
    AvailableClassAdapter availableClassAdapter;
    Button logout;

    FirebaseDatabase rootNode;
    DatabaseReference reference;
    FirebaseAuth firebaseAuth;
    ArrayList<SectionModel> list;

    @Override
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.activity_main_teacher, container, false);


        Button addButton = view.findViewById(R.id.addBtn);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddSectionDialog();
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();

        rootNode = FirebaseDatabase.getInstance();
        recyclerView = view.findViewById(R.id.recycle_view_av);
        logout = view.findViewById(R.id.logoutBtn);



        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        list = new ArrayList<>();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
               getActivity().finishAffinity();
            }
        });


        availableClassAdapter = new AvailableClassAdapter(getContext(),list);
        availableClassAdapter.setHasStableIds(true);
        recyclerView.setAdapter(availableClassAdapter);
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        String uid = currentUser.getUid();

        reference = rootNode.getReference("users/").child("sections").child("4smAFPenKKda95RNnHshnIaUHUV2");

        reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                list.clear();
                if (snapshot.hasChildren()){
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        SectionModel classModel = dataSnapshot.getValue(SectionModel.class);
                        list.add(classModel);
                    }

                }else {
                    Toast.makeText(getContext(), "No Section Available", Toast.LENGTH_SHORT).show();
                }
                availableClassAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    return view;
    }

    // Method to show the AlertDialog for adding a new section
    private void showAddSectionDialog() {
        // Inflate the custom layout
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.add_section, null);

        // Get references to the UI elements in the custom layout
        TextView titleTextView = dialogView.findViewById(R.id.dialogTitle);
        EditText editText = dialogView.findViewById(R.id.dialogEditText);
        Button cancelButton = dialogView.findViewById(R.id.dialogCancelButton);
        Button saveButton = dialogView.findViewById(R.id.dialogSaveButton);

        // Create the AlertDialog using the AlertDialog.Builder and set the custom view
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(dialogView);

        // Create and show the AlertDialog
        final AlertDialog dialog = builder.create();
        dialog.show();

        // Handle cancel button click
        cancelButton.setOnClickListener(v -> {
            dialog.dismiss();
        });

        // Handle save button click
        saveButton.setOnClickListener(v -> {
            String enteredText = editText.getText().toString().trim();
            // Perform actions with the entered text
//            Toast.makeText(MainTeacher.this, "Entered text: " + enteredText, Toast.LENGTH_SHORT).show();
            saveSectionToFirebase(enteredText);
            dialog.dismiss();

        });
    }

    private void saveSectionToFirebase(String sectionName) {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        String uid = currentUser.getUid();
        DatabaseReference sectionsRef = FirebaseDatabase.getInstance().getReference("users/sections/"+uid);
        String sectionId = sectionsRef.push().getKey();

        if (sectionId != null) {
            SectionModel section = new SectionModel(sectionId, sectionName);

            sectionsRef.child(sectionId).setValue(section)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getContext(), "Section saved successfully", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText(getContext(), "Failed to save section", Toast.LENGTH_SHORT).show();
                        }});
        }
    }


}