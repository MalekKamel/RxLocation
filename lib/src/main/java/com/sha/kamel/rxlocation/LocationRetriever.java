package com.sha.kamel.rxlocation;

import android.annotation.SuppressLint;
import android.location.Location;
import android.support.v4.app.FragmentActivity;

import com.annimon.stream.function.Consumer;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.sha.kamel.rxlocationsettingsrequest.RxGps;

class LocationRetriever {
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private UpdateQuality updateQuality;

    LocationRetriever(UpdateQuality updateQuality) {
        this.updateQuality = updateQuality;
    }

    @SuppressLint("MissingPermission")
    public void get(FragmentActivity activity, Consumer<Location> callback) {
        new GoogleApiClientUtil().create(
                activity,
                () -> {
                    // Get first location
                    fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity);
                    fusedLocationProviderClient.getLastLocation()
                            .addOnSuccessListener(location -> {
                                // Got last known location. In some rare situations, this can be null.
                                if (location != null){
                                    callback.accept(location);
                                    return;
                                }
                                requestLocationUpdate(activity, callback);
                            })
                            .addOnFailureListener(Throwable::printStackTrace);
                },
                LocationServices.API);
    }

    @SuppressLint({"MissingPermission", "CheckResult"})
    private void requestLocationUpdate(FragmentActivity activity, Consumer<Location> callback){
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
                    callback.accept(locationResult.getLastLocation());
                    removeLocationUpdates();
                }
            };

            new RxGps().enable(
                    locationRequest,
                    activity)
                    .subscribe(ok -> {
                        if (!ok) return;
                        fusedLocationProviderClient.requestLocationUpdates(
                                locationRequest,
                                locationCallback,
                                null);
                    });

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    void removeLocationUpdates(){
        if (fusedLocationProviderClient != null && locationCallback != null)
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

}
