package com.pointhub.earnredeemtab;



import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.pointhub.R;
import com.pointhub.db.DatabaseHelper;
import com.pointhub.db.Points;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPageAdapter viewPagerAdapter;
    TextView strnm, points;

    String storeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.earn_redeem_tab);

        // Make two icons invisible.

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        strnm = (TextView)findViewById(R.id.strn);
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


        TextView points = (TextView) findViewById(R.id.points);
        // Points pts = new Points();
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        Points pts = dbHelper.getPoints(storeName);
        if(null == pts) {
            points.setText("0");
        } else {
            points.setText(pts.getPoints());
        }

        /*MobileAds.initialize(getApplicationContext(),"ca-app-pub-3940256099942544/6300978111");

        // Load an ad into the AdMob banner view.
        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);*/
    }
}

