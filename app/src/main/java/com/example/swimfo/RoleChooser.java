package com.example.swimfo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.swimfo.unorganized.legacy.AddQuestion;
import com.example.swimfo.Teacher.TeacherMain;
import com.example.swimfo.Teacher.topic.TopicManagement;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RoleChooser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_chooser);
    }


    public void ClickSection(View view) {
        Intent intent = new Intent(RoleChooser.this, TeacherMain.class);
        startActivity(intent);
    }

    public void ClickTopic(View view) {
        Intent intent = new Intent(RoleChooser.this, TopicManagement.class);
        intent.putExtra("teacher", true);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        //alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //set title
        builder.setTitle("Exit");
        //set message
        builder.setMessage("Are you sure you want to exit?!");
        //positive yes
        builder.setPositiveButton("YES", (dialog, which) -> {
            //remove section subject
            //dismiss dialog
            dialog.dismiss();
            finishAffinity();
            System.exit(0);

        });
        //negative no
        builder.setNegativeButton("NO", (dialog, which) -> {
            //dismiss dialog
            dialog.dismiss();
        });
        builder.create();
        builder.show();
    }

    public void ClickAdd(View view) {
//        // Inflate the custom layout
//        View dialogView = LayoutInflater.from(this).inflate(R.layout.add_quiz_no, null);
//
//        // Get references to the UI elements in the custom layout
//        TextView titleTextView = dialogView.findViewById(R.id.dialogTitle);
//        EditText editText = dialogView.findViewById(R.id.dialogEditText);
//        Button cancelButton = dialogView.findViewById(R.id.dialogCancelButton);
//        Button saveButton = dialogView.findViewById(R.id.dialogSaveButton);
//
//        // Create the AlertDialog using the AlertDialog.Builder and set the custom view
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setView(dialogView);
//
//        // Create and show the AlertDialog
//        final AlertDialog dialog = builder.create();
//        dialog.show();
//
//        // Handle cancel button click
//        cancelButton.setOnClickListener(v -> {
//            dialog.dismiss();
//        });

        // Handle save button click
//        saveButton.setOnClickListener(v -> {
//            String enteredText = editText.getText().toString().trim();
//
//            // Check if the quiz number exists in Firebase RTDB
//            DatabaseReference quizRef = FirebaseDatabase.getInstance().getReference("Quizzes").child(enteredText);
//            quizRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    if (dataSnapshot.exists()) {
//                        // Quiz number exists in Firebase RTDB, show a toast message
//                        Toast.makeText(RoleChooser.this, "Quiz number already exists", Toast.LENGTH_SHORT).show();
//                    } else {
//                        // Quiz number doesn't exist, proceed to the next activity
//                        Intent intent = new Intent(RoleChooser.this, AddQuestion.class);
//                        intent.putExtra("category", enteredText);
//                        startActivity(intent);
//                    }
//                    dialog.dismiss();
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//                    // Handle database error
//                    dialog.dismiss();
//                }
//            });
//        });

        //alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //set title
        builder.setTitle("New Quiz");
        //set message
        builder.setMessage("Are you sure you want to create new questionnaires?!Old Questionnaire and leader board will be reset!");
        //positive yes
        builder.setPositiveButton("CONFIRM", (dialog, which) -> {
            //remove section subject
            //dismiss dialog
            dialog.dismiss();
            cleanQuiz();



        });
        //negative no
        builder.setNegativeButton("NO", (dialog, which) -> {
            //dismiss dialog
            dialog.dismiss();
        });
        builder.create();
        builder.show();

    }

    private void cleanQuiz() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference rf = db.getReference("NewQuiz");
        rf.setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> task) {
                Intent intent = new Intent(RoleChooser.this, AddQuestion.class);
                intent.putExtra("category", "1");
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Toast.makeText(RoleChooser.this, "Some Error Eccored: "+e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }

}