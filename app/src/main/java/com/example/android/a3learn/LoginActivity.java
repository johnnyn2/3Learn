//COMP4521 HO WAI KIN 20447589 wkhoae@connect.ust.hk
package com.example.android.a3learn;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText name;
    private EditText password;
    private TextView register;
    private TextView forgetPassword;
    private Button login;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if( getIntent().getBooleanExtra("Exit", false)){
            finish();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        name = findViewById(R.id.profileName);
        password = findViewById(R.id.etPassword);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        register = findViewById(R.id.txtRegister);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        forgetPassword = findViewById(R.id.txtForgetPassword);
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
            }
        });
        login = findViewById(R.id.btnLogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user_name = name.getText().toString().trim();
                String user_password = password.getText().toString().trim();
                if(!hasEmptyFields(user_name, user_password))
                    validate(user_name, user_password);
                else
                    Toast.makeText(LoginActivity.this, "Please fill in your email account and password!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean hasEmptyFields(String username, String userpassword){
        boolean hasEmpty = false;
        if(username.isEmpty() || userpassword.isEmpty())
            hasEmpty = true;
        return hasEmpty;
    }

    private void validate(String userName, String userPassword){
        progressDialog.setMessage("Loading...  Please wait");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(userName, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()){
                    checkEmailVerification();
                }
                else
                    Toast.makeText(LoginActivity.this, "Account not exist / Invalid account or password", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkEmailVerification(){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        Boolean emailFlag = user.isEmailVerified();
        if(emailFlag){
            Toast.makeText(LoginActivity.this, "Successful login!", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }else{
            Toast.makeText(LoginActivity.this, "Please verify your email!", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }
}
