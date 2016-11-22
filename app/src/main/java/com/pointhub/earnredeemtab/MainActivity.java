package com.pointhub.earnredeemtab;



import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.pointhub.R;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPageAdapter viewPagerAdapter;
    TextView strnm,points,lastvisit;
    ImageView imgmenu,share;

    String storeName;
    String lastvisited,point;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.earn_redeem_tab);
        imgmenu = (ImageView) findViewById(R.id.imgmenu);
        share=(ImageView) findViewById(R.id.share);
        imgmenu.setVisibility(View.INVISIBLE);
        share.setVisibility(View.INVISIBLE);


   /*     toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);*/

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        strnm=(TextView)findViewById(R.id.strn);
        viewPagerAdapter = new ViewPageAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(new Earn(), "EARN");
        viewPagerAdapter.addFragments(new Reedem(), "REDEEM");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            storeName = extras.getString("storename");
            strnm.setText(storeName);
        }

        MobileAds.initialize(getApplicationContext(),"ca-app-pub-3940256099942544/6300978111");


        // Load an ad into the AdMob banner view.
        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);



    }
}

