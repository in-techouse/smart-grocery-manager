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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.smartgroceryreminder.R;
import com.example.smartgroceryreminder.director.Helpers;
import com.example.smartgroceryreminder.model.DatabaseHelper;
import com.example.smartgroceryreminder.model.GroceryItems;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddItemAutomatic extends AppCompatActivity {
    private static final String TAG = "AddItem";

//    private static final String APP_KEY = "/75CdiYFv7qz";
//    private static final String AUTHORIZATION_KEY = "Fv03K3h9z9Gk1Yz7";

//    private static final String APP_KEY = "/29iu3F/TwMf";
//    private static final String AUTHORIZATION_KEY = "Tp23V0y4v0Bm7Ao4";

    //    private static final String APP_KEY = "/5T+M/PrPIlD";
//    private static final String AUTHORIZATION_KEY = "Vb51U6g4q7Ag1Gk3";
    private static final String APP_KEY = "///OlYKnuohh";
    private static final String AUTHORIZATION_KEY = "Ly78T3q8m9Ng8Jw5";


    private EditText brand, productName, useage, manufactureDate, expiryDate;
    private Button save;
    private RelativeLayout selectDate, selectTime, selectScan;
    private TextView date, time;
    private String strManufactureDate, strExpiryDate, strExpiryFormatted, strDate, strTime, strFinalDate, strFinalTime, strAlarmDateTime;
    private Helpers helpers;
    private ScrollView main;
    private LinearLayout loading;
    private String code, type;
    private ImageView productImage;
    private GroceryItems item;
    private DatabaseHelper databaseHelper;
    private boolean isWorking = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item_automatic);

        final Intent it = getIntent();
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


        databaseHelper = new DatabaseHelper(getApplicationContext());
        main = findViewById(R.id.main);
        loading = findViewById(R.id.loading);
        productImage = findViewById(R.id.productImage);
        brand = findViewById(R.id.brand);
        productName = findViewById(R.id.productName);
        useage = findViewById(R.id.useage);
        manufactureDate = findViewById(R.id.manufactureDate);
        expiryDate = findViewById(R.id.expiryDate);
        selectDate = findViewById(R.id.selectDate);
        selectTime = findViewById(R.id.selectTime);
        selectScan = findViewById(R.id.selectScan);
        save = findViewById(R.id.save);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);

        item = new GroceryItems();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean valid = isValid();
                if (valid) {
                    // Save product to database
                    item.setAlarm(strAlarmDateTime);
                    long result = databaseHelper.addData(item);
                    Log.e(TAG, "Result: " + result);
                    if (result > 0) {
                        helpers.setAlarm(AddItemAutomatic.this, strFinalDate, strFinalTime, item);
                        helpers.showSuccess(AddItemAutomatic.this, "PRODUCT ADDED", item.getName() + " has been saved to successfully.");
                    } else {
                        helpers.showError(AddItemAutomatic.this, "ERROR", "Product not saved.\nSomething went wrong.\nPlease try again later.");
                    }
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
                DatePickerDialog dialog = new DatePickerDialog(AddItemAutomatic.this,
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
                TimePickerDialog dialog = new TimePickerDialog(AddItemAutomatic.this, new TimePickerDialog.OnTimeSetListener() {
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

        selectScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(AddItemAutomatic.this, ScanProduct.class);
                startActivityForResult(it, 300);
            }
        });

        helpers = new Helpers();
        loading.setVisibility(View.GONE);
        main.setVisibility(View.VISIBLE);
        loadProductDetail();
    }

    private void loadProductDetail() {
        if (helpers.isConnected(getApplicationContext())) {
            if (type.toLowerCase().contains("ean") || type.toLowerCase().contains("upc") || type.toLowerCase().contains("gtin") || type.toLowerCase().contains("apn") || type.toLowerCase().contains("jpn")) {
                String signature = helpers.hmacSha1(code, AUTHORIZATION_KEY);
                Log.e(TAG, "Signature is: " + signature);
                String url = "https://www.digit-eyes.com/gtin/v2_0/?upcCode=" + code + "%20&field_names=all&language=en&app_key=" + APP_KEY + "&signature=" + signature;
                loadProduct(url);
            } else {
                helpers.showError(AddItemAutomatic.this, "ERROR!", "Cannot load the product detail. Didn't got the UPC or EAN Code.");
                loading.setVisibility(View.GONE);
                main.setVisibility(View.VISIBLE);
            }
        } else {
            helpers.showError(AddItemAutomatic.this, "ERROR!", "No internet connection found.\nConnect to a network and try again.");
            loading.setVisibility(View.GONE);
            main.setVisibility(View.VISIBLE);
        }
    }

    private void loadProduct(String url) {
        isWorking = true;
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "Product Detail: " + response);
                        isWorking = false;
                        loading.setVisibility(View.GONE);
                        main.setVisibility(View.VISIBLE);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.has("return_message")) {
                                if (!jsonObject.getString("return_message").equals("Success")) {
                                    helpers.showError(AddItemAutomatic.this, "ERROR!", "Sorry, couldn't load the product detail.\nPlease try again later.");
                                    return;
                                }
                            }
                            String strImage = "";
                            String strBrand = "";
                            String strProductName = "";
                            String strUseage = "";
                            if (jsonObject.has("image")) {
                                strImage = jsonObject.getString("image");
                                Glide.with(getApplicationContext()).load(strImage).into(productImage);
                            } else {
                                productImage.setVisibility(View.GONE);
                            }
                            if (jsonObject.has("brand")) {
                                strBrand = jsonObject.getString("brand");
                                brand.setText(strBrand);
                            }
                            if (jsonObject.has("description")) {
                                strProductName = jsonObject.getString("description");
                                productName.setText(strProductName);
                            }
                            if (jsonObject.has("useage")) {
                                strUseage = jsonObject.getString("useage");
                                useage.setText(strUseage);
                            }

                            item.setImage(strImage);
                            item.setBrand(strBrand);
                            item.setName(strProductName);
                            item.setUseage(strUseage);
                        } catch (Exception e) {
                            helpers.showError(AddItemAutomatic.this, "ERROR!", "Sorry, couldn't load the product detail.\nPlease try again later.");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error: " + error.getMessage());
                        loading.setVisibility(View.GONE);
                        main.setVisibility(View.VISIBLE);
                        helpers.showError(AddItemAutomatic.this, "ERROR!", "Sorry, couldn't load the product detail.\nPlease try again later.");
                        isWorking = false;
                    }
                });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 300 && resultCode == RESULT_OK && data != null) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                GroceryItems temp = (GroceryItems) bundle.getSerializable("result");
                if (temp != null) {
                    item.setExpiryFormatted(temp.getExpiryFormatted());
                    item.setExpiryDate(temp.getExpiryDate());
                    item.setManufactureDate(temp.getManufactureDate());
                    expiryDate.setText(item.getExpiryDate());
                    manufactureDate.setText(item.getManufactureDate());
                }
            }
        }
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
            helpers.showError(AddItemAutomatic.this, "ACTION REQUIRED!", error);
        }

        return flag;
    }

    @Override
    public void onBackPressed() {
        if (!isWorking) {
            finish();
        }
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