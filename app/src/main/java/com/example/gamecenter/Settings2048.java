package com.example.gamecenter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


public class Settings2048 extends AppCompatActivity {

    private ImageButton delete;
    private EditText name;
    private ListAdapter1 adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings2048);


        ActionBar actionBar;
        actionBar = getSupportActionBar();

        actionBar.setBackgroundDrawable(getDrawable(R.color.action));

        delete = findViewById(R.id.button);
        name = findViewById(R.id.name);

        this.adapter = new ListAdapter1(Settings2048.this,new ListOpenHelper(Settings2048.this));

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePlayer();
            }
        });

    }

    public void deletePlayer(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("ALERT");
        builder.setMessage("ARE YOU SURE YOU WANT TO DELETE THIS PLAYER")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int n) {
                        adapter.mDB.delete2048(name.getText().toString());
                        name.setText("");
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int n) {
                        name.setText("");
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();

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