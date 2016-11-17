package com.pointhub;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pointhub.db.DatabaseHelper;
import com.pointhub.db.Points;
import com.pointhub.earnredeemtab.NewQRcode;

/**
 * Created by Venu on 03-05-2016.
 */
public class EarnBillAmountActivity extends Activity {

    Button button;
    EditText billAmount;
    String storeName;


    @Override
    protected void onDestroy () {
        super.onDestroy();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.earn_bill_amount);

        // slide up slide down
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        overridePendingTransition(R.anim.slideup, R.anim.slidedown);
        setContentView(R.layout.earn_bill_amount);

        button = (Button) findViewById(R.id.okButton);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                boolean success = true;

                Bundle extras = getIntent().getExtras();
                if (extras != null) {

                    storeName = extras.getString("storeName");
                }

                // success = updateInMongoDBServer(storeName, billAmount.getText().toString());

                Intent i = new Intent(EarnBillAmountActivity.this, NewQRcode.class);
                startActivity(i);
            }
        });
    }

    private String updateInMongoDBServer(String storeName, String billAmount) {

        boolean success = false;
        try {

            PointHubMessage msg = new PointHubMessage("Earn",billAmount,"","khaizar", "");

            Gson gson = new Gson();
            return gson.toJson(msg);


        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private void saveInLocal(PointHubMessage pointHubMessage) {
        try {

// int bill = Integer.parseInt(billAmount.getText().toString());

            Toast.makeText(getApplicationContext(), "You have received " + pointHubMessage.getPoints() + " Points.", Toast.LENGTH_LONG).show();


            String lastUpdate = DatabaseHelper.getInstance(getApplicationContext()).getDateTime();
            Points pnts = DatabaseHelper.getInstance(getApplicationContext()).getPoints(storeName);

            if(null == pnts) {

                pnts = new Points(storeName, pointHubMessage.getPoints(), lastUpdate);
                DatabaseHelper.getInstance(getApplicationContext()).createPoints(pnts);
            } else {

                int lastPoints = Integer.parseInt(pnts.getPoints());
                Integer presentPoints = lastPoints + Integer.parseInt(pointHubMessage.getPoints());
                pnts.setPoints(presentPoints.toString());
                pnts.setLastVisited(lastUpdate);

                DatabaseHelper.getInstance(getApplicationContext()).updatePoints(pnts);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Hide keyboard.
     *
     * @param input
     */
    protected void hideSoftKeyboard(EditText input) {
        input.setInputType(0);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
    }

}
