package com.example.swimfo.Teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.swimfo.LoginActivity;
import com.example.swimfo.R;
import com.example.swimfo.Teacher.Profile.ProfileHomePage;
import com.example.swimfo.Teacher.coteacher.CoTeacherMain;
import com.example.swimfo.Teacher.quiz.QuizHomePage;
import com.example.swimfo.Teacher.section.MainTeacher;
import com.example.swimfo.Teacher.topic.TopicManagement;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class TeacherMain extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private Toolbar toolbar;
    Fragment selectedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_main_activity);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        // Set up the ActionBar with the hamburger icon
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setTitle("");

        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        selectedFragment = new MainTeacher();

        getSupportFragmentManager().beginTransaction().replace(R.id.main_content,
                selectedFragment).commit();


        // Handle navigation item clicks
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_section) {
                    // Handle action for home
                    selectedFragment = new MainTeacher();
                } else if (itemId == R.id.nav_topic) {
                    // Handle action for Notification
                    selectedFragment = new TopicManagement();

                }else if (itemId == R.id.nav_quiz) {
                    // Handle action for Feedback
                    selectedFragment = new QuizHomePage();

                }else if (itemId == R.id.nav_coteacher) {
                    // Handle action for About
                    selectedFragment = new CoTeacherMain();

                }else if (itemId == R.id.nav_profile) {
                    // Handle action for About
                    selectedFragment = new ProfileHomePage();

                }else if (itemId == R.id.nav_logout) {

                    //TODO LOGOUT
                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                    firebaseAuth.signOut();
                    startActivity(new Intent(TeacherMain.this, LoginActivity.class));
                }


                getSupportFragmentManager().beginTransaction().replace(R.id.main_content,
                        selectedFragment).commit();

                // Handle other cases if needed

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });



    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }


}