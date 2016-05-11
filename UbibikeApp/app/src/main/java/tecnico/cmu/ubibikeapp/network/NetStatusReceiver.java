package tecnico.cmu.ubibikeapp.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetStatusReceiver extends BroadcastReceiver {
    private final NetworkListener callback;

    public NetStatusReceiver(NetworkListener callback){
        this.callback = callback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        boolean isConnected = activeNetInfo != null && activeNetInfo.isConnectedOrConnecting();

        if(callback != null)
            callback.onConnection(isConnected);
    }

    public interface NetworkListener {
        void onConnection(boolean connected);
    }
}

