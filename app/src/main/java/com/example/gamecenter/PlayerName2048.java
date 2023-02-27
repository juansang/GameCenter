package com.example.gamecenter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;


public class PlayerName2048 extends AppCompatActivity {

    private ImageButton nameButton;

    private EditText mEditWordView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player2048);

        mEditWordView = (EditText) findViewById(R.id.name);

        nameButton = findViewById(R.id.button);

        nameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mEditWordView.getText().length()>0){
                    Intent intent = new Intent(getApplicationContext(), Board2048.class);
                    intent.putExtra("playername", mEditWordView.getText().toString());
                    startActivity(intent);
                }
            }
        });


        ActionBar actionBar;
        actionBar = getSupportActionBar();
        actionBar.hide();
    }
}
