package com.example.gamecenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class BoardLO extends AppCompatActivity {

    private Button fourBoard;
    private Button fiveBoard;
    private Button sixBoard;
    private Button sevenBoard;

    private String playerName;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_lightsout_layout);

        playerName = getIntent().getExtras().getString("player_name");

        fourBoard = (Button) findViewById(R.id.four);

        fourBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainLO.class);
                intent.putExtra("board", "4");
                intent.putExtra("player_name", playerName);
                startActivity(intent);
            }
        });

        fiveBoard = (Button) findViewById(R.id.five);

        fiveBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainLO.class);
                intent.putExtra("board", "5");
                intent.putExtra("player_name", playerName);
                startActivity(intent);
            }
        });

        sixBoard = (Button) findViewById(R.id.six);

        sixBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainLO.class);
                intent.putExtra("board", "6");
                intent.putExtra("player_name", playerName);
                startActivity(intent);
            }
        });

        sevenBoard = (Button) findViewById(R.id.seven);

        sevenBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainLO.class);
                intent.putExtra("board", "7");
                intent.putExtra("player_name", playerName);
                startActivity(intent);
            }
        });

        ActionBar actionBar;
        actionBar = getSupportActionBar();
        actionBar.hide();
    }

}