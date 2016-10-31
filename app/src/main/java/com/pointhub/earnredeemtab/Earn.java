package com.pointhub.earnredeemtab;


import android.content.Intent;
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
import com.pointhub.wifidirect.WifiDirectSend;

import static com.pointhub.R.layout;


/**
 * A simple {@link Fragment} subclass.
 */
public class Earn extends Fragment {

    View view;
    Button bnSubmit;
    EditText billAmountText;
    public Earn() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v =  inflater.inflate(layout.earn, container, false);
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
                if (billAmount.isEmpty()) {

                    Toast.makeText(getActivity(), "please enter bill amount", Toast.LENGTH_SHORT).show();
                } else {

                   // String billAmount = billAmountText.getText().toString();

                    PointHubMessage msg = new PointHubMessage("Earn", billAmount, getUserId(), "");
                    String earnString = "";
                    try{
                        Gson gson = new Gson();
                        earnString =  gson.toJson(msg);
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }

                    Intent intent = new Intent(getActivity(), WifiDirectSend.class);
                    intent.putExtra("earnString", earnString);
                    startActivity(intent);
                }
            }

        });
    }

    private String getUserId(){
        String userId = "";
        try {

            userId = "Venu";
        }catch(Throwable th){
            th.printStackTrace();
        }
        return userId;
    }

}
