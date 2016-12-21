package com.pointhub.wifidirect;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.pointhub.Navigation;
import com.pointhub.PointListActivity;
import com.pointhub.R;
import com.pointhub.db.AcknowledgePoints;
import com.pointhub.db.PointsBO;
import com.pointhub.util.Utility;
import com.pointhub.wifidirect.Adapter.WifiAdapter;
import com.pointhub.wifidirect.BroadcastReceiver.WifiDirectBroadcastReceiver;
import com.pointhub.wifidirect.Service.DataTransferService;
import com.pointhub.wifidirect.Task.AsyncResponse;
import com.pointhub.wifidirect.Task.DataServerAsyncTask;

import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class WifiDirectSend extends AppCompatActivity implements AsyncResponse {

    private RecyclerView mRecyclerView;
    private WifiAdapter mAdapter;
    private List peers = new ArrayList();
    private List<HashMap<String, String>> peersshow = new ArrayList();

    SimpleDateFormat simpledateformat;
    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private BroadcastReceiver mReceiver;
    private IntentFilter mFilter;

    // Connection info object.
    private WifiP2pInfo info;
    private DataServerAsyncTask acknowledgementFromSellerTask = null;
    private Button btRefresh;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi_direct_send);

        info = null;

        initView();
        initIntentFilter();
        initReceiver();
        initEvents();
        discoverPeers();

        Toast.makeText(getApplicationContext(),"Double click on store to Earn/Redeem points.", Toast.LENGTH_SHORT).show();
    }

    /**
     * Initialize all the views.
     */
    private void initView() {

        btRefresh = (Button) findViewById(R.id.btnRefresh);
       // testing = (Button) findViewById(R.id.testing);
       /* testing2 = (Button) findViewById(R.id.testing2);
*/
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mAdapter = new WifiAdapter(peersshow);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));

    }

    /**
     * Apply wifi filters for change listners.
     */
    private void initIntentFilter() {

        mFilter = new IntentFilter();
        mFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mFilter.addAction(WifiP2pManager.WIFI_P2P_DISCOVERY_CHANGED_ACTION);
        mFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
    }

    boolean firstClick = false;

    /**
     * Initialize the receiver for P2P service.
     */
    private void initReceiver() {

        mManager = (WifiP2pManager) getSystemService(WIFI_P2P_SERVICE);

        mChannel = mManager.initialize(this, Looper.myLooper(), null);

        WifiP2pManager.PeerListListener mPeerListListerner = new WifiP2pManager.PeerListListener() {

            @Override
            public void onPeersAvailable(WifiP2pDeviceList peersList) {

                peers.clear();
                peersshow.clear();

                Collection<WifiP2pDevice> aList = peersList.getDeviceList();
                peers.addAll(aList);

                for (int i = 0; i < aList.size(); i++) {

                    WifiP2pDevice a = (WifiP2pDevice) peers.get(i);
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("name", a.deviceName);
                    map.put("address", a.deviceAddress);
                    peersshow.add(map);
                }

                mAdapter = new WifiAdapter(peersshow);
                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(WifiDirectSend.this));

               /* if(null == info) {
                    Toast.makeText(getApplicationContext(),"Connection not established.", Toast.LENGTH_SHORT).show();
                    return;
                }*/

                mAdapter.SetOnItemClickListener(new WifiAdapter.OnItemClickListener() {

                    @Override
                    public void OnItemClick(View view, int position) {



                        if(!firstClick) {

                            firstClick = true;
                            createConnect(peersshow.get(position).get("address"));
                        } else {

                            if(null == info) {
                                Toast.makeText(getApplicationContext(),"Connection not established.", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            String groupOwnerAddress = info.groupOwnerAddress.getHostAddress();
                            sendMessage(groupOwnerAddress);
                            firstClick = false;
                        }


                        /*String address = peersshow.get(position).get("address");

                        // If connection info not available create connection.
                        if(null == info) {

                            Toast.makeText(getApplicationContext(),"Connection not established.", Toast.LENGTH_SHORT).show();
                            createConnect(peersshow.get(position).get("address"));

                            // if connection is available.
                        } else {

                            Toast.makeText(getApplicationContext(),"Connection established.", Toast.LENGTH_SHORT).show();
                            String groupOwnerAddress = info.groupOwnerAddress.getHostAddress();

                            /*String groupOwnerMac = null;
                            try {

                                NetworkInterface network = NetworkInterface.getByInetAddress(info.groupOwnerAddress);
                                groupOwnerMac = new String(network.getHardwareAddress());
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }


                            //Toast.makeText(getApplicationContext(),"groupOwnerAddress: " + groupOwnerMac + " address: " + address, Toast.LENGTH_LONG).show();

                            // If connection is for the selected seller.
                           //if(address.equalsIgnoreCase(groupOwnerMac)) {

                            Toast.makeText(getApplicationContext(),"Sending message.", Toast.LENGTH_SHORT).show();

                            // Send message to seller.
                            sendMessage(groupOwnerAddress);*/


                                // If connection is for different seller again establish connection.
                           /* } else {

                                createConnect(peersshow.get(position).get("address"));
                            }
                        }*/
                    }

                    @Override
                    public void OnItemLongClick(View view, int position) {

                    }
                });

            }
        };

        WifiP2pManager.ConnectionInfoListener mInfoListener = new WifiP2pManager.ConnectionInfoListener() {

            @Override
            public void onConnectionInfoAvailable(WifiP2pInfo minfo) {

                Log.i("xyz", "InfoAvailable is on");
                info = minfo;
            }
        };

        mReceiver = new WifiDirectBroadcastReceiver(mManager, mChannel, this, mPeerListListerner, mInfoListener);
    }

    private void initEvents() {

        btRefresh.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // New code Start.
                peers.clear();
                peersshow.clear();
                mAdapter = new WifiAdapter(peersshow);
                mRecyclerView.setAdapter(mAdapter);

                // Check whether service running.
                /*boolean instance = DataTransferService.isInstanceCreated();
                if(instance) {
                    boolean serviceRunning = isDataTransferServiceRunning();
                }*/

                // New code End.
                Toast.makeText(getApplicationContext(),"Double click on store to Earn/Redeem points.", Toast.LENGTH_SHORT).show();

                // ResetReceiver();

                discoverPeers();
             /*   Toast.makeText(getApplicationContext(),"Double click on store to Earn/Redeem points.", Toast.LENGTH_SHORT).show();
                discoverPeers();
*/
            }
        });



  /*      testing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id = getImeistring();
                DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                DatabaseReference myRef = database.child("customer");

                myRef.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot timeStampSnapShot : dataSnapshot.getChildren()) {

                            HashMap<String, String> timeStampKey = (HashMap)timeStampSnapShot.getValue();

                            String s=timeStampKey.toString();

                            Gson gson=new Gson();
                            PointsBO msg =  gson.fromJson(s, PointsBO.class);
                            Toast.makeText(getApplicationContext(),msg.getBillAmount().toString(),Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(),msg.getPoints().toString(),Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(),msg.getType().toString(),Toast.LENGTH_SHORT).show();

                            *//*String type = timeStampKey.get("type");
                            String pointsStr = timeStampKey.get("points");
                            String billAmountStr = timeStampKey.get("billAmount");
                            String discountAmountStr = timeStampKey.get("disCountAmount");
*//*

                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });




            }
            });

*/



                           /* Map<String, String> value = (Map<String, String>) postSnapshot.getValue();
                            String type = value.get("Type");

                            String billamount = value.get("Billamount");
                            Toast.makeText(getApplication(), type, Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplication(), billamount, Toast.LENGTH_SHORT).show();



                            HashMap<String, String> timeStampKey = (HashMap) timeStampSnapShot.getValue();
                            String type = timeStampKey.get("type");
                            String pointsStr = timeStampKey.get("points");
                            String billAmountStr = timeStampKey.get("billAmount");
                            String discountAmountStr = timeStampKey.get("disCountAmount");
                            String deviceid = timeStampKey.get("disCountAmount");
                            String time = timeStampKey.get("disCountAmount");
                            String storename = timeStampKey.get("disCountAmount");

                            Toast.makeText(getApplication(), type, Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplication(), pointsStr, Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplication(), billAmountStr, Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplication(), discountAmountStr, Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplication(), deviceid, Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplication(), time, Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplication(), storename, Toast.LENGTH_SHORT).show();

*/if(null == acknowledgementFromSellerTask) {

            acknowledgementFromSellerTask = new DataServerAsyncTask(getApplicationContext());
            //this to set delegate/listener back to this class
            acknowledgementFromSellerTask.delegate = this;
            acknowledgementFromSellerTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }

    }

    @Override
    public void onBackPressed() {
        try {

            Intent i = new Intent(WifiDirectSend.this, Navigation.class);
            startActivity(i);
        }catch(Exception ex){
           ex.printStackTrace();
        }
    }

    Intent serviceIntent = null;

    public void ResetReceiver() {

        unregisterReceiver(mReceiver);

        // deletePersistentGroups();
        registerReceiver(mReceiver, mFilter);
    }

    /*A demo base on API which you can connect android device by wifidirect,
    and you can send file or data by socket,what is the most important is that you can set
    which device is the client or service.*/

    private void createConnect(String address) {

        // Toast.makeText(getApplicationContext(),"Inside create connect.", Toast.LENGTH_SHORT).show();

        // WifiP2pConfig config = initWifiP2pConfig(address);

        // For the first time.
        //if(null == config) {

            connectToWIFIDirect(address);
       /* } else {

            // On selection of different seller.
            if(!config.deviceAddress.equalsIgnoreCase(address)) {
                connectToWIFIDirect(address);
            }
        }*/
    }

    private void connectToWIFIDirect(String address) {

        try {

            WifiP2pConfig config = initWifiP2pConfig(address);

            mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {

                @Override
                public void onSuccess() {

                    // Send message to seller.
                    Toast.makeText(getApplicationContext(), "WifiP2pManager connect success.", Toast.LENGTH_SHORT).show();
                    // sendMessage(address);
                }

                @Override
                public void onFailure(int reason) {

                    firstClick = false;
                    Toast.makeText(getApplicationContext(), "WifiP2pManager connect failure. Reason: " + reason, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

   // WifiP2pConfig config = null;

    /**
     * Initialize P2PConfiguration.
     * @param address
     */
    private WifiP2pConfig initWifiP2pConfig(String address) {

        // WifiP2pDevice device;
        WifiP2pConfig config = new WifiP2pConfig();

        try {
            Log.i("bizzmark", address);

            config.deviceAddress = address;

            // config.wps.setup = WpsInfo.PBC;
            Log.i("bizzmark", "MAC IS " + address);

            // Client app so not group owner.
            config.groupOwnerIntent = 0;
        } catch (Throwable th) {
            th.printStackTrace();
        }

        return config;
    }

    /**
     * Discover near by peers.
     */
    private void discoverPeers() {

        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {
                // Toast.makeText(getApplicationContext(),"WifiP2pManager discoverPeers success.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int reason) {

                // Wifi disabled, Enable.
                if(2 == reason) {

                    // Toast.makeText(getApplicationContext(),"Enabling wifi.", Toast.LENGTH_SHORT).show();
                    WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                    wifiManager.setWifiEnabled(true);
                }
                Toast.makeText(getApplicationContext(),"WifiP2pManager discoverPeers failure. Reason: " + reason, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {

        super.onResume();
        Log.i("bizzmark", "on resume.");
        registerReceiver(mReceiver, mFilter);
    }

    @Override
    public void onPause() {

        super.onPause();
        Log.i("bizzmark", "on pause.");
        unregisterReceiver(mReceiver);
    }

    @Override
    protected void onDestroy() {

        Log.i("bizzmark", "on destroy.");
        super.onDestroy();
        StopConnect();
    }

    private void StopConnect() {

        // SetButtonGone();
        mManager.removeGroup(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(int reason) {

            }
        });
    }


    public void showMessage(String title, String Message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);

        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(getApplicationContext(), "ok", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(WifiDirectSend.this, PointListActivity.class);
                startActivity(i);
            }
        });
        builder.show();

        // Remove the entire group and interrupt the existing network connection.
        StopConnect();

        // mManager.ca
    }

    private boolean isDataTransferServiceRunning() {

        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {

            if ("com.pointhub.wifidirect.Service.DataTransferService".equals(service.service.getClassName())) {

                return true;
            }
        }
        return false;
    }

    @Override
    public void processFinish(String result) {

        if(null == result || "".equalsIgnoreCase(result)){
            return;
        }

        Gson gson = Utility.getGsonObject();
        AcknowledgePoints acknowledgePoints = gson.fromJson(result, AcknowledgePoints.class);
        String status = acknowledgePoints.getStatus();

        String earnRedeemString =  acknowledgePoints.getEarnRedeemString();
        PointsBO msg =  gson.fromJson(earnRedeemString, PointsBO.class);

        if ("success".equalsIgnoreCase(status)) {

            Toast.makeText(getApplicationContext(),msg.toString(),Toast.LENGTH_SHORT).show();

            // Save to firebase.
            String dateandtime = getTimeAndDate();
            String imeiNumber = getImeistring();

            DatabaseReference customer =database.child("customer");
            DatabaseReference imeiNumberDB = customer.child(imeiNumber);
            DatabaseReference timebase = imeiNumberDB.child(dateandtime);

            timebase.child("Type").setValue(msg.getType());
            timebase.child("Billamount").setValue(msg.getBillAmount());
            timebase.child("Device id").setValue(msg.getDeviceId());
            timebase.child("Store name").setValue(msg.getStoreName());
            timebase.child("Discount amount").setValue(msg.getDisCountAmount());
            timebase.child("Points").setValue(msg.getPoints());
            timebase.child("Time").setValue(msg.getTime());

            StringBuffer buffer = new StringBuffer();

            buffer.append("Bill amount :" + msg.getBillAmount() + "\n\n");
            buffer.append("Type : " + msg.getType() + "\n\n");
            buffer.append("Points :" + msg.getPoints() + "\n\n");
            buffer.append("Time :" + msg.getTime() + "\n\n");

            showMessage(msg.getStoreName(), buffer.toString());
        } else {

            showMessage(msg.getStoreName(), "Seller has rejected your transaction.");
        }

        // ResetReceiver();
        WifiManager wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(false);
    }


    public String getImeistring() {

        String imeistring = null;
        try {

            TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
            // getDeviceId() function Returns the unique device ID.
            imeistring = telephonyManager.getDeviceId();
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return imeistring;
    }

    public String getTimeAndDate() {

        Calendar calander = Calendar.getInstance();
        simpledateformat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         String dateandtime = simpledateformat.format(calander.getTime());
        return dateandtime ;
    }
    /**
     * Send message to seller.
     * @param hostAddress
     */
    private void sendMessage(String hostAddress) {

        try {

            /*if (null == info) {
                return;
            }*/

            Intent intent = getIntent();
            String sendText = intent.getExtras().getString("earnRedeemString");

            boolean instance = DataTransferService.isInstanceCreated();
            if(!instance) {
                serviceIntent = new Intent(WifiDirectSend.this, DataTransferService.class);
            }

            // Send msg to seller.
            serviceIntent.setAction(DataTransferService.ACTION_SEND_DATA);
            // serviceIntent.putExtra(DataTransferService.EXTRAS_GROUP_OWNER_ADDRESS, info.groupOwnerAddress.getHostAddress());

            serviceIntent.putExtra(DataTransferService.EXTRAS_GROUP_OWNER_ADDRESS, hostAddress);
            if (null != sendText) {
                serviceIntent.putExtra(DataTransferService.MESSAGE, sendText);
            }

            // Log.i("bizzmark", "Seller Address: " + info.groupOwnerAddress.getHostAddress());

            Log.i("bizzmark", "Seller Address: " + hostAddress);
            serviceIntent.putExtra(DataTransferService.EXTRAS_GROUP_OWNER_PORT, 8888);

            // Start service.
            startService(serviceIntent);

        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

}


