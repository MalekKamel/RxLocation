package com.sha.kamel.rxlocation;

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
import com.sha.kamel.rxlocationsettingsrequest.RxGps;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by Sha on 1/7/18.
 */

public final class RxLocation {
    private FailureCallback failureCallback;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private boolean shouldRequestOnce;
    private PublishSubject<Location> ps = PublishSubject.create();
    private UpdateQuality updateQuality;
    /**
     * retrieve current location only once
     * @param activity an activity instance
     * @return {@link Observable} of type {@link Location}
     */
    public Single<Location> retrieveCurrentLocation(FragmentActivity activity){
        shouldRequestOnce = true;
        return Single.fromObservable(listenForUpdates(activity));
    }

    /**
     * listen for location updates
     * Note: call {@link RxLocation#removeLocationUpdates()} to stop listening
     * for location updates
     * @param activity an activity instance
     * @return {@link Observable} of type {@link Location}
     */
    public Observable<Location> listenForUpdates(FragmentActivity activity){
        handlePermissions(activity);

        if (!NetworkUtil.isConnected(activity)){
            error(FailureMessage.Error.NETWORK_DISABLED, "");
            return ps;
        }

        return ps;
    }

    /**
     * listen for location updates
     * Note: call {@link RxLocation#removeLocationUpdates()} to stop listening
     * for location updates
     * @param activity an activity instance
     * @param updateQuality location priority, interval and fastUpdateInterval
     * @return {@link Observable} of type {@link Location}
     */
    public Observable<Location> listenForUpdates(FragmentActivity activity, UpdateQuality updateQuality){
        this.updateQuality = updateQuality;

        handlePermissions(activity);

        if (!NetworkUtil.isConnected(activity)){
            error(FailureMessage.Error.NETWORK_DISABLED, "");
            return ps;
        }

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
        if (failureCallback != null) failureCallback.run(new FailureMessage(error, msg));
        Log.d(RxLocation.class.getSimpleName(), error.toString());
    }


    @SuppressLint("MissingPermission")
    private void requestLocationUpdate(FragmentActivity activity){
        try {
            if(updateQuality == null) updateQuality = new UpdateQuality();

            LocationRequest locationRequest = new LocationRequest();
            locationRequest.setPriority(updateQuality.getPriority());
            locationRequest.setInterval(updateQuality.getInterval());
            locationRequest.setFastestInterval(updateQuality.getFastestUpdateInterval());

            locationCallback = new LocationCallback(){
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    onResult(locationResult.getLastLocation());
                }
            };

            new RxGps().enable(
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
     * @param failureCallback callback of fail message
     * @return this
     */
    public RxLocation onFailure(FailureCallback failureCallback) {
        this.failureCallback = failureCallback;
        return this;
    }

    /**
     * Remove location updates to stop listening to location
     * updates
     * @return this
     */
    public RxLocation removeLocationUpdates(){
        if (fusedLocationProviderClient != null && locationCallback != null)
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        ps.onComplete();
        return this;
    }

}
