package com.example.swimfo.Teacher.Profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.swimfo.ProfileActivity;
import com.example.swimfo.R;
import com.example.swimfo.Teacher.adapter.MyInterface;
import com.example.swimfo.Teacher.adapter.QuizListAdapter;
import com.example.swimfo.Teacher.model.QuizListModel;
import com.example.swimfo.Teacher.quiz.AddQuiz;
import com.example.swimfo.Teacher.quiz.SectionLeaderBoard;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileHomePage extends Fragment {

    TextView name,id, changePass;
    CircleImageView prof;
    Button logout;
    ImageView back;


    private View view;


    @Override
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.teacher_profile, container, false);
        prof = view.findViewById(R.id.profile_image);
        changePass = view.findViewById(R.id.changePass);
        name = view.findViewById(R.id.name);
        id = view.findViewById(R.id.idnum);
        back = view.findViewById(R.id.back);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String email = user.getEmail();


            String idNumber = email.substring(0, email.indexOf("@"));
//            name.setText(displayName);
            id.setText(idNumber);

            // Use the email, displayName, and photoUrl as needed

            String userId = user.getUid(); // Assuming this is the user ID

            if(userId.equals("4smAFPenKKda95RNnHshnIaUHUV2")){

                name.setText("Admin");
                prof.setImageDrawable(getResources().getDrawable(R.drawable.profile));
            }else {

                String displayName = user.getDisplayName();
                Uri photoUrl = user.getPhotoUrl();
                String url = photoUrl.toString();

                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("coteacherlist").child(userId);
                databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String names = dataSnapshot.child("name").getValue(String.class);
                            String imgPath = dataSnapshot.child("imgUrl").getValue(String.class);
                            // Use the retrieved name
                            name.setText(names);
                            Glide.with(getContext()).load(imgPath).into(prof);
                            // ...
                        } else {
                            // Data does not exist for the given user ID
                            // Handle this case accordingly
                            // ...
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                /*
                 Failed to read data from Firebase
                 Handle this error condition
                 ...
                */
                    }
                });

            }


        } else {
            // User is not logged in
        }

        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.change_pass, null);
                builder.setView(dialogView);

                EditText editOldPassword = dialogView.findViewById(R.id.edit_old_password);
                EditText editNewPassword = dialogView.findViewById(R.id.edit_new_password);
                EditText editConfirmPassword = dialogView.findViewById(R.id.edit_confirm_password);
                Button cancelButton = dialogView.findViewById(R.id.dialogCancelButton);
                Button saveButton = dialogView.findViewById(R.id.dialogSaveButton);

                // Create and show the AlertDialog
                final AlertDialog dialog = builder.create();


// Handle cancel button click
                cancelButton.setOnClickListener(v -> {
                    dialog.dismiss();
                });

// Handle save button click
                saveButton.setOnClickListener(v -> {
                    String oldPassword = editOldPassword.getText().toString();
                    String newPassword = editNewPassword.getText().toString();
                    String confirmPassword = editConfirmPassword.getText().toString();

                    if (oldPassword.isEmpty() || newPassword.isEmpty() | confirmPassword.isEmpty()){
                        Toast.makeText(getContext(), "Please fill up fields", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Get the current user
                    // Create a credential with the user's email and the old password
                    AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), oldPassword);

                    // Reauthenticate the user with the provided credential
                    user.reauthenticate(credential)
                            .addOnCompleteListener(reauthTask -> {
                                if (reauthTask.isSuccessful()) {
                                    // User has been successfully reauthenticated
                                    // Now, check if the new password and confirm password match
                                    if (newPassword.equals(confirmPassword)) {
                                        // Update the user's password with the new password
                                        user.updatePassword(newPassword)
                                                .addOnCompleteListener(updateTask -> {
                                                    if (updateTask.isSuccessful()) {
                                                        // Password has been updated successfully
                                                        Toast.makeText(getContext(), "Password updated successfully", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        // Failed to update the password
                                                        Toast.makeText(getContext(), "Failed to update password", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    } else {
                                        // New password and confirm password do not match
                                        Toast.makeText(getContext(), "New password and confirm password do not match", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    // Reauthentication failed
                                    Toast.makeText(getContext(), "Reauthentication failed", Toast.LENGTH_SHORT).show();
                                }
                            });

                    dialog.dismiss();
                });

                dialog.show();
            }
        });



        return view;
    }




}