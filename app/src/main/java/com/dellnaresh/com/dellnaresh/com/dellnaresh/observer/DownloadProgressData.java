package com.dellnaresh.com.dellnaresh.com.dellnaresh.observer;

import java.util.Observable;

/**
 * Created by NARESHM on 24/03/2015.
 */
public class DownloadProgressData extends Observable {
    private double downloadProgress;
    private boolean downloadFailure;

    public DownloadProgressData() {
    }
    public void progressChanged(){
        setChanged();
        notifyObservers();
    }

    public void setDownloadProgress(double downloadProgress){
        this.downloadProgress=downloadProgress;
    }

    public double getDownloadProgress() {
        return downloadProgress;
    }

    public boolean isDownloadFailure() {
        return downloadFailure;
    }

    public void setDownloadFailure(boolean downloadFailure) {
        this.downloadFailure = downloadFailure;
    }
}
