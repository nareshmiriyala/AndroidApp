package com.dellnaresh.youtubeandroidapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dellnaresh.adapters.CustomListAdapter;
import com.dellnaresh.com.dellnaresh.entity.DownloadInfo;
import com.google.api.services.youtube.model.SearchResult;
import com.youtube.indianmovies.data.Search;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    // declare the dialog as a member field of your activity
    private static final int PROGRESS = 0x1;
    private List<DownloadInfo> downloadInfoList = null;
    private EditText jEditText;
    private ListView jListView;
    private Search search;
    private ProgressBar mProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        jEditText = (EditText) findViewById(R.id.editText);
        jListView = (ListView) findViewById(R.id.listView);
        search = new Search();
        Search.setNumberOfVideosReturned(10);
    }

    //Method called on clicking button
    public void startTask(View view) {
        SearchAsyncTask mTask = new SearchAsyncTask();
        mTask.execute(jEditText.getText().toString(), "10", "Hello world");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class SearchAsyncTask extends AsyncTask<String, Integer, String> {
        final String mTAG = "myAsyncTask";

        @Override
        protected void onPreExecute() {
            TextView output = (TextView) findViewById(R.id.output);
            output.setText("Loading....");
        }

        @Override
        protected String doInBackground(String... arg) {
            Log.d(mTAG, "Just started doing stuff in asynctask");
            downloadInfoList = new ArrayList<>();
            List<SearchResult> searchResults = search.find(arg[0]);
            for (SearchResult searchResult : searchResults) {
                DownloadInfo downloadInfo = new DownloadInfo("File ", 1000, searchResult);
                downloadInfoList.add(downloadInfo);
            }
            return "OK";
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d(mTAG, "Inside onPostExecute");
            CustomListAdapter customListAdapter = new CustomListAdapter(MainActivity.this, R.id.listView, downloadInfoList,MainActivity.this);
            jListView.setAdapter(customListAdapter);
            TextView output = (TextView) findViewById(R.id.output);
            output.setText("Result of the computation is: " + result);
        }

    }

}
