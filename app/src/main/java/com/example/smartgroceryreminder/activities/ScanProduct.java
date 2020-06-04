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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.smartgroceryreminder.R;
import com.example.smartgroceryreminder.director.Helpers;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ScanProduct extends AppCompatActivity {
    private static final String TAG = "ScanProduct";
    private static final int PERMISSION_CODE = 10;
    private SurfaceView cameraView;
    private CameraSource cameraSource;
    private Helpers helpers;
    private String result = "";
    private List<String> query;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_product);
        cameraView = findViewById(R.id.surface_view);

        helpers = new Helpers();
        query = new ArrayList<>();
        // Manufacturing Date
        query.add("Manufacture Date".toLowerCase());
        query.add("Date Manufacture".toLowerCase());
        query.add("Date of Manufacture".toLowerCase());
        query.add("Manufacturing Date".toLowerCase());
        query.add("MFG".toLowerCase());
        query.add("MFD".toLowerCase());
        query.add("MFG Date".toLowerCase());
        query.add("MFG. Date".toLowerCase());
        query.add("M.F.G".toLowerCase());
        // Expiry Date
        query.add("Expiry Date".toLowerCase());
        query.add("Date Expiry".toLowerCase());
        query.add("Date of Expiry".toLowerCase());
        query.add("EXP".toLowerCase());
        query.add("EXP Date".toLowerCase());
        query.add("EXP. Date".toLowerCase());
        query.add("E.X.P".toLowerCase());
        query.add("BB".toLowerCase());
        query.add("BBE".toLowerCase());
        query.add("B.B".toLowerCase());
        query.add("B.B.E".toLowerCase());
        query.add("Best Before".toLowerCase());
        query.add("Use by".toLowerCase());
        query.add("Use before".toLowerCase());
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
            cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
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
            });

            textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
                @Override
                public void release() {
                    Log.e(TAG, "Release Called");

                }

                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) {
                    final SparseArray<TextBlock> items = detections.getDetectedItems();
                    if (count > 4 && count < 40 && items.size() != 0) {
                        new Runnable() {
                            @Override
                            public void run() {
                                StringBuilder stringBuilder = new StringBuilder();
                                for (int i = 0; i < items.size(); ++i) {
                                    TextBlock item = items.valueAt(i);
                                    stringBuilder.append(item.getValue());
                                    Log.e(TAG, "Value from item list: " + item.getValue());
                                    stringBuilder.append(" ");
                                }
                                result = result + " " + stringBuilder.toString().toLowerCase();
                            }
                        }.run();
                    }
                    if (count > 40) {
                        searchDates();
                    } else {
                        Log.e(TAG, "Count is: " + count + " with text: " + result);
                    }
                    count++;
                }
            });
        }
    }

    private void searchDates() {
        Log.e(TAG, "Search Started");
        String strResult = result.toLowerCase();
        for (String key : query) {
            if (strResult.contains(key)) {
                Log.e(TAG, "Key Found: " + key);
            }
        }
//        Intent returnIntent = new Intent();
//        Bundle bundle = new Bundle();
////        bundle.putSerializable("result", post);
//        returnIntent.putExtras(bundle);
//        setResult(Activity.RESULT_OK, returnIntent);
//        finish();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
                break;
            }
        }
        return true;
    }
}