package com.example.petshop.dialog;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.petshop.R;
import com.example.petshop.Services;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class DateTimePickerDialog extends AppCompatActivity implements View.OnClickListener{
    private Button btnDate, btnTime, btnSubmit;
    private EditText etDate, etTime;
    String service;
    int mYear, mMonth, mDay, mHour, mMinute;

    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule);

        Intent intent = getIntent();
        service = intent.getStringExtra("service");

        btnSubmit = (Button) findViewById(R.id.btn_submit);
        btnDate = (Button) findViewById(R.id.btn_date);
        btnTime = (Button) findViewById(R.id.btn_time);
        btnDate.setOnClickListener(this);
        btnTime.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);


        etDate = (EditText) findViewById(R.id.et_date);
        etTime = (EditText) findViewById(R.id.et_time);
    }

    @Override
    public void onClick(View v) {
        if (v == btnDate){
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            if (dayOfMonth <= 9 && monthOfYear + 1 <= 9)
                                etDate.setText("0" + dayOfMonth + "-0" + (monthOfYear + 1) + "-" + year);
                            else if (dayOfMonth > 9 && monthOfYear + 1 <= 9)
                                etDate.setText(dayOfMonth + "-0" + (monthOfYear + 1) + "-" + year);
                            else if (dayOfMonth <= 9 && monthOfYear + 1 > 9)
                                etDate.setText("0" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            else
                                etDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == btnTime){
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            if(hourOfDay <= 9 && minute <= 9)
                                etTime.setText("0" + hourOfDay + ":0" + minute);
                            else if (hourOfDay > 9 && minute <= 9)
                                etTime.setText(hourOfDay + ":0" + minute);
                            else if (hourOfDay <= 9 && minute > 9)
                                etTime.setText("0" + hourOfDay + ":" + minute);
                            else
                                etTime.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
        if (v == btnSubmit){
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null){
                String uid = user.getUid();
                mDatabase = FirebaseDatabase.getInstance().getReference("user");
                mDatabase.child(uid).child(service).setValue(etDate.getText() + " " + etTime.getText());
            }
            Intent intent = new Intent(this, Services.class);
            startActivity(intent);
            finish();
        }
    }
}
