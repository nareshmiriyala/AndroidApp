package com.dellnaresh.youtubeandroidapp;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.youtube.indianmovies.data.Search;

import java.util.List;


public class MainActivity extends ActionBarActivity {
    private Button jButton;
    private EditText jEditText;
    private ListView jListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        jButton= (Button) findViewById(R.id.button);
        jEditText= (EditText) findViewById(R.id.editText);
        jListView= (ListView) findViewById(R.id.listView);
        jButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Search search=new Search();
                List<com.google.api.services.youtube.model.SearchResult> searchResults = search.find(String.valueOf(jEditText));
            }
        });
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
}
