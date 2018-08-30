package com.sha.kamel.rxcurrentlocation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by Sha on 1/7/18.
 */

public final class RxCurrentLocation {
    private long interval = 0;
    private long fastestUpdateInterval = (long) 2 * 1000;
    private int priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY;
    private OnFailure onFailure;

    /**
     * get current location
     * @param activity an activity instance
     * @return {@link Observable} of type {@link Location}
     */
    public Observable<Location> get(FragmentActivity activity){
        PublishSubject<Location> ps = PublishSubject.create();

        if (!NetworkUtil.isConnected(activity)){
            error(FailureMessage.Error.NETWORK_DISABLED, "");
            return ps;
        }

        if (!LocationManagerUtil.isLocationServiceEnabled(activity)){
            error(FailureMessage.Error.GPS_DISABLED, "");
            return ps;
        }

        handlePermissions(activity, ps);

        return ps;
    }

    private void handlePermissions(FragmentActivity activity, PublishSubject<Location> ps) {
        new RxPermissions(activity).request(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe(isGranted -> {
                    if (isGranted)
                        getLocation(activity, ps);
                });
    }

    @SuppressLint("MissingPermission")
    private void getLocation(FragmentActivity activity, PublishSubject<Location> ps) {
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
    }

    private void error(FailureMessage.Error error, String msg) {
        if (onFailure != null) onFailure.run(new FailureMessage(error, msg));
        Log.d(RxCurrentLocation.class.getSimpleName(), error.toString());
    }


    @SuppressLint("MissingPermission")
    private void requestLocationUpdate(PublishSubject<Location> ps, FusedLocationProviderClient flpc){
        try {
            LocationRequest locationRequest = new LocationRequest();
            locationRequest.setPriority(priority);
            locationRequest.setInterval(interval);
            locationRequest.setFastestInterval(fastestUpdateInterval);

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

    /**
     * Set interval of LocationRequest
     * default: 0
     * @param interval in milliseconds
     * @return thi
     */
    public RxCurrentLocation interval(long interval) {
        this.interval = interval;
        return this;
    }

    /**
     * Set fastest update interval of LocationRequest
     * default: 2 * 1000
     * @param interval in milliseconds
     * @return thi
     */
    public RxCurrentLocation fastestUpdateInterval(long interval) {
        this.fastestUpdateInterval = interval;
        return this;
    }

    /**
     * Set priority of LocationRequest
     * default: LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
     * @param priority for example: {@code LocationRequest.PRIORITY_LOW_POWER }
     * @return thi
     */
    public RxCurrentLocation priority(int priority) {
        this.priority = priority;
        return this;
    }

}
