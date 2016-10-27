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

import com.pointhub.R;

import static com.pointhub.R.layout;


/**
 * A simple {@link Fragment} subclass.
 */
public class Earn extends Fragment {

    View view;
    Button bn;
    EditText et;
    public Earn() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v =  inflater.inflate(layout.earn, container, false);

        findViewByid(v);
        return v;}

    private void findViewByid(View v) {
        et= (EditText)v.findViewById(R.id.billAmountText);
        bn = (Button) v.findViewById(R.id.submit);

          bn.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                   String s=et.getText().toString();
                 if (s.isEmpty()) {
                     Toast.makeText(getActivity(), "please enter bill amount", Toast.LENGTH_SHORT).show();
                  }
                    else {
                     Intent intent = new Intent(getActivity(), com.pointhub.wifidirect.WifiDirectSend.class);
                     intent.putExtra("amount",et.getText().toString());
                     ((MainActivity) getActivity()).startActivity(intent);
                  }
              }

          });
    }

}
