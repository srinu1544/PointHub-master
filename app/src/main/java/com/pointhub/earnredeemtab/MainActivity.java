package com.pointhub.earnredeemtab;



import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.pointhub.R;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPageAdapter viewPagerAdapter;
    TextView strnm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.earn_redeem_tab);
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
        String userName;

        if (extras != null) {
            userName = extras.getString("storename");
           strnm.setText(userName);
        }
    }
}

