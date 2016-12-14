package com.pointhub.db;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.pointhub.R;

import java.util.ArrayList;
import java.util.List;

public class RecycleViewFirebase extends AppCompatActivity {

    RecyclerView lstPoints;
    FirebaseAdapter firebaseAdapter;
    private List<PointsBO> pointsBOs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_view_firebase);

        lstPoints= (RecyclerView) findViewById(R.id.lstPoints);

        lstPoints.setLayoutManager(new LinearLayoutManager(this));
        lstPoints.setAdapter(new FirebaseAdapter(RecycleViewFirebase.this, (ArrayList<PointsBO>) pointsBOs));
        MobileAds.initialize(getApplicationContext(),"ca-app-pub-3940256099942544/6300978111");

        // Load an ad into the AdMob banner view.
        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

    }
}
