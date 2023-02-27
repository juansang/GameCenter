package com.example.gamecenter;


import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class Rank2048 extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ListAdapter1 mAdapter;

    private ListOpenHelper mDB;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stats2048);


        mDB = new ListOpenHelper(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        mAdapter = new ListAdapter1 (this, mDB);

        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ActionBar actionBar;
        actionBar = getSupportActionBar();

        actionBar.setBackgroundDrawable(getDrawable(R.color.action));


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.options_menu_play_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        String text = item.toString();
        if (text.length() > 0) {
            if(text.equals("HOME")){
                startActivity(new Intent(this, Home2048.class));
            }
            else if(text.equals("RANKING")) {
                startActivity(new Intent(this, Rank2048.class));
            }
            else if(text.equals("SETTINGS")) {
                startActivity(new Intent(this, Settings2048.class));
            }
            else if(text.equals("HELP")) {
                startActivity(new Intent(this, Help2048.class));
            }
        }
        return true;
    }

}