package com.gnorizon.solutions.pandemic_aid.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;

import com.gnorizon.solutions.pandemic_aid.R;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

public class Intro extends AppCompatActivity {
    private Button accept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

//        accept = findViewById(R.id.bt_accept);
//
////        Dexter.withActivity(this)
////                .withPermissions(
////
////                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
////                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
////                        android.Manifest.permission.ACCESS_FINE_LOCATION
////                )
////                .withListener(new MultiplePermissionsListener() {
////                    @Override public void onPermissionsChecked(MultiplePermissionsReport report) {
////                        if (report.areAllPermissionsGranted()) {
////                            Intent intent = new Intent(Intro.this, LandingPage.class);
////                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
////                            startActivity(intent);
////                        }
////                        for (int i=0;i<report.getDeniedPermissionResponses().size();i++) {
////                            Log.d("dennial permision res", report.getDeniedPermissionResponses().get(i).getPermissionName());
////                        }
////                        // check for permanent denial of any permission
////                        if (report.isAnyPermissionPermanentlyDenied()) {
////                            // permission is denied permenantly, navigate user to app settings
////                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
////                                    Uri.fromParts("package", getPackageName(), null));
////                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                            startActivity(intent);
////                        }
////                    }
////
//////                    @Override
//////                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
//////
//////                    }
////
////                    @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token)
////                    {
////                        token.continuePermissionRequest();
////                    }
////                }).check();
//
//        accept.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
        String[] permissions = {
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION};
        Permissions.check(this/*context*/, permissions, null/*rationale*/, null/*options*/, new PermissionHandler() {
            @Override
            public void onGranted() {
                // do your task.
                Intent intent = new Intent(Intro.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }
}
