package com.example.gamecenter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainLO extends AppCompatActivity {

    private int boardSize;
    private String playerName;

    private Button restart;
    private Button new_game;
    private Button hint;

    private RelativeLayout parentLayoutLights;
    private RelativeLayout parentLayoutSolution;

    private ImageView[][] lights;
    private int[][] lights_values;

    private ImageView[][] solution_lights;
    private int[][] solution_lights_values;

    private int[][] counters;

    private int[][] initial_lights;
    private int[][] initial_solLights;

    private int initial_lightsOn;
    private int initial_lightsOff;

    private int lightsOn;
    private int lightsOff;

    private TextView timerTV;
    private Timer timer;
    private TimerTask timerTask;
    Double time = 0.0;

    private ListAdapter2 adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_lightsout_layout);

        this.boardSize = Integer.parseInt(getIntent().getExtras().getString("board"));
        this.playerName = getIntent().getExtras().getString("player_name");

        this.initial_lightsOn = 0;
        this.initial_lightsOff = 0;
        this.lightsOn = 0;
        this.lightsOff = 0;

        this.restart = findViewById(R.id.rest);
        this.new_game = findViewById(R.id.new_game);
        this.hint = findViewById(R.id.hint);

        this.parentLayoutLights = findViewById(R.id.firstLights);

        this.parentLayoutSolution = findViewById(R.id.solLights);

        this.parentLayoutSolution.setVisibility(View.INVISIBLE);


        this.lights = new ImageView[boardSize][boardSize];

        this.lights_values = new int[boardSize][boardSize];

        this.counters = new int[boardSize][boardSize];

        this.solution_lights = new ImageView[boardSize][boardSize];

        this.solution_lights_values = new int[boardSize][boardSize];

        this.initial_lights = new int[boardSize][boardSize];
        this.initial_solLights = new int[boardSize][boardSize];

        this.timerTV = findViewById(R.id.timer);

        this.timer = new Timer();


        this.createSolutionBoard();

        this.createCounterMatrix(this.solution_lights_values);

        this.createMatrix();

        this.createBoard();


        this.time = 0.0;
        this.timerTV.setText(formatTime(0, 0, 0));
        startTimer();

        this.adapter = new ListAdapter2(MainLO.this,new ListOpenHelper(MainLO.this));

        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartBoard();
            }
        });

        new_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newGame();
            }
        });


        hint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSolution();
            }
        });

        ActionBar actionBar;
        actionBar = getSupportActionBar();

        actionBar.setBackgroundDrawable(getDrawable(R.color.action));
    }

    public void createBoard() {

        GridLayout board = new GridLayout(this);

        board.setColumnCount(boardSize);
        board.setRowCount(boardSize);

        GridLayout.LayoutParams boardParams = this.createBoardParams();

        this.addLights(board);

        this.parentLayoutLights.addView(board, boardParams);
    }


    public ImageView createLight(GridLayout gridLayout, int row, int column) {
        GridLayout.LayoutParams blockParams = createLightParams(row, column);
        ImageView light = new ImageView(this);
        int lightState = this.lights_values[row][column];
        if (lightState == 0) {
            light.setImageResource(R.drawable.on);
        } else {
            light.setImageResource(R.drawable.off);
        }
        gridLayout.addView(light, blockParams);
        return light;
    }


    public void addLights(GridLayout gridLayout) {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                this.lights[i][j] = this.createLight(gridLayout, i, j);
                int row = i;
                int column = j;
                this.lights[i][j].setOnClickListener(view ->
                        this.switchLight(row, column)

                );
            }
        }
    }

    public GridLayout.LayoutParams createBoardParams() {
        GridLayout.LayoutParams boardParams = new GridLayout.LayoutParams();
        boardParams.width = GridLayout.LayoutParams.MATCH_PARENT;
        boardParams.height = GridLayout.LayoutParams.MATCH_PARENT;
        boardParams.setMargins(40, 70, 40, 0);
        return boardParams;
    }

    public GridLayout.LayoutParams createLightParams(int row, int column) {
        GridLayout.LayoutParams lightParams = new GridLayout.LayoutParams(
                GridLayout.spec(row, 1f),
                GridLayout.spec(column, 1f)
        );

        lightParams.width = 1;
        lightParams.height = 1;

        lightParams.setMargins(20, 20, 20, 20);

        return lightParams;
    }

    public void changeState(int row, int column) {
        Bitmap bitmapLight = ((BitmapDrawable) this.lights[row][column].getDrawable()).getBitmap();
        Bitmap bitmapOn = ((BitmapDrawable) getDrawable(R.drawable.on)).getBitmap();

        if (bitmapLight.equals(bitmapOn)) {
            this.lights[row][column].setImageResource(R.drawable.off);
            this.lights_values[row][column] = 0;
            this.lightsOn--;
            this.lightsOff++;
        }
        else{
            this.lights[row][column].setImageResource(R.drawable.on);
            this.lights_values[row][column] = 1;
            this.lightsOff--;
            this.lightsOn++;
        }

    }

     public void switchLight(int row, int column) {
        if(this.solution_lights_values[row][column]==0){
            this.solution_lights_values[row][column]=1;
            this.solution_lights[row][column].setImageResource(R.drawable.solution);
        }
        else{
            this.solution_lights_values[row][column]=0;
            this.solution_lights[row][column].setImageResource(R.drawable.off);
        }
        this.changeState(row, column);
        if ((row > 0 && row < boardSize - 1) && (column > 0 && column < boardSize - 1)) {
            this.changeState(row + 1, column);
            this.changeState(row - 1, column);
            this.changeState(row, column + 1);
            this.changeState(row, column - 1);

        } else if ((row > 0 && row < boardSize - 1) && (column == 0)) {
            this.changeState(row + 1, column);
            this.changeState(row - 1, column);
            this.changeState(row, column + 1);
        } else if ((row > 0 && row < boardSize - 1) && (column == boardSize - 1)) {
            this.changeState(row + 1, column);
            this.changeState(row - 1, column);
            this.changeState(row, column - 1);
        } else if ((row == 0) && (column > 0 && column < boardSize - 1)) {
            this.changeState(row + 1, column);
            this.changeState(row, column + 1);
            this.changeState(row, column - 1);
        } else if ((row == boardSize - 1) && (column > 0 && column < boardSize - 1)) {
            this.changeState(row - 1, column);
            this.changeState(row, column + 1);
            this.changeState(row, column - 1);
        } else if (row == 0 && column == 0) {
            this.changeState(row, column + 1);
            this.changeState(row + 1, column);
        } else if (row == 0 && column == boardSize - 1) {
            this.changeState(row, column - 1);
            this.changeState(row + 1, column);
        } else if (row == boardSize - 1 && column == 0) {
            this.changeState(row, column + 1);
            this.changeState(row - 1, column);
        } else if (row == boardSize - 1 && column == boardSize - 1) {
            this.changeState(row, column - 1);
            this.changeState(row - 1, column);
        }
        showWinAlert();

    }

    public void createCounterMatrix(int[][] matrix) {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                this.counters[i][j] = 0;
            }
        }
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (matrix[i][j] == 1) {
                    if ((i > 0 && i < boardSize - 1) && (j > 0 && j < boardSize - 1)) {
                        this.counters[i][j] = this.counters[i][j] + 1;
                        this.counters[i + 1][j] = this.counters[i + 1][j] + 1;
                        this.counters[i - 1][j] = this.counters[i - 1][j] + 1;
                        this.counters[i][j + 1] = this.counters[i][j + 1] + 1;
                        this.counters[i][j - 1] = this.counters[i][j - 1] + 1;
                    } else if ((i > 0 && i < boardSize - 1) && (j == 0)) {
                        this.counters[i][j] = this.counters[i][j] + 1;
                        this.counters[i + 1][j] = this.counters[i + 1][j] + 1;
                        this.counters[i - 1][j] = this.counters[i - 1][j] + 1;
                        this.counters[i][j + 1] = this.counters[i][j + 1] + 1;
                    } else if ((i > 0 && i < boardSize - 1) && (j == boardSize - 1)) {
                        this.counters[i][j] = this.counters[i][j] + 1;
                        this.counters[i + 1][j] = this.counters[i + 1][j] + 1;
                        this.counters[i - 1][j] = this.counters[i - 1][j] + 1;
                        this.counters[i][j - 1] = this.counters[i][j - 1] + 1;
                    } else if ((i == 0) && (j > 0 && j < boardSize - 1)) {
                        this.counters[i][j] = this.counters[i][j] + 1;
                        this.counters[i + 1][j] = this.counters[i + 1][j] + 1;
                        this.counters[i][j + 1] = this.counters[i][j + 1] + 1;
                        this.counters[i][j - 1] = this.counters[i][j - 1] + 1;
                    } else if ((i == boardSize - 1) && (j > 0 && j < boardSize - 1)) {
                        this.counters[i][j] = this.counters[i][j] + 1;
                        this.counters[i - 1][j] = this.counters[i - 1][j] + 1;
                        this.counters[i][j + 1] = this.counters[i][j + 1] + 1;
                        this.counters[i][j - 1] = this.counters[i][j - 1] + 1;
                    } else if (i == 0 && j == 0) {
                        this.counters[i][j] = this.counters[i][j] + 1;
                        this.counters[i + 1][j] = this.counters[i + 1][j] + 1;
                        this.counters[i][j + 1] = this.counters[i][j + 1] + 1;
                    } else if (i == 0 && j == boardSize - 1) {
                        this.counters[i][j] = this.counters[i][j] + 1;
                        this.counters[i + 1][j] = this.counters[i + 1][j] + 1;
                        this.counters[i][j - 1] = this.counters[i][j - 1] + 1;
                    } else if (i == boardSize - 1 && j == 0) {
                        this.counters[i][j] = this.counters[i][j] + 1;
                        this.counters[i - 1][j] = this.counters[i - 1][j] + 1;
                        this.counters[i][j + 1] = this.counters[i][j + 1] + 1;
                    } else if (i == boardSize - 1 && j == boardSize - 1) {
                        this.counters[i][j] = this.counters[i][j] + 1;
                        this.counters[i - 1][j] = this.counters[i - 1][j] + 1;
                        this.counters[i][j - 1] = this.counters[i][j - 1] + 1;
                    }
                }
            }
        }

    }

    public void createMatrix() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (this.counters[i][j] % 2 == 0) {
                    this.lights_values[i][j] = 0;
                    this.initial_lights[i][j] = 0;
                    this.lightsOn++;
                    this.initial_lightsOn++;
                } else {
                    this.lights_values[i][j] = 1;
                    this.initial_lights[i][j] = 1;
                    this.initial_lightsOff++;
                    this.lightsOff++;
                }
            }
        }
    }


    public void setSolutionState(ImageView imageView, int row, int column) {
        int lightState = new Random().nextInt(1 + 1);
        if (lightState == 0) {
            imageView.setImageResource(R.drawable.off);
            this.solution_lights_values[row][column] = 0;
            this.initial_solLights[row][column] = 0;
        } else {
            imageView.setImageResource(R.drawable.solution);
            this.solution_lights_values[row][column] = 1;
            this.initial_solLights[row][column] = 1;
        }
    }

    public ImageView createSolutionLight(GridLayout gridLayout, int row, int column) {
        GridLayout.LayoutParams blockParams = createLightParams(row, column);
        ImageView light = new ImageView(this);
        this.setSolutionState(light, row, column);
        gridLayout.addView(light, blockParams);
        return light;
    }


    public void addSolutionLights(GridLayout gridLayout) {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                this.solution_lights[i][j] = this.createSolutionLight(gridLayout, i, j);
            }
        }

    }

    public ImageView createRestartedSolutionLight(GridLayout gridLayout, int row, int column) {
        GridLayout.LayoutParams blockParams = createLightParams(row, column);
        ImageView light = new ImageView(this);
        if (this.initial_solLights[row][column] == 0) {
            light.setImageResource(R.drawable.off);
        } else {
            light.setImageResource(R.drawable.solution);
        }
        gridLayout.addView(light, blockParams);
        return light;

    }

    public void restartSolutionLights(GridLayout gridLayout) {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                this.solution_lights[i][j] = this.createRestartedSolutionLight(gridLayout, i, j);
            }
        }

    }

    public void createSolutionBoard() {
        GridLayout board = new GridLayout(this);

        board.setColumnCount(boardSize);
        board.setRowCount(boardSize);

        GridLayout.LayoutParams boardParams = this.createBoardParams();

        this.addSolutionLights(board);

        this.parentLayoutSolution.addView(board, boardParams);

    }

    public void showSolution() {
        if (this.parentLayoutSolution.getVisibility() == View.INVISIBLE) {
            parentLayoutLights.setVisibility(View.INVISIBLE);
            parentLayoutSolution.setVisibility(View.VISIBLE);
        } else {
            parentLayoutLights.setVisibility(View.VISIBLE);
            parentLayoutSolution.setVisibility(View.INVISIBLE);
        }
    }

    public void newGame(){
        parentLayoutLights.removeAllViews();
        parentLayoutSolution.removeAllViews();
        initial_lightsOn = 0;
        initial_lightsOff = 0;
        lightsOff = 0;
        lightsOn = 0;
        createSolutionBoard();
        createCounterMatrix(solution_lights_values);
        createMatrix();
        createBoard();
        timerTask.cancel();
        time = 0.0;
        timerTV.setText(formatTime(0, 0, 0));
        startTimer();
    }

    public void restartBoard() {
        parentLayoutLights.removeAllViews();

        GridLayout board = new GridLayout(this);

        board.setColumnCount(boardSize);
        board.setRowCount(boardSize);

        GridLayout.LayoutParams boardParams = this.createBoardParams();

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                this.lights_values[i][j] = this.initial_lights[i][j];
                if (this.lights_values[i][j] == 0) {
                    lights[i][j].setImageResource(R.drawable.on);
                } else {
                    lights[i][j].setImageResource(R.drawable.off);
                }
            }
        }

        addLights(board);

        this.parentLayoutLights.addView(board, boardParams);

        this.lightsOn = this.initial_lightsOn;
        this.lightsOff = this.initial_lightsOff;


        GridLayout solutionBoard = new GridLayout(this);

        solutionBoard.setColumnCount(boardSize);
        solutionBoard.setRowCount(boardSize);

        GridLayout.LayoutParams solutionBoardParams = this.createBoardParams();

        restartSolutionLights(solutionBoard);

        this.parentLayoutSolution.addView(solutionBoard, solutionBoardParams);
        this.parentLayoutSolution.setVisibility(View.INVISIBLE);

    }


    public void showWinAlert() {
        if (this.lightsOn == boardSize * boardSize) {
            timerTask.cancel();
            adapter.mDB.insert(playerName,"LIGHTSOUT",String.valueOf(boardSize),"null",String.valueOf(timerTV.getText()));

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("YOU WIN !!");
            builder.setMessage("CONGRATULATIONS " + playerName + "!! ALL LIGHTS WERE SWITCHED ON IN "
                    + timerTV.getText() + " TIME. WANT TO PLAY AGAIN?")
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int n) {
                            newGame();
                        }
                    })
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int n) {
                            Intent intent = new Intent(getApplicationContext(), HomeLO.class);
                            startActivity(intent);
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();

        }
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
                startActivity(new Intent(this, HomeLO.class));
            }
            else if(text.equals("RANKING")) {
                startActivity(new Intent(this, RankLO.class));
            }
            else if(text.equals("SETTINGS")) {
                startActivity(new Intent(this, SettingsLO.class));
            }
            else if(text.equals("HELP")) {
                startActivity(new Intent(this, HelpLO.class));
            }
        }
        return true;
    }

    public void startTimer() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //timerTask.cancel();
                        time++;
                        timerTV.setText(getTimerText());
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 1200);
    }

    public String getTimerText() {
        int rounded = (int) Math.round(time);
        int seconds = ((rounded % 86400) % 3600) % 60;
        int minutes = ((rounded % 86400) % 3600) / 60;
        int hours = (rounded % 86400) / 3600;

        return formatTime(seconds, minutes, hours);
    }

    public String formatTime(int seconds, int minutes, int hours) {
        return String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds);
    }
}