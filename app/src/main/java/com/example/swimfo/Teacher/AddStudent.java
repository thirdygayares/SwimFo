package com.example.swimfo.Teacher;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.swimfo.R;
import com.example.swimfo.Teacher.section.StudentList;
import com.example.swimfo.Model.StudentModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.security.SecureRandom;
import java.util.UUID;

public class AddStudent extends AppCompatActivity {
    EditText nameEt, idNum,passwordEt,conPasswordEt;
    ImageView edit, profile, back;
    Button uploadImg;
    String section;
    String sectionId;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int PASSWORD_LENGTH = 8;
    public static Uri imageUriPic = null;
    private StorageReference storageReference;
    private FirebaseStorage storage;
    String randomPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        nameEt = findViewById(R.id.register_name);
        back = findViewById(R.id.back);
        idNum = findViewById(R.id.idnum);
        passwordEt = findViewById(R.id.register_password);
        conPasswordEt = findViewById(R.id.confirm_password);
        edit = findViewById(R.id.edit);
        uploadImg = findViewById(R.id.updateImg);
        profile = findViewById(R.id.profile_image);

         randomPassword= generateRandomPassword();
        passwordEt.setText(randomPassword);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        firebaseDatabase = FirebaseDatabase.getInstance();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        Intent getData = getIntent();

         section = getData.getStringExtra("section");
         sectionId = getData.getStringExtra("id");

        edit.setOnClickListener(view -> choosePicture());

        uploadImg.setOnClickListener(v->{
            String name = nameEt.getText().toString();
            String idnum = idNum.getText().toString();
            String pass = passwordEt.getText().toString();
            String conpass = conPasswordEt.getText().toString();

            if(imageUriPic!=null && !TextUtils.isEmpty(name) && !TextUtils.isEmpty(idnum) && !TextUtils.isEmpty(pass)){
//                if (conpass.equals(pass)){
                    String email = idnum+"@email.com";
                    createUserWithProfilePicture(imageUriPic, name, email, pass, section,sectionId);
//                }else{
//                    Toast.makeText(this, "Password Do not Matched", Toast.LENGTH_SHORT).show();
//                }

            }else {
                Toast.makeText(AddStudent.this,"Please Select Image and fill up ",Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void ShowHidePass(View view) {
        if(view.getId()==R.id.show_pass_btn){
            if(passwordEt.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                ((ImageView)(view)).setImageResource(R.drawable.ic_show);
                //Show Password
                passwordEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                ((ImageView)(view)).setImageResource(R.drawable.ic_hide);
                //Hide Password
                passwordEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    }

    public void ShowHidePass1(View view) {
        if(view.getId()==R.id.show_pass_btn1){
            if(conPasswordEt.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                ((ImageView)(view)).setImageResource(R.drawable.ic_show);
                //Show Password
                conPasswordEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                ((ImageView)(view)).setImageResource(R.drawable.ic_hide);
                //Hide Password
                conPasswordEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AddStudent.this, StudentList.class);
        intent.putExtra("section", section);
        intent.putExtra("id", sectionId);
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
            profile.setImageURI(imageUriPic);
        }
    }

    private void createUserWithProfilePicture(Uri imageUri, String name, String email, String password, String section, String sectionId) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading Image...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        final String randomKey = UUID.randomUUID().toString();
        StorageReference imageRef = storageReference.child("profilePictures/" + randomKey + "." + getFileExtension(imageUri));

        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        imageUriPic = null;
                        // Create user in Firebase Authentication
                        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        String userId = user.getUid();

                                        // Update user profile
                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(name)
                                                .setPhotoUri(uri)
                                                .build();

                                        user.updateProfile(profileUpdates)
                                                .addOnCompleteListener(updateTask -> {
                                                    progressDialog.dismiss();
                                                    if (updateTask.isSuccessful()) {
                                                        // User profile updated successfully
                                                        // Save additional data in Realtime Database
                                                        DatabaseReference studentRef = FirebaseDatabase.getInstance().getReference("studentlist");
                                                        String studentId = studentRef.push().getKey();

                                                        StudentModel student = new StudentModel(name,userId, section, sectionId, uri.toString(), email, randomPassword);
                                                        studentRef.child(userId).setValue(student)
                                                                .addOnSuccessListener(aVoid -> {
                                                                    // Reset EditText fields
                                                                    nameEt.setText("");
                                                                    idNum.setText("");
                                                                    passwordEt.setText("");
                                                                    conPasswordEt.setText("");

                                                                    profile.setImageResource(R.drawable.swim_logo);
                                                                    Toast.makeText(getApplicationContext(), "User created and data saved successfully", Toast.LENGTH_SHORT).show();
                                                                })
                                                                .addOnFailureListener(e -> {
                                                                    Toast.makeText(getApplicationContext(), "Failed to save user data", Toast.LENGTH_SHORT).show();
                                                                });
                                                    } else {
                                                        // Failed to update user profile
                                                        Toast.makeText(getApplicationContext(), "Failed to update user profile", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    } else {
                                        progressDialog.dismiss();
                                        // Failed to create user in Firebase Authentication
                                        Toast.makeText(getApplicationContext(), "Failed to create user", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    });
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Snackbar.make(findViewById(android.R.id.content), "Error While Saving", Snackbar.LENGTH_SHORT).show();
                })
                .addOnProgressListener(snapshot -> {
                    double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                    progressDialog.setMessage("Progress: " + progressPercent + "%");
                    Snackbar.make(findViewById(android.R.id.content), "Saved", Snackbar.LENGTH_SHORT).show();
                });
    }


    private String getFileExtension(Uri mUri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(mUri));
    }


        public static String generateRandomPassword() {
            SecureRandom random = new SecureRandom();
            StringBuilder password = new StringBuilder(PASSWORD_LENGTH);

            for (int i = 0; i < PASSWORD_LENGTH; i++) {
                int randomIndex = random.nextInt(CHARACTERS.length());
                char randomChar = CHARACTERS.charAt(randomIndex);
                password.append(randomChar);
            }

            return password.toString();
        }

}