package com.example.swimfo.Teacher.topic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.swimfo.R;


public class TopicContent extends AppCompatActivity {
    ImageView back;
    WebView web;
    TextView desc,demo,title,toptv;
    private int originalOrientation;
    private FrameLayout videoContainer;
    RelativeLayout top;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_content);

        back = findViewById(R.id.back);
        top = findViewById(R.id.action);
        web = findViewById(R.id.video);
        desc = findViewById(R.id.desc);
        demo = findViewById(R.id.demo);
        title = findViewById(R.id.title);
        toptv = findViewById(R.id.titleTop);
        originalOrientation = getRequestedOrientation();
        videoContainer = findViewById(R.id.video_container);
        //retrieve here
        Intent getData = getIntent();
        String url = getData.getStringExtra("vUrl");
        String chapId = getData.getStringExtra("chapId");
        String chapTitle = getData.getStringExtra("chapTitle");
        String chapDesc = getData.getStringExtra("chapDesc");
        String chapDemo = getData.getStringExtra("chapDemo");

//        String url = "https://youtu.be/fAbBd_Ev7w4";
        String videoId = extractVideoId(url);
        Toast.makeText(this, videoId, Toast.LENGTH_SHORT).show();
        String videoEmbed = "https://www.youtube.com/embed/"+videoId;

        String video = "<iframe width=\"100%\" height=\"100%\" src="+videoEmbed+" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>";

        web.loadData(video, "text/html","utf-8");
        web.getSettings().setJavaScriptEnabled(true);
        // Set a WebChromeClient to handle video rendering
        // Set a WebChromeClient to handle video rendering
//        web.setWebChromeClient(new WebChromeClient() {
//            @Override
//            public void onShowCustomView(View view, CustomViewCallback callback) {
//                super.onShowCustomView(view, callback);
//                // Save the original orientation
//                originalOrientation = getRequestedOrientation();
//                // Set landscape orientation
//                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
//                // Show the video in fullscreen
//                videoContainer.addView(view);
//                videoContainer.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onHideCustomView() {
//                super.onHideCustomView();
//                // Remove the fullscreen video
//                videoContainer.removeAllViews();
//                videoContainer.setVisibility(View.GONE);
//                // Restore the original orientation
//                setRequestedOrientation(originalOrientation);
//            }
//        });
        web.setWebChromeClient(new WebChromeClient() {
            private View customView;

            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                super.onShowCustomView(view, callback);
                // Save the original orientation
                originalOrientation = getRequestedOrientation();
                top.setVisibility(View.GONE);
                // Set landscape orientation
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

                // Hide other views and show the custom view (video)
                videoContainer.setVisibility(View.GONE);
                customView = view;
                videoContainer.addView(customView);
                videoContainer.setVisibility(View.VISIBLE);

                // Hide other views on the main layout
                web.setVisibility(View.GONE);
            }

            @Override
            public void onHideCustomView() {
                super.onHideCustomView();
                // Remove the custom view (video) and show other views
                if (customView != null) {
                    videoContainer.removeView(customView);
                    videoContainer.setVisibility(View.GONE);
                    customView = null;
                }

                // Restore the original orientation
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);

                top.setVisibility(View.VISIBLE);

                // Show other views on the main layout
                web.setVisibility(View.VISIBLE);
            }
        });

        title.setText(chapTitle);
        toptv.setText(chapTitle);
        desc.setText(chapDesc);
        demo.setText(chapDemo);


    }

    public static String extractVideoId(String youtubeUrl) {
        String videoId = null;

        String[] splitUrl = youtubeUrl.split("[/=?]");

        for (int i = 0; i < splitUrl.length; i++) {
            if (splitUrl[i].equals("youtu.be") || splitUrl[i].equals("watch") && i < splitUrl.length - 1) {
                videoId = splitUrl[i + 1];
                break;
            }
        }

        return videoId;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // Handle back button press to exit fullscreen or go back in WebView history
    @Override
    public void onBackPressed() {
        if (videoContainer.getVisibility() == View.VISIBLE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                web.getWebChromeClient().onHideCustomView();
            }
        } else if (web.isFocused() && web.canGoBack()) {
            web.goBack();
        } else {
            super.onBackPressed();
        }
    }

    // Handle hardware back key for WebView navigation
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && web.canGoBack()) {
            web.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}