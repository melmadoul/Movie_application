package com.example.mohamed.movi_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class Detailed_activity extends AppCompatActivity {

    SharedPreferences sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.detailed_container,new Detailed_activityFragment()).commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.action_popular)
        {
            SharedPreferences.Editor editor= sharedPref.edit();
            editor.putString(getString(R.string.choice),"popular").commit();
            startActivity(new Intent(this,MainActivity.class));
            return true;
        }
        else if(id==R.id.action_top_rated)
        {
            SharedPreferences.Editor editor= sharedPref.edit();
            editor.putString(getString(R.string.choice),"top_rated").commit();
            startActivity(new Intent(this,MainActivity.class));
            return true;
        }
        else if(id==R.id.action_favorite)
        {
            SharedPreferences.Editor editor= sharedPref.edit();
            editor.putString(getString(R.string.choice),"favorite").commit();
            startActivity(new Intent(this,MainActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
