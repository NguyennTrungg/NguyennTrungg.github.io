package com.example.petshop;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.petshop.evenbus.MyUpdateScheduleEvent;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;
import java.util.Date;

public class Services extends AppCompatActivity implements View.OnClickListener{
    EditText edDate, edTime;
    RelativeLayout serviceLayout;
    LinearLayout lnVaccine, lnTakecare, lnBath, lnGrooming;
    private DatabaseReference mDatabase;
    int mYear, mMonth, mDay, mHour, mMinute, currentDay, currentMonth, currentYear;
    String uid;
    FirebaseUser user;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.services);

        serviceLayout = (RelativeLayout) findViewById(R.id.service_layout);

        lnVaccine = (LinearLayout) findViewById(R.id.ln_Vaccine);
        lnTakecare = (LinearLayout) findViewById(R.id.ln_Takecare);
        lnBath = (LinearLayout) findViewById(R.id.ln_Bath);
        lnGrooming = (LinearLayout) findViewById(R.id.ln_Grooming);

        lnVaccine.setOnClickListener(this);
        lnTakecare.setOnClickListener(this);
        lnBath.setOnClickListener(this);
        lnGrooming.setOnClickListener(this);

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();


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
        if (v == lnVaccine)
            CheckSchedule("Vaccine");
        if (v == lnTakecare)
            CheckSchedule("TakeCare");
        if (v == lnBath)
            CheckSchedule("AnimalBath");
        if (v == lnGrooming)
            CheckSchedule("Grooming");
    }
    public void CheckSchedule(String service){
        FirebaseDatabase.getInstance().getReference("Schedule").child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String schedule = ""+snapshot.child(service).getValue();
                        if(schedule.compareTo("null") == 0)
                            Schedule(service, Gravity.CENTER);
                        else{
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(Services.this);
                            builder1.setTitle("Notification!")
                                    .setMessage(service + " has been booked!")
                                    .setCancelable(true)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    })
                                    .show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Snackbar.make(serviceLayout, error.getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                });
    }
    public void checkDate(int year, int monthOfYear, int dayOfMonth){
        if (dayOfMonth <= 9 && monthOfYear + 1 <= 9)
            edDate.setText("0" + dayOfMonth + "-0" + (monthOfYear + 1) + "-" + year);
        else if (dayOfMonth > 9 && monthOfYear + 1 <= 9)
            edDate.setText(dayOfMonth + "-0" + (monthOfYear + 1) + "-" + year);
        else if (dayOfMonth <= 9 && monthOfYear + 1 > 9)
            edDate.setText("0" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
        else
            edDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
    }
    public void Schedule(String service, int gravity){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.schedule);

        edDate = dialog.findViewById(R.id.et_date);
        edTime = dialog.findViewById(R.id.et_time);

        Button btnYes = dialog.findViewById(R.id.btn_Yes);
        Button btnNo = dialog.findViewById(R.id.btn_No);
        Button btnDate = dialog.findViewById(R.id.btn_date);
        Button btnTime = dialog.findViewById(R.id.btn_time);

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                currentDay = c.get(Calendar.DATE);
                currentMonth = c.get(Calendar.MONTH) + 1;
                currentYear = c.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(Services.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                if (currentDay >= dayOfMonth && currentMonth >= monthOfYear && currentYear >= year){
                                    checkDate(year, monthOfYear, dayOfMonth);
                                    btnYes.setEnabled(false);
                                    Toast.makeText(Services.this,"Can't choose a date in the past", Toast.LENGTH_SHORT).show();
                                } else {
                                    checkDate(year, monthOfYear, dayOfMonth);
                                    btnYes.setEnabled(true);
                                }
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(Services.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                if(hourOfDay <= 9 && minute <= 9)
                                    edTime.setText("0" + hourOfDay + ":0" + minute);
                                else if (hourOfDay > 9 && minute <= 9)
                                    edTime.setText(hourOfDay + ":0" + minute);
                                else if (hourOfDay <= 9 && minute > 9)
                                    edTime.setText("0" + hourOfDay + ":" + minute);
                                else
                                    edTime.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });
        Window window = dialog.getWindow();
        if (window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windownAttributes = window.getAttributes();
        windownAttributes.gravity = gravity;
        window.setAttributes(windownAttributes);

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edDate.getText().toString().compareTo("")!= 0
                        & edTime.getText().toString().compareTo("")!= 0){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null){
                        String uid = user.getUid();
                        mDatabase = FirebaseDatabase.getInstance().getReference("Schedule");
                        mDatabase.child(uid).child(service).setValue(edDate.getText() + " " + edTime.getText());
                        Toast.makeText(Services.this,"Your apointment has been set, see you there!", Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                } else {
                    Toast.makeText(Services.this,"Date or Time can't be empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }

}
