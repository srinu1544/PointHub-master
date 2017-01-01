package com.pointhub.earnredeemtab;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.pointhub.R;
import com.pointhub.db.Createdb;

public class NewQRcode extends AppCompatActivity {

    ImageView createPoints;


    private static final int PERMISSION_REQUEST_CAMERA = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.qrcode);

        try {
            showCameraPreview();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        createPoints = (ImageView) findViewById(R.id.imgmenu);

        createPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(NewQRcode.this, Createdb.class);
                startActivity(i);
            }
        });

    }

    private void showCameraPreview() {

        // BEGIN_INCLUDE(startCamera)
        // Check if the Camera permission has been granted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

            // Permission is already available, start camera preview
        } else {

            // Permission is missing and must be requested.
            requestCameraPermission();
        }

    }

    private void requestCameraPermission() {

        // Permission has not been granted and must be requested.
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {

            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // Display a SnackBar with a button to request the missing permission.

            // Request the permission
            ActivityCompat.requestPermissions(NewQRcode.this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
        } else {

            // Request the permission. The result will be received in onRequestPermissionResult().
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
        }
    }

    @Override
    protected void onResume() {

        super.onResume();
        requestCameraPermission();
    }
}


