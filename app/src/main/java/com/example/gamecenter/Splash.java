package com.example.gamecenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class Splash extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        ImageView mandoIV = findViewById(R.id.mando);
        ImageView crackIV = findViewById(R.id.crack);
        ImageView textIV = findViewById(R.id.texto);
        ImageView squareIV = findViewById(R.id.square);

        crackIV.setVisibility(View.INVISIBLE);
        textIV.setVisibility(View.INVISIBLE);

        Animation rotate = AnimationUtils.loadAnimation(this, R.anim.rotation_splash);
        mandoIV.startAnimation(rotate);

        rotate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Animation shrink = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shrink_splash);
                textIV.setVisibility(View.VISIBLE);
                textIV.startAnimation(shrink);

                shrink.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        crackIV.setVisibility(View.VISIBLE);
                        Animation appear = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.appear_splash);
                        squareIV.startAnimation(appear);
                        appear.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                startActivity(new Intent(Splash.this,GameElection.class));
                                Splash.this.finish();
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {
                            }
                        });
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        ActionBar actionBar;
        actionBar = getSupportActionBar();
        actionBar.hide();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ImageView mandoIV = findViewById(R.id.mando);
        mandoIV.clearAnimation();

        ImageView crackIV = findViewById(R.id.crack);
        crackIV.clearAnimation();

        ImageView textIV = findViewById(R.id.texto);
        textIV.clearAnimation();
    }

}