package com.example.gamecenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class Board2048 extends AppCompatActivity {
    private Button b3x3;
    private Button b4x4;
    private Button b5x5;
    private Button b6x6;

    private String playerName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_2048_layout);
        playerName = getIntent().getExtras().getString("playername");

        b3x3 = findViewById(R.id.three);
        b4x4 = findViewById(R.id.four);
        b5x5 = findViewById(R.id.five);
        b6x6 = findViewById(R.id.six);

        b3x3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Main2048.class);
                intent.putExtra("board", "3");
                intent.putExtra("playername", playerName);
                startActivity(intent);
            }
        });

        b4x4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Main2048.class);
                intent.putExtra("board", "4");
                intent.putExtra("playername", playerName);
                startActivity(intent);
            }
        });

        b5x5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Main2048.class);
                intent.putExtra("board", "5");
                intent.putExtra("playername", playerName);
                startActivity(intent);
            }
        });

        b6x6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Main2048.class);
                intent.putExtra("board", "6");
                intent.putExtra("playername", playerName);
                startActivity(intent);
            }
        });

        ActionBar actionBar;
        actionBar = getSupportActionBar();
        actionBar.hide();


    }
}