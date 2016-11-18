package com.pointhub.earnredeemtab;



import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pointhub.R;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPageAdapter viewPagerAdapter;
    TextView strnm;
    ImageView imgmenu,share;

    String storeName;

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
    }
}

