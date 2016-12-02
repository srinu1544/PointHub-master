package com.pointhub.wifidirect;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.pointhub.PointListActivity;
import com.pointhub.R;
import com.pointhub.wifidirect.Adapter.WifiAdapter;
import com.pointhub.wifidirect.BroadcastReceiver.WifiDirectBroadcastReceiver;
import com.pointhub.wifidirect.Service.DataTransferService;
import com.pointhub.wifidirect.Task.DataServerAsyncTask;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class WifiDirectSend extends AppCompatActivity implements View.OnClickListener {

    private Button btRefresh;

    private RecyclerView mRecyclerView;
    private WifiAdapter mAdapter;
    private List peers = new ArrayList();
    private List<HashMap<String, String>> peersshow = new ArrayList();

    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private BroadcastReceiver mReceiver;
    private IntentFilter mFilter;

    // Connection info object.
    private WifiP2pInfo info;

    DataServerAsyncTask acknowledgementFromSellerTask = null;

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

        // Add
        /*MobileAds.initialize(getApplicationContext(),"ca-app-pub-3940256099942544/6300978111");
        // Load an ad into the AdMob banner view.
        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);*/
    }

    /**
     * Initialize all the views.
     */
    private void initView() {

        btRefresh = (Button) findViewById(R.id.btnRefresh);

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

                mAdapter.SetOnItemClickListener(new WifiAdapter.OnItemClickListener() {
                    @Override
                    public void OnItemClick(View view, int position) {
                        // createConnect(peersshow.get(position).get("address"), peersshow.get(position).get("name"));
                        createConnect(peersshow.get(position).get("address"));
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
                boolean instance = DataTransferService.isInstanceCreated();
                if(instance) {
                    boolean serviceRunning = isDataTransferServiceRunning();
                }

                // New code End.
                Toast.makeText(getApplicationContext(),"Double click on store to Earn/Redeem points.", Toast.LENGTH_SHORT).show();
                discoverPeers();
            }
        });

        if(null == acknowledgementFromSellerTask) {
            acknowledgementFromSellerTask = new DataServerAsyncTask(getApplicationContext());
            acknowledgementFromSellerTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }

    }

    Intent serviceIntent = null;

    private void sendMessage() {

        try {

            if (null == info) {
                return;
            }

            Intent intent = getIntent();
            String sendText = intent.getExtras().getString("earnRedeemString");

            boolean instance = DataTransferService.isInstanceCreated();
            if(!instance) {
                serviceIntent = new Intent(WifiDirectSend.this, DataTransferService.class);
            }

            // Send msg to seller.
            serviceIntent.setAction(DataTransferService.ACTION_SEND_DATA);
            serviceIntent.putExtra(DataTransferService.EXTRAS_GROUP_OWNER_ADDRESS, info.groupOwnerAddress.getHostAddress());
            if (null != sendText) {
                serviceIntent.putExtra(DataTransferService.MESSAGE, sendText);
            }

            Log.i("bizzmark", "Seller Address: " + info.groupOwnerAddress.getHostAddress());
            serviceIntent.putExtra(DataTransferService.EXTRAS_GROUP_OWNER_PORT, 8888);

            // Start service.
            startService(serviceIntent);

        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    /*A demo base on API which you can connect android device by wifidirect,
    and you can send file or data by socket,what is the most important is that you can set
    which device is the client or service.*/

    private void createConnect(String address) {

        WifiP2pConfig config = initWifiP2pConfig(address);

        mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {

                // Send message to seller.
                sendMessage();
            }

            @Override
            public void onFailure(int reason) {

                Toast.makeText(getApplicationContext(),"WifiP2pManager connect failure. Reason: " + reason, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Initialize P2PConfiguration.
     * @param address
     */
    private WifiP2pConfig initWifiP2pConfig(String address) {

        WifiP2pConfig config = null;

        try {
            // WifiP2pDevice device;
            config = new WifiP2pConfig();
            Log.i("bizzmark", address);

            config.deviceAddress = address;

            config.wps.setup = WpsInfo.PBC;
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

    @Override
    public void onClick(View v)
    {
        /*if(v==btSignOut)
        {
            firebaseAuth.signOut();

            // Closing activity.
            finish();


            startActivity(new Intent(this, Navigation.class));
        }*/
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
}

