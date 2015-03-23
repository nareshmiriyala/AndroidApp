package com.dellnaresh.com.dellnaresh.asynctasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.api.services.youtube.model.SearchResult;
import com.youtube.downloader.DownloadJob;
import com.youtube.workerpool.WorkerPool;

import java.io.File;

/**
 * Created by nareshm on 20/03/2015.
 */
public class DownloadFileFromURL extends AsyncTask<SearchResult, Integer, String> {
    Context context;

    String mTAG = "DownloadFileFromURL";
    private int mProgressStatus = 0;
    private Handler mHandler = new Handler();
    private ProgressBar progressBar;


    public DownloadFileFromURL(Context context, ProgressBar mProgressBar) {
        this.context = context;
        this.progressBar = mProgressBar;
    }

    /**
     * Before starting background thread
     * Show Progress Bar Dialog
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d(mTAG, "Just started doing download in asynctask");
    }

    @Override
    protected String doInBackground(SearchResult... params) {
        WorkerPool.getInstance();
        final DownloadJob downloadJob = new DownloadJob("job");
        File directory = getAlbumStorageDir(context);
        downloadJob.setFileDownloadPath(directory.getAbsolutePath());
        downloadJob.setUrlToDownload("https://www.youtube.com/watch?v=" + params[0].getId().getVideoId());
        downloadJob.setTitle(params[0].getSnippet().getTitle());
        WorkerPool.deployJob(downloadJob);

        Thread updateProgress = new Thread() {
            public void run() {
                while (mProgressStatus < 100) {
                    mProgressStatus = (int) (downloadJob.getDownloadProgress() * 100);
                    // Update the progress bar
                    mHandler.post(new Runnable() {
                        public void run() {
                            progressBar.setProgress(mProgressStatus);
                        }
                    });
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


            }
        };
        updateProgress.start();

        try {
            WorkerPool.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "SUCCESS";
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }


    public File getAlbumStorageDir(Context context) {
        if (isExternalStorageWritable()) {
            Log.e(mTAG, "External directory writable");
        }
        // Get the directory for the app's private pictures directory.
        File file = new File(String.valueOf(context.getExternalFilesDir(
                Environment.DIRECTORY_PICTURES)));
        if (!file.exists()) {
            if (!file.mkdirs()) {
                Log.e(mTAG, "Directory not created");
            }
        }
        Log.i(mTAG, "Directory path "+file.getAbsolutePath());
        return file;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d(mTAG, "Inside onPostExecute:Download done to file directory:" + result);
        if (result != null)
            Toast.makeText(context, "Download error: " + result, Toast.LENGTH_LONG).show();
        else
            Toast.makeText(context, "File downloaded", Toast.LENGTH_SHORT).show();
    }
}
