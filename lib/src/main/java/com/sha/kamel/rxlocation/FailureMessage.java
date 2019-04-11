package com.sha.kamel.rxlocation;

import android.text.TextUtils;

public class FailureMessage {
    private Error error;
    private String msg;

    public FailureMessage(Error error, String msg) {
        this.error = error;
        this.msg = msg;
    }

    public enum Error {
        GPS_DISABLED("GPS is disabled."),
        UNKNOWN("Unknown error."),
        NETWORK_DISABLED("No network available.");

        private String message;

        Error(String msg) {
            this.message = msg;
        }

        @Override
        public String toString() {
            return message;
        }
    }

    public Error getError() {
        return error;
    }

    public String getMessage() {
        return TextUtils.isEmpty(msg) ? error.toString() : msg;
    }
}

