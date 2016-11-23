package com.example.mohamed.movi_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    String user_choice;
    SharedPreferences sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        user_choice=sharedPref.getString(getString(R.string.choice),"popular");

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container,new MainActivityFragment(sharedPref)).commit();
        }
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
        else if(id==R.id.action_popular)
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
