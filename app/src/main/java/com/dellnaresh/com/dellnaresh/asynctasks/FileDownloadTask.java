package com.dellnaresh.com.dellnaresh.asynctasks;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dellnaresh.com.dellnaresh.com.dellnaresh.observer.CurrentProgressUpdate;
import com.dellnaresh.com.dellnaresh.com.dellnaresh.observer.DownloadProgressData;
import com.dellnaresh.com.dellnaresh.entity.DownloadInfo;
import com.youtube.workerpool.WorkerPool;

import java.io.File;

import static com.dellnaresh.com.dellnaresh.entity.DownloadInfo.DownloadState;


/**
 * Simulate downloading a file, by increasing the progress of the FileInfo from 0 to size - 1.
 */
public class FileDownloadTask extends AsyncTask<Void, Integer, DownloadState> {
    private static final String TAG = FileDownloadTask.class.getSimpleName();
    private final DownloadInfo mInfo;
    private final Context context;
    private final Activity activity;
    private Handler mHandler = new Handler();

    public FileDownloadTask(DownloadInfo info, Context context, Activity activity) {
        mInfo = info;
        this.context = context;
        this.activity = activity;
    }


    @Override
    protected void onProgressUpdate(Integer... values) {
        mInfo.setProgress(values[0]);
        ProgressBar bar = mInfo.getProgressBar();
        if (bar != null) {
            bar.setProgress(mInfo.getProgress());
            bar.invalidate();
        }
    }

    /* Checks if external storage is available for read and write */
    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }


    private File getAlbumStorageDir(Context context) {
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
    protected DownloadState doInBackground(Void... params) {
        Log.d(TAG, "Starting download for " + mInfo.getFilename());
        mInfo.setDownloadState(DownloadState.DOWNLOADING);
        DownloadProgressData downloadProgressData = new DownloadProgressData();
        final CurrentProgressUpdate currentProgressUpdate = new CurrentProgressUpdate(downloadProgressData);
        WorkerPool.getInstance();
        final DownloadJob downloadJob = new DownloadJob(downloadProgressData);
        File directory = getAlbumStorageDir(context);
        downloadJob.setFileDownloadPath(directory.getAbsolutePath());
        downloadJob.setUrlToDownload("https://www.youtube.com/watch?v=" + mInfo.getSearchResult().getId().getVideoId());
        downloadJob.setTitle(mInfo.getSearchResult().getSnippet().getTitle());
        WorkerPool.deployJob(downloadJob);

        Thread updateProgress = new Thread() {
            public void run() {
                while (mInfo.getProgress() < 100) {
                    if (currentProgressUpdate.isDownloadFailure()) {
                        Log.w(TAG, "Cant Download Video");
                        mInfo.setDownloadState(DownloadState.FAILURE);
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, mInfo.getSearchResult().getSnippet().getTitle() + " Downloading:" + " " + DownloadState.FAILURE,
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                        break;
                    }
                    int value = (int) (currentProgressUpdate.getDownloadProgress() * 100);
                    Log.i(TAG, downloadJob.getTitle() + ":Progress Update Value:" + value);
                    publishProgress(value);
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        updateProgress.start();

        if (mInfo.getProgress() >= 100) {
            mInfo.setDownloadState(DownloadState.COMPLETE);
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, mInfo.getSearchResult().getSnippet().getTitle() + " Downloading:" + " " + DownloadState.COMPLETE,
                            Toast.LENGTH_LONG).show();
                }
            });
        }
        return mInfo.getDownloadState();
    }

    @Override
    protected void onPostExecute(DownloadState result) {
        Toast.makeText(context, mInfo.getSearchResult().getSnippet().getTitle() + " Downloading:" + " " + result,
                Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPreExecute() {
        mInfo.setDownloadState(DownloadState.DOWNLOADING);
        mInfo.setProgress(0);
    }

}