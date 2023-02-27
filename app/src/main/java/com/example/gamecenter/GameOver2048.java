package com.example.gamecenter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class GameOver2048 extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_over2048);

        TextView score = findViewById(R.id.score);
        TextView time = findViewById(R.id.time);

        Button go_home = findViewById(R.id.home);
        Button view_ranking = findViewById(R.id.ranking);

        score.setText("Score: " + getIntent().getExtras().getString("score"));
        time.setText("Time: " + getIntent().getExtras().getString("time"));

        go_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GameOver2048.this, Home2048.class));
            }
        });

        view_ranking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GameOver2048.this, Rank2048.class));
            }
        });

        ActionBar actionBar;
        actionBar = getSupportActionBar();

        actionBar.hide();
    }
}
