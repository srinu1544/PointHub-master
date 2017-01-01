package com.pointhub.earnredeemtab;


import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.pointhub.R;

import static com.pointhub.R.layout;

/**
 * A simple {@link Fragment} subclass.
 */
public class Earn extends Fragment {


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

        if (billAmountText.getText() == null) {

            Toast.makeText(getActivity(), "please enter bill amount", Toast.LENGTH_SHORT).show();
                    /*Snackbar snackbar=Snackbar.make(getView(),"Please Enter Bill Amount to Continue",Snackbar.LENGTH_INDEFINITE)
                            .setAction("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                }
                            });
                    snackbar.show();*/
        }


            String userId = getImeistring();
            String storName = getStoreID();


        }


    public String getStoreID() {

        String storeName = getActivity().getIntent().getStringExtra("storename");
        return storeName;
    }

    public String getImeistring() {

        String imeistring = null;
        try {
            TelephonyManager telephonyManager = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
            // getDeviceId() function Returns the unique device ID.
            imeistring = telephonyManager.getDeviceId();
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return imeistring;
    }

    private boolean isNetworkConnected() {

        ConnectivityManager cm = (ConnectivityManager) this.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

}