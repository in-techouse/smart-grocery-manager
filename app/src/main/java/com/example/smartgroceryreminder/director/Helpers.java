package com.example.smartgroceryreminder.director;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.util.Log;

import com.example.smartgroceryreminder.R;
import com.example.smartgroceryreminder.receivers.AlarmReceiver;
import com.shreyaspatil.MaterialDialog.MaterialDialog;
import com.shreyaspatil.MaterialDialog.interfaces.DialogInterface;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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

    public void showSuccess(final Activity activity, String title, String message) {
        MaterialDialog mDialog = new MaterialDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Okay", R.drawable.ic_action_tick, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                        activity.finish();
                    }
                })
                .setNegativeButton("Cancel", R.drawable.ic_action_close, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                        activity.finish();
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

    public int calculateMonthNumber(String month) {
        int number = 0;
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

        switch (month.toLowerCase()) {
            case "jan": {
                number = 1;
                break;
            }
            case "feb": {
                number = 2;
                break;
            }
            case "mar": {
                number = 3;
                break;
            }
            case "apr": {
                number = 4;
                break;
            }
            case "may": {
                number = 5;
                break;
            }
            case "jun": {
                number = 6;
                break;
            }
            case "jul": {
                number = 7;
                break;
            }
            case "aug": {
                number = 8;
                break;
            }
            case "sep": {
                number = 9;
                break;
            }
            case "oct": {
                number = 10;
                break;
            }
            case "nov": {
                number = 11;
                break;
            }
            case "dec": {
                number = 12;
                break;
            }
        }

        return number;
    }

    public void setAlarm(Activity activity, String strDate, String strTime) {
        int year = 0;
        int month = 0;
        int day = 0;
        int hour = 0;
        int minute = 0;
        String[] datearray = strDate.split("/");
        month = Integer.parseInt(datearray[0]);
        day = Integer.parseInt(datearray[1]);
        year = Integer.parseInt(datearray[2]);
        Log.e("Alarm", "Date: " + strDate + " Time: " + strTime);
        Log.e("Alarm", "Year: " + year);
        Log.e("Alarm", "Month: " + month);
        Log.e("Alarm", "Day: " + day);
        String[] timearray = strTime.split(" ");
        String[] tArray = timearray[0].split(":");
        hour = Integer.parseInt(tArray[0]);
        minute = Integer.parseInt(tArray[1]);
        Log.e("Alarm", "AM/PM: " + timearray[1]);

        if (timearray[1].equalsIgnoreCase("pm")) {
            Log.e("Alarm", "PM Condition true");

            hour = hour + 12;
        }
        Log.e("Alarm", "Hour: " + hour);
        Log.e("Alarm", "Minutes: " + minute);

        Calendar calendar = Calendar.getInstance();
        Intent intent = new Intent(activity, AlarmReceiver.class);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR, hour);
        calendar.set(Calendar.MINUTE, minute);


        Log.e("Alarm", "Alarm Time: " + calendar.getTime());

        String number = (year + month) + "" + (day + hour) + minute + "";

        int num = Integer.parseInt(number);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(activity, num, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }
}
