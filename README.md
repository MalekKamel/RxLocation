




# RxCurrentLocation
###  RxJava wrapper for Android current location.

![alt text](https://github.com/ShabanKamell/RxCurrentLocation/blob/master/blob/master/raw/mobile-location.png "Sample App")

# Installation
[ ![Download](https://api.bintray.com/packages/shabankamel/android/rxcurrentlocation/images/download.svg) ](https://bintray.com/shabankamel/android/rxcurrentlocation/_latestVersion)
```groovy
dependencies {
    implementation 'com.sha.kamel:rx-current-location:1.0.0@aar'
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

# Errors
if and error occureed it will be passed to `onFailureListener(OnFailure)`
### Types of expected errors:

 - [ ] GPS_DISABLED
 - [ ] NETWORK_DISABLED
 - [ ] PERMISSIONS_REQUIRED
 - [ ] UNKNOWN

#### Example
```java
.new RxCurrentLocation().onFailureListener(failMessage -> {  
      tv_location.setText(failMessage.getMessage()); // you can show error directly
      // or you can handle each error separately
      switch (failMessage.getError()){  
        case GPS_DISABLED:  
            // handle error  
           break;  
        case NETWORK_DISABLED:  
            // handle error  
           break;  
        case PERMISSIONS_REQUIRED:  
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
