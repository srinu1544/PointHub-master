package com.pointhub.earnredeemtab;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.pointhub.PointListActivity;
import com.pointhub.R;
import com.pointhub.db.Createdb;

public class NewQRcode extends AppCompatActivity  {

    Button points;
    ImageView share;
    ImageView createPoints;


    private static final int PERMISSION_REQUEST_CAMERA = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.qrcode);

        showCameraPreview();
        points = (Button) findViewById(R.id.pointsbutton);

        share = (ImageView) findViewById(R.id.share);

        //share.setBackgroundResource(R.drawable.whatsappicon);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 /* Intent intent = new Intent();
                intent.setAction(android.content.Intent.ACTION_SEND);
                intent.putExtra(android.content.Intent.EXTRA_TEXT, "");
                intent.setPackage("com.whatsapp");
                intent.setType("text/plain");
                startActivity(intent);
                */

                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("image/jpg");
                String imagePath = "http://www.ijaitra.com/womenswear/kurtis/image2/image2a.jpg";
                //File imagefiletoshare = new File(imagePath);
                //Uri uri = Uri.fromFile(imagefiletoshare);
                //sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
                // Uri screenshotUri = Uri.parse("http://www.ijaitra.com/womenswear/kurtis/image2/image2f.jpg");
                //  sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                startActivity(Intent.createChooser(sharingIntent, "Share image using"));


            }
        });
        points.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(NewQRcode.this, PointListActivity.class);
                startActivity(i);

            }
        });

        createPoints = (ImageView) findViewById(R.id.imgmenu);

        //share.setBackgroundResource(R.drawable.whatsappicon);
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            // Permission is already available, start camera preview
           /* Snackbar.make(mLayout,
                    "Camera permission is available. Starting preview.",
                    Snackbar.LENGTH_SHORT).show();*/
        } else {
            // Permission is missing and must be requested.
            requestCameraPermission();
        }

    }

    private void requestCameraPermission() {
        // Permission has not been granted and must be requested.
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // Display a SnackBar with a button to request the missing permission.


            // Request the permission
            ActivityCompat.requestPermissions(NewQRcode.this,
                    new String[]{Manifest.permission.CAMERA},
                    PERMISSION_REQUEST_CAMERA);
        } else {
          /*  Snackbar.make(mLayout,
                    "Permission is not available. Requesting camera permission.",
                    Snackbar.LENGTH_SHORT).show();*/
            // Request the permission. The result will be received in onRequestPermissionResult().
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    PERMISSION_REQUEST_CAMERA);
        }
    }
}


