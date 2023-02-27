package com.example.gamecenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class Home2048 extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_2048_layout);

        TextView title = findViewById(R.id.textView);
        Button start = findViewById(R.id.start_game);
        Button stats = findViewById(R.id.stats);
        Button configuration = (Button) findViewById(R.id.config);
        Button help = (Button) findViewById(R.id.help);

        Animation app1 = AnimationUtils.loadAnimation(this, R.anim.appear_2048);
        title.startAnimation(app1);

        Animation trans1 = AnimationUtils.loadAnimation(this, R.anim.translation1_2048);
        start.startAnimation(trans1);

        Animation trans2 = AnimationUtils.loadAnimation(this, R.anim.translation2_2048);
        stats.startAnimation(trans2);

        Animation trans3 = AnimationUtils.loadAnimation(this, R.anim.translation1_2048);
        configuration.startAnimation(trans3);

        Animation trans4 = AnimationUtils.loadAnimation(this, R.anim.translation2_2048);
        help.startAnimation(trans4);


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home2048.this, PlayerName2048.class));
                Home2048.this.finish();
            }
        });

        stats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home2048.this, Rank2048.class));
                Home2048.this.finish();
            }
        });

        configuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home2048.this, Settings2048.class));
                Home2048.this.finish();
            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home2048.this, Help2048.class));
                Home2048.this.finish();
            }
        });

        ActionBar actionBar;
        actionBar = getSupportActionBar();

        actionBar.setBackgroundDrawable(getDrawable(R.color.yellow));

    }
    @Override
    protected void onPause() {
        super.onPause();
        TextView title = findViewById(R.id.textView);
        title.clearAnimation();

        Button start = findViewById(R.id.start_game);
        start.clearAnimation();

        Button stats = findViewById(R.id.stats);
        stats.clearAnimation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.game_selector_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        String text = item.toString();
        if (text.length() > 0) {
            startActivity(new Intent(this, GameElection.class));
        }
        return true;
    }
}