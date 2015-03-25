package com.dellnaresh.com.dellnaresh.asynctasks;

import com.dellnaresh.com.dellnaresh.com.dellnaresh.observer.DownloadProgressData;
import com.youtube.workerpool.WorkerThread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Created by nareshm on 8/03/2015.
 */
public class DownloadJob extends WorkerThread {
    private static final Logger logger = LoggerFactory.getLogger(DownloadJob.class);
    private String urlToDownload;
    private String fileDownloadPath;
    private String title;
    private DownloadFile downloadFile;
    private DownloadProgressData downloadProgressData;

    public DownloadJob(String s, String urlToDownload, String fileDownloadPath, String title,DownloadProgressData downloadProgressData) {
        super(s);
        this.urlToDownload = urlToDownload;
        this.fileDownloadPath = fileDownloadPath;
        this.title = title;
        this.downloadFile = new DownloadFile(downloadProgressData);
        this.downloadProgressData=downloadProgressData;
    }

    public DownloadJob(String s) {
        super(s);
    }

    public DownloadJob(String s, DownloadProgressData downloadProgressData) {
        super(s);
        this.downloadFile = new DownloadFile(downloadProgressData);
        this.downloadProgressData=downloadProgressData;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setFileDownloadPath(String fileDownloadPath) {
        this.fileDownloadPath = fileDownloadPath;
    }

    public void setUrlToDownload(String urlToDownload) {
        this.urlToDownload = urlToDownload;
    }

    @Override
    public void processCommand() {
        logger.info("Downloading ULR:" + this.urlToDownload + " to path:" + this.fileDownloadPath);
        try {
            downloadFile.run(this.urlToDownload, new File(fileDownloadPath), this.title);
        } catch (Exception e) {
            logger.warn("Can't Download File");
            downloadProgressData.setDownloadFailure(true);
            downloadProgressData.progressChanged();
        }

    }

}
