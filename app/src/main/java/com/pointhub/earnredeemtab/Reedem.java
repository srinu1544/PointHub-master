package com.pointhub.earnredeemtab;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pointhub.PointHubMessage;
import com.pointhub.R;
import com.pointhub.login.LoginActivity;


public class Reedem extends Fragment {

    private Spinner spinner;
    Button submitButton;
    TextView redeemBillAmountText;



    public Reedem() {
        // Required empty public constructor
    }

    String points = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.reedem, container, false);
        findViewByid(v);
        setSpinnerCategories();
        return v;


    }

    private void findViewByid(View v) {

        spinner = (Spinner) v.findViewById(R.id.spinner1);
        submitButton = (Button) v.findViewById(R.id.submitButton);
        redeemBillAmountText = (TextView) v.findViewById(R.id.redeemBillAmountText);



    }

    public String getStoreID() {

        String storeName = getActivity().getIntent().getStringExtra("storename");
        return storeName;
    }

    private String getUserId(){

        String userId = null;
        try {
            TelephonyManager telephonyManager = (TelephonyManager) getContext().getSystemService(getContext().TELEPHONY_SERVICE);
            //getDeviceId() function Returns the unique device ID.
            String imeistring = telephonyManager.getDeviceId();
           userId=imeistring;
        }catch(Throwable th){
            th.printStackTrace();
        }
        return userId;
    }

    private void setSpinnerCategories() {

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                points = String.valueOf(spinner.getSelectedItem());
                // Toast.makeText(getActivity(), "You Selected  " + String.valueOf(spinner.getSelectedItem()), Toast.LENGTH_LONG).show();

                submitButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        String billAmount = redeemBillAmountText.getText().toString();

                        String storName = getStoreID();
                        String userid = getUserId();
                        if (billAmount.isEmpty() || points.isEmpty() ) {

                            //Toast.makeText(getActivity(), "Please Fill the Details", Toast.LENGTH_SHORT).show();
                            Snackbar snackbar=Snackbar.make(getView(),"Please Fill the details to Continue",Snackbar.LENGTH_INDEFINITE)
                                    .setAction("OK", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                        }
                                    });
                            snackbar.show();
                        } else {

                            PointHubMessage msg = new PointHubMessage("Redeem", billAmount, userid, storName, points);

                            Gson gson = new Gson();
                            String redeemString = gson.toJson(msg);

                            Intent intent = new Intent(getActivity(),LoginActivity.class);
                            intent.putExtra("earnRedeemString", redeemString);
                            startActivity(intent);
                        }
                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                Toast.makeText(getActivity(),"please choose points",Toast.LENGTH_SHORT).show();


            }
        });
    }


}