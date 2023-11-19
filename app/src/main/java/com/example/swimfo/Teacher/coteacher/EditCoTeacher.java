package com.example.swimfo.Teacher.coteacher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.swimfo.MainActivity;
import com.example.swimfo.R;

import com.example.swimfo.Teacher.TeacherMain;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditCoTeacher extends AppCompatActivity {

    CircleImageView profileImage;
    ImageView back ;

    EditText nameEditText, passEt ;
    EditText idNumEditText ;

    ProgressBar pb;

    Button uploadImg, upName;
    ImageView edit;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public static Uri imageUriPic = null;
    private StorageReference storageReference;
    private FirebaseStorage storage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_coteacher_activity_edit_co_teacher);

        // Set up the custom ActionBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Set the custom back button icon
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        profileImage = findViewById(R.id.profile_image);
        back = findViewById(R.id.back);

        nameEditText = findViewById(R.id.register_name);
        idNumEditText = findViewById(R.id.idnum);

        edit = findViewById(R.id.edit);
        uploadImg = findViewById(R.id.updateImg);
        upName = findViewById(R.id.updateName);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        pb = findViewById(R.id.progress_bar);

        Intent intent = getIntent();

        String name = intent.getStringExtra("name");
        String uid = intent.getStringExtra("uid");
        String imgUrl = intent.getStringExtra("img");
        String email = intent.getStringExtra("email");
        String idNumber = email.substring(0, email.indexOf("@"));

        // Set the received data to the views
        nameEditText.setText(name);
        idNumEditText.setText(idNumber);
        Glide.with(this).load(imgUrl).into(profileImage);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        edit.setOnClickListener(view -> choosePicture());

        uploadImg.setOnClickListener(v->{
            if(imageUriPic!=null){
                uploadPicture(imageUriPic, uid);
            }else {
                Toast.makeText(getApplicationContext(),"Please Select Image by clicking the pencil",Toast.LENGTH_SHORT).show();
            }
        });

        upName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newName = nameEditText.getText().toString().trim();
                if (newName.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please provide name.", Toast.LENGTH_SHORT).show();
                    return;
                }

                updateName(newName,uid);
                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("studentlist");
            }
        });


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), TeacherMain.class);
        startActivity(intent);
        finish();
    }

    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imageUriPic = data.getData();
            profileImage.setImageURI(imageUriPic);
        }
    }

    private void uploadPicture(Uri imageUri, String uid) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading Image...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        // Create a reference to 'images/mountains.jpg'
        final String randomKey = UUID.randomUUID().toString();
        StorageReference imageRef = storageReference.child("courseImages/" + randomKey + "." + getfileExtension(imageUri));

        imageRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            progressDialog.dismiss();
            imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                // Update the "imgUrl" field in the Realtime Database
                updateImageUrlInDatabase(uid, uri.toString());
            });
        }).addOnFailureListener(e -> {
            progressDialog.dismiss();
            Snackbar.make(findViewById(android.R.id.content), "Error While Saving", Snackbar.LENGTH_SHORT).show();
        }).addOnProgressListener(snapshot -> {
            double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
            progressDialog.setMessage("Progress: " + progressPercent + "%");
            Snackbar.make(findViewById(android.R.id.content), "Saved", Snackbar.LENGTH_SHORT).show();
        });
    }

    private String getfileExtension(Uri mUri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(mUri));
    }

    private void updateImageUrlInDatabase(String uid, String imageUrl) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("coteacherlist");
        Query query = databaseRef.orderByChild("uid").equalTo(uid);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String randomId = snapshot.getKey();
                    DatabaseReference childRef = databaseRef.child(randomId);
                    childRef.child("imgUrl").setValue(imageUrl)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    // Image URL updated successfully
                                    // Handle this case accordingly
                                    // ...
                                    Toast.makeText(getApplicationContext(), "Image Updated", Toast.LENGTH_SHORT).show();
                                    onBackPressed();
                                } else {
                                    // Failed to update the image URL
                                    // Handle this case accordingly
                                    // ...
                                }
                            });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any potential errors
                // ...
            }
        });
    }

    private void updateName(String newName, String uid) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("coteacherlist").child(uid);
        databaseRef.child("name").setValue(newName)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Name updated successfully
                        // Handle this case accordingly
                        // ...
                        Toast.makeText(getApplicationContext(), "Name Updated", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    } else {
                        // Failed to update the name
                        // Handle this case accordingly
                        // ...
                        Toast.makeText(getApplicationContext(), "Failed to update", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_profile) {
            // Handle profile click
            return true;
        } else if (id == R.id.menu_logout) {
            // Handle logout click
            return true;
        } else if (id == android.R.id.home) {
            // Handle the back button click (optional)
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}