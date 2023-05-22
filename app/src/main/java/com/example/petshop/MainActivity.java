package com.example.petshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import android.widget.ImageView;
import android.widget.RelativeLayout;


import com.example.petshop.adapter.CategoryAdapter;
import com.example.petshop.listener.IPetLoadListener;
import com.example.petshop.model.CategoryModel;
import com.example.petshop.model.PetModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements IPetLoadListener{

    FirebaseAuth auth;
    FirebaseUser user;

    private RecyclerView rcvCategory, rcvPet;
    private CategoryAdapter categoryAdapter;

    private RelativeLayout mainLayout;
    IPetLoadListener petLoadListener;

    List<CategoryModel> listCategory = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        LoadDogFromFireBase();
        LoadCatFromFireBase();


        BottomNavigationView botNav = (BottomNavigationView) findViewById(R.id.nav);
        botNav.setSelectedItemId(R.id.home);
        botNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        break;
                    case R.id.Shopping:
                        startActivity(new Intent(getApplicationContext(), Shoping.class));
                        break;
                    case R.id.Service:
                        startActivity(new Intent(getApplicationContext(), Services.class));
                        break;
                    case R.id.Other:
                        startActivity(new Intent(getApplicationContext(), Other.class));
                        break;
                }
                return true;
            }
        });

        ImageView Web = (ImageView) findViewById(R.id.postermain);
        Web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoUrl("https://azpet.com.vn/?fbclid=IwAR3nDKxAP_qK_lFEkztJfwmYJ8lzZe4g6m48PKIOFEMi8ygkfn7OFcItXFc");
            }
        });

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user == null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void gotoUrl(String s) {
        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }

    private void LoadCatFromFireBase() {
        ArrayList<PetModel> cat = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("Pet").child("Cat")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot petSnapshot : snapshot.getChildren()) {
                                String title = ""+petSnapshot.child("title").getValue();
                                String age = ""+petSnapshot.child("age").getValue();
                                String img = ""+petSnapshot.child("img").getValue();
                                PetModel pet = new PetModel();
                                pet.setTitle(title);
                                pet.setAge(age);
                                pet.setImg(img);
                                pet.setKey(petSnapshot.getKey());
                                cat.add(pet);
                            }
                            petLoadListener.onPetLoadSuccess("Cat", cat);
                        } else {
                            petLoadListener.onPetLoadFailed("Can't find cat");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        petLoadListener.onPetLoadFailed(error.getMessage());
                    }
                });
    }

    private void LoadDogFromFireBase() {
        ArrayList<PetModel> dog = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("Pet").child("Dog")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot petSnapshot : snapshot.getChildren()) {
                                String title = ""+petSnapshot.child("title").getValue();
                                String age = ""+petSnapshot.child("age").getValue();
                                String img = ""+petSnapshot.child("img").getValue();
                                PetModel pet = new PetModel();
                                pet.setTitle(title);
                                pet.setAge(age);
                                pet.setImg(img);
                                pet.setKey(petSnapshot.getKey());
                                dog.add(pet);
                            }
                            petLoadListener.onPetLoadSuccess("Dog", dog);
                        } else {
                            petLoadListener.onPetLoadFailed("Can't find dog");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        petLoadListener.onPetLoadFailed(error.getMessage());
                    }
                });
    }

    private void init() {

        petLoadListener = this;

        mainLayout = findViewById(R.id.main_layout);
        rcvCategory = findViewById(R.id.rcv_category);
        rcvPet = findViewById(R.id.rcv_pet);
        categoryAdapter = new CategoryAdapter(this, listCategory);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcvCategory.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onPetLoadSuccess(String category, List<PetModel> petList) {
        listCategory.add(new CategoryModel(category, petList));
        categoryAdapter.setData(listCategory);
        rcvCategory.setAdapter(categoryAdapter);
    }

    @Override
    public void onPetLoadFailed(String message) {
        Snackbar.make(mainLayout, message, Snackbar.LENGTH_LONG).show();
    }
}

