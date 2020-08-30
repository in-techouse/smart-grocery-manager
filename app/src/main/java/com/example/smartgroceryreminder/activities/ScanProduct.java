package com.example.smartgroceryreminder.activities;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.smartgroceryreminder.R;
import com.example.smartgroceryreminder.director.Helpers;
import com.example.smartgroceryreminder.model.GroceryItems;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Stack;

public class ScanProduct extends AppCompatActivity {
    private static final String TAG = "ScanProduct";
    private static final int PERMISSION_CODE = 10;
    private SurfaceView cameraView;
    private CameraSource cameraSource;
    private Helpers helpers;
    private String result = "";
    private List<String> manufacturingKeys, expiryKeys;
    private int count = 0;
    private SurfaceHolder.Callback callback;
    private GroceryItems item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_product);

        cameraView = findViewById(R.id.surface_view);
        helpers = new Helpers();
        manufacturingKeys = new ArrayList<>();
        expiryKeys = new ArrayList<>();

        // Manufacturing Date
        manufacturingKeys.add("Manufacture Date".toLowerCase());
        manufacturingKeys.add("Date Manufacture".toLowerCase());
        manufacturingKeys.add("Date of Manufacture".toLowerCase());
        manufacturingKeys.add("Manufacturing Date".toLowerCase());
        manufacturingKeys.add("MFG".toLowerCase());
        manufacturingKeys.add("MFD".toLowerCase());
        manufacturingKeys.add("MFG Date".toLowerCase());
        manufacturingKeys.add("MFG. Date".toLowerCase());
        manufacturingKeys.add("M.F.G".toLowerCase());
        // Expiry Date
        expiryKeys.add("Expiry Date".toLowerCase());
        expiryKeys.add("Date Expiry".toLowerCase());
        expiryKeys.add("Date of Expiry".toLowerCase());
        expiryKeys.add("EXP".toLowerCase());
        expiryKeys.add("EXP Date".toLowerCase());
        expiryKeys.add("EXP. Date".toLowerCase());
        expiryKeys.add("E.X.P".toLowerCase());
        expiryKeys.add("BB".toLowerCase());
        expiryKeys.add("BBE".toLowerCase());
        expiryKeys.add("B.B".toLowerCase());
        expiryKeys.add("B.B.E".toLowerCase());
        expiryKeys.add("Best Before".toLowerCase());
        expiryKeys.add("Use by".toLowerCase());
        expiryKeys.add("Use before".toLowerCase());
        item = new GroceryItems();
        startScanning();
        AlertDialog.Builder dialog = new AlertDialog.Builder(ScanProduct.this);
        dialog.setMessage("Hold the mobile on the MANUFACTURE & EXPIRY DATE, upto 30 seconds to do a clean and accurate scan.");
        dialog.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        dialog.setNegativeButton("I can't", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        dialog.show();
    }

    private void startScanning() {
        TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
        if (!textRecognizer.isOperational()) {
            Log.e(TAG, "Detector dependencies are not yet available");
            helpers.showError(ScanProduct.this, "ERROR!", "Detector dependencies are not yet available");
        } else {
            cameraSource = new CameraSource.Builder(getApplicationContext(), textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280, 1024)
                    .setRequestedFps(2.0f)
                    .setAutoFocusEnabled(true)
                    .build();

            callback = new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    try {
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(ScanProduct.this, new String[]{Manifest.permission.CAMERA}, PERMISSION_CODE);
                            return;
                        }
                        cameraSource.start(cameraView.getHolder());
                    } catch (IOException e) {
                        helpers.showError(ScanProduct.this, "ERROR!", "Something went wrong.\nPlease try again later.");
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    cameraSource.stop();
                }
            };
            cameraView.getHolder().addCallback(callback);

            textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
                @Override
                public void release() {
                    Log.e(TAG, "Release Called");
                }

                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) {
                    final SparseArray<TextBlock> items = detections.getDetectedItems();
                    if (count > 4 && count < 20 && items.size() != 0) {
                        new Runnable() {
                            @Override
                            public void run() {
                                StringBuilder stringBuilder = new StringBuilder();
                                for (int i = 0; i < items.size(); ++i) {
                                    TextBlock item = items.valueAt(i);
                                    stringBuilder.append(item.getValue());
                                    stringBuilder.append(" ");
                                }
                                result = result + " " + stringBuilder.toString().toLowerCase();
//                                Log.e(TAG, "Count is: " + count + " Result is: " + result);
                            }
                        }.run();
                    } else if (count == 20) {
//                        Log.e(TAG, "Inside else count is: " + count);
                        searchDates();
                    }
                    count++;
                }
            });
        }
    }

    public void showError() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                cameraView.getHolder().removeCallback(callback);
                cameraView.setVisibility(View.GONE);
                AlertDialog.Builder dialog = new AlertDialog.Builder(ScanProduct.this);
                dialog.setMessage("Could not found any date.");
                dialog.setPositiveButton("CLOSE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sendNADates();
                    }
                });
                dialog.show();
            }
        });
    }


    private void searchDates() {
//        Log.e(TAG, "Search Started with complete result: " + result);
        String strResult = result.toLowerCase();
        String strMfd = "";
        String strExp = "";
        boolean isMfdFound = false;
        boolean isExpFound = false;
        for (String key : manufacturingKeys) {
            if (strResult.contains(key)) {
                isMfdFound = true;
//                Log.e(TAG, "Key Found: " + key);
                strMfd = key;
                break;
            }
        }

        for (String key : expiryKeys) {
            if (strResult.contains(key)) {
                isExpFound = true;
//                Log.e(TAG, "Key Found: " + key);
                strExp = key;
                break;
            }
        }

        if (!isMfdFound && !isExpFound) {
            Log.e(TAG, "No Key Found");
            ScanProduct.this.showError();
            return;
        }

        Log.e(TAG, "Manufacture Key found is: " + strMfd);
        Log.e(TAG, "Expiry Key found is: " + strExp);

        if (isMfdFound) {
            int indexOfMfd = strResult.indexOf(strMfd);
//            Log.e(TAG, "Index of manufacture Key is: " + indexOfMfd);
//            Log.e(TAG, "Total String Length is: " + strResult.length());
            String finalMfd = strResult.substring(indexOfMfd);
//            Log.e(TAG, "MFD New String Length is: " + finalMfd.length());
            calculateDate(finalMfd, "MFD_DATE");
        } else {
            item.setManufactureDate("N/A");
        }

        if (isExpFound) {
            int indexOfExp = strResult.indexOf(strExp);
//            Log.e(TAG, "Index of expiry Key is: " + indexOfExp);
//            Log.e(TAG, "Total String Length is: " + strResult.length());
            String finalExp = strResult.substring(indexOfExp);
//            Log.e(TAG, "EXP New String Length is: " + finalExp.length());
            calculateDate(finalExp, "EXP_DATE");
        } else {
            item.setExpiryDate("N/A");
        }

        Log.e(TAG, "Manufacture Date: " + item.getManufactureDate());
        Log.e(TAG, "Expiry Date: " + item.getExpiryDate());
        Log.e(TAG, "Expiry Date Formatted: " + item.getExpiryFormatted());

        if (item.getManufactureDate() == null)
            item.setManufactureDate("N/A");
        if (item.getExpiryDate() == null)
            item.setExpiryDate("N/A");

        Intent returnIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("result", item);
        returnIntent.putExtras(bundle);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    private void calculateDate(String str, String DATE_TAG) {
//        Log.e(TAG, "Calculate Date Called");
//        Log.e(TAG, "" + DATE_TAG + ", " + str);

        List<String> months = new ArrayList<>();
        months.add("Jan".toLowerCase());
        months.add("Feb".toLowerCase());
        months.add("Mar".toLowerCase());
        months.add("Apr".toLowerCase());
        months.add("May".toLowerCase());
        months.add("Jun".toLowerCase());
        months.add("Jul".toLowerCase());
        months.add("Aug".toLowerCase());
        months.add("Sep".toLowerCase());
        months.add("Oct".toLowerCase());
        months.add("Nov".toLowerCase());
        months.add("Dec".toLowerCase());

        Stack<Character> yearStack = new Stack<>();
        Stack<Character> monthStack = new Stack<>();
        StringBuilder buffer = new StringBuilder(str);
        char[] dateChars = buffer.reverse().toString().toCharArray();
        for (char ch : dateChars) {
            yearStack.push(ch);
        }
        String strYear = "";
        int intYear = -1;
        int intMonth = -1;
        int intDay = -1;
        char tempCh = yearStack.pop();
        monthStack.push(tempCh);
        strYear = tempCh + "";

        tempCh = yearStack.pop();
        monthStack.push(tempCh);
        strYear = strYear + tempCh + "";

        tempCh = yearStack.pop();
        monthStack.push(tempCh);
        strYear = strYear + tempCh + "";

        tempCh = yearStack.pop();
        monthStack.push(tempCh);
        strYear = strYear + tempCh + "";

        for (int i = 0; i < yearStack.size(); i++) {
            try {
                int tempInt = Integer.parseInt(strYear);
                Log.e(TAG, DATE_TAG + " Year is: " + tempInt);
                if (tempInt >= 2018 && tempInt < 2100) {
                    if (intYear == -1) {
                        intYear = tempInt;
                    } else if (DATE_TAG.equals("EXP_DATE") && tempInt > intYear) {
                        intYear = tempInt;
                    } else if (DATE_TAG.equals("MFD_DATE") && tempInt < intYear) {
                        intYear = tempInt;
                    }
                }
                Log.e(TAG, DATE_TAG + " Final Year is: " + intYear);
            } catch (Exception e) {
            }
            strYear = strYear.substring(1);
            tempCh = yearStack.pop();
            monthStack.push(tempCh);
            strYear = strYear + tempCh + "";
        }

        if (intYear == -1) {
            setNADate(DATE_TAG);
        } else {
            Log.e(TAG, "Correct Year for " + DATE_TAG + " is: " + intYear);
            StringBuilder monthString = new StringBuilder();
            for (int i = 0; i < monthStack.size(); i++) {
                monthString.append(monthStack.get(i).toString());
            }
            String strMonth = monthString.toString();
            int yearIndex = strMonth.indexOf(intYear + "");
//            Log.e(TAG, "Year index in Month String is: " + yearIndex);
            strMonth = strMonth.substring(0, yearIndex);
            Log.e(TAG, "Month String is: " + strMonth);
            String separator = strMonth.toCharArray()[yearIndex - 1] + "";
            Log.e(TAG, "Month String, Separator is: " + separator);
            if (separator.equals(" ") || separator.equals(".") || separator.equals("-") || separator.equals("/")) {
                Log.e(TAG, "Month String, Desired Separator Found: " + separator);
                boolean flag = false;
                String expiryMonth = "";
                for (String strM : months) {
                    if (strMonth.contains(strM)) {
                        expiryMonth = strM;
                        flag = true;
                        break;
                    }
                }
                Log.e(TAG, "Expiry Month is: " + expiryMonth + " Str Month: " + strMonth);
                if (flag) {
                    intMonth = helpers.calculateMonthNumber(expiryMonth);
                    Log.e(TAG, "Int Month: " + intMonth);
                    int monthIndex = strMonth.indexOf(expiryMonth);
                    Log.e(TAG, "Int Month Index: " + monthIndex);
                    strMonth = strMonth.substring(0, monthIndex - 1);
                    Log.e(TAG, "Remaining String: " + strMonth);
                    String strDay = strMonth.substring(monthIndex - 3);
                    Log.e(TAG, "Day String: " + strDay);
                    try {
                        int tempDay = Integer.parseInt(strDay);
                        intDay = tempDay;
                    } catch (Exception e) {

                    }
                    setFinalDate(DATE_TAG, intYear, intMonth, intDay);
                } else {
                    try {
                        Log.e(TAG, "Month name not found, Month String is: " + strMonth);
                        String strM = strMonth.substring(strMonth.length() - 3, strMonth.length() - 1);
                        Log.e(TAG, "Month: " + strM);
                        String strD = strMonth.substring(strMonth.length() - 6, strMonth.length() - 4);
                        Log.e(TAG, "Day: " + strD);
                        intMonth = Integer.parseInt(strM);
                        intDay = Integer.parseInt(strD);
                        setFinalDate(DATE_TAG, intYear, intMonth, intDay);
                    } catch (Exception e) {
                        sendNADates();
                    }
                }
            } else {
                setNADate(DATE_TAG);
            }
        }
    }

    private void setFinalDate(String DATE_TAG, int year, int month, int day) {
        String strDate = "";
        String strFormattedDate = "";
        boolean isDay = false;
        boolean isMonth = false;
        boolean isYear = false;
        if (day > 0 && day < 32) {
            strDate = strDate + day + "/";
            isDay = true;
        }
        if (month > 0 && month < 13) {
            strDate = strDate + month + "/";
            isMonth = true;
        }
        if (year > 2000 && month < 2100) {
            strDate = strDate + year;
            isYear = true;
        }
        Log.e(TAG, DATE_TAG + " date is: " + strDate);
        Calendar calendar = Calendar.getInstance();
        try {
            if (isDay) {
                calendar.set(Calendar.DAY_OF_MONTH, day);
            }
            if (isMonth) {
                calendar.set(Calendar.MONTH, month - 1);
            }
            if (isYear) {
                calendar.set(Calendar.YEAR, year);
            }
            Log.e(TAG, DATE_TAG + " parse date is: " + calendar.getTime());
            strFormattedDate = new SimpleDateFormat("EEE, dd, MMM, yyyy").format(calendar.getTime());
            Log.e(TAG, DATE_TAG + " Formatted date is: " + strFormattedDate);
        } catch (Exception e) {
            setNADate(DATE_TAG);
        }

        if (DATE_TAG.equals("EXP_DATE")) {
            item.setExpiryDate(strFormattedDate);
            String expiryDateFormatted = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
            item.setExpiryFormatted(expiryDateFormatted);
        } else if (DATE_TAG.equals("MFD_DATE")) {
            item.setManufactureDate(strFormattedDate);
        }
    }

    private void setNADate(String DATE_TAG) {
        Log.e(TAG, "Set NA Called for: " + DATE_TAG);
        if (DATE_TAG.equals("EXP_DATE") && item.getExpiryDate() == null) {
            item.setExpiryDate("N/A");
        } else if (DATE_TAG.equals("MFD_DATE") && item.getManufactureDate() == null) {
            item.setManufactureDate("N/A");
        }
        if (item.getExpiryDate() == null) {
            item.setExpiryDate("N/A");
        }
        if (item.getManufactureDate() == null) {
            item.setManufactureDate("N/A");
        }
        if (item.getManufactureDate().equals("N/A") && item.getExpiryDate().equals("N/A")) {
            showError();
            return;
        }
        if (DATE_TAG.equals("EXP_DATE")) {
            item.setExpiryDate("N/A");
        } else if (DATE_TAG.equals("MFD_DATE")) {
            item.setManufactureDate("N/A");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    try {
                        cameraSource.start(cameraView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
            break;
        }
    }

    private void sendNADates() {
        item.setManufactureDate("N/A");
        item.setExpiryDate("N/A");
        Intent returnIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("result", item);
        returnIntent.putExtras(bundle);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        sendNADates();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                sendNADates();
                break;
            }
        }
        return true;
    }
}