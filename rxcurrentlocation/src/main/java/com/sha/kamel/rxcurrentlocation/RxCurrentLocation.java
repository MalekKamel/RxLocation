package com.sha.kamel.rxcurrentlocation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by Sha on 1/7/18.
 */

public final class RxCurrentLocation {
    private static final long updateInterval = 0;
    private static final long FASTEST_UPDATE_INTERVAL = (long) 10 * 1000;
    private OnFailure onFailure;

    /**
     * get current location
     * @param activity an activity instance
     * @return {@link Observable} of type {@link Location}
     */
    @SuppressLint("MissingPermission")
    public Observable<Location> get(Activity activity){
        PublishSubject<Location> ps = PublishSubject.create();

        if (!NetworkUtil.isConnected(activity)){
            error(FailureMessage.Error.NETWORK_DISABLED, "");
            return ps;
        }

        if (!LocationManagerUtil.isLocationServiceEnabled(activity)){
            error(FailureMessage.Error.GPS_DISABLED, "");
            return ps;
        }

        GoogleApiClientUtil.create(
                activity,
                () -> {
                    // Get first location
                    FusedLocationProviderClient flpc = LocationServices.getFusedLocationProviderClient(activity);
                    flpc.getLastLocation()
                            .addOnSuccessListener(activity, location -> {
                                // Got last known location. In some rare situations, this can be null.
                                if (location == null)
                                    requestLocationUpdate(ps, flpc);
                                else{
                                    ps.onNext(location);
                                    ps.onComplete();
                                }
                            })
                            .addOnFailureListener(e -> {
                                e.printStackTrace();
                                error(FailureMessage.Error.UNKNOWN, e.getMessage());
                            });
                },
                LocationServices.API);
        return ps;
    }

    private void error(FailureMessage.Error error, String msg) {
        if (onFailure != null) onFailure.run(new FailureMessage(error, msg));
        Log.d(RxCurrentLocation.class.getSimpleName(), error.toString());
    }

    @SuppressLint("MissingPermission")
    private void requestLocationUpdate(PublishSubject<Location> ps, FusedLocationProviderClient flpc){
        try {
            LocationRequest locationRequest = new LocationRequest();
            locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            locationRequest.setInterval(updateInterval);
            locationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL);

            flpc.requestLocationUpdates(
                    locationRequest,
                    new LocationCallback(){
                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                            super.onLocationResult(locationResult);
                            ps.onNext(locationResult.getLastLocation());
                            ps.onComplete();
                        }
                    },
                    null);

        }catch (SecurityException securityException){
            securityException.printStackTrace();
            error(FailureMessage.Error.PERMISSIONS_REQUIRED, "");
        }catch (Exception e){
            e.printStackTrace();
            error(FailureMessage.Error.UNKNOWN, e.getMessage());
        }
    }

    /**
     * Attach listener for fail errors
     * @param onFailure callback of fail message
     * @return this
     */
    public RxCurrentLocation onFailureListener(OnFailure onFailure) {
        this.onFailure = onFailure;
        return this;
    }
}
