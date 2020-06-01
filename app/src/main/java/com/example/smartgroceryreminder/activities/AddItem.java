package com.example.smartgroceryreminder.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartgroceryreminder.R;
import com.example.smartgroceryreminder.director.Helpers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddItem extends AppCompatActivity {
    private static final String TAG = "AddItem";
    private EditText brand, productName;
    private Button save;
    private RelativeLayout selectDate, selectTime;
    private TextView date, time;
    private String strDate, strTime;
    private Helpers helpers;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        brand = findViewById(R.id.brand);
        productName = findViewById(R.id.productName);
        selectDate = findViewById(R.id.selectDate);
        selectTime = findViewById(R.id.selectTime);
        save = findViewById(R.id.save);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean valid = isValid();

                if (valid) {
                    // Save product to database
                }
            }
        });


        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(AddItem.this,
                        android.R.style.Theme_DeviceDefault_Dialog,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                try {
                                    month = month + 1;
                                    Log.e(TAG, "onDateSet: mm/dd/yy: " + month + "/" + dayOfMonth + "/" + year);
                                    String strDate = month + "/" + dayOfMonth + "/" + year;
                                    date.setText(strDate);
                                    SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
                                    Date d = format.parse(strDate);
                                    strDate = new SimpleDateFormat("EEE, dd, MMM-yyyy").format(d);
                                    date.setText(strDate);
                                } catch (Exception e) {
                                    Log.e(TAG, "Date parsing Exception: " + e.getMessage());
                                }
                            }
                        }, year, month, day);
                dialog.show();
            }
        });

        selectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int hour = cal.get(Calendar.HOUR);
                int minute = cal.get(Calendar.MINUTE);
                TimePickerDialog dialog = new TimePickerDialog(AddItem.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        try {
                            Log.e(TAG, "onTimeSet: hh:mm: " + hourOfDay + ":" + minute);
                            String strTime = hourOfDay + ":" + minute;
                            time.setText(strTime);
                            SimpleDateFormat formatter = new SimpleDateFormat("hh:mm");
                            Date d = formatter.parse(strTime);
                            Log.e(TAG, "Time Parsed: " + d.toString());
                            strTime = new SimpleDateFormat("hh:mm aa").format(d);
                            Log.e(TAG, "Time Formatted: " + strTime);
                            time.setText(strTime);
                        } catch (Exception e) {
                            Log.e(TAG, "Time parsing exception: " + e.getMessage());
                        }
                    }
                }, hour, minute, false);
                dialog.show();
            }
        });

        helpers = new Helpers();
        dialog = new ProgressDialog(this);
    }

    private boolean isValid() {
        boolean flag = true;
        strDate = date.getText().toString();
        strTime = time.getText().toString();
        String error = "";
        if (strDate == null || strDate.length() < 1 || strDate.equals("Select Alarm Date")) {
            error = error + "*Select product alarm date.\n";
            flag = false;
        }
        if (strTime == null || strTime.length() < 1 || strTime.equals("Select Alarm Time")) {
            error = error + "*Select product alarm time.\n";
            flag = false;
        }

        if (!flag) {
            helpers.showError(AddItem.this, "ACTION REQUIRED!", error);
        }

        return flag;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                break;
            }
        }
        return true;
    }
}

