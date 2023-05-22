package com.example.petshop;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.petshop.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class Profile extends AppCompatActivity {
    private ImageView btnBack, imgProfile;
    private EditText edName, edPhone, edAddress;
    private Button btnUpdate;
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();


        edName = (EditText) findViewById(R.id.ed_name);
        edPhone = (EditText) findViewById(R.id.ed_PhoneNum);
        edAddress = (EditText) findViewById(R.id.ed_address);

        imgProfile = (ImageView) findViewById(R.id.avatar);
        btnBack = (ImageView) findViewById(R.id.btnBack);
        btnUpdate = (Button) findViewById(R.id.btn_update);
        btnBack.setOnClickListener(v -> finish());

        FirebaseDatabase.getInstance().getReference("Users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = ""+snapshot.child("name").getValue();
                String phone = ""+snapshot.child("phone").getValue();
                String address = ""+snapshot.child("address").getValue();

                if (name.compareTo("null") == 0) edName.setText("");
                else edName.setText(name);
                if (phone.compareTo("null") == 0) edPhone.setText("");
                else edPhone.setText(phone);
                if (address.compareTo("null") == 0) edAddress.setText("");
                else edAddress.setText(address);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Profile.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edPhone.getText().toString().length() == 11 || edPhone.getText().toString().length() == 10 )
                    UploadData();
                else Toast.makeText(Profile.this,"Phone number invalid!", Toast.LENGTH_SHORT).show();
            }
        });
        }
    public void UploadData(){
        String name = edName.getText().toString();
        String phone = edPhone.getText().toString();
        String address = edAddress.getText().toString();

        UserModel userModel = new UserModel(name, phone, address);

        FirebaseDatabase.getInstance().getReference("Users")
                .child(uid).setValue(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(Profile.this, "Saved", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Profile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}