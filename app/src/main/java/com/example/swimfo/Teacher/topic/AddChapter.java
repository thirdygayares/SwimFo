package com.example.swimfo.Teacher.topic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.swimfo.Chapter;
import com.example.swimfo.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddChapter extends AppCompatActivity {
    private EditText editTextVideoUrl, editTextTitle, editTextDescription, editTextDemonstrationGuide;
    private Button buttonSave;
    ProgressBar pb;
    private DatabaseReference lessonRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_chapter);
        editTextVideoUrl = findViewById(R.id.editTextVideoUrl);
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        pb = findViewById(R.id.pb);
        editTextDemonstrationGuide = findViewById(R.id.editTextDemonstrationGuide);
        buttonSave = findViewById(R.id.buttonSave);

        Intent getData = getIntent();

//        intent.putExtra("lessonId", lesson.getLessonId());

        // Initialize Firebase reference
        String lessonId = getData.getStringExtra("lessonId"); // Replace with your lesson ID

        lessonRef = FirebaseDatabase.getInstance().getReference().child("lessons").child(lessonId).child("chapters");

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChapter();
            }
        });
    }
    private void saveChapter() {
        String videoUrl = editTextVideoUrl.getText().toString();
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        String demonstrationGuide = editTextDemonstrationGuide.getText().toString();

        if (videoUrl.isEmpty() || title.isEmpty() || description.isEmpty() || demonstrationGuide.isEmpty()) {
            Toast.makeText(getApplicationContext(), "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        String chapterId = lessonRef.push().getKey(); // Generate push key

        Chapter chapter = new Chapter(chapterId, title, videoUrl, demonstrationGuide, description);

        if (chapterId != null) {
            lessonRef.child(chapterId).setValue(chapter)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(getApplicationContext(), "Chapter saved successfully", Toast.LENGTH_SHORT).show();
                        // Clear input fields if needed
                        clearInputFields(); // Clear input fields
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getApplicationContext(), "Failed to save chapter", Toast.LENGTH_SHORT).show();
                    });
        }
    }
    private void clearInputFields() {
        editTextVideoUrl.setText("");
        editTextTitle.setText("");
        editTextDescription.setText("");
        editTextDemonstrationGuide.setText("");
    }

    public void BackClick(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}