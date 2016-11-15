package com.pointhub;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.pointhub.db.Adapter;
import com.pointhub.db.DatabaseHelper;
import com.pointhub.db.Points;

import java.util.ArrayList;

/**
 * Created by Venu on 03-05-2016.
 */
public class PointListActivity extends Activity {

    RecyclerView lstPoints;
    ImageView menuButtom;
    DrawerLayout slider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_list);

        /*final String store_name;
        store_name = getIntent().getExtras().getString(DatabaseHelper.STORE_NAME);*/
        DatabaseHelper dbHelper=new DatabaseHelper(getApplicationContext());
        ArrayList<Points> pointses= (ArrayList<Points>) DatabaseHelper.getInstance(this).getAllPoints();
        lstPoints= (RecyclerView) findViewById(R.id.lstPoints);

        lstPoints.setLayoutManager(new LinearLayoutManager(this));
        lstPoints.setAdapter(new Adapter(PointListActivity.this,pointses));

    }
}
