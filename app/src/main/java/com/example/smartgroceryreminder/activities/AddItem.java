package com.example.smartgroceryreminder.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartgroceryreminder.R;
import com.example.smartgroceryreminder.director.Helpers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddItem extends AppCompatActivity {
    private static final String TAG = "AddItem";
    private EditText productName, manufactureDate, expiryDate;
    private Button save;
    private RelativeLayout selectDate, selectTime;
    private TextView date, time;
    private String strDate, strTime;
    private Helpers helpers;
    private ScrollView main;
    private LinearLayout loading;
    private String code, type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        Intent it = getIntent();

        if (it == null) {
            finish();
            return;
        }

        code = it.getStringExtra("code");
        type = it.getStringExtra("type");

        if (code == null || type == null) {
            finish();
            return;
        }

        Log.e(TAG, "Code: " + code);
        Log.e(TAG, "Type: " + type);

        main = findViewById(R.id.main);
        loading = findViewById(R.id.loading);
        productName = findViewById(R.id.productName);
        manufactureDate = findViewById(R.id.manufactureDate);
        expiryDate = findViewById(R.id.expiryDate);
        selectDate = findViewById(R.id.selectDate);
        selectTime = findViewById(R.id.selectTime);
        save = findViewById(R.id.save);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);

        productName.setText("SomeText");
        manufactureDate.setText("SomeText");
        expiryDate.setText("SomeText");

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
        loadProductDetail();
    }

    private void loadProductDetail() {
        if (helpers.isConnected(getApplicationContext())) {
            if (type.toLowerCase().contains("ean") || type.toLowerCase().contains("upc") || type.toLowerCase().contains("gtin") || type.toLowerCase().contains("apn") || type.toLowerCase().contains("jpn")) {
                String signature = helpers.hmacSha1(code, "Tp23V0y4v0Bm7Ao4");
                Log.e(TAG, "Signature is: " + signature);
                String url = "https://www.digit-eyes.com/gtin/v2_0/?upcCode=" + code + "%20&field_names=all&language=en&app_key=/29iu3F/TwMf&signature=" + signature;
                loadProduct(url);
            }
        } else {
            helpers.showError(AddItem.this, "ERROR!", "No internet connection found.\nConnect to a network and try again.");
            loading.setVisibility(View.GONE);
            main.setVisibility(View.VISIBLE);
        }
    }

    private void loadProduct(String url) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "Product Detail: " + response);
                        loading.setVisibility(View.GONE);
                        main.setVisibility(View.VISIBLE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.setVisibility(View.GONE);
                        main.setVisibility(View.VISIBLE);
                        helpers.showError(AddItem.this, "ERROR!", "Sorry, couldn't load the product detail.\nPlease try again later.");
                    }
                });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
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

