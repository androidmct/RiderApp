package com.sage.cabapp.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Patterns;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;
import com.sage.cabapp.R;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * Created by Maroof Ahmed Siddique on 14-11-2019.
 * Lala
 * maroofahmedsiddique@gmail.com
 */
public class Constant {

    public static final int SUCCESS_RESULT = 0;
    public static final float ZOOM_LOCATION = 12.0f;
    public static final double CAMERA_BOUNDS = 0.11;

    public static final int FAILURE_RESULT = 1;

    public static final String PACKAGE_NAME =
            "com.google.android.gms.location.sample.locationaddress";

    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";

    public static final String RESULT_DATA_KEY = PACKAGE_NAME + ".RESULT_DATA_KEY";

    public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_DATA_EXTRA";


    public static final String LAST_LATITUDE = "LAST_LATITUDE";
    public static final String LAST_LONGITUDE = "LAST_LONGITUDE";
    public static final String TEMP_PHONE_NUMBER = "TEMP_PHONE_NUMBER";
    public static final String TEMP_EMAIL = "TEMP_EMAIL";

    public static final String PHONE_NUMBER = "PHONE_NUMBER";
    public static final String EMAIL = "EMAIL";
    public static final String USERID = "USERID";
    public static final String DRIVER_USERID = "DRIVER_USERID";
    public static final String DRIVER_NAME = "DRIVER_NAME";
    public static final String DRIVER_PIC = "DRIVER_PIC";
    public static final String DRIVER_TRIP_FARE = "DRIVER_TRIP_FARE";
    public static final String FIRST_NAME = "FIRST_NAME";
    public static final String LAST_NAME = "LAST_NAME";
    public static final String REFRESHED_TOKEN = "REFRESHED_TOKEN";
    public static final String NO_CARS = "NO_CARS";
    public static final String RIDE_FARE = "RIDE_FARE";

    public static final String TEMP_REQUEST_ID = "TEMP_REQUEST_ID";
    public static final String PERM_REQUEST_ID = "PERM_REQUEST_ID";
    public static final String PERM_REQUEST_STATUS = "PERM_REQUEST_STATUS";
    public static final String PERM_REQUEST_CURRENT_STATUS = "PERM_REQUEST_CURRENT_STATUS";

    public static final String PROFILE_UPDATED = "PROFILE_UPDATED";
    public static final String PROFILE_PIC = "PROFILE_PIC";
    public static final String RIDE_STARTED = "RIDE_STARTED";
    public static final String REFERRAL_CODE = "REFERRAL_CODE";
    public static final String MY_REFERRAL_CODE = "MY_REFERRAL_CODE";

    public static ProgressDialog showLoadingDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.show();
        if (progressDialog.getWindow() != null) {
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        return progressDialog;
    }

    public static boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static void showErrorToast(String message, Context context) {
        MDToast mdToast = MDToast.makeText(context, message, Toast.LENGTH_SHORT, MDToast.TYPE_ERROR);
        mdToast.show();
    }

    public static void showSuccessToast(String message, Context context) {
        MDToast mdToast = MDToast.makeText(context, message, Toast.LENGTH_SHORT, MDToast.TYPE_SUCCESS);
        mdToast.show();
    }

    public static void showInfoToast(String message, Context context) {
        MDToast mdToast = MDToast.makeText(context, message, Toast.LENGTH_SHORT, MDToast.TYPE_INFO);
        mdToast.show();
    }

    public static void showWarningToast(String message, Context context) {
        MDToast mdToast = MDToast.makeText(context, message, Toast.LENGTH_SHORT, MDToast.TYPE_WARNING);
        mdToast.show();
    }

    public static String convertKmsToMiles(double kms) {
        double miles = 0.621371 * kms;
        DecimalFormat precision = new DecimalFormat("0.00");
        return precision.format(miles) + "mi";
    }

    public static String convertDate(String dateString) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = null;
        try {
            date = fmt.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        @SuppressLint("SimpleDateFormat") SimpleDateFormat fmtOut = new SimpleDateFormat("MMM dd, yyyy, hh:mm aaa");
        return fmtOut.format(Objects.requireNonNull(date));

    }

    public static String convertTime(String dateString) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = null;
        try {
            date = fmt.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        @SuppressLint("SimpleDateFormat") SimpleDateFormat fmtOut = new SimpleDateFormat("hh:mm aaa");
        return fmtOut.format(Objects.requireNonNull(date));
    }

    public static String convertCarTime(String dateString) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat fmt = new SimpleDateFormat("hh:mm:ss");
        Date date = null;
        try {
            date = fmt.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        @SuppressLint("SimpleDateFormat") SimpleDateFormat fmtOut = new SimpleDateFormat("hh:mm aaa");
        return fmtOut.format(Objects.requireNonNull(date));

    }

    public static void shareOnlySMS(Context context, String smsBody) {

        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setData(Uri.parse("sms:"));
        sendIntent.putExtra("sms_body", smsBody);
        context.startActivity(sendIntent);
    }

    public static void shareOnlyEmail(Context context, String smsBody) {

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Sage Free Rides");
        emailIntent.putExtra(Intent.EXTRA_TEXT, smsBody);
        context.startActivity(Intent.createChooser(emailIntent, "Send Email..."));
    }

    public static void shareOtherApps(Context context, String smsBody) {

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, smsBody);
        context.startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    public static int getWidthAndHeight(Activity activity, boolean isWidth) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;
        if (isWidth) {
            return widthPixels;
        } else {
            return heightPixels;
        }
    }

    public static double distanceBetween(LatLng point1, LatLng point2) {
        if (point1 == null || point2 == null) {
            return 0.0;
        }

        BigDecimal bd = BigDecimal.valueOf(SphericalUtil.computeDistanceBetween(point1, point2));
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}
