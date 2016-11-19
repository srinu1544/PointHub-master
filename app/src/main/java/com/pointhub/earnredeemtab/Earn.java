package com.pointhub.earnredeemtab;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pointhub.PointHubMessage;
import com.pointhub.R;
import com.pointhub.gcm.GCMToken;
import com.pointhub.util.Utility;
import com.pointhub.wifidirect.WifiDirectSend;

import static com.pointhub.R.layout;


/**
 * A simple {@link Fragment} subclass.
 */
public class Earn extends Fragment {

    Button bnSubmit;
    EditText billAmountText;

    public Earn() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(layout.earn, container, false);
        findViewByid(v);
        return v;
    }


    private void findViewByid(View v) {

        billAmountText = (EditText) v.findViewById(R.id.billAmountText);
        bnSubmit = (Button) v.findViewById(R.id.submit);

        bnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String billAmount = billAmountText.getText().toString();

                String userId = getImeistring();
                String storName = getStoreID();

                if (billAmount.isEmpty()) {

                    Toast.makeText(getActivity(), "please enter bill amount", Toast.LENGTH_SHORT).show();
                } else {

                    PointHubMessage msg = new PointHubMessage("Earn", billAmount, userId, storName, billAmount);
                    String earnString = "";
                    try {
                        Gson gson = Utility.getGsonObject();
                        earnString = gson.toJson(msg);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    boolean internetAvailable = false;

                            // isNetworkConnected();
                    if(internetAvailable) {

                        GCMToken.sendNotification(msg);
                    } else {
                        Intent intent = new Intent(getActivity(), WifiDirectSend.class);
                        intent.putExtra("earnRedeemString", earnString);
                        startActivity(intent);
                    }
                }
            }

        });
    }

    public String getStoreID() {

        String storeName = getActivity().getIntent().getStringExtra("storename");
        return storeName;
    }

    public String getImeistring() {
        String imeistring = null;
        /*try {
           // TelephonyManager telephonyManager = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);

            // getDeviceId() function Returns the unique device ID.
            //imeistring = telephonyManager.getDeviceId();
            //System.out.println(imeistring);

            // userId = "Venu";
        } catch (Throwable th) {
            th.printStackTrace();
        }*/
        return imeistring;
    }

    private boolean isNetworkConnected() {

        ConnectivityManager cm = (ConnectivityManager) this.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

}

