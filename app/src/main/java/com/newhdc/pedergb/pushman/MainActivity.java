package com.newhdc.pedergb.pushman;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import static com.newhdc.pedergb.pushman.Globals.mainTheme;
import static com.newhdc.pedergb.pushman.Globals.stopSound;
import static com.newhdc.pedergb.pushman.Globals.stopTheme;

public class MainActivity extends AppCompatActivity {

    private Button up, down, left, right, reset, mainMenu;
    private TextView timer;
    private ProgressBar progressBar;
    private CountDownTimer countDown;
    private long countDownLeft;

    // ---------------------- Data for each level ------------------ \\

    private int[][] playerPos = {{1, 1}, {1, 3}, {1, 1}, {1, 1}, {5, 5}, {1, 2}};
    private int[] boardSize = {7, 8, 9, 9, 8, 8};
    public static int[] levelTime = {45000, 70000, 90000, 110000, 110000, 150000};

    private int[][][] boxWinningPos = {{{2, 2}, {3, 3}},//level 1
            {{3,3},{3,2}}, // level 2
            {{1, 5}, {5, 4}, {7, 7}},// level 3
            {{1,2},{7,1},{4,4}},// level 4
            {{1,1},{1,3},{6,6}}, // level 5
            {{3,3}, {4,3}, {3,5}, {5,5}, {4,4}}};// level 6
    private int[][][] boxStartingPos = {{{3,2}, {4,4}},
            {{3,5}, {4,5}},
            {{3, 2}, {4, 4}, {3, 6}},
            {{5,6},{3,3},{3,2}},
            {{1,2},{2,2},{3,2}},
            {{2,4}, {3,3}, {5,3}, {3,5}, {4,4}}};
    private int[][][] walls= {{{4,1},{4,2}},
            {{2,2},{2,3},{2,4},{3,4},{4,4},{4,3},{3,6},{4,6}},
            {{4,2},{4,3},{4,4},{4,5},{4,5},{4,6},{4,7},{5,3},{6,3},{5,5},{6,5}},
            {{1,3},{2,3},{4,3},{5,3},{5,4},{5,5},{5,7},{1,7},{2,5},{3,5},{3,7},{6,1}},
            {{2,1},{2,3},{2,4},{4,2},{5,3},{5,4},{5,6},{7,7}},
            {{1,1},{2,1},{3,1},{4,1},{5,1},{6,1},{7,1},{2,3},{4,2},{3,6},{4,6},{5,6},{6,6}}};
    // ................................................................ \\

    private int NUM_ROWS = boardSize[Globals.level];
    private int NUM_COLS = boardSize[Globals.level];

    Button board [][] = new Button[NUM_ROWS][NUM_COLS];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Globals.movesCount = 0;


        up = (Button) findViewById(R.id.button);
        down = (Button) findViewById(R.id.button2);
        left = (Button) findViewById(R.id.button3);
        right = (Button) findViewById(R.id.button4);
        reset = (Button) findViewById(R.id.btReset);
        mainMenu = (Button) findViewById(R.id.btMainMenu);


        if (Globals.allowSound) Globals.moveSound = MediaPlayer.create(MainActivity.this, R.raw.move_sound2);
        if (Globals.allowMusic) Globals.mainTheme = MediaPlayer.create(MainActivity.this, R.raw.game_music);
        if (Globals.allowMusic) Globals.mainTheme.start();

