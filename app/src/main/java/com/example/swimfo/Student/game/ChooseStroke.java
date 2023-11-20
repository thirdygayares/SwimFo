package com.example.swimfo.Student.game;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.swimfo.R;

public class ChooseStroke extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_stroke);

        Button btnfreeStyle = findViewById(R.id.btnfreeStyle);

        btnfreeStyle.setOnClickListener(v -> {
            startActivity(new Intent(ChooseStroke.this, MainActivityMainGame.class));
        });

    }
}