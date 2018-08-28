package com.sha.kamel.rxcurrentlocationsample;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.sha.kamel.rxcurrentlocation.RxCurrentLocation;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {

    private TextView tv_location;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Disposable disposable = new RxPermissions(this).request(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe(isGranted -> {
                    if (isGranted)
                        getCurrentLocation();
                });
        compositeDisposable.add(disposable);
    }

    private void getCurrentLocation() {
        tv_location = findViewById(R.id.tv_location);
       Disposable disposable = new RxCurrentLocation()
                .onFailureListener(failMessage -> tv_location.setText(failMessage.getMessage()))
                .get(this)
                .subscribe(location -> {
                    String msg = "lat = " +
                            location.getLatitude() +
                            ", lng = " +
                            location.getLongitude();
                    tv_location.setText(msg);
                });
       compositeDisposable.add(disposable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }
}
