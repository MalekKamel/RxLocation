



# RxCurrentLocation
###  RxJava wrapper for Android current location.

![alt text](https://github.com/ShabanKamell/RxCurrentLocation/blob/master/blob/master/raw/mobile-location.png "Sample App")

# Installation
[ ![Download](https://api.bintray.com/packages/shabankamel/android/rxcurrentlocation/images/download.svg) ](https://bintray.com/shabankamel/android/rxcurrentlocation/_latestVersion)
```groovy
dependencies {
    implementation 'com.sha.kamel:rx-current-location:1.0.0@aar'
}
repositories { 
maven { url "https://dl.bintray.com/shabankamel/android" } 
}
```
# Usage
```java
 new RxCurrentLocation()
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

### See 'app' module for the full code.

# License

## Apache license 2.0
