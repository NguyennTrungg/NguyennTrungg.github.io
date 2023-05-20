package com.example.petshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AppointmentSchedule extends AppCompatActivity {
    private TextView txtVaccine, txtTakecare, txtBath, txtGrooming;
    private DatabaseReference mDatabase;
    RelativeLayout appointmentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_schedule);

        mDatabase = FirebaseDatabase.getInstance().getReference("user");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        txtVaccine = (TextView) findViewById(R.id.txt_vaccine);
        txtTakecare = (TextView) findViewById(R.id.txt_takecare);
        txtBath = (TextView) findViewById(R.id.txt_bath);
        txtGrooming = (TextView) findViewById(R.id.txt_grooming);

        appointmentLayout = (RelativeLayout) findViewById(R.id.appointment_layout);

        mDatabase.child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String vaccine = ""+snapshot.child("Vaccine").getValue();
                        String takecare = ""+snapshot.child("TakeCare").getValue();
                        String bath = ""+snapshot.child("AnimalBath").getValue();
                        String grooming = ""+snapshot.child("Grooming").getValue();

                        if (vaccine.compareTo("null") != 0) txtVaccine.setText(txtVaccine.getText() + vaccine);
                        else txtVaccine.setText(txtVaccine.getText());
                        if (takecare.compareTo("null") != 0) txtTakecare.setText(txtTakecare.getText() + takecare);
                        else txtTakecare.setText(txtTakecare.getText());
                        if (bath.compareTo("null") != 0) txtBath.setText(txtBath.getText() + bath);
                        else txtBath.setText(txtBath.getText());
                        if (grooming.compareTo("null") != 0) txtGrooming.setText(txtGrooming.getText() + grooming);
                        else txtGrooming.setText(txtGrooming.getText());

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Snackbar.make(appointmentLayout, "Don't have appointment!", Snackbar.LENGTH_LONG).show();
                    }
                });

    }
}