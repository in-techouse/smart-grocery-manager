package com.example.smartgroceryreminder.activities;

import android.app.DatePickerDialog;
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
import com.example.smartgroceryreminder.model.DatabaseHelper;
import com.example.smartgroceryreminder.model.GroceryItems;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddItem extends AppCompatActivity {
    private static final String TAG = "AddItem";
    private EditText brand, productName, useage;
    private Button save;
    private RelativeLayout selectManufactureDate, selectExpiryDate, selectDate, selectTime;
    private TextView manufactureDate, expiryDate, date, time;
    private String strBrand, strProductName, strUseage, strManufactureDate, strExpiryDate, strExpiryFormatted, strDate, strTime, strFinalDate, strFinalTime, strAlarmDateTime;
    private Helpers helpers;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        brand = findViewById(R.id.brand);
        productName = findViewById(R.id.productName);
        useage = findViewById(R.id.useage);
        selectManufactureDate = findViewById(R.id.selectManufactureDate);
        selectExpiryDate = findViewById(R.id.selectExpiryDate);
        selectDate = findViewById(R.id.selectDate);
        selectTime = findViewById(R.id.selectTime);
        save = findViewById(R.id.save);
        manufactureDate = findViewById(R.id.manufactureDate);
        expiryDate = findViewById(R.id.expiryDate);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);

        databaseHelper = new DatabaseHelper(getApplicationContext());

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean valid = isValid();
                if (valid) {
                    // Save product to database
                    GroceryItems item = new GroceryItems();
                    item.setImage("");
                    item.setBrand(strBrand);
                    item.setName(strProductName);
                    item.setUseage(strUseage);
                    item.setManufactureDate(strManufactureDate);
                    item.setExpiryDate(strExpiryDate);
                    item.setAlarm(strAlarmDateTime);
                    item.setExpiryFormatted(strExpiryFormatted);
                    long result = databaseHelper.addData(item);
                    Log.e(TAG, "Result: " + result);
                    if (result > 0) {
                        helpers.setAlarm(AddItem.this, strFinalDate, strFinalTime);
                        helpers.showSuccess(AddItem.this, "PRODUCT ADDED", strProductName + " has been saved to successfully.");
                    } else {
                        helpers.showError(AddItem.this, "ERROR", "Product not saved.\nSomething went wrong.\nPlease try again later.");
                    }
                }
            }
        });

        selectManufactureDate.setOnClickListener(new View.OnClickListener() {
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
                                    manufactureDate.setText(strDate);
                                    SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
                                    Date d = format.parse(strDate);
                                    strDate = new SimpleDateFormat("EEE, dd, MMM-yyyy").format(d);
                                    manufactureDate.setText(strDate);
                                } catch (Exception e) {
                                    Log.e(TAG, "Date parsing Exception: " + e.getMessage());
                                }
                            }
                        }, year, month, day);
                dialog.show();
            }
        });

        selectExpiryDate.setOnClickListener(new View.OnClickListener() {
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
                                    expiryDate.setText(strDate);
                                    SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
                                    SimpleDateFormat formatted = new SimpleDateFormat("yyyy-MM-dd");
                                    Date d = format.parse(strDate);
                                    strExpiryFormatted = formatted.format(d);
                                    Log.e(TAG, "Expiry Formatted Date is: " + strExpiryFormatted);
                                    strDate = new SimpleDateFormat("EEE, dd, MMM-yyyy").format(d);
                                    expiryDate.setText(strDate);
                                } catch (Exception e) {
                                    Log.e(TAG, "Date parsing Exception: " + e.getMessage());
                                }
                            }
                        }, year, month, day);
                dialog.show();
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
                                    strFinalDate = strDate;
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
                            strFinalTime = strTime;
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
    }

    private boolean isValid() {
        boolean flag = true;
        strBrand = brand.getText().toString();
        strProductName = productName.getText().toString();
        strUseage = useage.getText().toString();
        strManufactureDate = manufactureDate.getText().toString();
        strExpiryDate = expiryDate.getText().toString();
        strDate = date.getText().toString();
        strTime = time.getText().toString();
        strAlarmDateTime = strFinalDate + " " + strFinalTime;
        Log.e(TAG, "Alarm Date Time: " + strAlarmDateTime);
        String error = "";
        if (strProductName.length() < 1) {
            productName.setError("Product name is required");
            flag = false;
        }
        if (strManufactureDate == null || strManufactureDate.length() < 1 || strManufactureDate.equals("Select Manufacturing Date")) {
            error = error + "*Select product manufacturing date.\n";
            flag = false;
        }
        if (strExpiryDate == null || strExpiryDate.length() < 1 || strExpiryDate.equals("Select Expiry Date")) {
            error = error + "*Select product expiry date.\n";
            flag = false;
        }

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

