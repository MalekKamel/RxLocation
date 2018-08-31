package com.sha.kamel.rxlocation;

import android.content.Context;
import android.location.LocationManager;

/**
 * Created by Sha on 1/7/18.
 */

public final class LocationManagerUtil {

    public static boolean isLocationServiceEnabled(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (lm == null) return false;

        boolean gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean networkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        return gpsEnabled || networkEnabled;
    }



}
