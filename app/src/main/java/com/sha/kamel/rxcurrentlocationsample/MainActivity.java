package com.sha.kamel.rxcurrentlocationsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.android.gms.location.LocationRequest;
import com.sha.kamel.rxcurrentlocation.RxCurrentLocation;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {

    private TextView tv_location;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getCurrentLocation();
    }

    private void getCurrentLocation() {
        tv_location = findViewById(R.id.tv_location);
        Disposable disposable = new RxCurrentLocation()
                .fastestUpdateInterval(2 * 1000)
                .interval(10 * 1000)
                .priority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .onFailureListener(failMessage -> {
                    // you can show error directly
                    tv_location.setText(failMessage.getMessage());
                    // or you can handle each error separately
                    switch (failMessage.getError()){
                        case GPS_DISABLED:
                            // handle error
                            break;
                        case NETWORK_DISABLED:
                            // handle error
                            break;

                        case UNKNOWN:
                            // handle error
                            break;
                    }})
                .get(MainActivity.this)
                .subscribe(location -> {
                            String msg = "lat = " +
                                    location.getLatitude() +
                                    ", lng = " +
                                    location.getLongitude();
                            tv_location.setText(msg);
                        }
                );
        compositeDisposable.add(disposable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }
}
