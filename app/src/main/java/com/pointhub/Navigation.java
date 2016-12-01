package com.pointhub;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.pointhub.db.Createdb;
import com.pointhub.earnredeemtab.MainActivity;
import com.pointhub.util.Utility;

import java.io.File;

public class Navigation extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    public FirebaseAuth firebaseAuth;
    private static final int PERMISSION_REQUEST_CAMERA = 0;
    private static final int REQUEST_READ_PHONE_STATE = 1;

    ImageView menuButtom;
    ImageButton log_out;
    Button points;
    ImageView share;
    final Context context = this;
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        try {
            showCameraPreview();
            requestMobilePermission();
        }catch(Exception ex){
            ex.printStackTrace();
        }

        log_out= (ImageButton) findViewById(R.id.logout);
        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth = FirebaseAuth.getInstance();
                if (firebaseAuth.getCurrentUser()!=null) {
                    firebaseAuth.signOut();
                    Toast.makeText(getApplicationContext(), "signedout successfully", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), " You alredy signedout ", Toast.LENGTH_SHORT).show();
                }

            }
        });



        // share button
        share = (ImageView) findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Utility.isTesting()) {

                    Intent i = new Intent(Navigation.this, MainActivity.class);
                    i.putExtra("storename", "teststore");
                    startActivity(i);
                } else {

                    shareFromBluetooth();
                }
            }
        });


        // Points button
        points = (Button) findViewById(R.id.pointsbutton);
        points.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(Utility.isTesting()) {

                    /*PointHubMessage msg = new PointHubMessage("Earn", "2500", "Venu", "TestStore", "250");
                    Utility.saveToDB(getApplicationContext(), msg);*/

                    Intent i = new Intent(Navigation.this, Createdb.class);
                    startActivity(i);
                } else {

                    Intent intent = new Intent(Navigation.this, PointListActivity.class);
                    startActivity(intent);
                }
            }
        });

          // Slide menu button
        menuButtom= (ImageView) findViewById(R.id.imgmenu);
        menuButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DrawerLayout slider = (DrawerLayout) findViewById(R.id.drawer_layout);
                slider.openDrawer(Gravity.LEFT);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void requestMobilePermission() {
        try{
            int permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE);

            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private void shareFromBluetooth() {
        try {

            ApplicationInfo app = getApplicationContext().getApplicationInfo();
            String filePath = app.sourceDir;
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("*/*");

            // Only use Bluetooth to send .apk
             intent.setPackage("com.android.bluetooth");

            // Append file and send Intent
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(filePath)));
            startActivity(Intent.createChooser(intent, "Share app"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this,"To exit press back again.",Toast.LENGTH_SHORT).show();
        }
        }


    @Override
    protected void onResume() {
        super.onResume();
        this.doubleBackToExitPressedOnce = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {

            Intent i = new Intent(Navigation.this,FaqExpandable.class);
            startActivity(i);

        } else if (id == R.id.nav_gallery) {
            Intent i =new Intent(Navigation.this,TermsAndConditions.class);
            startActivity(i);


        } else if (id == R.id.nav_slideshow) {
            Intent i =new Intent(Navigation.this,Privact_policy.class);
            startActivity(i);

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

            if(Utility.isTesting()) {

                Intent i = new Intent(Navigation.this, MainActivity.class);
                i.putExtra("storename", "teststore");
                startActivity(i);
            } else {

                shareFromBluetooth();
            }

        }else if (id== R.id.nav_version) {

            showAppVersion();
        } else if (id == R.id.nav_back) {

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
             drawer.closeDrawer(GravityCompat.START);
                super.onBackPressed();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showAppVersion() {
        try {

            AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
            PackageInfo packageInfo = null;
            try {
                packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            builder1.setMessage("SmartPoints\nVersion: " + packageInfo.versionName);
            builder1.setCancelable(true);
            builder1.setIcon(R.drawable.smartpoints_logo);
            builder1.setPositiveButton(
                    "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert11 = builder1.create();
            alert11.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
            ActivityCompat.requestPermissions(Navigation.this,
                    new String[]{Manifest.permission.CAMERA},
                    PERMISSION_REQUEST_CAMERA);
        } else {

            // Request the permission. The result will be received in onRequestPermissionResult().
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    PERMISSION_REQUEST_CAMERA);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // TODO
                }
                break;

            default:
                break;
        }
    }
}
