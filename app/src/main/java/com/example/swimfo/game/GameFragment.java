package com.example.swimfo.game;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.swimfo.MainActivity;
import com.example.swimfo.R;
import com.example.swimfo.game.StartGameStudent;


public class GameFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_homepage, container, false);

        ImageView imgBack =  view.findViewById(R.id.imgBack);
        ImageView imgStart =  view.findViewById(R.id.imgStart);

        imgBack.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), MainActivity.class));

        });

        imgStart.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), StartGameStudent.class));
        });


        return view;
    }
}