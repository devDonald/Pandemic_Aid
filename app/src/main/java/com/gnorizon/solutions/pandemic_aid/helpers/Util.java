package com.gnorizon.solutions.pandemic_aid.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Patterns;

public class Util {
    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectMgr;
        connectMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectMgr.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public boolean isValidEmail(Context context, CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
    public boolean isValidPhoneNumber(Context context, String mobile) {
        String regEx = "^[0-9]{11}$";
        return mobile.matches(regEx);
    }
}
