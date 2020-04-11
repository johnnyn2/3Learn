//COMP4521 HO WAI KIN 20447589 wkhoae@connect.ust.hk
package com.example.android.a3learn;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {

    private EditText etPasswordEmail;
    private Button btnPasswordReset;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        getSupportActionBar().setTitle("Forget Password");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#202F66")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etPasswordEmail = findViewById(R.id.etPasswordEmail);
        btnPasswordReset = findViewById(R.id.btnPasswordReset);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        btnPasswordReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String useremail = etPasswordEmail.getText().toString().trim();
                if(useremail.isEmpty())
                    Toast.makeText(ForgetPasswordActivity.this, "Please enter your registered email ID!", Toast.LENGTH_SHORT).show();
                else{
                    progressDialog.setMessage("Sending password reset email...");
                    progressDialog.show();
                    firebaseAuth.sendPasswordResetEmail(useremail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.dismiss();
                            if(task.isSuccessful()) {
                                Toast.makeText(ForgetPasswordActivity.this, "Password reset email sent!", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(ForgetPasswordActivity.this, LoginActivity.class));
                            }else{
                                Toast.makeText(ForgetPasswordActivity.this, "Error in sending password reset email!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
