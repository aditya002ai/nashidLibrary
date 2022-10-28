package com.kyc.nashidmrz;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;

//import com.omanid.MainActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class MyUtils {
    private static final MyUtils ourInstance = new MyUtils();
    public String version = "1.08";
    public boolean landScape = true;
    public String customerID;

    public String getReferenceNumber() {
        return ReferenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        ReferenceNumber = referenceNumber;
    }

    public String ReferenceNumber;

    public String getFacecomparisonStatus() {
        return facecomparisonStatus;
    }

    public void setFacecomparisonStatus(String facecomparisonStatus) {
        this.facecomparisonStatus = facecomparisonStatus;
    }

    public String facecomparisonStatus;
    public boolean passportId = false;
    ProgressDialog progressDialog;

    private MyUtils() {
    }

    public static MyUtils getInstance() {
        return ourInstance;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public boolean isPassportId() {
        return passportId;
    }

    public void setPassportId(boolean passportId) {
        this.passportId = passportId;
    }

    public void showProgressDialog(Activity activity) {
        try {
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    progressDialog.setCancelable(true);

                }
            }, 5000);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void dismissDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public String changeDateFormate(String myDate) {

        DateFormat originalFormat = new SimpleDateFormat("yyMMdd", new Locale("en", "US"));
        DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = originalFormat.parse(myDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return targetFormat.format(date);
    }


//    public boolean checkInternetConnection(Context context) {
//        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
//        if (netInfo == null) {
//            return false;
//        } else {
//            return true;
//
//        }
//    }


    public void deletedData(Context context) {
        SharedPreferences settings = context.getSharedPreferences("NFC_PASSPORT", Context.MODE_PRIVATE);
        settings.edit().clear().commit();
    }

    public void saveString(Context context, String key, String value) {
        SharedPreferences sharedPref1 = context.getSharedPreferences("NFC_PASSPORT", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref1.edit();
        editor.putString(key, value);  // Pick a key here.
        editor.commit();
    }

    public void saveInteger(Context context, String key, int value) {
        SharedPreferences sharedPref1 = context.getSharedPreferences("NFC_PASSPORT", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref1.edit();
        editor.putInt(key, value);  // Pick a key here.
        editor.commit();
    }

    public int getIntegerValue(Context context, String key) {
        SharedPreferences sharedPref1 = context.getSharedPreferences("NFC_PASSPORT", Context.MODE_PRIVATE);
        return sharedPref1.getInt(key, 0); //
    }


    public void saveBoolean(Context context, String key, boolean value) {
        SharedPreferences sharedPref1 = context.getSharedPreferences("NFC_PASSPORT", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref1.edit();
        editor.putBoolean(key, value);  // Pick a key here.
        editor.commit();
    }

    public boolean getBooleanValue(Context context, String key) {
        SharedPreferences sharedPref1 = context.getSharedPreferences("NFC_PASSPORT", Context.MODE_PRIVATE);
        return sharedPref1.getBoolean(key, false); //
    }


    public String getStringValue(Context context, String key) {
        SharedPreferences sharedPref1 = context.getSharedPreferences("NFC_PASSPORT", Context.MODE_PRIVATE);
        return sharedPref1.getString(key, ""); //
    }

/*

    public void alertDialogNoYes(final Activity activity) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.MyDialogTheme);

        builder.setTitle("OMAN-ID");
        builder.setMessage("Thank you for your verification \n Click YES to Exit");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(activity, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                activity.startActivity(intent);

                dialog.dismiss();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();


    }

    public void processend(final Activity activity, String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.MyDialogTheme);
        builder.setTitle("OMAN-ID");
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(activity, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                activity.startActivity(intent);

                dialog.dismiss();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();


    }



    public void errorDialog(final Activity activity, String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.MyDialogTheme);
        builder.setTitle("OMAN-ID");
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(activity, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                activity.startActivity(intent);

                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();


    }

    public void messageDialog(final Context activity, String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.MyDialogTheme);
        builder.setTitle("OMAN-ID");
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();


    }

*/


    public String todayDate() {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:sss", Locale.getDefault());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS", Locale.getDefault());
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss.SSS", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());
        return currentDateandTime;

    }


}
