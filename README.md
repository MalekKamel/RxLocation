
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
    implementation 'com.sha.kamel:rx-location:1.9.0@aar'
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
                .onFailure(failMessage -> tv_location.setText(failMessage.getMessage()))
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

# Errors
if and error occureed it will be passed to `onFailureListener(OnFailure)`
### Types of expected errors:

 - [ ] GPS_DISABLED
 - [ ] NETWORK_DISABLED
 - [ ] UNKNOWN

#### Example
```java
new RxLocation().onFailureListener(failMessage -> {  
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
