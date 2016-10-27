package com.pointhub;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.pointhub.db.Createdb;
import com.pointhub.earnredeemtab.MainActivity;

public class Navigation extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int PERMISSION_REQUEST_CAMERA = 0;
    ImageView menuButtom;
    Button points;
    ImageView share;
    private boolean doubleBackToExitPressedOnce=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        showCameraPreview();
       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

*/
        //share button
        share = (ImageView) findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //this is for only whats app
                /*Intent intent = new Intent();
                intent.setAction(android.content.Intent.ACTION_SEND);
                intent.putExtra(android.content.Intent.EXTRA_TEXT, "");
                intent.setPackage("com.whatsapp");
                intent.setType("text/plain");
                startActivity(intent);
*/
                // this for all type of sharing apps
               /* Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("image/jpg");
                String imagePath = "http://www.Bizz mark.in";
                File imagefiletoshare = new File(imagePath);
                Uri uri = Uri.fromFile(imagefiletoshare);
                sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
                Uri screenshotUri = Uri.parse("http://www.ijaitra.com/womenswear/kurtis/image2/image2f.jpg");
                 sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                startActivity(Intent.createChooser(sharingIntent, "Share image using"));*/


                //for testing storename
                Intent i = new Intent(Navigation.this, MainActivity.class);
                i.putExtra("storename", "teststore");
                startActivity(i);


            }
        });

        //points button
        points = (Button) findViewById(R.id.pointsbutton);
        points.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Navigation.this, Createdb.class);
                startActivity(i);

            }
        });

          //slide menu button
        menuButtom= (ImageView) findViewById(R.id.imgmenu);
        menuButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DrawerLayout slider = (DrawerLayout) findViewById(R.id.drawer_layout);
                slider.openDrawer(Gravity.LEFT);
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
            Toast.makeText(this," To exit_press_back_again,",Toast.LENGTH_SHORT).show();
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
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_back) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
             drawer.closeDrawer(GravityCompat.START);
                super.onBackPressed();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
            ActivityCompat.requestPermissions(Navigation.this,
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
