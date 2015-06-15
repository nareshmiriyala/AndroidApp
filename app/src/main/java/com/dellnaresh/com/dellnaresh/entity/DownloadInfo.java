package com.dellnaresh.com.dellnaresh.entity;

import android.widget.ProgressBar;

import com.google.api.services.youtube.model.SearchResult;


public class DownloadInfo {
    private final static String TAG = DownloadInfo.class.getSimpleName();
    private final String mFilename;
    private final Integer mFileSize;
    private volatile DownloadState mDownloadState = DownloadState.NOT_STARTED;
    private volatile Integer mProgress;
    private volatile ProgressBar mProgressBar;
    private SearchResult searchResult;
    public DownloadInfo(SearchResult mSearchResult) {
        mFilename = "File ";
        mProgress = 0;
        mFileSize = 1000;
        mProgressBar = null;
        searchResult = mSearchResult;
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
        mProgressBar = progressBar;
    }

    public DownloadState getDownloadState() {
        return mDownloadState;
    }

    public void setDownloadState(DownloadState state) {
        mDownloadState = state;
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

    public enum DownloadState {
        NOT_STARTED,
        QUEUED,
        DOWNLOADING,
        COMPLETE,
        FAILURE
    }
}