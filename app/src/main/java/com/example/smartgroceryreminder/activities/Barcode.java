package com.example.smartgroceryreminder.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class Barcode extends AppCompatActivity implements ZXingScannerView.ResultHandler
//        implements BarcodeScannerView.ActivityCallback
{
    private ZXingScannerView mScannerView;
    private static final String TAG = "BarCode";

//    private ViewGroup mContentFrame;
//    private BarcodeScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScannerView = new ZXingScannerView(this);
        mScannerView.setFlash(true);
        mScannerView.setAutoFocus(true);
        mScannerView.setAspectTolerance(0.5f);
        List<BarcodeFormat> barCodes = new ArrayList<>();
//        BarcodeFormat format = new BarcodeFormat();
        mScannerView.setFormats(barCodes);
        setContentView(mScannerView);
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_barcode);

//        Window window = getWindow();
//        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

//        mContentFrame = findViewById(R.id.layout_content);
//        if (ActivityUtil.solicitarPermisos(this, Manifest.permission.CAMERA,
//                R.string.text_alert, R.string.msg_camera_permission,
//                Constants.PERMISSION_CAMERA)) {
//            initCamera();
//        }
//        initCamera();
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        Log.e(TAG, rawResult.getText()); // Prints scan results
        Log.e(TAG, rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)

        // If you would like to resume scanning, call this method below:
        mScannerView.resumeCameraPreview(this);
    }
//
//    private void initCamera() {
//        mScannerView = new BarcodeScannerView(this);
//        mContentFrame.addView(mScannerView);
//        // Here setFormats
//    }
//
//    private void startCamera() {
//        if (mScannerView != null) {
//            Log.e("BarCode", "Camera Started");
//
//            mScannerView.setResultHandler(this);
//            mScannerView.startCamera();
//        }
//    }
//
//    private void stopCamera() {
//        if (mScannerView != null) {
//            mScannerView.stopCamera();
//        }
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        startCamera();
//    }
//
//    @Override
//    public void onPause() {
//        stopCamera();
//        super.onPause();
//    }
//
//    @Override
//    public void onResult(Result result) {
//        Log.e("BarCode", "Result: " + result.getText());
//    }
//
//    @Override
//    public void onErrorExit(Exception e) {
//        Log.e("BarCode", "Excepton: " + e.getMessage());
//    }

//    @Override
//    public void handleResult(Result rawResult) {
//
//    }
}
