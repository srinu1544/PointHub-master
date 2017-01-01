package com.pointhub.earnredeemtab;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.zxing.Result;
import com.pointhub.PointHubMessage;
import com.pointhub.db.Points;
import com.pointhub.wifidirect.WifiDirectSend;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


/**
 * A simple {@link Fragment} subclass.
 */
public class Scanner_Fragment extends Fragment implements ZXingScannerView.ResultHandler,
        ActivityCompat.OnRequestPermissionsResultCallback {

    // Context mContext;
    ZXingScannerView mScannerView;
    private List<HashMap<String, String>> peersshow = new ArrayList();
    Calendar calander;
    SimpleDateFormat simpledateformat;
    private String Date;
    private String deviceid;
    PointHubMessage pointHubMessage;
    Points pts;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        deviceid = getImeistring();


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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mScannerView = new ZXingScannerView(getActivity());
        return mScannerView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }


    @Override
    public void handleResult(Result result) {

        final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
        tg.startTone(ToneGenerator.TONE_PROP_BEEP);

        // Direct to earn an1d redeem tab functionality.
        if (result != null) {
            //Log.i("tag",">>>>"+result.toString());
            // Toast.makeText(getContext(),"result is"+result.toString(),Toast.LENGTH_LONG).show();
            Intent i = new Intent(getActivity(), WifiDirectSend.class);
            i.putExtra("storename", result.toString());
            startActivity(i);
        } else {
            Toast.makeText(getActivity(), "scan again", Toast.LENGTH_SHORT).show();
        }
    }
}
