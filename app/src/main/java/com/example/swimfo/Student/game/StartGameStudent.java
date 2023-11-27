package com.example.swimfo.Student.game;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.swimfo.R;
import com.google.android.material.button.MaterialButton;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        showDialogBox();
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
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true); // Enable JavaScript
        webSettings.setDomStorageEnabled(true);

        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.setAcceptThirdPartyCookies(myWebView, true);
        }




        myWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // This JavaScript code will remove the element with the ID 'MobileAdInGamePreroll-Box-Body'
                myWebView.evaluateJavascript("document.getElementById('MobileAdInGamePreroll-Box-Body').style.display='none';", null);
                myWebView.evaluateJavascript("document.getElementById('more-games').style.display='none';", null);

            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();

                // Map of replacements with corresponding new dimensions
                Map<String, ReplacementInfo> replacements = new HashMap<>();
                replacements.put("title.png", new ReplacementInfo("swimlogo.png", 500, 500));
                replacements.put("more-games.png", new ReplacementInfo("img.png", 200, 200)); // Replace with your actual image file name

                for (Map.Entry<String, ReplacementInfo> entry : replacements.entrySet()) {
                    if (url.endsWith(entry.getKey())) {
                        try {
                            // Get the replacement info
                            ReplacementInfo info = entry.getValue();
                            InputStream stream = getAssets().open(info.assetFileName);
                            Bitmap bitmap = BitmapFactory.decodeStream(stream);
                            // Resize the bitmap
                            Bitmap resizedBitmap = resizeBitmap(bitmap, info.width, info.height);
                            // Convert the resized Bitmap back to an InputStream
                            InputStream resizedStream = bitmapToInputStream(resizedBitmap);
                            return new WebResourceResponse("image/png", "UTF-8", resizedStream);
                        } catch (IOException e) {
                            e.printStackTrace(); // Handle exception
                        }
                    }
                }

                return super.shouldInterceptRequest(view, request);
            }



        });

        myWebView.loadUrl("https://cdn-factory.marketjs.com/en/swimming-hero/index.html");
    }

    // This function converts a Bitmap to an InputStream
    private InputStream bitmapToInputStream(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] bitmapData = bos.toByteArray();
        return new ByteArrayInputStream(bitmapData);
    }

    // This function resizes a bitmap to new width and height
    private Bitmap resizeBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
    }



    // This function converts a drawable to a base64 string
    private String convertDrawableToBase64(int drawableId) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), drawableId);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }

    // Call this function to replace the image in the WebView
    private void replaceImageWithDrawable() {
        final String base64Image = convertDrawableToBase64(R.drawable.swim_logo);
        // Replace 'title.png' with the base64 encoded image string
        myWebView.evaluateJavascript(
                "ig['Image'].prototype.drawTile = function(x, y, tile, tileWidth, tileHeight) {" +
                        "var image = new Image();" +
                        "image.src = 'data:image/png;base64," + base64Image + "';" +
                        "ig.system.context.drawImage(image, x, y, tileWidth, tileHeight);" +
                        "};", null);
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


    private void showDialogBox(){


        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_game_mechanics);

        Button btnOkay = dialog.findViewById(R.id.btn_okay);

        btnOkay.setOnClickListener(v -> {
            myWebView.evaluateJavascript("javascript:MobileAdInGamePreroll.Close();", null);
            dialog.dismiss();
        });

        // Get the Window object of the dialog
        Window window = dialog.getWindow();
        if (window != null) {
            // Set the layout parameters to match the window size
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            // Optional: Remove dialog background to make it full screen
            window.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        }



        dialog.show();

    }

    @Override
    protected void onPause() {
        super.onPause();
        myWebView.evaluateJavascript("localStorage.getItem('key');", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                // Save to SharedPreferences
                SharedPreferences prefs = getSharedPreferences("WebViewLocalStorage", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("key", value);
                editor.apply();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = getSharedPreferences("WebViewLocalStorage", MODE_PRIVATE);
        String value = prefs.getString("key", null);
        if (value != null) {
            myWebView.evaluateJavascript("localStorage.setItem('key', " + value + ");", null);
        }
    }


    // Class to hold replacement information
    class ReplacementInfo {
        String assetFileName;
        int width;
        int height;

        ReplacementInfo(String assetFileName, int width, int height) {
            this.assetFileName = assetFileName;
            this.width = width;
            this.height = height;
        }
    }



}