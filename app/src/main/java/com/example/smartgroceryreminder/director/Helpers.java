package com.example.smartgroceryreminder.director;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;

import com.example.smartgroceryreminder.R;
import com.shreyaspatil.MaterialDialog.MaterialDialog;
import com.shreyaspatil.MaterialDialog.interfaces.DialogInterface;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Helpers {

    public boolean isConnected(Context c) {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        connected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
        return connected;
    }

    public void showError(Activity activity, String title, String message) {
        MaterialDialog mDialog = new MaterialDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Okay", R.drawable.ic_action_tick, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("Cancel", R.drawable.ic_action_close, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                    }
                })
                .build();

        // Show Dialog
        mDialog.show();
    }

    public String hmacSha1(String value, String key) {
        try {
            String type = "HmacSHA1";
            SecretKeySpec secret = new SecretKeySpec(key.getBytes(), type);
            Mac mac = Mac.getInstance(type);
            mac.init(secret);
            byte[] bytes = mac.doFinal(value.getBytes());
            String base64 = Base64.encodeToString(bytes, Base64.DEFAULT);
            return base64;
//            String hex = bytesToHex(bytes);
        } catch (Exception e) {
            return "";
        }
    }

    private final static char[] hexArray = "0123456789abcdef".toCharArray();

    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

//    public String generateHashWithHmac1(String message, String key) {
//        try {
//            final String hashingAlgorithm = "HmacSHA1"; //or "HmacSHA1", "HmacSHA512"
//            byte[] bytes = hmac(hashingAlgorithm, key.getBytes(), message.getBytes());
//            final String messageDigest = bytesToHex(bytes);
//            return messageDigest;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "";
//        }
//    }
//
//    public static byte[] hmac(String algorithm, byte[] key, byte[] message) throws NoSuchAlgorithmException, InvalidKeyException {
//        Mac mac = Mac.getInstance(algorithm);
//        mac.init(new SecretKeySpec(key, algorithm));
//        return mac.doFinal(message);
//    }
//
//    public static String bytesToHex(byte[] bytes) {
//        final char[] hexArray = "0123456789abcdef".toCharArray();
//        char[] hexChars = new char[bytes.length * 2];
//        for (int j = 0, v; j < bytes.length; j++) {
//            v = bytes[j] & 0xFF;
//            hexChars[j * 2] = hexArray[v >>> 4];
//            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
//        }
//        return new String(hexChars);
//    }
}
