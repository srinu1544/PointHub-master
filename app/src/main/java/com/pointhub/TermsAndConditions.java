package com.pointhub;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class TermsAndConditions extends AppCompatActivity {


    ImageView backbut;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_conditions);

        backbut= (ImageView) findViewById(R.id.backbut);
        backbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TermsAndConditions.super.onBackPressed();
            }
        });
    }
}
