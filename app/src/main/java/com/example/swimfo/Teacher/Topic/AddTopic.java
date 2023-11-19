package com.example.swimfo.Teacher.Topic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.swimfo.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddTopic extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText topicTitleEditText;
    private EditText topicDescEditText;
    private ImageView topicImageView;
    private Uri imageUri;
    TextView titleTop;

    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    String id;
    String imgUrl;
    String title;
    String desc;
    Boolean isUpdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_topic);

        Intent intent = getIntent();
         isUpdate = intent.getExtras().getBoolean("isUpdate");
         id = intent.getStringExtra("topicId");
         imgUrl = intent.getStringExtra("imgUrl");
         title = intent.getStringExtra("title");
         desc = intent.getStringExtra("desc");



        databaseReference = FirebaseDatabase.getInstance().getReference("topics");
        storageReference = FirebaseStorage.getInstance().getReference("topic_images");

        topicTitleEditText = findViewById(R.id.topicTitle);
        topicDescEditText = findViewById(R.id.topicDesc);
        topicImageView = findViewById(R.id.topicBanner);
        Button addButton = findViewById(R.id.add);
         titleTop = findViewById(R.id.actionTitle);

        if (isUpdate){
            if (imgUrl != null){
                Glide.with(this).load(imgUrl).into(topicImageView);
            }
            topicTitleEditText.setText(title);
            topicDescEditText.setText(desc);
            addButton.setText("Update");
            titleTop.setText("Update topic");

        }

        ImageView editImageView = findViewById(R.id.edit);
        editImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });
        topicTitleEditText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        topicDescEditText.requestFocus();
        InputMethodManager imm2 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm2.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String topicTitle = topicTitleEditText.getText().toString();
                String topicDesc = topicDescEditText.getText().toString();
                if(topicTitle.isEmpty() && topicDesc.isEmpty()){
                    topicTitleEditText.setError("Required");
                    topicDescEditText.setError("Required");
                    return;
                }
                if (imageUri == null){
                    Toast.makeText(AddTopic.this, "Image is required", Toast.LENGTH_SHORT).show();
                    return;
                }

                uploadDataToFirebase();
            }
        });
    }
    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            topicImageView.setImageURI(imageUri);
        }
    }

    private void uploadDataToFirebase() {
        if (imageUri != null) {
            final StorageReference imageRef = storageReference.child(UUID.randomUUID().toString());

            UploadTask uploadTask = imageRef.putFile(imageUri);
            uploadTask.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    imageRef.getDownloadUrl().addOnCompleteListener(downloadUrlTask -> {
                        if (downloadUrlTask.isSuccessful()) {
                            String imageUrl = downloadUrlTask.getResult().toString();
                            String topicTitle = topicTitleEditText.getText().toString();
                            String topicDesc = topicDescEditText.getText().toString();
                            String topicId = databaseReference.push().getKey();

                            if (isUpdate){
                                Map<String, Object> updates = new HashMap<>();
                                updates.put("topicTitle",topicTitle);
                                updates.put("topicDesc", topicDesc);
                                updates.put("imageUrl", imgUrl);
                                updates.put("topicId", id);
                                updates.put("topicHidden", true);
                                databaseReference.child(id).updateChildren(updates)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // Data updated successfully

                                                showCustomSnackbar(AddTopic.this);


                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(Exception e) {
                                                // Handle error
                                            }
                                        });
                            }


                            TopicModel topic = new TopicModel(topicId, topicTitle, topicDesc, imageUrl, true);
                            databaseReference.child(topicId).setValue(topic);

                            // Clear input fields and refresh ImageView
                            topicTitleEditText.setText("");
                            topicDescEditText.setText("");
                            topicImageView.setImageDrawable(null);
                            imageUri = null;
                        }
                    });
                }
            });
        }else{
            String topicTitle = topicTitleEditText.getText().toString();
            String topicDesc = topicDescEditText.getText().toString();

            Map<String, Object> updates = new HashMap<>();
            updates.put("topicTitle",topicTitle);
            updates.put("topicDesc", topicDesc);
            updates.put("imageUrl", imgUrl);
            updates.put("topicId", id);


            databaseReference.child(id).updateChildren(updates)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Data updated successfully

                            showCustomSnackbar(AddTopic.this);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            // Handle error
                        }
                    });
        }
    }



    private void showCustomSnackbar(Context context) {
        Snackbar snackbar = Snackbar.make(
                findViewById(android.R.id.content), // Pass a View (e.g., root layout) here
                "Data updated successfully",
                Snackbar.LENGTH_LONG
        ).setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        onBackPressed();

        snackbar.show();
    }
    @Override
    public void onBackPressed() {

        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(topicTitleEditText.getWindowToken(), 0);
        InputMethodManager imm2 = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm2.hideSoftInputFromWindow(topicDescEditText.getWindowToken(), 0);
        super.onBackPressed();

    }

    public void BackClick(View view) {
        onBackPressed();
    }


}