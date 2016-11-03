package com.pointhub.wifidirect;

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
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pointhub.R;
import com.pointhub.wifidirect.Adapter.WifiAdapter;
import com.pointhub.wifidirect.BroadcastReceiver.WifiDirectBroadcastReceiver;
import com.pointhub.wifidirect.Service.DataTransferService;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class WifiDirectSend extends AppCompatActivity {

    private Button btRefresh;
    private Button btnRequest;
    // private EditText editText;
    private ImageView imagebut;
    private CardView cardView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi_direct_send);


        initView();
        initIntentFilter();
        initReceiver();
        initEvents();
        discoverPeers();
    }

    private void initView() {

        Bundle extras = getIntent().getExtras();

        imagebut= (ImageView) findViewById(R.id.imgmenu);
        btRefresh = (Button) findViewById(R.id.btnRefresh);
        btnRequest = (Button) findViewById(R.id.btnRequest);
        // editText = (EditText) findViewById(R.id.txtSend);
        cardView= (CardView) findViewById(R.id.cardview);
        /*InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);*/

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mAdapter = new WifiAdapter(peersshow);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));

        btnRequest.setVisibility(View.INVISIBLE);

    }

    private void initIntentFilter() {

        mFilter = new IntentFilter();
        mFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mFilter.addAction(WifiP2pManager.WIFI_P2P_DISCOVERY_CHANGED_ACTION);
        mFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
    }

    private void initReceiver() {

        mManager = (WifiP2pManager) getSystemService(WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, Looper.myLooper(), null);

        WifiP2pManager.PeerListListener mPeerListListerner = new WifiP2pManager.PeerListListener() {

            @Override
            public void onPeersAvailable(WifiP2pDeviceList peersList) {

                //Toast.makeText(getApplicationContext(),"WifiP2pManager.PeerListListener onPeersAvailable.",Toast.LENGTH_SHORT).show();
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

                        createConnect(peersshow.get(position).get("address"), peersshow.get(position).get("name"));

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

               // btRefresh.setVisibility(View.VISIBLE);

                // Toast.makeText(getApplicationContext(),"ConnectionInfoListener onConnectionInfoAvailable.",Toast.LENGTH_SHORT).show();
                Log.i("xyz", "InfoAvailable is on");
                info = minfo;
                // btnRequest.setVisibility(View.VISIBLE);
            }
        };

        mReceiver = new WifiDirectBroadcastReceiver(mManager, mChannel, this, mPeerListListerner, mInfoListener);
    }

    /**
     * Initialize all the events.
     */
    private void initEvents() {

        btRefresh.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Toast.makeText(getApplicationContext(),"Send button clicked.",Toast.LENGTH_SHORT).show();
                discoverPeers();
                // sendMessage();
               // btRefresh.setVisibility(View.INVISIBLE);
            }
        });

        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
                btnRequest.setVisibility(View.INVISIBLE);
            }
        });

    }

    /**
     * Send message.
     */
    private void sendMessage() {

        try {

            if(null == info) {
                Toast.makeText(getApplicationContext(),"null == info.",Toast.LENGTH_SHORT).show();
                return;
            }


            Intent serviceIntent = new Intent(WifiDirectSend.this, DataTransferService.class);

            serviceIntent.setAction(DataTransferService.ACTION_SEND_DATA);

            serviceIntent.putExtra(DataTransferService.EXTRAS_GROUP_OWNER_ADDRESS, info.groupOwnerAddress.getHostAddress());
            Log.i("address", "owenerip is " + info.groupOwnerAddress.getHostAddress());
            serviceIntent.putExtra(DataTransferService.EXTRAS_GROUP_OWNER_PORT, 8888);

            String sendMsg = getIntent().getExtras().getString("earnString");
            serviceIntent.putExtra(DataTransferService.MESSAGE, sendMsg);

            startService(serviceIntent);

            /*Intent serviceIntent = new Intent(WifiDirectSend.this, DataTransferService.class);
            serviceIntent.setAction(DataTransferService.ACTION_SEND_DATA);
            serviceIntent.putExtra(DataTransferService.EXTRAS_GROUP_OWNER_ADDRESS, info.groupOwnerAddress.getHostAddress());

            Log.i("bizzmark", "Ownerip: " + info.groupOwnerAddress.getHostAddress());
            // Log.i("bizzmark", "Owner Name: " + info.groupOwnerAddress.getCanonicalHostName());
            serviceIntent.putExtra(DataTransferService.EXTRAS_GROUP_OWNER_PORT, 8888);

            // String sendText = editText.getText().toString();
            String sendMsg = getIntent().getExtras().getString("earnString");
            serviceIntent.putExtra(DataTransferService.MESSAGE, sendMsg);

            Log.i("bizzmark", sendMsg);
            WifiDirectSend.this.startService(serviceIntent);*/

        }catch (Throwable th){
            th.printStackTrace();
        }
    }

    /**
     * Get IP address from first non-localhost interface
     * @param
     * @return  address or empty string
     */
    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':')<0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim<0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) { } // for now eat exceptions
        return "";
    }

    WifiP2pConfig config = null;

    /*A demo base on API which you can connect android device by wifidirect,
    and you can send file or data by socket,what is the most important is that you can set
    which device is the client or service.*/

    private void createConnect(String address, final String name) {

        //if(null == config) {
            initWifiP2pConfig(address);
        //}

        mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {

                 Toast.makeText(getApplicationContext(),"WifiP2pManager connect success.", Toast.LENGTH_SHORT).show();
                // btnRequest.setVisibility(View.VISIBLE);
                sendMessage();
                // btSend.setVisibility(View.VISIBLE);

            }

            @Override
            public void onFailure(int reason) {
                Toast.makeText(getApplicationContext(),"WifiP2pManager connect failure. Reason: " + reason, Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void initWifiP2pConfig(String address) {

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
    }


    private void discoverPeers() {

        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {
                Toast.makeText(getApplicationContext(),"WifiP2pManager discoverPeers success.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int reason) {

                if(2 == reason) {

                    Toast.makeText(getApplicationContext(),"Enabling wifi.", Toast.LENGTH_SHORT).show();
                    WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                    wifiManager.setWifiEnabled(true);
                }
                Toast.makeText(getApplicationContext(),"WifiP2pManager discoverPeers failure. Reason: " + reason, Toast.LENGTH_SHORT).show();
            }
        });
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

    public void ResetReceiver() {

        unregisterReceiver(mReceiver);
        registerReceiver(mReceiver, mFilter);
    }

}
