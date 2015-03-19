package com.dellnaresh.youtubeandroidapp;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.api.services.youtube.model.SearchResult;
import com.youtube.indianmovies.data.Search;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    private Button jButton;
    private EditText jEditText;
    private ListView jListView;
    List<String> stringList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        jButton = (Button) findViewById(R.id.button);
        jEditText = (EditText) findViewById(R.id.editText);
        jListView = (ListView) findViewById(R.id.listView);


    }
    //Method called on clicking button
    public void startTask(View view) {
        SearchAsyncTask mTask = new SearchAsyncTask();
        mTask.execute(jEditText.getText().toString(),"10","Hello world");
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
            TextView output = (TextView)findViewById(R.id.output);
            output.setText("Loading....");
        }
        @Override
        protected String doInBackground(String... arg) {
            Log.d(mTAG, "Just started doing stuff in asynctask");
            try {
                Thread.sleep(5000);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.d(mTAG, "I got "+arg.length+" arguments and they are: ");
            String result = null;
            for (int i = 0 ; i < arg.length ; i++ ) {
                result = arg[i]+",";
                Log.d(mTAG, (i+1)+" => "+arg[i]);
            }
            Search search=new Search();
            search.setNumberOfVideosReturned(10);
            List<com.google.api.services.youtube.model.SearchResult> searchResults = search.find(arg[0]);

            if(searchResults!=null) {
                for (SearchResult searchResult : searchResults) {
                    stringList.add(searchResult.getSnippet().getTitle());
                }
            }

            runOnUiThread(new Thread() {
                public void run() {
                    TextView output = (TextView) findViewById(R.id.output);
                    output.setText("I am done");
                }
            });

            try {
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }

            return result;
        }
        @Override
        protected void onPostExecute(String result) {
            Log.d(mTAG, "Inside onPostExecute");
            ArrayAdapter adapter = new ArrayAdapter(MainActivity.this,
                    android.R.layout.simple_list_item_1, stringList);
            jListView.setAdapter(adapter);
            TextView output = (TextView) findViewById(R.id.output);
            output.setText("Result of the computation is: "+result);
        }
    }

}
