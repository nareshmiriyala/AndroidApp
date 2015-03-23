package com.dellnaresh.com.dellnaresh.asynctasks;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;

import com.dellnaresh.com.dellnaresh.entity.DownloadInfo;
import com.dellnaresh.youtubeandroidapp.MainActivity;
import com.youtube.downloader.DownloadJob;
import com.youtube.workerpool.WorkerPool;

import java.io.File;

import static com.dellnaresh.com.dellnaresh.entity.DownloadInfo.*;


/**
 * Simulate downloading a file, by increasing the progress of the FileInfo from 0 to size - 1.
 */
public class FileDownloadTask extends AsyncTask<Void, Integer, Void> {
    private static final String    TAG = FileDownloadTask.class.getSimpleName();
    final DownloadInfo mInfo;
    Context context;
    private Handler mHandler = new Handler();

    public FileDownloadTask(DownloadInfo info,Context context) {
        mInfo = info;
        this.context=context;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        mInfo.setProgress(values[0]);
        ProgressBar bar = mInfo.getProgressBar();
        if(bar != null) {
            bar.setProgress(mInfo.getProgress());
            bar.invalidate();
        }
    }
    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }


    public File getAlbumStorageDir(Context context) {
        if (isExternalStorageWritable()) {
            Log.e(TAG, "External directory writable");
        }
        // Get the directory for the app's private pictures directory.
        File file = new File(String.valueOf(context.getExternalFilesDir(
                Environment.DIRECTORY_PICTURES)));
        if (!file.exists()) {
            if (!file.mkdirs()) {
                Log.e(TAG, "Directory not created");
            }
        }
        Log.i(TAG, "Directory path " + file.getAbsolutePath());
        return file;
    }

        @Override
    protected Void doInBackground(Void... params) {
        Log.d(TAG, "Starting download for " + mInfo.getFilename());
        mInfo.setDownloadState(DownloadState.DOWNLOADING);

        WorkerPool.getInstance();
        final DownloadJob downloadJob = new DownloadJob("job");
        File directory = getAlbumStorageDir(context);
        downloadJob.setFileDownloadPath(directory.getAbsolutePath());
        downloadJob.setUrlToDownload("https://www.youtube.com/watch?v=" + mInfo.getSearchResult().getId().getVideoId());
        downloadJob.setTitle(mInfo.getSearchResult().getSnippet().getTitle());
        WorkerPool.deployJob(downloadJob);
            Thread updateProgress = new Thread() {
                public void run() {
                    while (mInfo.getProgress() < 100) {
//                        mProgressStatus = (int) (downloadJob.getDownloadProgress() * 100);
//                        // Update the progress bar
//                        mHandler.post(new Runnable() {
//                            public void run() {
//                                progressBar.setProgress(mProgressStatus);
//                            }
//                        });
                        publishProgress((int) (downloadJob.getDownloadProgress() * 100));
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }


                }
            };
            updateProgress.start();
//
//            Thread updateProgress = new Thread() {
//                public void run() {
//                    try {
//                        Thread.sleep(500);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    publishProgress((int) (downloadJob.getDownloadProgress() * 100));
//                }
//            };
//            updateProgress.start();
//        for (int i = 0; i <= mInfo.getFileSize(); ++i) {
//
//        }
        mInfo.setDownloadState(DownloadState.COMPLETE);
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        mInfo.setDownloadState(DownloadState.COMPLETE);
    }

    @Override
    protected void onPreExecute() {
        mInfo.setDownloadState(DownloadState.DOWNLOADING);
    }

}