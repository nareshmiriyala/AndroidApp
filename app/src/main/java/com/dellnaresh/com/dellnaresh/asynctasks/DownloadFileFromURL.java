package com.dellnaresh.com.dellnaresh.asynctasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.PowerManager;
import android.util.Log;
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
    private PowerManager.WakeLock mWakeLock;
    private ProgressDialog mProgressDialog;

    public DownloadFileFromURL(Context context, ProgressDialog mProgressDialog) {
        this.context = context;
        this.mProgressDialog = mProgressDialog;
    }

    /**
     * Before starting background thread
     * Show Progress Bar Dialog
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d(mTAG, "Just started doing download in asynctask");

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                getClass().getName());
        mWakeLock.acquire();
        mProgressDialog.show();
    }

    @Override
    protected String doInBackground(SearchResult... params) {
        WorkerPool.getInstance();
        DownloadJob downloadJob = new DownloadJob("job");
        File directory = getAlbumStorageDir(context);
        downloadJob.setFileDownloadPath(directory.getAbsolutePath());
        downloadJob.setUrlToDownload("https://www.youtube.com/watch?v=" + params[0].getId().getVideoId());
        downloadJob.setTitle(params[0].getSnippet().getTitle());
        WorkerPool.deployJob(downloadJob);
        do {
            publishProgress(((int) downloadJob.getDownloadProgress()));
        } while (downloadJob.getDownloadProgress() <= 100);
        return "SUCCESS";
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);
        // if we get here, length is known, now set indeterminate to false
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setMax(100);
        mProgressDialog.setProgress(progress[0]);
    }


    public File getAlbumStorageDir(Context context) {
        if (isExternalStorageWritable()) {
            Log.e(mTAG, "External directory writable");
        }
        // Get the directory for the app's private pictures directory.
        File file = new File(String.valueOf(context.getExternalFilesDir(
                Environment.DIRECTORY_PICTURES)));
        if (!file.mkdirs()) {
            Log.e(mTAG, "Directory not created");
        }
        return file;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d(mTAG, "Inside onPostExecute:Download done to file directory:" + result);
        try {
            WorkerPool.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mWakeLock.release();
        mProgressDialog.dismiss();
        if (result != null)
            Toast.makeText(context, "Download error: " + result, Toast.LENGTH_LONG).show();
        else
            Toast.makeText(context, "File downloaded", Toast.LENGTH_SHORT).show();
    }
}
