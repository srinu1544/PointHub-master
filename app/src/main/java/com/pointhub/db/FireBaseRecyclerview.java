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


public class FireBaseRecyclerview extends AppCompatActivity {

    private List<FirebaseData> firebaseDatas = new ArrayList<FirebaseData>();
    private RecyclerView recyclerView;
    private FirebaseAdapter mAdapter;
    PointsBO pbo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firebase_recycler_view);


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new FirebaseAdapter(getApplicationContext(), (List<FirebaseData>) firebaseDatas);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
        preparePointsBoData();

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-3940256099942544/6300978111");


        // Load an ad into the AdMob banner view.
        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    private void preparePointsBoData() {

        String Storename = getIntent().getExtras().getString("storename");
        String Points = getIntent().getExtras().getString("points");
        String Billamount = getIntent().getExtras().getString("billamount");

        FirebaseData fbd = new FirebaseData(Storename,Points,Billamount);
        firebaseDatas.add(fbd);

    }


}
