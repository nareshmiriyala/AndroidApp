package com.dellnaresh.youtubeandroidapp;

import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by nareshm on 25/03/2015.
 */
public class MainActivityTest extends
        ActivityInstrumentationTestCase2<MainActivity> {
    private MainActivity mainActivity;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    public MainActivityTest(Class<MainActivity> activityClass) {
        super(activityClass);
    }

    public MainActivityTest(Class<MainActivity> activityClass, MainActivity mainActivity) {
        super(activityClass);
        this.mainActivity = mainActivity;
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mainActivity = getActivity();
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        setActivityInitialTouchMode(true);
    }

    @UiThreadTest
    public void testSearch() throws Exception {

        Button button = (Button) mainActivity.findViewById(R.id.button);
        EditText editText = (EditText) mainActivity.findViewById(R.id.editText);
        editText.setText("pawan");
        button.performClick();
    }
}

