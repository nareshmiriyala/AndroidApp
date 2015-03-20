package com.dellnaresh.youtubeandroidapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dellnaresh.adapters.CustomListAdapter;
import com.dellnaresh.com.dellnaresh.asynctasks.DownloadFileFromURL;
import com.google.api.services.youtube.model.SearchResult;
import com.youtube.indianmovies.data.Search;

import java.util.List;


public class MainActivity extends ActionBarActivity {
    List<com.google.api.services.youtube.model.SearchResult> searchResults = null;
    private EditText jEditText;
    private ListView jListView;
    private Search search;

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
        String mTAG = "myAsyncTask";

        @Override
        protected void onPreExecute() {
            TextView output = (TextView) findViewById(R.id.output);
            output.setText("Loading....");
        }

        @Override
        protected String doInBackground(String... arg) {
            Log.d(mTAG, "Just started doing stuff in asynctask");

            searchResults = search.find(arg[0]);
            runOnUiThread(new Thread() {
                public void run() {
                    TextView output = (TextView) findViewById(R.id.output);
                    output.setText("Search Completed");
                }
            });

            return "OK";
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d(mTAG, "Inside onPostExecute");
            CustomListAdapter imageListAdapter = new CustomListAdapter(MainActivity.this, searchResults);
            jListView.setAdapter(imageListAdapter);
            jListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                    Object o = jListView.getItemAtPosition(position);
                    SearchResult searchResult = (SearchResult) o;
                    Toast.makeText(MainActivity.this, "Downloading:" + " " + searchResult.getSnippet().getTitle(),
                            Toast.LENGTH_LONG).show();
                    new DownloadFileFromURL(MainActivity.this).execute(searchResult);

                }
            });

            TextView output = (TextView) findViewById(R.id.output);
            output.setText("Result of the computation is: " + result);
        }

    }

}
