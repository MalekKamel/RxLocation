# RxCurrentLocation
###  RxJava wrapper for Android current location.

![alt text](https://github.com/ShabanKamell/RxCurrentLocation/blob/master/blob/master/raw/mobile-location.png "Sample App")

# Features

 - [ ] Easy-to-use APIs
 - [ ] Handle runtime.
 - [ ] Enable GPS.

# Installation
[ ![Download](https://api.bintray.com/packages/shabankamel/android/rxcurrentlocation/images/download.svg) ](https://bintray.com/shabankamel/android/rxcurrentlocation/_latestVersion)
```groovy
dependencies {
    implementation 'com.sha.kamel:rx-current-location:1.5.0@aar'
}

allprojects {
 repositories { 
  maven { url "https://dl.bintray.com/shabankamel/android" } 
 }
}
```
# Usage
```java
 new RxCurrentLocation()
                .fastestUpdateInterval(2 * 1000)
                .interval(10 * 1000)
                .priority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .onFailureListener(failMessage -> tv_location.setText(failMessage.getMessage()))
                .get(MainActivity.this)
                .subscribe(location -> {
                            String msg = "lat = " +
                                    location.getLatitude() +
                                    ", lng = " +
                                    location.getLongitude();
                            tv_location.setText(msg);
                        }
                );
```
## getOnce vs get
Use `getOnce(FragmentActivity)` to get the location only once. And use `get(FragmentActivity)` to get continuous updates of current locations
```java
     rxCurrentLocation.getOnce(MainActivity.this)...
```
## Note
Call `RxCurrentLocation.removeLocationUpdates()` to stop location updates when you don't need updates anymore.

## LocationRequest Interval
```java
     rxCurrentLocation.interval(10 * 1000);
```
### Note
Default = 0

## LocationRequest fastest update interval
```java
     rxCurrentLocation.fastestUpdateInterval(2 * 1000);
```
### Note
Default = 2 * 1000

## LocationRequest priority

```java
     rxCurrentLocation.priority(LocationRequest.PRIORITY_LOW_POWER);
```
### Note
Default = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY

# Errors
if and error occureed it will be passed to `onFailureListener(OnFailure)`
### Types of expected errors:

 - [ ] GPS_DISABLED
 - [ ] NETWORK_DISABLED
 - [ ] UNKNOWN

#### Example
```java
new RxCurrentLocation().onFailureListener(failMessage -> {  
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
  } 
})
```

### See 'app' module for the full code.

# License

## Apache license 2.0
