package com.pointhub.wifidirect.Task;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;


import com.google.gson.Gson;
import com.pointhub.db.AcknowledgePoints;
import com.pointhub.util.Utility;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class DataServerAsyncTask extends AsyncTask<Void, Void, String> {

    Context context = null;
    public DataServerAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(Void... params) {
        try {

             Log.i("bizzmark", "data doing back");
             ServerSocket serverSocket = new ServerSocket(9999);
             serverSocket.setReuseAddress(true);

             Log.i("bizzmark", "Opening socket on 9999.");
             Socket client = serverSocket.accept();

             Log.i("bizzmark", "Client connected.");
             InputStream inputstream = client.getInputStream();
             ByteArrayOutputStream baos = new ByteArrayOutputStream();
             int i;
             while ((i = inputstream.read()) != -1) {
                 baos.write(i);
             }

             String str = baos.toString();

            serverSocket.close();

            return str;

        } catch (Throwable e) {
            Log.e("bizzmark", e.toString());
            return null;
        }
    }


    @Override
    protected void onPostExecute(String result) {

        Log.i("bizzmark", "data on post execute.Result: " + result);

        if (result != null) {

            try {

                Gson gson = Utility.getGsonObject();
                AcknowledgePoints acknowledgePoints = gson.fromJson(result, AcknowledgePoints.class);
                String status = acknowledgePoints.getStatus();


                Log.i("bizzmark", "Acknowledgement: " + result);

                Utility.savePointsToMobile(context, acknowledgePoints.getEarnRedeemString());

                // Move to Points report.
            /*Intent i = new Intent(WifiDirectSend.this, PointListActivity.class);
            startActivity(i);*/
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPreExecute() {

    }

}
