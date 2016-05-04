package com.example.ben.mazegame;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.content.ContextCompat;
import android.app.Activity;
import android.view.View;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.content.Context;
import java.text.SimpleDateFormat;
import java.util.Date;


public class GameView extends View implements SensorEventListener {

    private Maze maze;
    private Activity context;
    private Paint line, ball, mazeBackground;
    private int mazeWidth, mazeHeight;
    private int mazeHorizontalCells, mazeVerticalCells;
    private int screenWidth, screenHeight;
    private int mazeEndPosX, mazeEndPosY;
    private int delta, deltaDelay;  // delta for catching sensor events
    private float totalCellWidth, totalCellHeight;
    private float cellWidth, cellHeight;
    private SensorManager manager;
    private Sensor accelerometer;
    private float [] history = new float[2];  // history of x and y position of the phone
    private long delay, startTime, currentScore, bestScore;  // times in millis
    private AppEntryBestScoresDAL dal;

    public GameView(Context context, Maze maze) {
        super(context);
        // initialize maze components
        this.context = (Activity)context;
        this.maze = maze;
        mazeEndPosX = maze.getMazeEndXPos();
        mazeEndPosY = maze.getMazeEndYPos();
        mazeHorizontalCells = maze.getNumOfHorizontalCells();
        mazeVerticalCells   = maze.getNumOfVerticalCells();
        line = new Paint();
        ball = new Paint();
        mazeBackground = new Paint();
        line.setColor(ContextCompat.getColor(context, R.color.black));
        ball.setColor(ContextCompat.getColor(context, R.color.blue));
        mazeBackground.setColor(ContextCompat.getColor(context, R.color.gray));

        // initialize sensor components
        delta = context.getResources().getInteger(R.integer.delta);
        deltaDelay = context.getResources().getInteger(R.integer.deltaDelay);
        manager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = manager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
        delay = System.currentTimeMillis();

        // initialize DB components
        dal = new AppEntryBestScoresDAL(context);
        bestScore = dal.getBestScore(maze.getLevel());

        setFocusable(true);
        this.setFocusableInTouchMode(true);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        // change all the widths and heights accordingly to the change
        screenWidth = w;
        screenHeight = h;
        if (screenWidth < screenHeight) {
            mazeWidth = mazeHeight = screenWidth;
        }
        else {
            mazeWidth = mazeHeight = screenHeight;
        }

        int wallWidth = 1;

        cellWidth = (mazeWidth - mazeHorizontalCells) / mazeHorizontalCells;
        totalCellWidth = cellWidth+wallWidth;

        cellHeight = (mazeHeight - mazeVerticalCells) / mazeVerticalCells;
        totalCellHeight = cellHeight+wallWidth;

        ball.setTextSize(cellHeight * 0.75f);
        super.onSizeChanged(w, h, oldw, oldh);
    }

    protected void onDraw(Canvas canvas) {
        int mazeLocCorrection = getResources().getInteger(R.integer.mazeLocCorrection);

        // Draw the screen background.
        Drawable screenBackground = ContextCompat.getDrawable(context, R.drawable.background);
        screenBackground.setBounds(0, 0, screenWidth, screenHeight);
        screenBackground.draw(canvas);

        //fill in the background
        canvas.drawRect(mazeLocCorrection, 0, mazeLocCorrection+mazeWidth, mazeHeight,
                mazeBackground);

        boolean[][] hLines = maze.getHorizontalLines();
        boolean[][] vLines = maze.getVerticalLines();

        // Draw the walls
        for(int i = 0; i < mazeHorizontalCells; i++) {
            for(int j = 0; j < mazeVerticalCells; j++){
                float x = j * totalCellWidth + mazeLocCorrection;
                float y = i * totalCellHeight;
                if(j < mazeHorizontalCells - 1 && vLines[i][j]) {
                    // Draw a vertical line
                    canvas.drawLine(
                            x + cellWidth,   //start X
                            y,               //start Y
                            x + cellWidth,   //stop X
                            y + cellHeight,  //stop Y
                            line);
                }
                if(i < mazeVerticalCells - 1 && hLines[i][j]) {
                    // Draw a horizontal line
                    canvas.drawLine(x,       //startX
                            y + cellHeight,  //startY
                            x + cellWidth,   //stopX
                            y + cellHeight,  //stopY
                            line);
                }
            }
        }

        // Draw the ball
        int currentX = maze.getBallXPos();
        int currentY = maze.getBallYPos();
        canvas.drawCircle(
                (currentX * totalCellWidth) + (cellWidth / 2) + mazeLocCorrection, //x of center
                (currentY * totalCellHeight) + (cellWidth / 2),  //y of center
                (cellWidth * 0.45f),                           //radius
                ball);

        // Draw the finishing point indicator
        canvas.drawText("F",
                (mazeEndPosX * totalCellWidth) + (cellWidth * 0.25f) + mazeLocCorrection,
                (mazeEndPosY * totalCellHeight) + (cellHeight * 0.75f),
                ball);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // check the change of x and y positions of the phone
        float xChange = history[0] - event.values[0];
        float yChange = history[1] - event.values[1];

        history[0] -= event.values[0];
        history[1] -= event.values[1];

        // if the delay has passed
        if (System.currentTimeMillis() > delay + deltaDelay) {
            // save the current x and y position of the phone
            history[0] = event.values[0];
            history[1] = event.values[1];

            boolean moved = false;

            // check if the ball should move
            if (xChange > delta){
                moved = maze.move(Maze.UP);
            }
            else if (xChange < -delta){
                moved = maze.move(Maze.DOWN);
            }
            // only one move at a time
            if (moved) {
                invalidate();
                yChange = 0;
            }

            if (yChange > delta){
                moved = maze.move(Maze.LEFT);
            }
            else if (yChange < -delta) {
                moved = maze.move(Maze.RIGHT);
            }

            // if moved then repaint the new location
            if (moved) {
                invalidate();
            }

            // save the current time to calculate the delay
            delay = System.currentTimeMillis();
        }

        // if the ball reached the finishing point than check if he maid new higher score
        // and if so, save it to the DB.
        // unregister the sensor listener to prevent battery cost.
        if (maze.isGameComplete()) {
            currentScore = calculateScore();
            if (currentScore < bestScore || bestScore == 0) {
                bestScore = currentScore;
                dal.addBestScoreEntry(bestScore, maze.getLevel());
            }
            manager.unregisterListener(this);
            showGameCompleteMessage();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }

    // when attaching the view to some activity than start listening to the sensor
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        manager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        startTime = System.currentTimeMillis();
    }

    // when detaching the view from some activity than stop listening to the sensor
    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        manager.unregisterListener(this);
    }

    // show the game complete message with the current score and the best score
    private void showGameCompleteMessage() {
        SimpleDateFormat fmt = new SimpleDateFormat("mm:ss:SS");
        Date currentScoreDate = new Date(currentScore);
        Date bestScoreDate = new Date(bestScore);
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(context);
        dlgAlert.setMessage("You did it in " + fmt.format(currentScoreDate) + "\n" +
                            "Your Best Time is " + fmt.format(bestScoreDate));
        dlgAlert.setTitle("Great Job!");
        dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // go to the previous activity
                context.onBackPressed();
            }
        });
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }

    // calculate the score according to the attached start time of the view
    private long calculateScore() {
        currentScore = System.currentTimeMillis() - startTime;
        return currentScore;
    }
}
