package com.pointhub.db;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.pointhub.R;

/**
 * Created by venu gopal on 03-10-2016.
 */
public class Detaildb extends Activity {

    private  EditText dsnamej;
    private TextView  upointsj , udatej ;
    private Button usavej,updatej;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detaildb);
        dsnamej = (EditText)findViewById(R.id.dsnamej);
        upointsj = (TextView)findViewById(R.id.upoints);
        udatej = (TextView)findViewById(R.id.udate);
        usavej = (Button)findViewById(R.id.usave);
        updatej = (Button)findViewById(R.id.update);
        final String store_name=getIntent().getExtras().getString(DatabaseHelper.STORE_NAME);

        dsnamej.setText(store_name);

        usavej.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
                Points pts = new Points();

                pts=dbHelper.getPoints(store_name);

                pts.getPoints();
                pts.getLastVisited();




            }
        });

        updatej.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });


    }
}