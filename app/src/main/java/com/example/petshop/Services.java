package com.example.petshop;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.example.petshop.dialog.DateTimePickerDialog;
import com.google.android.material.bottomnavigation.BottomNavigationView;
public class Services extends AppCompatActivity implements View.OnClickListener{
    LinearLayout lnVaccine, lnTakecare, lnBath, lnGrooming;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.services);

        lnVaccine = (LinearLayout) findViewById(R.id.ln_Vaccine);
        lnTakecare = (LinearLayout) findViewById(R.id.ln_Takecare);
        lnBath = (LinearLayout) findViewById(R.id.ln_Bath);
        lnGrooming = (LinearLayout) findViewById(R.id.ln_Grooming);

        lnVaccine.setOnClickListener(this);
        lnTakecare.setOnClickListener(this);
        lnBath.setOnClickListener(this);
        lnGrooming.setOnClickListener(this);


        BottomNavigationView botNav = (BottomNavigationView) findViewById(R.id.nav);
        botNav.setSelectedItemId(R.id.Service);
        botNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        break;
                    case R.id.Shopping:
                        startActivity(new Intent(getApplicationContext(), Shoping.class));
                        break;
                    case R.id.Service:
                        break;
                    case R.id.Other:
                        startActivity(new Intent(getApplicationContext(), Other.class));
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == lnVaccine){
            Schedule("Vaccine");
        }
        if (v == lnTakecare){
            Schedule("TakeCare");
        }
        if (v == lnBath){
            Schedule("AnimalBath");
        }
        if (v == lnGrooming){
            Schedule("Grooming");
        }
    }
    public void Schedule(String service){
        Intent intent = new Intent(this, DateTimePickerDialog.class);
        intent.putExtra("service", service);
        startActivity(intent);
    }

}
