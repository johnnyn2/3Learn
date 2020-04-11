//COMP4521 HO WAI KIN 20447589 wkhoae@connect.ust.hk
package com.example.android.a3learn;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class RegisterActivity extends AppCompatActivity {
    private EditText userName, userPassword, userEmail, confirmUserPassword;
    private Button regButton;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private ProgressDialog progressDialog;
    private String name, password, confirmPassword, email;
    private ImageView profilePic;
    private static int PICK_IMAGE = 123;
    private Uri imagePath;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==123 && resultCode==RESULT_OK && data.getData()!=null){
            imagePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
                profilePic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#202F66")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        profilePic = findViewById(R.id.profilePic);
        userName = findViewById(R.id.profileName);
        userPassword = findViewById(R.id.userPassword);
        confirmUserPassword = findViewById(R.id.confirmUserPassword);
        userEmail = findViewById(R.id.profileEmail);
        regButton = findViewById(R.id.regButton);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        progressDialog = new ProgressDialog(this);

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*"); // type: e.g. "application/pdf" / "application/doc" / "audio/mp3"
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent, "Select image"), PICK_IMAGE);
            }
        });

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Register button", "clicked");
                if (validate()){
                    Log.d("Validating", "TRUE");
                    String user_email = userEmail.getText().toString().trim();
                    String user_password = userPassword.getText().toString().trim();
                    firebaseAuth.createUserWithEmailAndPassword(user_email, user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                    sendEmailVerification();
                            } else
                                Toast.makeText(RegisterActivity.this, "Account already exist!", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });

    }

    private boolean validate(){
        boolean noError = true;
        name = userName.getText().toString();
        password = userPassword.getText().toString();
        confirmPassword = confirmUserPassword.getText().toString();
        email = userEmail.getText().toString();
        Log.d("user name", name);
        Log.d("password", password);
        Log.d("confirmpw", confirmPassword);
        Log.d("email", email);
        if(name.isEmpty() || password.isEmpty() || email.isEmpty() || confirmPassword.isEmpty() || imagePath == null) {
            Toast.makeText(this, "Please enter all details!", Toast.LENGTH_SHORT).show();
            noError = false;
        }
        if(!password.equals(confirmPassword)){
            Toast.makeText(this, "Password does not equal confirm password!", Toast.LENGTH_SHORT).show();
            noError = false;
        }
        return noError;
    }

    private void sendEmailVerification(){
        progressDialog.setMessage("Sending verification email... Please wait");
        progressDialog.show();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user!=null){
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    progressDialog.dismiss();
                    if(task.isSuccessful()) {
                        sendUserData();
                        Toast.makeText(RegisterActivity.this, "Please check your email for verification!", Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                        finish();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(RegisterActivity.this, "Verification mail cannot be sent!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void sendUserData(){
        String uid = firebaseAuth.getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        StorageReference imageRef = FirebaseStorage.getInstance().getReference("Users").child(uid).child("Images").child("ProfilePic");
        UploadTask uploadTask = imageRef.putFile(imagePath);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, "Profile picture can't be uploaded!", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

            }
        });
        UserProfile userProfile = new UserProfile(uid, email, name, password);
        Log.d("Create profile","ok");
        ref.setValue(userProfile);
        Log.d("Insert user profile", "ok");
    }
}
