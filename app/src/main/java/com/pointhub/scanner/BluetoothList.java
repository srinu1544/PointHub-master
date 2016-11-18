package com.pointhub.scanner;

/**
 * Created by venu gopal on 07-09-2016.
 */

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.pointhub.R;

import java.util.Set;

public class BluetoothList extends Activity {
    TextView textview1;
    private static final int REQUEST_ENABLE_BT = 1;
    BluetoothAdapter btAdapter;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetoothlist);

        /*textview1 = (TextView) findViewById(R.id.textView1);*/
        ListView listView = (ListView) findViewById(R.id.listView);

        // Getting the Bluetooth adapter
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        textview1.append("\nAdapter: " + btAdapter);

        CheckBluetoothState();
    }

    /* It is called when an activity completes.*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            CheckBluetoothState();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void CheckBluetoothState() {
        // Checks for the Bluetooth support and then makes sure it is turned on
        // If it isn't turned on, request to turn it on
        // List paired devices
        if (btAdapter == null) {
            textview1.append("\nBluetooth NOT supported. Aborting.");
            return;
        } else {
            if (btAdapter.isEnabled()) {
                textview1.append("\nBluetooth is enabled...");

                // Listing paired devices
                textview1.append("\nPaired Devices are:");
                Set<BluetoothDevice> devices = btAdapter.getBondedDevices();
                for (BluetoothDevice device : devices) {
                    textview1.append("\n  Device: " + device.getName() + ", " + device);
                }
            } else {
                //Prompt user to turn on Bluetooth
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
    }
}

