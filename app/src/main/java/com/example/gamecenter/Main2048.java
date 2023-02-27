package com.example.gamecenter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Main2048 extends AppCompatActivity implements GestureDetector.OnGestureListener {

    private int boardSize;

    private Button moveBack;
    private Button newGame;
    private LinearLayout parentLayout;
    private ImageView[][] blocks;
    private ImageView[][] blocksBeforeMove;
    private GestureDetectorCompat mDetector;

    private Bitmap back;

    private int score;
    private final static Random r = new Random();

    private TextView scoreTV;

    private TextView timerTV;
    private Timer timer;
    private TimerTask timerTask;
    Double time = 0.0;

    private String playerName;

    private ListAdapter1 adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_2048_layout);

        this.boardSize = Integer.valueOf(getIntent().getExtras().getString("board"));

        this.playerName = getIntent().getExtras().getString("playername");

        mDetector = new GestureDetectorCompat(getApplicationContext(), this);

        moveBack = findViewById(R.id.moveBack);
        newGame = findViewById(R.id.new_game);

        this.parentLayout = findViewById(R.id.parent);

        this.blocks = new ImageView[boardSize][boardSize];
        this.blocksBeforeMove = new ImageView[boardSize][boardSize];
        this.back = ((BitmapDrawable) getDrawable(R.drawable.background)).getBitmap();

        this.timerTV = findViewById(R.id.timer);

        this.timer = new Timer();

        this.createBoard();

        this.score = 0;

        this.scoreTV = findViewById(R.id.score);


        time = 0.0;
        timerTV.setText(formatTime(0, 0, 0));
        startTimer();

        this.adapter = new ListAdapter1(Main2048.this,new ListOpenHelper(Main2048.this));

        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newGame();
            }
        });

        moveBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveBack();
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

        this.addBlocks(board);

        this.parentLayout.addView(board, boardParams);
    }


    public ImageView createBlock(GridLayout gridLayout, int row, int column) {

        ImageView block = new ImageView(this);

        GridLayout.LayoutParams blockParams = createBlockParams(row, column);

        gridLayout.addView(block, blockParams);

        return block;
    }

    public void addBlocks(GridLayout gridLayout) {
        int row = r.nextInt(boardSize);
        int col = r.nextInt(boardSize);


        for (int i = 0; i < this.blocks.length; i++) {
            for (int j = 0; j < this.blocks[i].length; j++) {
                if (i == row && j == col) {
                    this.blocks[i][j] = this.createBlock(gridLayout, i, j);
                    this.blocks[i][j].setImageResource(R.drawable.two_image);
                    this.blocks[i][j].setTag(R.drawable.two_image);
                    this.blocksBeforeMove[i][j] = this.createBlock(gridLayout, i, j);
                    this.blocksBeforeMove[i][j].setTag(R.drawable.two_image);
                } else {
                    this.blocks[i][j] = this.createBlock(gridLayout, i, j);
                    this.blocks[i][j].setImageResource(R.drawable.background);
                    this.blocks[i][j].setTag(R.drawable.background);
                    this.blocksBeforeMove[i][j] = this.createBlock(gridLayout, i, j);
                    this.blocksBeforeMove[i][j].setTag(R.drawable.background);
                }
            }
        }

        for (int x = 0; x < boardSize; x++) {
            for (int y = 0; y < boardSize; y++) {
                System.out.print(this.blocks[x][y].getTag());
                System.out.print(" ");
            }
            System.out.println(" ");
        }

    }

    public void addBeforeMoveBlocks(GridLayout gridLayout){

        for (int x= 0; x < this.blocks.length; x++) {
            for (int y = this.blocks[x].length - 1; y >= 0; y--) {
                this.blocks[x][y]=this.blocksBeforeMove[x][y];
                this.blocks[x][y] = this.createBlock(gridLayout,x,y);
                this.blocks[x][y].setImageResource(tagConversion(this.blocksBeforeMove[x][y].getTag()));
                this.blocks[x][y].setTag(this.blocksBeforeMove[x][y].getTag());
            }
        }
    }


    public GridLayout.LayoutParams createBoardParams() {
        GridLayout.LayoutParams boardParams = new GridLayout.LayoutParams();
        boardParams.width = GridLayout.LayoutParams.MATCH_PARENT;
        boardParams.height = GridLayout.LayoutParams.MATCH_PARENT;
        boardParams.setMargins(10, 10, 10, 10);

        return boardParams;
    }

    public GridLayout.LayoutParams createBlockParams(int row, int column) {
        GridLayout.LayoutParams blockParams = new GridLayout.LayoutParams(
                GridLayout.spec(row, 1f),
                GridLayout.spec(column, 1f)
        );
        blockParams.width = 1;
        blockParams.height = 1;

        blockParams.setMargins(10, 10, 10, 10);

        return blockParams;
    }


    public void moveBlock(int row, int column, String direction, Object tag) {
        switch (direction) {
            case "RIGHT":
                if (column < boardSize - 1) {
                    int initial_column = column;
                    int moves = 0;
                    while (column < boardSize - 1 && ((BitmapDrawable) this.blocks[row][column + 1].getDrawable()).getBitmap() == this.back) {
                        column++;
                        moves++;
                    }
                    column = initial_column;
                    if ((column + moves) < boardSize - 1) {
                        if (((BitmapDrawable) this.blocks[row][column + moves + 1].getDrawable()).getBitmap() != this.back) {
                            if (this.blocks[row][column].getTag().equals(this.blocks[row][column + moves + 1].getTag())) {
                                joinBlocks("RIGHT", row, column + moves, this.blocks[row][column].getTag());

                                this.blocks[row][column].setImageResource(R.drawable.background);
                                this.blocks[row][column].setTag(R.drawable.background);

                            } else {
                                if (moves > 0) {
                                    this.blocks[row][column + moves].setImageResource(tagConversion(tag));
                                    this.blocks[row][column + moves].setTag(tag);

                                    this.blocks[row][column].setImageResource(R.drawable.background);
                                    this.blocks[row][column].setTag(R.drawable.background);
                                } else {
                                    this.blocks[row][column].setImageResource(tagConversion(tag));
                                    this.blocks[row][column].setTag(tag);
                                }
                            }
                        } else {
                            if (moves > 0) {
                                this.blocks[row][column + moves].setImageResource(tagConversion(tag));
                                this.blocks[row][column + moves].setTag(tag);

                                this.blocks[row][column].setImageResource(R.drawable.background);
                                this.blocks[row][column].setTag(R.drawable.background);
                            } else {
                                this.blocks[row][column].setImageResource(tagConversion(tag));
                                this.blocks[row][column].setTag(tag);
                            }

                        }
                    } else {
                        if (moves > 0) {
                            this.blocks[row][column + moves].setImageResource(tagConversion(tag));
                            this.blocks[row][column + moves].setTag(tag);

                            this.blocks[row][column].setImageResource(R.drawable.background);
                            this.blocks[row][column].setTag(R.drawable.background);
                        } else {
                            this.blocks[row][column].setImageResource(tagConversion(tag));
                            this.blocks[row][column].setTag(tag);
                        }

                    }
                }
                break;

            case "LEFT":
                if (column > 0) {
                    int initial_column = column;
                    int moves = 0;
                    while (column > 0 && ((BitmapDrawable) this.blocks[row][column - 1].getDrawable()).getBitmap() == this.back) {
                        column--;
                        moves++;
                    }
                    column = initial_column;
                    if ((column - moves) > 0) {
                        if (((BitmapDrawable) this.blocks[row][column - moves - 1].getDrawable()).getBitmap() != this.back) {
                            if (this.blocks[row][column].getTag().equals(this.blocks[row][column - moves - 1].getTag())) {
                                joinBlocks("LEFT", row, column - moves, this.blocks[row][column].getTag());

                                this.blocks[row][column].setImageResource(R.drawable.background);
                                this.blocks[row][column].setTag(R.drawable.background);

                            } else {
                                if (moves > 0) {
                                    this.blocks[row][column - moves].setImageResource(tagConversion(tag));
                                    this.blocks[row][column - moves].setTag(tag);

                                    this.blocks[row][column].setImageResource(R.drawable.background);
                                    this.blocks[row][column].setTag(R.drawable.background);
                                } else {
                                    this.blocks[row][column].setImageResource(tagConversion(tag));
                                    this.blocks[row][column].setTag(tag);
                                }
                            }
                        } else {
                            if (moves > 0) {
                                this.blocks[row][column - moves].setImageResource(tagConversion(tag));
                                this.blocks[row][column - moves].setTag(tag);

                                this.blocks[row][column].setImageResource(R.drawable.background);
                                this.blocks[row][column].setTag(R.drawable.background);
                            } else {
                                this.blocks[row][column].setImageResource(tagConversion(tag));
                                this.blocks[row][column].setTag(tag);
                            }

                        }
                    } else {
                        if (moves > 0) {
                            this.blocks[row][column - moves].setImageResource(tagConversion(tag));
                            this.blocks[row][column - moves].setTag(tag);

                            this.blocks[row][column].setImageResource(R.drawable.background);
                            this.blocks[row][column].setTag(R.drawable.background);
                        } else {
                            this.blocks[row][column].setImageResource(tagConversion(tag));
                            this.blocks[row][column].setTag(tag);
                        }

                    }
                }
                break;

            case "UP":
                if (row > 0) {
                    int initial_row = row;
                    int moves = 0;
                    while (row > 0 && ((BitmapDrawable) this.blocks[row - 1][column].getDrawable()).getBitmap() == this.back) {
                        row--;
                        moves++;
                    }
                    row = initial_row;
                    if ((row - moves) > 0) {
                        if (((BitmapDrawable) this.blocks[row - moves - 1][column].getDrawable()).getBitmap() != this.back) {
                            if (this.blocks[row][column].getTag().equals(this.blocks[row - moves - 1][column].getTag())) {
                                joinBlocks("UP", row - moves, column, this.blocks[row][column].getTag());

                                this.blocks[row][column].setImageResource(R.drawable.background);
                                this.blocks[row][column].setTag(R.drawable.background);

                            } else {
                                if (moves > 0) {
                                    this.blocks[row - moves][column].setImageResource(tagConversion(tag));
                                    this.blocks[row - moves][column].setTag(tag);

                                    this.blocks[row][column].setImageResource(R.drawable.background);
                                    this.blocks[row][column].setTag(R.drawable.background);
                                } else {
                                    this.blocks[row][column].setImageResource(tagConversion(tag));
                                    this.blocks[row][column].setTag(tag);
                                }
                            }
                        } else {
                            if (moves > 0) {
                                this.blocks[row - moves][column].setImageResource(tagConversion(tag));
                                this.blocks[row - moves][column].setTag(tag);

                                this.blocks[row][column].setImageResource(R.drawable.background);
                                this.blocks[row][column].setTag(R.drawable.background);
                            } else {
                                this.blocks[row][column].setImageResource(tagConversion(tag));
                                this.blocks[row][column].setTag(tag);
                            }

                        }
                    } else {
                        if (moves > 0) {
                            this.blocks[row - moves][column].setImageResource(tagConversion(tag));
                            this.blocks[row - moves][column].setTag(tag);

                            this.blocks[row][column].setImageResource(R.drawable.background);
                            this.blocks[row][column].setTag(R.drawable.background);
                        } else {
                            this.blocks[row][column].setImageResource(tagConversion(tag));
                            this.blocks[row][column].setTag(tag);
                        }

                    }
                }
                break;

            case "DOWN":
                if (row < boardSize - 1) {
                    int initial_row = row;
                    int moves = 0;
                    while (row < boardSize - 1 && ((BitmapDrawable) this.blocks[row + 1][column].getDrawable()).getBitmap() == this.back) {
                        row++;
                        moves++;
                    }
                    row = initial_row;
                    if ((row + moves) < boardSize - 1) {
                        if (((BitmapDrawable) this.blocks[row + moves + 1][column].getDrawable()).getBitmap() != this.back) {
                            if (this.blocks[row][column].getTag().equals(this.blocks[row + moves + 1][column].getTag())) {
                                joinBlocks("DOWN", row + moves, column, this.blocks[row][column].getTag());

                                this.blocks[row][column].setImageResource(R.drawable.background);
                                this.blocks[row][column].setTag(R.drawable.background);
                            } else {
                                if (moves > 0) {
                                    this.blocks[row + moves][column].setImageResource(tagConversion(tag));
                                    this.blocks[row + moves][column].setTag(tag);

                                    this.blocks[row][column].setImageResource(R.drawable.background);
                                    this.blocks[row][column].setTag(R.drawable.background);
                                } else {
                                    this.blocks[row][column].setImageResource(tagConversion(tag));
                                    this.blocks[row][column].setTag(tag);
                                }

                            }
                        } else {
                            if (moves > 0) {
                                this.blocks[row + moves][column].setImageResource(tagConversion(tag));
                                this.blocks[row + moves][column].setTag(tag);

                                this.blocks[row][column].setImageResource(R.drawable.background);
                                this.blocks[row][column].setTag(R.drawable.background);
                            } else {
                                this.blocks[row][column].setImageResource(tagConversion(tag));
                                this.blocks[row][column].setTag(tag);
                            }
                        }
                    } else {
                        if (moves > 0) {
                            this.blocks[row + moves][column].setImageResource(tagConversion(tag));
                            this.blocks[row + moves][column].setTag(tag);

                            this.blocks[row][column].setImageResource(R.drawable.background);
                            this.blocks[row][column].setTag(R.drawable.background);
                        } else {
                            this.blocks[row][column].setImageResource(tagConversion(tag));
                            this.blocks[row][column].setTag(tag);
                        }

                    }
                }

                break;
        }

    }

    public void joinBlocks(String direction, int row, int column, Object number) {

        switch (direction) {
            case "RIGHT":
                if (number.equals(R.drawable.two_image)) {
                    this.blocks[row][column + 1].setImageResource(R.drawable.four_image);
                    this.blocks[row][column + 1].setTag(R.drawable.four_image);
                    this.score += 4;
                    this.scoreTV.setText(String.valueOf(score));

                } else if (number.equals(R.drawable.four_image)) {
                    this.blocks[row][column + 1].setImageResource(R.drawable.eight_image);
                    this.blocks[row][column + 1].setTag(R.drawable.eight_image);
                    this.score += 8;
                    this.scoreTV.setText(String.valueOf(score));

                } else if (number.equals(R.drawable.eight_image)) {
                    this.blocks[row][column + 1].setImageResource(R.drawable.sixteen_image);
                    this.blocks[row][column + 1].setTag(R.drawable.sixteen_image);
                    this.score += 16;
                    this.scoreTV.setText(String.valueOf(score));

                } else if (number.equals(R.drawable.sixteen_image)) {
                    this.blocks[row][column + 1].setImageResource(R.drawable.thirtytwo_image);
                    this.blocks[row][column + 1].setTag(R.drawable.thirtytwo_image);
                    this.score += 32;
                    this.scoreTV.setText(String.valueOf(score));

                } else if (number.equals(R.drawable.thirtytwo_image)) {
                    this.blocks[row][column + 1].setImageResource(R.drawable.sixtyfour_image);
                    this.blocks[row][column + 1].setTag(R.drawable.sixtyfour_image);
                    this.score += 64;
                    this.scoreTV.setText(String.valueOf(score));

                } else if (number.equals(R.drawable.sixtyfour_image)) {
                    this.blocks[row][column + 1].setImageResource(R.drawable.htwentyeight_image);
                    this.blocks[row][column + 1].setTag(R.drawable.htwentyeight_image);
                    this.score += 128;
                    this.scoreTV.setText(String.valueOf(score));

                } else if (number.equals(R.drawable.htwentyeight_image)) {
                    this.blocks[row][column + 1].setImageResource(R.drawable.thfiftysix_image);
                    this.blocks[row][column + 1].setTag(R.drawable.thfiftysix_image);
                    this.score += 256;
                    this.scoreTV.setText(String.valueOf(score));

                } else if (number.equals(R.drawable.thfiftysix_image)) {
                    this.blocks[row][column + 1].setImageResource(R.drawable.fhtwelve_image);
                    this.blocks[row][column + 1].setTag(R.drawable.fhtwelve_image);
                    this.score += 512;
                    this.scoreTV.setText(String.valueOf(score));

                } else if (number.equals(R.drawable.fhtwelve_image)) {
                    this.blocks[row][column + 1].setImageResource(R.drawable.ttwentyfour_image);
                    this.blocks[row][column + 1].setTag(R.drawable.ttwentyfour_image);
                    this.score += 1024;
                    this.scoreTV.setText(String.valueOf(score));

                } else if (number.equals(R.drawable.ttwentyfour_image)) {
                    this.blocks[row][column + 1].setImageResource(R.drawable.ttfortyeight_image);
                    this.blocks[row][column + 1].setTag(R.drawable.ttfortyeight_image);
                    this.score += 2048;
                    this.scoreTV.setText(String.valueOf(score));
                    winScreen();
                }
                break;

            case "LEFT":
                if (number.equals(R.drawable.two_image)) {
                    this.blocks[row][column - 1].setImageResource(R.drawable.four_image);
                    this.blocks[row][column - 1].setTag(R.drawable.four_image);
                    this.score += 4;
                    this.scoreTV.setText(String.valueOf(score));

                } else if (number.equals(R.drawable.four_image)) {
                    this.blocks[row][column - 1].setImageResource(R.drawable.eight_image);
                    this.blocks[row][column - 1].setTag(R.drawable.eight_image);
                    this.score += 8;
                    this.scoreTV.setText(String.valueOf(score));

                } else if (number.equals(R.drawable.eight_image)) {
                    this.blocks[row][column - 1].setImageResource(R.drawable.sixteen_image);
                    this.blocks[row][column - 1].setTag(R.drawable.sixteen_image);
                    this.score += 16;
                    this.scoreTV.setText(String.valueOf(score));

                } else if (number.equals(R.drawable.sixteen_image)) {
                    this.blocks[row][column - 1].setImageResource(R.drawable.thirtytwo_image);
                    this.blocks[row][column - 1].setTag(R.drawable.thirtytwo_image);
                    this.score += 32;
                    this.scoreTV.setText(String.valueOf(score));

                } else if (number.equals(R.drawable.thirtytwo_image)) {
                    this.blocks[row][column - 1].setImageResource(R.drawable.sixtyfour_image);
                    this.blocks[row][column - 1].setTag(R.drawable.sixtyfour_image);
                    this.score += 64;
                    this.scoreTV.setText(String.valueOf(score));

                } else if (number.equals(R.drawable.sixtyfour_image)) {
                    this.blocks[row][column - 1].setImageResource(R.drawable.htwentyeight_image);
                    this.blocks[row][column - 1].setTag(R.drawable.htwentyeight_image);
                    this.score += 128;
                    this.scoreTV.setText(String.valueOf(score));

                } else if (number.equals(R.drawable.htwentyeight_image)) {
                    this.blocks[row][column - 1].setImageResource(R.drawable.thfiftysix_image);
                    this.blocks[row][column - 1].setTag(R.drawable.thfiftysix_image);
                    this.score += 256;
                    this.scoreTV.setText(String.valueOf(score));

                } else if (number.equals(R.drawable.thfiftysix_image)) {
                    this.blocks[row][column - 1].setImageResource(R.drawable.fhtwelve_image);
                    this.blocks[row][column - 1].setTag(R.drawable.fhtwelve_image);
                    this.score += 512;
                    this.scoreTV.setText(String.valueOf(score));

                } else if (number.equals(R.drawable.fhtwelve_image)) {
                    this.blocks[row][column - 1].setImageResource(R.drawable.ttwentyfour_image);
                    this.blocks[row][column - 1].setTag(R.drawable.ttwentyfour_image);
                    this.score += 1024;
                    this.scoreTV.setText(String.valueOf(score));

                } else if (number.equals(R.drawable.ttwentyfour_image)) {
                    this.blocks[row][column - 1].setImageResource(R.drawable.ttfortyeight_image);
                    this.blocks[row][column - 1].setTag(R.drawable.ttfortyeight_image);
                    this.score += 2048;
                    this.scoreTV.setText(String.valueOf(score));
                    winScreen();
                }
                break;

            case "UP":
                if (number.equals(R.drawable.two_image)) {
                    this.blocks[row - 1][column].setImageResource(R.drawable.four_image);
                    this.blocks[row - 1][column].setTag(R.drawable.four_image);
                    this.score += 4;
                    this.scoreTV.setText(String.valueOf(score));

                } else if (number.equals(R.drawable.four_image)) {
                    this.blocks[row - 1][column].setImageResource(R.drawable.eight_image);
                    this.blocks[row - 1][column].setTag(R.drawable.eight_image);
                    this.score += 8;
                    this.scoreTV.setText(String.valueOf(score));

                } else if (number.equals(R.drawable.eight_image)) {
                    this.blocks[row - 1][column].setImageResource(R.drawable.sixteen_image);
                    this.blocks[row - 1][column].setTag(R.drawable.sixteen_image);
                    this.score += 16;
                    this.scoreTV.setText(String.valueOf(score));

                } else if (number.equals(R.drawable.sixteen_image)) {
                    this.blocks[row - 1][column].setImageResource(R.drawable.thirtytwo_image);
                    this.blocks[row - 1][column].setTag(R.drawable.thirtytwo_image);
                    this.score += 32;
                    this.scoreTV.setText(String.valueOf(score));

                } else if (number.equals(R.drawable.thirtytwo_image)) {
                    this.blocks[row - 1][column].setImageResource(R.drawable.sixtyfour_image);
                    this.blocks[row - 1][column].setTag(R.drawable.sixtyfour_image);
                    this.score += 64;
                    this.scoreTV.setText(String.valueOf(score));

                } else if (number.equals(R.drawable.sixtyfour_image)) {
                    this.blocks[row - 1][column].setImageResource(R.drawable.htwentyeight_image);
                    this.blocks[row - 1][column].setTag(R.drawable.htwentyeight_image);
                    this.score += 128;
                    this.scoreTV.setText(String.valueOf(score));

                } else if (number.equals(R.drawable.htwentyeight_image)) {
                    this.blocks[row - 1][column].setImageResource(R.drawable.thfiftysix_image);
                    this.blocks[row - 1][column].setTag(R.drawable.thfiftysix_image);
                    this.score += 256;
                    this.scoreTV.setText(String.valueOf(score));

                } else if (number.equals(R.drawable.thfiftysix_image)) {
                    this.blocks[row - 1][column].setImageResource(R.drawable.fhtwelve_image);
                    this.blocks[row - 1][column].setTag(R.drawable.fhtwelve_image);
                    this.score += 512;
                    this.scoreTV.setText(String.valueOf(score));

                } else if (number.equals(R.drawable.fhtwelve_image)) {
                    this.blocks[row - 1][column].setImageResource(R.drawable.ttwentyfour_image);
                    this.blocks[row - 1][column].setTag(R.drawable.ttwentyfour_image);
                    this.score += 1024;
                    this.scoreTV.setText(String.valueOf(score));

                } else if (number.equals(R.drawable.ttwentyfour_image)) {
                    this.blocks[row - 1][column].setImageResource(R.drawable.ttfortyeight_image);
                    this.blocks[row - 1][column].setTag(R.drawable.ttfortyeight_image);
                    this.score += 2048;
                    this.scoreTV.setText(String.valueOf(score));
                    winScreen();
                }
                break;

            case "DOWN":
                if (number.equals(R.drawable.two_image)) {
                    this.blocks[row + 1][column].setImageResource(R.drawable.four_image);
                    this.blocks[row + 1][column].setTag(R.drawable.four_image);
                    this.score += 4;
                    this.scoreTV.setText(String.valueOf(score));

                } else if (number.equals(R.drawable.four_image)) {
                    this.blocks[row + 1][column].setImageResource(R.drawable.eight_image);
                    this.blocks[row + 1][column].setTag(R.drawable.eight_image);
                    this.score += 8;
                    this.scoreTV.setText(String.valueOf(score));

                } else if (number.equals(R.drawable.eight_image)) {
                    this.blocks[row + 1][column].setImageResource(R.drawable.sixteen_image);
                    this.blocks[row + 1][column].setTag(R.drawable.sixteen_image);
                    this.score += 16;
                    this.scoreTV.setText(String.valueOf(score));

                } else if (number.equals(R.drawable.sixteen_image)) {
                    this.blocks[row + 1][column].setImageResource(R.drawable.thirtytwo_image);
                    this.blocks[row + 1][column].setTag(R.drawable.thirtytwo_image);
                    this.score += 32;
                    this.scoreTV.setText(String.valueOf(score));

                } else if (number.equals(R.drawable.thirtytwo_image)) {
                    this.blocks[row + 1][column].setImageResource(R.drawable.sixtyfour_image);
                    this.blocks[row + 1][column].setTag(R.drawable.sixtyfour_image);
                    this.score += 64;
                    this.scoreTV.setText(String.valueOf(score));

                } else if (number.equals(R.drawable.sixtyfour_image)) {
                    this.blocks[row + 1][column].setImageResource(R.drawable.htwentyeight_image);
                    this.blocks[row + 1][column].setTag(R.drawable.htwentyeight_image);
                    this.score += 128;
                    this.scoreTV.setText(String.valueOf(score));

                } else if (number.equals(R.drawable.htwentyeight_image)) {
                    this.blocks[row + 1][column].setImageResource(R.drawable.thfiftysix_image);
                    this.blocks[row + 1][column].setTag(R.drawable.thfiftysix_image);
                    this.score += 256;
                    this.scoreTV.setText(String.valueOf(score));

                } else if (number.equals(R.drawable.thfiftysix_image)) {
                    this.blocks[row + 1][column].setImageResource(R.drawable.fhtwelve_image);
                    this.blocks[row + 1][column].setTag(R.drawable.fhtwelve_image);
                    this.score += 512;
                    this.scoreTV.setText(String.valueOf(score));

                } else if (number.equals(R.drawable.fhtwelve_image)) {
                    this.blocks[row + 1][column].setImageResource(R.drawable.ttwentyfour_image);
                    this.blocks[row + 1][column].setTag(R.drawable.ttwentyfour_image);
                    this.score += 1024;
                    this.scoreTV.setText(String.valueOf(score));

                } else if (number.equals(R.drawable.ttwentyfour_image)) {
                    this.blocks[row + 1][column].setImageResource(R.drawable.ttfortyeight_image);
                    this.blocks[row + 1][column].setTag(R.drawable.ttfortyeight_image);
                    this.score += 2048;
                    this.scoreTV.setText(String.valueOf(score));
                    winScreen();
                }
                break;
        }


    }

    public void addRandomBlock() {
        if (!checkBoard()) {
            int rRow = r.nextInt(boardSize);
            int rColumn = r.nextInt(boardSize);
            while (!this.blocks[rRow][rColumn].getTag().equals(R.drawable.background)) {
                rRow = r.nextInt(boardSize);
                rColumn = r.nextInt(boardSize);
            }
            this.blocks[rRow][rColumn].setImageResource(R.drawable.two_image);
            this.blocks[rRow][rColumn].setTag(R.drawable.two_image);
        } else {
            gameOverScreen();
        }
    }

    public boolean checkBoard() {
        boolean end = false;
        int count = 0;
        int possible_moves=0;
        for (int i = 0; i < this.blocks.length; i++) {
            for (int j = 0; j < this.blocks[i].length; j++) {
                if (this.blocks[i][j].getTag().equals(R.drawable.background)) {
                    count++;
                }
            }
        }
        for (int i = 0; i < this.blocks.length; i++) {
            for (int j = this.blocks[i].length - 1; j >= 0; j--) {
                if(j<boardSize-1){
                    if(this.blocks[i][j].getTag().equals(this.blocks[i][j+1].getTag())){
                        possible_moves++;
                    }
                }
            }
        }
        for (int i = 0; i < this.blocks.length; i++) {
            for (int j = 0; j < this.blocks[i].length; j++) {
                if(j>0){
                    if(this.blocks[i][j].getTag().equals(this.blocks[i][j-1].getTag())){
                        possible_moves++;
                    }
                }
            }
        }
        for (int i = 0; i < this.blocks.length; i++) {
            for (int j = 0; j < this.blocks[i].length; j++) {
                if(i<boardSize-1){
                    if(this.blocks[i][j].getTag().equals(this.blocks[i+1][j].getTag())){
                        possible_moves++;
                    }
                }
            }
        }
        for (int i = this.blocks.length - 1; i >= 0; i--) {
            for (int j = 0; j < this.blocks[i].length; j++) {
                if(i>0){
                    if(this.blocks[i][j].getTag().equals(this.blocks[i-1][j].getTag())){
                        possible_moves++;
                    }
                }
            }
        }

        if(count==0 && possible_moves==0){
            end=true;
        }

        return end;
    }

    public void moveBack(){
        this.parentLayout.removeAllViews();
        GridLayout board = new GridLayout(this);

        board.setColumnCount(boardSize);
        board.setRowCount(boardSize);

        GridLayout.LayoutParams boardParams = this.createBoardParams();

        addBeforeMoveBlocks(board);

        this.parentLayout.addView(board, boardParams);

        moveBack.setEnabled(false);
    }

    public void gameOverScreen() {
        timerTask.cancel();
        adapter.mDB.insert(playerName,"2048",String.valueOf(boardSize),String.valueOf(score),String.valueOf(timerTV.getText()));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("GAME OVER");
        builder.setMessage("NO POSSIBLE MOVES, YOU OBTAIN A " + score + " SCORE IN "
                        + timerTV.getText() + " TIME. WANT TO PLAY AGAIN?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int n) {
                        newGame();
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int n) {
                        Intent intent = new Intent(getApplicationContext(), GameOver2048.class);
                        intent.putExtra("score", String.valueOf(score));
                        intent.putExtra("time",time.toString());
                        startActivity(intent);
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void winScreen() {
        timerTask.cancel();
        adapter.mDB.insert(playerName,"2048",String.valueOf(boardSize),String.valueOf(score),String.valueOf(timerTV.getText()));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("YOU WIN !!");
        builder.setMessage("CONGRATULATIONS " + playerName + "!! YOU ARRIVE TO 2048 IN"
                        + timerTV.getText() + " TIME. WANT TO PLAY AGAIN?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int n) {
                        newGame();
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int n) {
                        String final_score = String.valueOf(score);
                        Intent intent = new Intent(getApplicationContext(), Win2048.class);
                        intent.putExtra("score", final_score);
                        intent.putExtra("time",time.toString());
                        startActivity(intent);
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void newGame(){
        timerTask.cancel();
        parentLayout.removeAllViews();
        createBoard();
        score = 0;
        time = 0.0;
        timerTV.setText(formatTime(0, 0, 0));
        startTimer();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        Context context = getApplicationContext();

        float distX = e2.getX() - e1.getX();
        float distY = e2.getY() - e1.getY();

        if (Math.abs(distX) > Math.abs(distY)) {
            if (distX > 0) {
                for (int i = 0; i < this.blocksBeforeMove.length; i++) {
                    for (int j = this.blocksBeforeMove[i].length - 1; j >= 0; j--) {
                        this.blocksBeforeMove[i][j].setTag(this.blocks[i][j].getTag());
                    }
                }

                for (int i = 0; i < this.blocks.length; i++) {
                    for (int j = this.blocks[i].length - 1; j >= 0; j--) {
                        Bitmap bitmapB = ((BitmapDrawable) this.blocks[i][j].getDrawable()).getBitmap();
                        if (bitmapB != this.back) {
                            this.moveBlock(i, j, "RIGHT", this.blocks[i][j].getTag());
                        }
                    }
                }
                moveBack.setEnabled(true);
                addRandomBlock();

            } else if (distX < 0) {
                for (int i = 0; i < this.blocks.length; i++) {
                    for (int j = 0; j < this.blocks[i].length; j++) {
                        this.blocksBeforeMove[i][j].setTag(this.blocks[i][j].getTag());
                    }
                }
                for (int i = 0; i < this.blocks.length; i++) {
                    for (int j = 0; j < this.blocks[i].length; j++) {
                        Bitmap bitmapB = ((BitmapDrawable) this.blocks[i][j].getDrawable()).getBitmap();
                        if (bitmapB != this.back) {
                            this.moveBlock(i, j, "LEFT", this.blocks[i][j].getTag());
                        }
                    }
                }
                moveBack.setEnabled(true);
                addRandomBlock();
            }
            return true;
        }


        if (Math.abs(distY) > Math.abs(distX)) {
            if (distY < 0) {
                for (int i = 0; i < this.blocks.length; i++) {
                    for (int j = 0; j < this.blocks[i].length; j++) {
                        this.blocksBeforeMove[i][j].setTag(this.blocks[i][j].getTag());
                    }
                }
                for (int i = 0; i < this.blocks.length; i++) {
                    for (int j = 0; j < this.blocks[i].length; j++) {
                        Bitmap bitmapB = ((BitmapDrawable) this.blocks[i][j].getDrawable()).getBitmap();
                        if (bitmapB != this.back) {
                            this.moveBlock(i, j, "UP", this.blocks[i][j].getTag());
                        }
                    }
                }
                moveBack.setEnabled(true);
                addRandomBlock();

            } else if (distY > 0) {
                for (int i = this.blocks.length - 1; i >= 0; i--) {
                    for (int j = 0; j < this.blocks[i].length; j++) {
                        this.blocksBeforeMove[i][j].setTag(this.blocks[i][j].getTag());
                    }
                }
                for (int i = this.blocks.length - 1; i >= 0; i--) {
                    for (int j = 0; j < this.blocks[i].length; j++) {
                        Bitmap bitmapB = ((BitmapDrawable) this.blocks[i][j].getDrawable()).getBitmap();
                        if (bitmapB != this.back) {
                            this.moveBlock(i, j, "DOWN", this.blocks[i][j].getTag());
                        }
                    }
                }
                moveBack.setEnabled(true);
                addRandomBlock();
            }
            return true;
        }
        return false;
    }

    public void startTimer() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
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
                startActivity(new Intent(this, Home2048.class));
            }
            else if(text.equals("RANKING")) {
                startActivity(new Intent(this, Rank2048.class));
            }
            else if(text.equals("SETTINGS")) {
                startActivity(new Intent(this, Settings2048.class));
            }
            else if(text.equals("HELP")) {
                startActivity(new Intent(this, Help2048.class));
            }
        }
        return true;
    }

    public int tagConversion(Object tag) {
        int drawable = 0;
        if (tag.equals(2131165301)) {
            drawable = R.drawable.background;
        } else if (tag.equals(2131165447)) {
            drawable = R.drawable.two_image;
        } else if (tag.equals(2131165325)) {
            drawable = R.drawable.four_image;
        } else if (tag.equals(2131165323)) {
            drawable = R.drawable.eight_image;
        } else if (tag.equals(2131165433)) {
            drawable = R.drawable.sixteen_image;
        } else if (tag.equals(2131165439)) {
            drawable = R.drawable.thirtytwo_image;
        } else if (tag.equals(2131165434)) {
            drawable = R.drawable.sixtyfour_image;
        } else if (tag.equals(2131165331)) {
            drawable = R.drawable.htwentyeight_image;
        } else if (tag.equals(2131165438)) {
            drawable = R.drawable.thfiftysix_image;
        } else if (tag.equals(2131165324)) {
            drawable = R.drawable.fhtwelve_image;
        } else if (tag.equals(2131165446)) {
            drawable = R.drawable.ttwentyfour_image;
        }else if (tag.equals(2131165445)) {
            drawable = R.drawable.ttfortyeight_image;
        }
        return drawable;
    }


    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return true;
    }


    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {
    }
}
