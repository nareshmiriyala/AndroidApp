package com.dellnaresh.com.dellnaresh.asynctasks;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.google.api.services.youtube.model.SearchResult;
import com.youtube.downloader.DownloadJob;
import com.youtube.workerpool.WorkerPool;

import java.io.File;

/**
 * Created by nareshm on 20/03/2015.
 */
public class DownloadFileFromURL extends AsyncTask<SearchResult, String, File> {
    Context context;

    String mTAG = "DownloadFileFromURL";

    public DownloadFileFromURL(Context context) {
        this.context = context;
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
    protected File doInBackground(SearchResult... params) {
        WorkerPool.getInstance();
        DownloadJob downloadJob = new DownloadJob("job");
        //set the path where we want to save the file
        //in this case, going to save it on the root directory of the
        //sd card.
//                    File SDCardRoot = Environment.getExternalStorageDirectory();
        //     ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = getAlbumStorageDir(context, "videos");
        downloadJob.setFileDownloadPath(directory.getAbsolutePath());
        downloadJob.setUrlToDownload("https://www.youtube.com/watch?v=" + params[0].getId().getVideoId());
        downloadJob.setTitle(params[0].getSnippet().getTitle());
        WorkerPool.deployJob(downloadJob);

        return directory;
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public File getAlbumStorageDir(Context context, String albumName) {
        if(isExternalStorageWritable()){
            Log.e(mTAG,"External directory writable");
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
    protected void onPostExecute(File result) {
        Log.d(mTAG, "Inside onPostExecute:Download done to file directory:" + result.getAbsolutePath());
    }
}
