package com.sha.kamel.rxlocation;

import com.google.android.gms.location.LocationRequest;

public class UpdateQuality {
    private long interval = 0;
    private long fastestUpdateInterval = (long) 2 * 1000;
    private int priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY;

    public long getInterval() {
        return interval;
    }

    /**
     * Set interval of LocationRequest
     * default: 0
     * @param interval in milliseconds
     * @return thi
     */
    public UpdateQuality interval(long interval) {
        this.interval = interval;
        return this;
    }

    public long getFastestUpdateInterval() {
        return fastestUpdateInterval;
    }

    /**
     * Set fastest update interval of LocationRequest
     * default: 2 * 1000
     * @param interval in milliseconds
     * @return thi
     */
    public UpdateQuality fastestUpdateInterval(long interval) {
        this.fastestUpdateInterval = interval;
        return this;
    }

    public int getPriority() {
        return priority;
    }

    /**
     * Set priority of LocationRequest
     * default: LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
     * @param priority for example: {@code LocationRequest.PRIORITY_LOW_POWER }
     * @return thi
     */
    public UpdateQuality priority(int priority) {
        this.priority = priority;
        return this;
    }
}
