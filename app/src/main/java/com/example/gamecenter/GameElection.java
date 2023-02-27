package com.example.gamecenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class GameElection extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_election_layout);

        Button game1 = findViewById(R.id.two_zero_four_eight);
        Button game2 = findViewById(R.id.lights);

        game1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GameElection.this, Home2048.class));
            }
        });

        game2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GameElection.this, HomeLO.class));
            }
        });

        ActionBar actionBar;
        actionBar = getSupportActionBar();
        actionBar.hide();
    }
}