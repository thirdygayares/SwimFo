package com.example.swimfo.Teacher.Topic;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.swimfo.Adapter.LessonsAdapter;
import com.example.swimfo.Chapter;
import com.example.swimfo.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LessonsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LessonsAdapter lessonsAdapter;
    private DatabaseReference databaseReference;
    TextView tvt;
    ImageView ban;
    ArrayList<Lesson> lessons;
    String topicId;
    String imgUrl;
    String title;
    String desc;
    Button add;
    Boolean isAdmin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessons);
        Intent getDta = getIntent();
        topicId = getDta.getStringExtra("topicId");
        imgUrl = getDta.getStringExtra("imgUrl");
        title = getDta.getStringExtra("title");
        desc = getDta.getStringExtra("desc");
        isAdmin = getDta.getExtras().getBoolean("isAdmin");


        recyclerView = findViewById(R.id.recyclerViewLessons);
        tvt = findViewById(R.id.desc);
        ban = findViewById(R.id.banner);
        add = findViewById(R.id.add);
        recyclerView = findViewById(R.id.recyclerViewLessons);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        lessonsAdapter = new LessonsAdapter();
        recyclerView.setAdapter(lessonsAdapter);

        if (isAdmin){
            add.setVisibility(View.VISIBLE);
        }

        lessons = new ArrayList<>();

        if (imgUrl != null){
            Glide.with(this).load(imgUrl).into(ban);
        }
        tvt.setText(desc);

        databaseReference = FirebaseDatabase.getInstance().getReference("lessons");
        setupLessonsListener();



        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddSectionDialog();
            }
        });
    }
    private void setupLessonsListener() {
        Query query = databaseReference.orderByChild("topicId").equalTo(topicId);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Lesson> lessons = new ArrayList<>();
                lessons.clear();


                for (DataSnapshot lessonSnapshot : dataSnapshot.getChildren()) {
                    // Retrieve the individual fields for the lesson
                    String lessonName = lessonSnapshot.child("lessonName").getValue(String.class);
                    String lessonId = lessonSnapshot.child("lessonId").getValue(String.class);
                    String topicId = lessonSnapshot.child("topicId").getValue(String.class);

                    // Prepare an ArrayList to store the chapters for this lesson
                    List<Chapter> chaptersList = new ArrayList<>();

                    // Retrieve the chapters for this lesson
                    DataSnapshot chaptersSnapshot = lessonSnapshot.child("chapters");
                    for (DataSnapshot chapterSnapshot : chaptersSnapshot.getChildren()) {
                        String chapterId = chapterSnapshot.child("chapterId").getValue(String.class);
                        String chapterTitle = chapterSnapshot.child("chapterTitle").getValue(String.class);
                        String chapterVUrl = chapterSnapshot.child("videoUrl").getValue(String.class);
                        String chapterDemo = chapterSnapshot.child("chapterDemo").getValue(String.class);
                        String chapterDesc = chapterSnapshot.child("chapterDesc").getValue(String.class);
                        Chapter chapter = new Chapter(chapterId, chapterTitle,chapterVUrl,chapterDemo,chapterDesc);
                        chaptersList.add(chapter);

                    }

                    // Create the Lesson object and add it to the lessons ArrayList
                    Lesson lesson = new Lesson(lessonId, topicId, lessonName, chaptersList);
                    lessons.add(lesson);
                }
// Sort the lessons based on lessonName
                Collections.sort(lessons, new Comparator<Lesson>() {
                    @Override
                    public int compare(Lesson lesson1, Lesson lesson2) {
                        return lesson1.getLessonName().compareTo(lesson2.getLessonName());
                    }
                });



                lessonsAdapter.setLessons(lessons, isAdmin);
                lessonsAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("LessonsActivity", "Error fetching lessons from Firebase: " + databaseError.getMessage());
            }
        });

    }

    // Method to show the AlertDialog for adding a new section
    private void showAddSectionDialog() {
        // Inflate the custom layout
        View dialogView = LayoutInflater.from(this).inflate(R.layout.add_lesson, null);

        // Get references to the UI elements in the custom layout
        TextView titleTextView = dialogView.findViewById(R.id.dialogTitle);
        EditText editText = dialogView.findViewById(R.id.dialogEditText);
        Button cancelButton = dialogView.findViewById(R.id.dialogCancelButton);
        Button saveButton = dialogView.findViewById(R.id.dialogSaveButton);

        // Create the AlertDialog using the AlertDialog.Builder and set the custom view
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
            if(enteredText.isEmpty()){
                Toast.makeText(this, "Please specify lesson number.", Toast.LENGTH_SHORT).show();
            return;
            }
            // Perform actions with the entered text
//            Toast.makeText(MainTeacher.this, "Entered text: " + enteredText, Toast.LENGTH_SHORT).show();
            saveSectionToFirebase(enteredText);
            dialog.dismiss();

        });
    }

    private void saveSectionToFirebase(String lessonName) {
        DatabaseReference lessonRef = FirebaseDatabase.getInstance().getReference("lessons");
        String lessonId = lessonRef.push().getKey();

//        if (lessonId != null) {
//           Lesson lesson = new Lesson(lessonId,topicId,"Lesson "+lessonName,null);
//
//            lessonRef.child(lessonId).setValue(lesson)
//                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//                            Toast.makeText(getApplicationContext(), "Lesson saved successfully", Toast.LENGTH_SHORT).show();
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(Exception e) {
//                            Toast.makeText(getApplicationContext(), "Failed to save lesson", Toast.LENGTH_SHORT).show();
//                        }});
//        }
        if (lessonId != null) {
            String lessonFullName = "Lesson " + lessonName;

            // Check if lesson with the same name exists
            lessonRef.orderByChild("lessonName").equalTo(lessonFullName).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange( DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Lesson already exists
                        Toast.makeText(getApplicationContext(), "Lesson already exists", Toast.LENGTH_SHORT).show();
                    } else {
                        // Lesson doesn't exist, save it
                        Lesson lesson = new Lesson(lessonId, topicId, lessonFullName, null);

                        lessonRef.child(lessonId).setValue(lesson)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getApplicationContext(), "Lesson saved successfully", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(Exception e) {
                                        Toast.makeText(getApplicationContext(), "Failed to save lesson", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }

                @Override
                public void onCancelled( DatabaseError databaseError) {
                    // Handle cancellation or errors
                }
            });
        }

    }

    public void BackClick(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}