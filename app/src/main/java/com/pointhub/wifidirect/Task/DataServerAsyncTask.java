package com.pointhub.wifidirect.Task;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.pointhub.earnredeemtab.MainActivity;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class DataServerAsyncTask extends AsyncTask<Void, Void, String> {

    Context context = null;

    public AsyncResponse delegate = null;


    public DataServerAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(Void... params) {
        try {

            Log.i("bizzmark", "data doing  `back");
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

        delegate.processFinish(result);
        Log.i("bizzmark", "data on post execute.Result: " + result);
    }

    private void showMessageIfCancel() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setTitle("Response from Seller ");
        builder.setMessage("seller Rejected your transaction, send again");
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(context.getApplicationContext(), "ok", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(null, MainActivity.class);
                context.startActivity(i);

            }
        });
        builder.show();
    }


    @Override
    protected void onPreExecute() {

    }

    public void showToast(String message) {
        final String msg = message;
        new Handler(Looper.getMainLooper())
                .post(
                        new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                            }
                        });
    }

}
