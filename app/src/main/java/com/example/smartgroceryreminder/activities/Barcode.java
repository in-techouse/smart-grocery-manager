package com.example.smartgroceryreminder.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class Barcode extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private static final String TAG = "BarCode";
    private static final int PERMISSION_CODE = 10;
    private final String[] PERMISSIONS = {
            Manifest.permission.CAMERA,
    };
    private ZXingScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScannerView = new ZXingScannerView(this);
        mScannerView.setFlash(true);
        mScannerView.setAutoFocus(true);
        mScannerView.setAspectTolerance(0.5f);
        List<BarcodeFormat> barCodes = new ArrayList<>();
        mScannerView.setFormats(barCodes);
        setContentView(mScannerView);
        startScanner();
    }

    private boolean hasPermissions(Context c, String... permission) {
        for (String p : permission) {
            if (ActivityCompat.checkSelfPermission(c, p) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
    }

    private void startScanner() {
        if (hasPermissions(getApplicationContext(), PERMISSIONS)) {
            mScannerView.startCamera();
        } else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(Barcode.this);
            dialog.setMessage("To scan the product, you need to grant the camera permission.\nDo you allow SMART GROCERY REMINDER to access the camera?");
            dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ActivityCompat.requestPermissions(Barcode.this, PERMISSIONS, PERMISSION_CODE);
                }
            });
            dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            dialog.show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE) {
            startScanner();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        Log.e(TAG, rawResult.getText());
        Log.e(TAG, rawResult.getBarcodeFormat().toString());
        mScannerView.resumeCameraPreview(this);
        Intent it = new Intent(Barcode.this, AddItem.class);
        Bundle bundle = new Bundle();
//        bundle.putString("code", rawResult.getText());
//        bundle.putString("type", rawResult.getBarcodeFormat().name());
        it.putExtra("code", rawResult.getText());
        it.putExtra("type", rawResult.getBarcodeFormat().name());
        startActivity(it);
        finish();
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
