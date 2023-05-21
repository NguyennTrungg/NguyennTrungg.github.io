package com.example.petshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Other extends AppCompatActivity implements View.OnClickListener{
    RelativeLayout orderLayout;
    private TextView appointmentSchedule, changePassword, btnLogout;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orders);

        orderLayout = (RelativeLayout) findViewById(R.id.order_layout);
        changePassword = (TextView) findViewById(R.id.ed_ChangePass);
        appointmentSchedule = (TextView) findViewById(R.id.et_LichHen);
        btnLogout = (TextView) findViewById(R.id.ed_LogOut);

        changePassword.setOnClickListener(this);
        appointmentSchedule.setOnClickListener(this);
        btnLogout.setOnClickListener(this);

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
    }

    @Override
    public void onClick(View v) {
        if (v == appointmentSchedule)
            startActivity(new Intent(this, AppointmentSchedule.class));
        if (v == changePassword){
            FirebaseAuth auth = FirebaseAuth.getInstance();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String emailAddress = user.getEmail();
            auth.sendPasswordResetEmail(emailAddress)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Snackbar.make(orderLayout, "Email sent!", Snackbar.LENGTH_LONG).show();
                            }
                        }
                    });
        }
        if (v == btnLogout){
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), SignUp_In.class);
            startActivity(intent);
            finish();
        }
    }
}
