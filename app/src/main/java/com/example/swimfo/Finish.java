package com.example.swimfo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Finish extends AppCompatActivity {
    TextView score, wronh,missed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
        score = findViewById(R.id.score);
        wronh = findViewById(R.id.wscore);
        missed = findViewById(R.id.mscore);

        Integer points =  getIntent().getIntExtra("correct", 0);
        Integer wpoints =  getIntent().getIntExtra("wrong", 0);
        Integer mpoints =  getIntent().getIntExtra("unanswered", 0);

        score.setText(String.valueOf(points));
        wronh.setText(String.valueOf(wpoints));
        missed.setText(String.valueOf(mpoints));
    }

    public void Finish(View view) {
        Intent intent = new Intent(Finish.this, MainActivity.class);
        startActivity(intent);
        finishAffinity();
    }

    @Override
    public void onBackPressed() {
       finish();
    }
}