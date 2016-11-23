package com.pointhub;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;


public class FaqExpandable extends AppCompatActivity {

    ImageView backbut;
    ExpandableRelativeLayout expandableLayout1, expandableLayout2, expandableLayout3, expandableLayout4,expandableLayout5,
            expandableLayout6,expandableLayout7,expandableLayout8,expandableLayout9,expandableLayout10,expandableLayout11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq_expandable);
        backbut= (ImageView) findViewById(R.id.backbut);
        backbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FaqExpandable.super.onBackPressed();
            }
        });


    }

    public void expandableButton1(View view) {
        expandableLayout1 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout1);
        expandableLayout1.toggle(); // toggle expand and collapse
    }

    public void expandableButton2(View view) {
        expandableLayout2 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout2);
        expandableLayout2.toggle(); // toggle expand and collapse
    }

    public void expandableButton3(View view) {
        expandableLayout2 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout2);
        expandableLayout2.toggle(); // toggle expand and collapse
    }




    public void expandableButton4(View view) {
        expandableLayout4 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout4);
        expandableLayout4.toggle(); // toggle expand and collapse
    }

    public void expandableButton5(View view) {
        expandableLayout5 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout5);
        expandableLayout5.toggle(); // toggle expand and collapse
    }
    public void expandableButton6(View view) {
        expandableLayout6 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout6);
        expandableLayout6.toggle(); // toggle expand and collapse
    }
    public void expandableButton7(View view) {
        expandableLayout7 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout7);
        expandableLayout7.toggle(); // toggle expand and collapse
    }
    public void expandableButton8(View view) {
        expandableLayout8 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout8);
        expandableLayout8.toggle(); // toggle expand and collapse
    }
    public void expandableButton9(View view) {
        expandableLayout9 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout9);
        expandableLayout9.toggle(); // toggle expand and collapse
    }
    public void expandableButton10(View view) {
        expandableLayout10 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout10);
        expandableLayout10.toggle(); // toggle expand and collapse
    }
    public void expandableButton11(View view) {
        expandableLayout11 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout11);
        expandableLayout11.toggle(); // toggle expand and collapse
    }
}

