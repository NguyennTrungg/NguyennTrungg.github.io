package com.example.petshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.petshop.evenbus.MyUpdateCartEvent;
import com.example.petshop.evenbus.MyUpdateScheduleEvent;
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

import org.greenrobot.eventbus.EventBus;

public class AppointmentSchedule extends AppCompatActivity implements View.OnLongClickListener{
    private TextView txtVaccine, txtTakecare, txtBath, txtGrooming;
    private ImageView btnBack;
    private DatabaseReference mDatabase;
    RelativeLayout appointmentLayout;
    String vaccine, takecare, bath, grooming;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_schedule);

        mDatabase = FirebaseDatabase.getInstance().getReference("Schedule");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        txtVaccine = (TextView) findViewById(R.id.txt_vaccine);
        txtTakecare = (TextView) findViewById(R.id.txt_takecare);
        txtBath = (TextView) findViewById(R.id.txt_bath);
        txtGrooming = (TextView) findViewById(R.id.txt_grooming);

        txtVaccine.setOnLongClickListener(this);
        txtTakecare.setOnLongClickListener(this);
        txtBath.setOnLongClickListener(this);
        txtGrooming.setOnLongClickListener(this);

        btnBack = (ImageView) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        appointmentLayout = (RelativeLayout) findViewById(R.id.appointment_layout);

        mDatabase.child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        vaccine = ""+snapshot.child("Vaccine").getValue();
                        takecare = ""+snapshot.child("TakeCare").getValue();
                        bath = ""+snapshot.child("AnimalBath").getValue();
                        grooming = ""+snapshot.child("Grooming").getValue();

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

    @Override
    public boolean onLongClick(View v) {
        if (v == txtVaccine)
            if (vaccine.compareTo("null") != 0)
                deleteSchedule("Vaccine");
        if (v == txtTakecare)
            if (takecare.compareTo("null") != 0)
                deleteSchedule("TakeCare");
        if (v == txtBath)
            if (bath.compareTo("null") != 0)
                deleteSchedule("AnimalBath");
        if (v == txtGrooming)
            if (grooming.compareTo("null") != 0)
                deleteSchedule("Grooming");
        return false;
    }

    public void deleteSchedule(String service){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(AppointmentSchedule.this);
        builder1.setTitle("Delete Schedule?")
                .setMessage("Do you want to delete your scheduled " + service + " ?")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        String uid = user.getUid();
                        FirebaseDatabase.getInstance()
                                .getReference("Schedule")
                                .child(uid)
                                .child(service)
                                .removeValue()
                                .addOnSuccessListener(aVoid -> EventBus.getDefault().postSticky(new MyUpdateScheduleEvent()));
                        dialog.cancel();
                        finish();
                        startActivity(getIntent());
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }
}