        populateButtons();
        makeBoard();
        setField(playerPos[Globals.level][0], playerPos[Globals.level][1], 'x');
        initBoardImages();

        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveUp();
                if (Globals.allowSound) Globals.moveSound.start();
            }
        });
        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveDown();
                if (Globals.allowSound) Globals.moveSound.start();
            }
        });
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveLeft();
                if (Globals.allowSound) Globals.moveSound.start();
            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveRight();
                if (Globals.allowSound) Globals.moveSound.start();
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTheme();
                stopSound();
                Intent resetIntent = new Intent(MainActivity.this, MainActivity.class);
                MainActivity.this.startActivity(resetIntent);
            }
        });
        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTheme();
                stopSound();
                Intent mainMenuIntent = new Intent(MainActivity.this, StartupActivity.class);
                MainActivity.this.startActivity(mainMenuIntent);
                finish();
            }
        });


        timer = (TextView) findViewById(R.id.timer);
        progressBar=(ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(levelTime[Globals.level]); //TODO color on progBar?

        countDown = new CountDownTimer(levelTime[Globals.level], 1) {

            public void onTick(long millisUntilFinished) {
                progressBar.setProgress((int) millisUntilFinished);
                timer.setText("Time remaining: " + millisUntilFinished / 1000);
                countDownLeft = millisUntilFinished;
                if (millisUntilFinished < 10000)timer.setTextColor(Color.parseColor("#ff3200"));
            }

            public void onFinish() {
                progressBar.setProgress(0);
                timer.setText("Times up, you loose !");
                timer.setTextColor(Color.parseColor("red"));
                up.setClickable(false);
                down.setClickable(false);
                left.setClickable(false);
                right.setClickable(false);
                showToastMessage("Too slow!", 1500);
            }
        };
        countDown.start();
    }
    // ------------------ Handler for back press button ---------------- \\
    boolean twice;
    @Override
    public void onBackPressed() {
        if (twice){
            stopTheme();
            Intent mainMenuIntent = new Intent(MainActivity.this, StartupActivity.class);
            MainActivity.this.startActivity(mainMenuIntent);
            finish();
        }
        twice = true;
        showToastMessage("Go to main menu?", 500);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                twice = false;
            }
        }, 1500);
    }
    // ...................................................................... \\


    @Override
    protected void onPause() {
        super.onPause();
        if (mainTheme != null) mainTheme.pause();
        countDown.cancel();
        countDown = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if ((Globals.allowMusic) && (mainTheme != null)){
            mainTheme.start();
        }
        if (countDown == null) countDown = new CountDownTimer(countDownLeft, 1) {

            public void onTick(long millisUntilFinished) {
                progressBar.setProgress((int) millisUntilFinished);
                timer.setText("Time remaining: " + millisUntilFinished / 1000);
                countDownLeft = millisUntilFinished;
                if (millisUntilFinished < 10000)timer.setTextColor(Color.parseColor("#ff3200"));
            }

            public void onFinish() {
                progressBar.setProgress(0);
                timer.setText("Times up, you loose !");
                timer.setTextColor(Color.parseColor("red"));
                up.setClickable(false);
                down.setClickable(false);
                left.setClickable(false);
                right.setClickable(false);
                showToastMessage("Too slow!", 1500);
            }
        };
        countDown.start();    }

    private void makeBoard(){
        for (int i = 0; i < boardSize[Globals.level]; i++){
            for (int j = 0; j < boardSize[Globals.level]; j++){
                board[i][j].setBackgroundColor(Color.parseColor("#b29770"));
                char type = ' ';
                if (i == 0 || i == boardSize[Globals.level]-1 || j == 0 || j == boardSize[Globals.level]-1) type = '-';
                for (int y = 0; y < walls[Globals.level].length; y++){
                    if (i == walls[Globals.level][y][0] && j == walls[Globals.level][y][1]){
                        type = '-';
                    }
                }
                for (int z = 0; z < boxWinningPos[Globals.level].length; z++){
                    if (i == boxWinningPos[Globals.level][z][0] && j == boxWinningPos[Globals.level][z][1]){
                        board[i][j].setBackgroundColor(Color.parseColor("#4c9955"));
                        board[i][j].setEnabled(false);
                    }
                    if (i == boxStartingPos[Globals.level][z][0] && j == boxStartingPos[Globals.level][z][1]){
                        type = 'B';
                    }
                }
                board[i][j].setText("" + type);
                /*if (board[i][j].getText().equals("-")){
                    ShapeDrawable shapedrawable = new ShapeDrawable();
                    shapedrawable.setShape(new RectShape());
                    shapedrawable.getPaint().setColor(Color.rgb(91, 52, 0));
                    shapedrawable.getPaint().setStrokeWidth(3f);
                    shapedrawable.getPaint().setStyle(Paint.Style.STROKE);
                    board[i][j].setBackground(shapedrawable);
                    //board[i][j].setBackgroundColor(Color.parseColor("#492b00"));
                }*/
            }
        }
    }

    private void initBoardImages(){
        for (int i = 0; i < boardSize[Globals.level]; i++){
            for (int j = 0; j < boardSize[Globals.level]; j++){
                //board[i][j].setTextColor(Color.argb(0, 1, 1, 1)); TODO: fikse transparent player figur for å erstatte "x"'en !
                if (board[i][j].getText().equals("-")){
                    Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.wall);
                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, 103, 103, true);
                    Resources resource = getResources();   //TODO HVORFOR FUNKER DENNE STATISKE STØRRELSEN ?!?
                    board[i][j].setBackground(new BitmapDrawable(resource, scaledBitmap));
                    board[i][j].setTextColor(Color.argb(0, 1, 1, 1));
                }
                else if (board[i][j].getText().equals("B")){
                    Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.box);
                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, 103, 103, true);
                    Resources resource = getResources();
                    board[i][j].setBackground(new BitmapDrawable(resource, scaledBitmap));
                    board[i][j].setTextColor(Color.argb(0, 1, 1, 1));
                }
                /*else if (board[i][j].getText().equals("x")){  //TODO
                    Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.x);
                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, 103, 103, true);
                    Resources resource = getResources();
                    board[i][j].setBackground(new BitmapDrawable(resource, scaledBitmap));
                    //board[x][y].setTextColor(Color.argb(0, 1, 1, 1));
                }*/
            }
        }

    }

    private void populateButtons(){
        TableLayout table = (TableLayout) findViewById(R.id.tableForButtons);
        for(int row = 0; row < NUM_ROWS; row++){
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1.0f));
            table.addView(tableRow);
            for(int col = 0; col < NUM_COLS; col++){
                Button button = new Button(this);
                button.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT,
                        1.0f));
                button.setText("" + col + "," + row + ": ");    //Adding text to buttons.
                button.setPadding(0, 0, 0, 0);     //Make text fit in small buttons.

                tableRow.addView(button);
                board[row][col] = button;
            }
        }
    }
    // -------------- HELP FUNCTIONS --------------- \\

    public void setPlayerPos(int x, int y){
        this.playerPos[Globals.level][0] = x;
        this.playerPos[Globals.level][1] = y;
    }

    public Character getField(int x, int y){
        return board[x][y].getText().charAt(0);
    }

    public void setField(int x, int y, char type){
        board[x][y].setText("" + type);
        if (board[x][y].getText().equals("B")){
            Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.box);
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, 103, 103, true);
            Resources resource = getResources();
            board[x][y].setBackground(new BitmapDrawable(resource, scaledBitmap));
            board[x][y].setTextColor(Color.argb(0, 1, 1, 1));
        }
        else if (board[x][y].getText().equals(" ") && !(board[x][y].isEnabled())){
            board[x][y].setBackgroundColor(Color.parseColor("#4c9955"));
            board[x][y].setTextColor(Color.argb(0, 1, 1, 1));
        }
        else if (board[x][y].getText().equals(" ")){
            board[x][y].setBackgroundColor(Color.parseColor("#b29770"));
            board[x][y].setTextColor(Color.argb(0, 1, 1, 1));
        }
        else if (board[x][y].getText().equals("x")){
            if(board[x][y].isEnabled()){
                Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.x2);
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, 103, 103, true);
                Resources resource = getResources();
                board[x][y].setBackground(new BitmapDrawable(resource, scaledBitmap));
                board[x][y].setTextColor(Color.argb(0, 1, 1, 1));
                /*board[x][y].setBackgroundColor(Color.parseColor("#b29770"));
                board[x][y].setTextColor(Color.WHITE);*/
            }
            else{
                Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.xwin);
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, 103, 103, true);
                Resources resource = getResources();
                board[x][y].setBackground(new BitmapDrawable(resource, scaledBitmap));
                board[x][y].setTextColor(Color.argb(0, 1, 1, 1));
                /*board[x][y].setBackgroundColor(Color.parseColor("#4c9955"));
                board[x][y].setTextColor(Color.WHITE);*/
            }
        }
    }

    public int checkOpen(String direction){
        if (direction.equals("right")){
            for (int i = playerPos[Globals.level][1]; i < boardSize[Globals.level]; i++){
                if (getField(playerPos[Globals.level][0], i).equals('-')) return -1; //Stopper hvis den møter '-' før ' '
                else if (getField(playerPos[Globals.level][0], i).equals(' ')) return i;
            }
            return -1; //bare 'B' bokser
        }

        else if (direction.equals("left")){
            for (int i = playerPos[Globals.level][1]; i < boardSize[Globals.level]; i--){
                if (getField(playerPos[Globals.level][0], i).equals('-')) return -1; //Stopper hvis den møter '-' før ' '
                else if (getField(playerPos[Globals.level][0], i).equals(' ')) return i;
            }
            return -1; //bare 'B' bokser
        }

        else if (direction.equals("up")){
            for (int i = playerPos[Globals.level][0]; i < boardSize[Globals.level]; i--){
                if (getField(i, playerPos[Globals.level][1]).equals('-')) return -1; //Stopper hvis den møter '-' før ' '
                else if (getField(i, playerPos[Globals.level][1]).equals(' ')) return i;
            }
            return -1; //bare 'B' bokser
        }

        else if (direction.equals("down")){
            for (int i = playerPos[Globals.level][0]; i < boardSize[Globals.level]; i++){
                if (getField(i, playerPos[Globals.level][1]).equals('-')) return -1; //Stopper hvis den møter '-' før ' '
                else if (getField(i, playerPos[Globals.level][1]).equals(' ')) return i;
            }
            return -1; //bare 'B' bokser
        }
        return -1;
    }

    private boolean checkWin(){
        for (int i = 0; i < boxWinningPos[Globals.level].length; i++){
            if (!board[boxWinningPos[Globals.level][i][0]][boxWinningPos[Globals.level][i][1]].getText().equals("B")) return false;
        }
        boardWon();
        return true;
    }

    private void boardWon(){
        //Globals.level += 1; moved to levelWonActivity
        stopTheme();
        //stopSound(); TODO small bug

        Intent levelWonIntent = new Intent(MainActivity.this, LevelWonActivity.class);
        MainActivity.this.startActivity(levelWonIntent);
    }

    // ---------------- MOVES --------------------- \\
    private void moveRight(){
        if (checkOpen("right") > 0){
            if (getField(playerPos[Globals.level][0], playerPos[Globals.level][1] + 1).equals(' ')){
                setField(playerPos[Globals.level][0], playerPos[Globals.level][1], ' ');
                setPlayerPos(playerPos[Globals.level][0], playerPos[Globals.level][1] + 1);
                setField(playerPos[Globals.level][0], playerPos[Globals.level][1], 'x');
            }
            else{
                setField(playerPos[Globals.level][0], checkOpen("right"), 'B');

                setField(playerPos[Globals.level][0], playerPos[Globals.level][1], ' ');
                setPlayerPos(playerPos[Globals.level][0], playerPos[Globals.level][1] + 1);
                setField(playerPos[Globals.level][0], playerPos[Globals.level][1], 'x');
            }
            Globals.movesCount +=1;
            checkWin();
        }
    }

    private void moveLeft(){
        if (checkOpen("left") > 0){
            if (getField(playerPos[Globals.level][0], playerPos[Globals.level][1] - 1).equals(' ')){
                setField(playerPos[Globals.level][0], playerPos[Globals.level][1], ' ');
                setPlayerPos(playerPos[Globals.level][0], playerPos[Globals.level][1] - 1);
                setField(playerPos[Globals.level][0], playerPos[Globals.level][1], 'x');
            }
            else {
                setField(playerPos[Globals.level][0], checkOpen("left"), 'B');

                setField(playerPos[Globals.level][0], playerPos[Globals.level][1], ' ');
                setPlayerPos(playerPos[Globals.level][0], playerPos[Globals.level][1] - 1);
                setField(playerPos[Globals.level][0], playerPos[Globals.level][1], 'x');
            }
            Globals.movesCount +=1;
            checkWin();
        }
    }

    private void moveUp(){
        if (checkOpen("up") > 0){if (getField(playerPos[Globals.level][0] - 1, playerPos[Globals.level][1]).equals(' ')){
                setField(playerPos[Globals.level][0], playerPos[Globals.level][1], ' ');
                setPlayerPos(playerPos[Globals.level][0] - 1, playerPos[Globals.level][1]);
                setField(playerPos[Globals.level][0], playerPos[Globals.level][1], 'x');
            }
            else{
                setField(checkOpen("up"), playerPos[Globals.level][1], 'B');

                setField(playerPos[Globals.level][0], playerPos[Globals.level][1], ' ');
                setPlayerPos(playerPos[Globals.level][0] - 1, playerPos[Globals.level][1]);
                setField(playerPos[Globals.level][0], playerPos[Globals.level][1], 'x');
            }
            Globals.movesCount +=1;
            checkWin();
        }
    }

    private void moveDown(){
        if (checkOpen("down") > 0){
            if (getField(playerPos[Globals.level][0] + 1, playerPos[Globals.level][1]).equals(' ')){
                setField(playerPos[Globals.level][0], playerPos[Globals.level][1], ' ');
                setPlayerPos(playerPos[Globals.level][0] + 1, playerPos[Globals.level][1]);
                setField(playerPos[Globals.level][0], playerPos[Globals.level][1], 'x');
            }
            else{
                this.setField(checkOpen("down"), playerPos[Globals.level][1], 'B');

                setField(playerPos[Globals.level][0], playerPos[Globals.level][1], ' ');
                setPlayerPos(playerPos[Globals.level][0] + 1, playerPos[Globals.level][1]);
                setField(playerPos[Globals.level][0], playerPos[Globals.level][1], 'x');
            }
            Globals.movesCount +=1;
            checkWin();
        }
    }



    // ---------------- Toast message --------------------- \\
    public void showToastMessage(String text, int duration){
        final Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        toast.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, duration);
    }
}
