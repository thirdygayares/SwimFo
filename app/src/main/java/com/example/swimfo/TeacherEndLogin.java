package com.example.swimfo;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.swimfo.Teacher.Section.MainTeacher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class TeacherEndLogin extends AppCompatActivity {
    EditText email, password;
    Button loginBtn;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_end_login);

        email = findViewById(R.id.email_et);
        password = findViewById(R.id.password_et);
        loginBtn = findViewById(R.id.login_btn);
        fAuth = FirebaseAuth.getInstance();

        email.setSelection(email.getText().length());
        password.setSelection(password.getText().length());

        loginBtn.setOnClickListener((View v) -> {
            //content login here
            String emailLogin = email.getText().toString();
            String passLogin = password.getText().toString();

            if (TextUtils.isEmpty(emailLogin)) {
                email.setError("Username is Required");
                return;
            } else if (TextUtils.isEmpty(passLogin)) {
                password.setError("Password is Required");
                return;
            } else if(!TextUtils.isEmpty(emailLogin) || !TextUtils.isEmpty(passLogin)) {
                String newMail = emailLogin+"@admin.com";

                fAuth.signInWithEmailAndPassword(newMail, passLogin).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText( TeacherEndLogin.this, "Logged In", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainTeacher.class));
                        } else {
                            Toast.makeText( TeacherEndLogin.this, "Error Occurred" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            //end of login
        });

    }
}