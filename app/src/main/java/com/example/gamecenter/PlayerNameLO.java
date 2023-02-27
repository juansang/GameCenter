package com.example.gamecenter;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;


public class PlayerNameLO extends AppCompatActivity {

    private ImageButton nameButton;
    private EditText mEditWordView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playerlo);

        mEditWordView = (EditText) findViewById(R.id.name);

        nameButton = findViewById(R.id.button);


        nameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mEditWordView.getText().length()>0){
                    Intent intent = new Intent(getApplicationContext(), BoardLO.class);
                    intent.putExtra("player_name", mEditWordView.getText().toString());
                    startActivity(intent);
                }
            }
        });


        ActionBar actionBar;
        actionBar = getSupportActionBar();
        actionBar.hide();
    }

}
