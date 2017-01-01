package com.pointhub;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class Privact_policy extends AppCompatActivity {

    ImageView backbut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privact_policy);
        backbut = (ImageView) findViewById(R.id.backbut);
        backbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Privact_policy.super.onBackPressed();
            }
        });

    }
}
