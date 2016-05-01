package tecnico.cmu.ubibikeapp.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import pt.inesc.termite.wifidirect.SimWifiP2pBroadcast;
import pt.inesc.termite.wifidirect.SimWifiP2pInfo;


public class WDEventReceiver extends BroadcastReceiver{

    static String TAG = "WDEvent";
    WDService service;

    public WDEventReceiver(WDService service) {
        super();
        this.service = service;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, "WiFi Direct Event");
        if (SimWifiP2pBroadcast.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {

            // This action is triggered when the Termite service changes state:
            // - creating the service generates the WIFI_P2P_STATE_ENABLED event
            // - destroying the service generates the WIFI_P2P_STATE_DISABLED event

            int state = intent.getIntExtra(SimWifiP2pBroadcast.EXTRA_WIFI_STATE, -1);
            if (state == SimWifiP2pBroadcast.WIFI_P2P_STATE_ENABLED) {
                //Toast.makeText(mActivity, "WiFi Direct enabled", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "WiFi Direct enabled");
            } else {
                //Toast.makeText(mActivity, "WiFi Direct disabled", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "WiFi Direct disabled");
            }

        } else if (SimWifiP2pBroadcast.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            Log.d(TAG, "Peers list changed");

            // Request available peers from the wifi p2p manager. This is an
            // asynchronous call and the calling activity is notified with a
            // callback on PeerListListener.onPeersAvailable()
            service.requestPeers();
            //Toast.makeText(mActivity, "Peer list changed", Toast.LENGTH_SHORT).show();

        } else if (SimWifiP2pBroadcast.WIFI_P2P_NETWORK_MEMBERSHIP_CHANGED_ACTION.equals(action)) {
            Log.d(TAG, "Network membership changed");

            service.requestGroupInfo();
            SimWifiP2pInfo ginfo = (SimWifiP2pInfo) intent.getSerializableExtra(SimWifiP2pBroadcast.EXTRA_GROUP_INFO);
            //ginfo.print();
            //Toast.makeText(mActivity, "Network membership changed", Toast.LENGTH_SHORT).show();

        } else if (SimWifiP2pBroadcast.WIFI_P2P_GROUP_OWNERSHIP_CHANGED_ACTION.equals(action)) {
            Log.d(TAG, "Group ownership changed");

            SimWifiP2pInfo ginfo = (SimWifiP2pInfo) intent.getSerializableExtra(SimWifiP2pBroadcast.EXTRA_GROUP_INFO);
            //ginfo.print();
            //Toast.makeText(mActivity, "Group ownership changed", Toast.LENGTH_SHORT).show();
        }
    }

}
