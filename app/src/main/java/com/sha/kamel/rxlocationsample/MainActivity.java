package com.sha.kamel.rxlocationsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.android.gms.location.LocationRequest;
import com.sha.kamel.rxlocation.UpdateQuality;
import com.sha.kamel.rxlocation.RxLocation;

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
        Disposable disposable = new RxLocation()
                .listenForUpdates(
                        MainActivity.this,
                        new UpdateQuality()
                                .priority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .interval(10 * 1000)
                                .fastestUpdateInterval(2 * 1000))
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
