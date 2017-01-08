package com.map.pathfinder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Husnain on 5/7/2016.
 */
public class Home extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
    }

    public void findDriver(View v) {
        startActivity(new Intent(Home.this, FindDriver.class));
    }

    public void useFeature(View v) {
        startActivity(new Intent(Home.this, MainActivity.class));
    }
}
