package com.example.petshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPass extends AppCompatActivity {

    FirebaseAuth auth;
    EditText etForgot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);

        TextView tvLogin = (TextView) findViewById(R.id.tv_Login);
        Button btnSubmit = (Button) findViewById(R.id.btn_forgotpass);
        etForgot = findViewById(R.id.et_forgotpass);
        auth = FirebaseAuth.getInstance();

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPass.this, SignIn.class);
                startActivity(intent);
                finish();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickForgotPass();
            }
        });
    }

    private void onClickForgotPass() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String emailAddress;
        emailAddress = etForgot.getText().toString();

        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ForgotPass.this,"Email sent",Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ForgotPass.this,"Something wrong!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
