package com.example.gamecenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class HomeLO extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_lightsout);

        ImageView logo = (ImageView) findViewById(R.id.imageView);
        ImageButton start = findViewById(R.id.start_game);
        ImageButton stats = findViewById(R.id.stats);
        ImageButton configuration = findViewById(R.id.config);
        ImageButton help = findViewById(R.id.help);


        start.setVisibility(View.INVISIBLE);
        stats.setVisibility(View.INVISIBLE);
        configuration.setVisibility(View.INVISIBLE);
        help.setVisibility(View.INVISIBLE);

        Animation shrink = AnimationUtils.loadAnimation(this, R.anim.shrink_lightsout);
        logo.startAnimation(shrink);

        shrink.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                start.setVisibility(View.VISIBLE);
                stats.setVisibility(View.VISIBLE);
                configuration.setVisibility(View.VISIBLE);
                help.setVisibility(View.VISIBLE);

                Animation app1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.appear_lightsout);
                start.startAnimation(app1);

                Animation app2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.appear_lightsout);
                stats.startAnimation(app2);

                Animation app3 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.appear_lightsout);
                configuration.startAnimation(app3);

                Animation app4 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.appear_lightsout);
                help.startAnimation(app4);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeLO.this, PlayerNameLO.class));
            }
        });

        stats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeLO.this, RankLO.class));
                HomeLO.this.finish();
            }
        });

        configuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeLO.this, SettingsLO.class));
                HomeLO.this.finish();
            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeLO.this, HelpLO.class));
                HomeLO.this.finish();
            }
        });

        ActionBar actionBar;
        actionBar = getSupportActionBar();

        actionBar.setBackgroundDrawable(getDrawable(R.color.black));

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