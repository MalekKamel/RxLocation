

# RxGallery
###  RxJava wrapper for Android Gallery & Camera.
### With RxGallery you get rid of Activity.onActivityResult() and receive the result on the call site. Also,  no runtime permissions are required

# Features

 - [ ] Easy-to-use APIs
 - [ ] Results are delivered at the call site not at Activity.onActivityResult 
 - [ ] Handle all Camera and Gallery with the same APIs.
 - [ ]  Handle runtime permissions.

# Installation
[ ![Download](https://api.bintray.com/packages/shabankamel/android/RxGallery/images/download.svg) ](https://bintray.com/shabankamel/android/RxGallery/_latestVersion)
```groovy
dependencies {
    implementation 'com.sha.kamel:rx-gallery:1.0.0@aar'
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
## Pick Image
```java
     new RxGallery()
         .image(fragmentActivity, )
         .subscribe(result -> {
           // Handle logic here
         };
```
##  Pick Multiple Images
```java
     new RxGallery()
     .multipleImages(fragmentActivity)
         .subscribe(result -> {
           // Handle logic here
         };
```
## Pick Video
```java
     new RxGallery()
          .video(fragmentActivity)
          .subscribe(result -> {
             // Handle logic here
          };
```
## Pick Multiple Videos
```java
     new RxGallery()
         .multipleVideos(fragmentActivity)
         .subscribe(result -> {
            // Handle logic here
         };
```
## Pick Audio
```java
     new RxGallery()
          .audio(fragmentActivity, MimeType.VIDEO, MimeType.AUDIO)
          .subscribe(result -> {
             // Handle logic here
          };
```
## Pick Multiple Audio Files
```java
     new RxGallery()
           .multipleAudio(fragmentActivity, MimeType.VIDEO, MimeType.AUDIO)
           .subscribe(result -> {
              // Handle logic here
           };
```

## Request By Type
#### Yo can request any type by 
```java
     new RxGallery(fragmentActivity)
         .requestByType()
         .subscribe(result -> {
            // Handle logic here
         };
```

## Request Multiple By Type
```java
     new RxGallery(fragmentActivity)
           .requestMultipleByType()
           .subscribe(result -> {
             // Handle logic here
           };
```

### See 'app' module for the full code.

# License

## Apache license 2.0
