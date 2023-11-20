package com.example.swimfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.example.swimfo.unorganized.Adapter.ViewPagerAdapter;
import com.example.swimfo.Student.Fragment.HomeFragment;
import com.example.swimfo.Student.Fragment.QuizFragment;
import com.example.swimfo.Student.Fragment.GameFragment;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Hide the ActionBar (Toolbar)
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        tabLayout = findViewById(R.id.tablayout_id);
        viewPager = findViewById(R.id.viewpager_id);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.AddFragment(new HomeFragment(),"Home");
        adapter.AddFragment(new QuizFragment(),"Quiz");
        adapter.AddFragment(new GameFragment(),"Game");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);


        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_quiz);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_game);

        TabLayout.Tab tab1 = tabLayout.getTabAt(0);
        tabLayout.selectTab(tab1);
        View selected = tab1.view;
        selected.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.blue_darker));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.view.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.blue_darker));

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.view.setBackgroundColor(Color.TRANSPARENT);

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}