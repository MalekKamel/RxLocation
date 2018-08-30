package com.sha.kamel.rxcurrentlocation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.sha.kamel.rxlocationsettingsrequest.RxLocationSettingsRequest;
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
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private boolean shouldRequestOnce;
    private PublishSubject<Location> ps = PublishSubject.create();

    /**
     * get current location updates continuously
     * @param activity an activity instance
     * @return {@link Observable} of type {@link Location}
     */
    public Observable<Location> getOnce(FragmentActivity activity){
        shouldRequestOnce = true;
        return get(activity);
    }

    /**
     * get current location only once
     * @param activity an activity instance
     * @return {@link Observable} of type {@link Location}
     */
    public Observable<Location> get(FragmentActivity activity){
        if (!NetworkUtil.isConnected(activity)){
            error(FailureMessage.Error.NETWORK_DISABLED, "");
            return ps;
        }

        handlePermissions(activity);

        return ps;
    }

    private void handlePermissions(FragmentActivity activity) {
        new RxPermissions(activity).request(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe(isGranted -> {
                    if (isGranted)
                        getLocation(activity);
                });
    }

    @SuppressLint("MissingPermission")
    private void getLocation(FragmentActivity activity) {
        GoogleApiClientUtil.create(
                activity,
                () -> {
                    // Get first location
                    fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity);
                    fusedLocationProviderClient.getLastLocation()
                            .addOnSuccessListener(activity, location -> {
                                // Got last known location. In some rare situations, this can be null.
                                if (location == null)
                                    requestLocationUpdate(activity);
                                else{
                                    onResult(location);
                                }
                            })
                            .addOnFailureListener(e -> {
                                e.printStackTrace();
                                error(FailureMessage.Error.UNKNOWN, e.getMessage());
                            });
                },
                LocationServices.API);
    }

    private void onResult(Location location) {
        ps.onNext(location);
        if (shouldRequestOnce) removeLocationUpdates();
    }

    private void error(FailureMessage.Error error, String msg) {
        if (onFailure != null) onFailure.run(new FailureMessage(error, msg));
        Log.d(RxCurrentLocation.class.getSimpleName(), error.toString());
    }


    @SuppressLint("MissingPermission")
    private void requestLocationUpdate(FragmentActivity activity){
        try {
            LocationRequest locationRequest = new LocationRequest();
            locationRequest.setPriority(priority);
            locationRequest.setInterval(interval);
            locationRequest.setFastestInterval(fastestUpdateInterval);

            locationCallback = new LocationCallback(){
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    onResult(locationResult.getLastLocation());
                }
            };

            new RxLocationSettingsRequest().request(
                    locationRequest,
                    activity)
            .subscribe(ok -> {
                if (ok){
                    fusedLocationProviderClient.requestLocationUpdates(
                            locationRequest,
                            locationCallback,
                            null);
                }
                else error(FailureMessage.Error.GPS_DISABLED, "");
            });

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

    /**
     * Remove location updates to stop listening to location
     * updates
     * @return this
     */
    public RxCurrentLocation removeLocationUpdates(){
        if (fusedLocationProviderClient != null && locationCallback != null)
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        ps.onComplete();
        return this;
    }

}
