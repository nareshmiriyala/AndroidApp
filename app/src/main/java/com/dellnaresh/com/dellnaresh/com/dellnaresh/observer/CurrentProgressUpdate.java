package com.dellnaresh.com.dellnaresh.com.dellnaresh.observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by NARESHM on 24/03/2015.
 */
public class CurrentProgressUpdate implements Observer {
    Logger logger = LoggerFactory.getLogger(CurrentProgressUpdate.class);
    private double downloadProgress;
    private boolean downloadFailure = false;

    public CurrentProgressUpdate(Observable observable) {
        observable.addObserver(this);
    }

    @Override
    public void update(Observable observable, Object data) {
        if (observable instanceof DownloadProgressData) {
            DownloadProgressData downloadProgressData = (DownloadProgressData) observable;
            this.downloadProgress = downloadProgressData.getDownloadProgress();
            this.downloadFailure = downloadProgressData.isDownloadFailure();
        }
    }

    public double getDownloadProgress() {
        return downloadProgress;
    }

    public boolean isDownloadFailure() {
        return downloadFailure;
    }

    @Override
    public String toString() {
        return "CurrentProgressUpdate{" +
                "downloadFailure=" + downloadFailure +
                ", downloadProgress=" + downloadProgress +
                '}';
    }
}
