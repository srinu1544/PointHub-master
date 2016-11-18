package com.pointhub;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Faq extends AppCompatActivity implements View.OnClickListener {


    Button nav_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
       nav_back= (Button) findViewById(R.id.nav_back);
        nav_back.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.nav_back:
               super.onBackPressed();
                break;

        }
    }
}

