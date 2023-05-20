package com.example.petshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;


public class Other extends AppCompatActivity implements View.OnClickListener{
    private TextView appointmentSchedule;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orders);

        appointmentSchedule = (TextView) findViewById(R.id.et_LichHen);
        appointmentSchedule.setOnClickListener(this);

        BottomNavigationView botNav = (BottomNavigationView) findViewById(R.id.nav);
        botNav.setSelectedItemId(R.id.Other);
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
                        startActivity(new Intent(getApplicationContext(), Services.class));
                        break;
                    case R.id.Other:
                        break;
                }
                return true;
            }
        });

        TextView btnLogout = (TextView) findViewById(R.id.ed_LogOut);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), SignUp_In.class);
                startActivity(intent);
                finish();
            }
        });

        TextView txtProfile = (TextView) findViewById(R.id.ed_Profile);
        txtProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Profile.class));
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == appointmentSchedule){
            Intent intent = new Intent(this, AppointmentSchedule.class);
            startActivity(intent);
        }
    }
}
