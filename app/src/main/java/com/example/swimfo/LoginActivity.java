package com.example.swimfo;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.swimfo.Teacher.TeacherMain;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class  LoginActivity extends AppCompatActivity {
    private Button loginBtn;
    private EditText email, password;
    private FirebaseAuth fAuth;


    int progress = 0;
    ProgressBar simpleProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Hide the ActionBar (Toolbar)
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        loginBtn = findViewById(R.id.login_btn);
        email = findViewById(R.id.email_et);
        password = findViewById(R.id.password_et);
        simpleProgressBar = (ProgressBar) findViewById(R.id.simpleProgress);

        fAuth = FirebaseAuth.getInstance();

        loginBtn.setOnClickListener(v -> login());

    }

    private void login() {
        loginBtn.setClickable(false);
        simpleProgressBar.setVisibility(View.VISIBLE);

        String emailLogin = email.getText().toString().trim();
        String passLogin = password.getText().toString().trim();

        if (TextUtils.isEmpty(emailLogin)) {
            email.setError("Email is required");
            simpleProgressBar.setVisibility(View.GONE);
            loginBtn.setClickable(true);


            return;
        }

        if (TextUtils.isEmpty(passLogin)) {
            password.setError("Password is required");
            simpleProgressBar.setVisibility(View.GONE);
            loginBtn.setClickable(true);


            return;
        }



        String loginType = emailLogin.equals("4207505") ? "teacher" : "email";
        String newMail = emailLogin + "@" + loginType + ".com";

        fAuth.signInWithEmailAndPassword(newMail, passLogin).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                simpleProgressBar.setVisibility(View.GONE);
                loginBtn.setClickable(false);

                //TODO add Co teacher

                Toast.makeText(LoginActivity.this, "Logged In", Toast.LENGTH_SHORT).show();
                Class<?> destinationActivity = loginType.equals("teacher") ? TeacherMain.class : CoTeacherOrStudent.class;
                startActivity(new Intent(getApplicationContext(), destinationActivity));
            } else {
                simpleProgressBar.setVisibility(View.GONE);
                loginBtn.setClickable(true);
                Toast.makeText(LoginActivity.this, "Error Occurred: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void ShowHidePass(View view) {
        if(view.getId()==R.id.show_pass_btn){
            if(password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                ((ImageView)(view)).setImageResource(R.drawable.ic_show);
                //Show Password
                password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                ((ImageView)(view)).setImageResource(R.drawable.ic_hide);
                //Hide Password
                password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    }


}
