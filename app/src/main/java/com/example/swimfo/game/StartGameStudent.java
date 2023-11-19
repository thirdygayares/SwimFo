package com.example.swimfo.game;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.example.swimfo.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StartGameStudent extends AppCompatActivity {
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

    private WebView myWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        game();

    }


    private void placeRandomLifesaver() {

        if (isPaused) {

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
    private void game() {
        myWebView = (WebView) findViewById(R.id.webview);
        myWebView.loadUrl("https://www.y8.com/games/swimming_hero/mobile");

        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
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


}