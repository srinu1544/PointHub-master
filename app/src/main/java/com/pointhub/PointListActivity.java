package com.pointhub;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.pointhub.db.Adapter;
import com.pointhub.db.DatabaseHelper;
import com.pointhub.db.Points;

import java.util.ArrayList;

/**
 * Created by Venu on 03-05-2016.
 */
public class PointListActivity extends Activity {

    RecyclerView lstPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_list);

        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        ArrayList<Points> pointses= (ArrayList<Points>) DatabaseHelper.getInstance(this).getAllPoints();
        lstPoints= (RecyclerView) findViewById(R.id.lstPoints);

        lstPoints.setLayoutManager(new LinearLayoutManager(this));
        lstPoints.setAdapter(new Adapter(PointListActivity.this, pointses));

        MobileAds.initialize(getApplicationContext(),"ca-app-pub-3940256099942544/6300978111");


        // Load an ad into the AdMob banner view.
        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }
}
