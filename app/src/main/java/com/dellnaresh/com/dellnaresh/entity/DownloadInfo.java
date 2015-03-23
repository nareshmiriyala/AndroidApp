package com.dellnaresh.com.dellnaresh.entity;

import android.util.Log;
import android.widget.ProgressBar;

import com.google.api.services.youtube.model.SearchResult;


public class DownloadInfo {
    private final static String TAG = DownloadInfo.class.getSimpleName();
    public enum DownloadState {
        NOT_STARTED,
        QUEUED,
        DOWNLOADING,
        COMPLETE
    }
    private volatile DownloadState mDownloadState = DownloadState.NOT_STARTED;
    private final String mFilename;
    private volatile Integer mProgress;
    private final Integer mFileSize;
    private volatile ProgressBar mProgressBar;
    private SearchResult searchResult;

    public DownloadInfo(String filename, Integer size,SearchResult mSearchResult){
        mFilename = filename;
        mProgress = 0;
        mFileSize = size;
        mProgressBar = null;
        searchResult=mSearchResult;
    }

    public SearchResult getSearchResult() {
        return searchResult;
    }

    public void setSearchResult(SearchResult searchResult) {
        this.searchResult = searchResult;
    }

    public ProgressBar getProgressBar() {
        return mProgressBar;
    }
    public void setProgressBar(ProgressBar progressBar) {
        Log.d(TAG, "setProgressBar " + mFilename + " to " + progressBar);
        mProgressBar = progressBar;
    }

    public void setDownloadState(DownloadState state) {
        mDownloadState = state;
    }
    public DownloadState getDownloadState() {
        return mDownloadState;
    }

    public Integer getProgress() {
        return mProgress;
    }

    public void setProgress(Integer progress) {
        this.mProgress = progress;
    }

    public Integer getFileSize() {
        return mFileSize;
    }

    public String getFilename() {
        return mFilename;
    }
}