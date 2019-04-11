
# RxLocation
###  RxJava wrapper for Android location.

![alt text](https://github.com/ShabanKamell/RxLocation/blob/master/blob/master/raw/mobile-location.png "Sample App")

# Features

 - [ ] Easy-to-use APIs
 - [ ] Handle runtime.
 - [ ] Enable GPS.

# Installation
[ ![Download](https://api.bintray.com/packages/shabankamel/android/rxcurrentlocation/images/download.svg) ](https://bintray.com/shabankamel/android/rxcurrentlocation/_latestVersion)
```groovy
dependencies {
    implementation 'com.sha.kamel:rx-location:1.10.0@aar'
}

repositories {
maven {
url "https://dl.bintray.com/shabankamel/android"
}
...
}
```

# Usage
```java
 new RxLocation()
                .retrieveCurrentLocation(MainActivity.this)
                .subscribe(location -> {
                            String msg = "lat = " +
                                    location.getLatitude() +
                                    ", lng = " +
                                    location.getLongitude();
                            tv_location.setText(msg);
                        }
                );
```
## listenForUpdates
```java
     rxLocation.listenForUpdates(MainActivity.this)...
```
### Update Quality 
You can set update quality by passing `UpdateQuality` object to the overloaded `listenForUpdates` function
```java
rxLocation.listenForUpdates(  
        MainActivity.this,  
        new UpdateQuality()  
                .priority(LocationRequest.PRIORITY_HIGH_ACCURACY)  
                .interval(10 * 1000)  
                .fastestUpdateInterval(2 * 1000))
```
## Note
Call `RxLocation.removeLocationUpdates()` to stop location updates when you don't need updates anymore.

### Update Quality Defaults
 - [ ] priority default value = 0.
 - [ ] interval default value =  LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY.
 - [ ] fastestUpdateInterval default value = 2 * 1000.

### See 'app' module for the full code.

# License

## Apache license 2.0
