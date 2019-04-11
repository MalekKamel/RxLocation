package com.sha.kamel.rxlocation;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Sha on 9/28/17.
 */

public class NetworkUtil {


    public static NetworkInfo getNetworkInfo(Context context){
        ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return connMgr.getActiveNetworkInfo();
    }

    public static boolean isConnected(Context context) {
        NetworkInfo networkInfo = NetworkUtil.getNetworkInfo(context);
        return networkInfo != null && networkInfo.isConnected();
    }
}
