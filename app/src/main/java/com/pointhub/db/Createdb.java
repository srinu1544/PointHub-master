package com.pointhub.db;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.pointhub.PointListActivity;
import com.pointhub.R;

/**
 * Created by venu gopal on 01-10-2016.
 */
public class Createdb extends Activity {


    private EditText csnamej, cpointsj, cdatej;
    private Button csubj;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.createdb);
        csnamej = (EditText) findViewById(R.id.csname);
        cpointsj = (EditText) findViewById(R.id.cpoints);
        cdatej = (EditText) findViewById(R.id.cdate);
        csubj = (Button) findViewById(R.id.csub);


        csubj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
                Points pts = new Points();

                pts.setStoreName(csnamej.getText().toString());
                //  Log.i("Tag", "points : >> " +pts.getPoints());

                pts.setPoints(cpointsj.getText().toString());
                // Log.i("Tag", "points : > " +pts.getPoints());

                pts.setLastVisited(cdatej.getText().toString());
                //  Log.i("Tag", "points : >>> " +pts.getPoints());
                dbHelper.createPoints(pts);

                Intent intent = new Intent(Createdb.this, PointListActivity.class);
                intent.putExtra(DatabaseHelper.STORE_NAME, csnamej.getText().toString());
                startActivity(intent);

            }
        });


    }

}

