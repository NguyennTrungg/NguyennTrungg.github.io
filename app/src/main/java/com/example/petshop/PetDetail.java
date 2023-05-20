package com.example.petshop;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PetDetail extends AppCompatActivity implements View.OnClickListener{
    private RelativeLayout detailLayout;
    private LinearLayout detailLinear;
    private TextView txtName, txtBreed, txtAge, txtDetail, btnAdopt;
    private ImageView imgPet,imgDetail1, imgDetail2, imgDetail3, imgDetail4, btnBack;
    private String key,img,name,age;
    String img1, img2, img3, img4;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_detail);

        detailLayout = findViewById(R.id.detail_layout);
        detailLinear = findViewById(R.id.detail_linear);

        txtName = (TextView) findViewById(R.id.txt_name);
        txtBreed = (TextView) findViewById(R.id.txt_breed);
        txtAge = (TextView) findViewById(R.id.txt_age) ;
        txtDetail = (TextView) findViewById(R.id.txt_detail);


        imgPet = (ImageView) findViewById(R.id.img_pet);
        imgDetail1 = (ImageView) findViewById(R.id.img_detail1);
        imgDetail2 = (ImageView) findViewById(R.id.img_detail2);
        imgDetail3 = (ImageView) findViewById(R.id.img_detail3);
        imgDetail4 = (ImageView) findViewById(R.id.img_detail4);

        btnBack = (ImageView) findViewById(R.id.btn_Back);
        btnAdopt = (TextView) findViewById(R.id.btn_adopt);
        btnBack.setOnClickListener(v -> finish());
        btnAdopt.setOnClickListener(this);

        Intent intent = getIntent();
        key = intent.getStringExtra("key");
        img = intent.getStringExtra("image");
        name = intent.getStringExtra("name");
        age = intent.getStringExtra("age");
        txtName.setText(name);
        txtAge.setText("Age: " + age);
        Glide.with(this).load(img).into(imgPet);

        String[] category = key.split("-");
        if(category[1].compareTo("Cat") == 0){
            LoadDetailFromFirebase("Cat");
        }
        else if(category[1].compareTo("Dog") == 0){
            LoadDetailFromFirebase("Dog");
        }

        builder = new AlertDialog.Builder(this);
    }
    public void LoadDetailFromFirebase(String category){

        FirebaseDatabase.getInstance().getReference("Pet").child(category).child(key)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String breed = ""+snapshot.child("breed").getValue();
                        txtBreed.setText(breed);

                        img1 = ""+snapshot.child("img1").getValue();
                        img2 = ""+snapshot.child("img2").getValue();
                        img3 = ""+snapshot.child("img3").getValue();
                        img4 = ""+snapshot.child("img4").getValue();
                        Glide.with(detailLinear).load(img1).into(imgDetail1);
                        Glide.with(detailLinear).load(img2).into(imgDetail2);
                        Glide.with(detailLinear).load(img3).into(imgDetail3);
                        Glide.with(detailLinear).load(img4).into(imgDetail4);

                        imgDetail1.setOnClickListener(PetDetail.this);
                        imgDetail2.setOnClickListener(PetDetail.this);
                        imgDetail3.setOnClickListener(PetDetail.this);
                        imgDetail4.setOnClickListener(PetDetail.this);

                        String male = ""+snapshot.child("sex").getValue();
                        if(male.compareTo("male") == 0){
                            txtName.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.baseline_male_24, 0);
                        }
                        else if(male.compareTo("female") == 0){
                            txtName.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.baseline_female_24, 0);
                        }
                        else txtName.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);

                        String detail = ""+snapshot.child("detail").getValue();
                        txtDetail.setText("\t" + detail);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Snackbar.make(detailLayout, error.getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                });

    }

    @Override
    public void onClick(View v) {
        if(v == imgDetail1){
            Glide.with(this).load(img1).into(imgPet);
        }
        if(v == imgDetail2){
            Glide.with(this).load(img2).into(imgPet);
        }
        if(v == imgDetail3){
            Glide.with(this).load(img3).into(imgPet);
        }
        if(v == imgDetail4){
            Glide.with(this).load(img4).into(imgPet);
        }
        if(v == btnAdopt){
            builder.setIcon(R.drawable.bone)
                    .setTitle("Adopt Pet?")
                    .setMessage("Do you want to adopt a pet?")
                    .setCancelable(true)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(PetDetail.this);
                            builder1.setIcon(R.drawable.bone)
                                    .setTitle("Notification!")
                                    .setMessage("Please come to our chain of stores to complete the pet adoption procedure!")
                                    .setCancelable(true)
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    })
                                    .show();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Snackbar.make(detailLayout, "Thanks you for coming by!", Snackbar.LENGTH_LONG).show();
                            dialog.cancel();
                        }
                    })
                    .show();
        }
    }
}