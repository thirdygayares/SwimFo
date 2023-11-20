package com.example.swimfo.Student.game;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.swimfo.MainActivity;
import com.example.swimfo.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivityMainGame extends AppCompatActivity {
    private ScrollView swimmingPoolScrollView;
    private Handler handler = new Handler();
    private int scrollSpeed = 5;
    private ImageView btnPause;
    private String activeLane = "firstLane";

    private LinearLayout firstLane, secondLane, thirdLane, fourthLane;
    private Random random = new Random();
    private Drawable lifesaver1, lifesaver2;
    private ImageView currentLifesaver;  // New variable to keep track of the current lifesaver

    private boolean isPaused = false; // New variable to track the pause state of the game
    private List<ImageView> lifesavers = new ArrayList<>(); // New list to keep track of all active lifesavers

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_game);

        swimmingPoolScrollView = findViewById(R.id.swimmingPoolScrollView);
        btnPause = findViewById(R.id.btnPause);

        // Start the animation
        handler.post(scrollRunnable);



        // Initializing lanes and touch listeners
        firstLane = findViewById(R.id.firstLane);
        secondLane = findViewById(R.id.secondLane);
        thirdLane = findViewById(R.id.thirdLane);
        fourthLane = findViewById(R.id.fourthLane);

        firstLane.setOnClickListener(v -> handleLaneTouch("firstLane"));
        secondLane.setOnClickListener(v -> handleLaneTouch("secondLane"));
        thirdLane.setOnClickListener(v -> handleLaneTouch("thirdLane"));
        fourthLane.setOnClickListener(v -> handleLaneTouch("fourthLane"));

        lifesaver1 = getResources().getDrawable(R.drawable.lifesaver3, null);
        lifesaver2 = getResources().getDrawable(R.drawable.lifesaver2, null);

        final Handler lifesaverHandler = new Handler();
        lifesaverHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                placeRandomLifesaver();
                int randomDelay = random.nextInt(5000) + 1000; // random delay between 1 and 5 seconds
                lifesaverHandler.postDelayed(this, randomDelay);
            }
        }, 5000);


        // Pause button action
        btnPause.setOnClickListener(v -> {
            isPaused = true; // Mark the game as paused

            // Stop the pool scrolling and lifesaver animations
            if (handler != null) {
                handler.removeCallbacks(scrollRunnable);
            }
            pauseLifesaverAnimations();

            // Dialog box for pause
            Dialog dialog = new Dialog(MainActivityMainGame.this);
            dialog.setContentView(R.layout.dialog_pause);
            dialog.show();
            dialog.setCanceledOnTouchOutside(true);

            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }

            ImageView btnResume = dialog.findViewById(R.id.btnResume);
            ImageView btnHome = dialog.findViewById(R.id.btnHome);

            btnHome.setOnClickListener(v1 -> {
                startActivity(new Intent(MainActivityMainGame.this, MainActivity.class));
            });

            btnResume.setOnClickListener(v1 -> {
                dialog.dismiss();
                isPaused = false; // Mark the game as unpaused
                if (handler != null) {
                    handler.post(scrollRunnable);
                }
                resumeLifesaverAnimations(); // Resume the lifesaver animat
            });
        });
    }


    private void placeRandomLifesaver() {

        if (isPaused) {
            return; // Don't place a new lifesaver if the game is paused
        }

        int numberOfLifesavers = random.nextInt(4) + 1; // Random number between 1 and 4

        List<Integer> usedLanes = new ArrayList<>(); // To keep track of lanes already used for this cycle

        for (int i = 0; i < numberOfLifesavers; i++) {
            int randomLane;
            do {
                randomLane = random.nextInt(4);  // 0 to 3
            } while (usedLanes.contains(randomLane));  // Ensure the lane hasn't been used yet

            usedLanes.add(randomLane);  // Mark the lane as used

            Drawable selectedLifesaver = (random.nextInt(2) == 0) ? lifesaver1 : lifesaver2;

            ImageView lifesaverView = new ImageView(this);
            lifesaverView.setImageDrawable(selectedLifesaver);
            moveLifesaver(lifesaverView);

            switch (randomLane) {
                case 0:
                    firstLane.addView(lifesaverView);
                    break;
                case 1:
                    secondLane.addView(lifesaverView);
                    break;
                case 2:
                    thirdLane.addView(lifesaverView);
                    break;
                case 3:
                    fourthLane.addView(lifesaverView);
                    break;
            }
        }
    }


    private void moveLifesaver(ImageView lifesaverView) {
        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        int duration = (screenHeight / scrollSpeed) * 10;

        lifesaverView.animate()
                .translationY(screenHeight)
                .setDuration(duration)
                .withEndAction(() -> {
                    ((LinearLayout) lifesaverView.getParent()).removeView(lifesaverView);
                })
                .start();

        lifesavers.add(lifesaverView); // Add the new lifesaver to the list
    }

    // New method to pause the animation of all current lifesavers
    private void pauseLifesaverAnimations() {
        for (ImageView lifesaver : lifesavers) {
            lifesaver.animate().cancel();
        }
    }

    // New method to resume the animation of all current lifesavers
    private void resumeLifesaverAnimations() {
        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        for (ImageView lifesaver : lifesavers) {
            long remainingDuration = (long) ((screenHeight - lifesaver.getTranslationY()) / scrollSpeed) * 10;
            lifesaver.animate()
                    .translationY(screenHeight)
                    .setDuration(remainingDuration)
                    .withEndAction(() -> {
                        if (lifesaver.getParent() instanceof LinearLayout) {
                            ((LinearLayout) lifesaver.getParent()).removeView(lifesaver);
                        }
                        lifesavers.remove(lifesaver);
                    })
                    .start();
        }
    }
    private void handleLaneTouch(String touchedLane) {
        switch (activeLane) {
            case "firstLane":
                if (touchedLane.equals("secondLane")) {
                    activeLane = "secondLane";
                }
                break;
            case "secondLane":
                if (touchedLane.equals("firstLane") || touchedLane.equals("thirdLane")) {
                    activeLane = touchedLane;
                }
                break;
            case "thirdLane":
                if (touchedLane.equals("secondLane") || touchedLane.equals("fourthLane")) {
                    activeLane = touchedLane;
                }
                break;
            case "fourthLane":
                if (touchedLane.equals("thirdLane")) {
                    activeLane = "thirdLane";
                }
                break;
        }
    }

    private Runnable scrollRunnable = new Runnable() {
        @Override
        public void run() {
            swimmingPoolScrollView.smoothScrollBy(0, -scrollSpeed);
            int scrollPosition = swimmingPoolScrollView.getScrollY();

            if (scrollPosition <= 0) {
                swimmingPoolScrollView.scrollTo(0, swimmingPoolScrollView.getChildAt(0).getHeight() - swimmingPoolScrollView.getHeight());
            }

            handler.postDelayed(this, 10);
        }
    };


    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.removeCallbacks(scrollRunnable);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (handler != null) {
            handler.post(scrollRunnable);
        }
    }



}
