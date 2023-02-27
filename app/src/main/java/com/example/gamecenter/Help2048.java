package com.example.gamecenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class Help2048 extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help2048);

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
