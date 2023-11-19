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

        String points =  getIntent().getStringExtra("SCORE");
        String wpoints =  getIntent().getStringExtra("WSCORE");
        String mpoints =  getIntent().getStringExtra("MSCORE");

        score.setText(points);
        wronh.setText(wpoints);
        missed.setText(mpoints);
    }

    public void Finish(View view) {
        Intent intent = new Intent(Finish.this, MainActivity.class);
        startActivity(intent);
        finishAffinity();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Finish.this, MainActivity.class);
        startActivity(intent);
        finishAffinity();
    }
}