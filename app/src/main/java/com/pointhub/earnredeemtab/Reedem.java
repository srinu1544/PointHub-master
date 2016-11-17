package com.pointhub.earnredeemtab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pointhub.PointHubMessage;
import com.pointhub.R;


public class Reedem extends Fragment {

    private Spinner spinner;
    Button submitButton1;
    EditText redeemBillAmountText1;

    public Reedem() {
        // Required empty public constructor
    }

    String points = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.reedem, container, false);
        findViewByid(v);
        setSpinnerCategories();
        return v;
    }

    private void findViewByid(View v) {

        spinner = (Spinner) v.findViewById(R.id.spinner1);
        submitButton1 = (Button) v.findViewById(R.id.submitButton);
        redeemBillAmountText1 = (EditText) v.findViewById(R.id.redeemBillAmountText);

        submitButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String billAmount = redeemBillAmountText1.getText().toString();

                String storName = getStoreID();
                String userid = getUserId();

                PointHubMessage msg = new PointHubMessage("Redeem", billAmount, userid, storName, points);

                Gson gson = new Gson();
                String redeemString = gson.toJson(msg);

                Intent intent = new Intent(getActivity(), com.pointhub.wifidirect.WifiDirectSend.class);
                intent.putExtra("earnRedeemString", redeemString);
                startActivity(intent);
            }
        });

    }

    public String getStoreID() {

        String storeName = getActivity().getIntent().getStringExtra("storename");
        return storeName;
    }

    private String getUserId(){

       String userId = null;
        /*try {

            TelephonyManager telephonyManager = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);

            //getDeviceId() function Returns the unique device ID.

            String imeistring = telephonyManager.getDeviceId();
           userId=imeistring;


        }catch(Throwable th){
            th.printStackTrace();
        }*/
        return userId;
    }

    private void setSpinnerCategories() {

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                points = String.valueOf(spinner.getSelectedItem());
                Toast.makeText(getActivity(), "You Selected  " + String.valueOf(spinner.getSelectedItem()), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


}



