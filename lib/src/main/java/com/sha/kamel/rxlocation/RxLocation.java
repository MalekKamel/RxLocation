package com.sha.kamel.rxlocation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.location.Location;
import android.support.v4.app.FragmentActivity;

import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by Sha on 1/7/18.
 */

public final class RxLocation {
    private boolean shouldRequestOnce;
    private PublishSubject<Location> ps = PublishSubject.create();
    private UpdateQuality updateQuality;
    private LocationRetriever locationRetriever;

    public RxLocation() {
        locationRetriever = new LocationRetriever(updateQuality);
    }

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

        return ps;
    }

    @SuppressLint("CheckResult")
    private void handlePermissions(FragmentActivity activity) {
        new RxPermissions(activity).request(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe(isGranted -> {
                    if (!isGranted) return;
                    locationRetriever.get(
                            activity,
                            this::onResult
                    );
                });
    }

    private void onResult(Location location) {
        ps.onNext(location);
        if (shouldRequestOnce) removeLocationUpdates();
    }


    /**
     * Remove location updates to stop listening to location
     * updates
     * @return this
     */
    public RxLocation removeLocationUpdates(){
        locationRetriever.removeLocationUpdates();
        ps.onComplete();
        return this;
    }

}
