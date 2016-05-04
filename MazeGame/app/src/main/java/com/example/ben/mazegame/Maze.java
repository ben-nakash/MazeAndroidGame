package com.example.ben.mazegame;

import android.content.Context;
import java.io.Serializable;

public class Maze implements Serializable {
    private static final long serialVersionUID = 1L;

    // public directions to be consistent
    public static int UP, DOWN, RIGHT, LEFT;

    private boolean[][] verticalLines;
    private boolean[][] horizontalLines;
    private int ballX, ballY;
    private int mazeEndXPos, mazeEndYPos;
    private int level;
    private boolean gameComplete;
    private transient Context context;

    // constructor that gets the maze number and generates the correct maze number
    public Maze(Context context, int mazeNumber) {
        this.context = context;
        level = mazeNumber;
        switch (mazeNumber) {
            case 1:
                generateMaze1();
                break;
            case 2:
                generateMaze2();
                break;
            case 3:
                generateMaze3();
                break;
        }
        setStartPosition(mazeNumber);
        setMazeEndPosition(mazeNumber);
        setDirections(context);
    }

    // Getters
    public int getNumOfHorizontalCells() {
        return horizontalLines[0].length;
    }
    public int getNumOfVerticalCells() {
        return verticalLines.length;
    }
    public boolean isGameComplete() {
        return gameComplete;
    }
    public int getMazeEndXPos() {
        return mazeEndXPos;
    }
    public int getMazeEndYPos() {
        return mazeEndYPos;
    }
    public int getBallXPos() {
        return ballX;
    }
    public int getBallYPos() {
        return ballY;
    }
    public int getLevel() {
        return level;
    }
    public boolean[][] getHorizontalLines() {
        return horizontalLines;
    }
    public boolean[][] getVerticalLines() {
        return verticalLines;
    }

    // generate the first maze
    private void generateMaze1() {
        verticalLines = new boolean[][]{
                {true ,false,false,false,true ,false,false},
                {true ,false,false,true ,false,true ,true },
                {false,true ,false,false,true ,false,false},
                {false,true ,true ,false,false,false,true },
                {true ,false,false,false,true ,true ,false},
                {false,true ,false,false,true ,false,false},
                {false,true ,true ,true ,true ,true ,false},
                {false,false,false,true ,false,false,false}
        };
        horizontalLines = new boolean[][]{
                {false,false,true ,true ,false,false,true ,false},
                {false,false,true ,true ,false,true ,false,false},
                {true ,true ,false,true ,true ,false,true ,true },
                {false,false,true ,false,true ,true ,false,false},
                {false,true ,true ,true ,true ,false,true ,true },
                {true ,false,false,true ,false,false,true ,false},
                {false,true ,false,false,false,true ,false,true }
        };
    }

    // generate the second maze
    private void generateMaze2() {
        verticalLines = new boolean[][]{
                {false,false,false,true ,false,false,false},
                {false,false,true ,false,true ,false,false},
                {false,false,true ,true ,false,false,false},
                {false,false,true ,true ,true ,false,false},
                {false,false,true ,false,true ,false,false},
                {true ,false,false,true ,false,true ,false},
                {true ,false,true ,true ,false,false,false},
                {false,false,true ,false,false,false,true }
        };
        horizontalLines = new boolean[][]{
                {false,true ,true ,true ,false,true ,true ,true },
                {true ,true ,false,false,true ,true ,true ,false},
                {false,true ,true ,false,false,false,true ,true },
                {true ,true ,false,false,false,true ,true ,false},
                {false,true ,true ,true ,true ,false,true ,false},
                {false,false,true ,false,false,true ,true ,true },
                {false,true ,false,false,true ,true ,false,false}
        };
    }

    // generate the third maze
    private void generateMaze3() {
        verticalLines = new boolean[][]{
                {false,false,true ,false,false,false,true ,false,false,false,false,false},
                {false,true ,false,false,false,true ,false,false,false,false,true ,true },
                {true ,false,false,false,false,true ,false,false,false,false,true ,true },
                {true ,true ,false,false,false,true ,true ,true ,false,false,true ,true },
                {true ,true ,true ,false,false,true ,true ,false,true ,false,true ,true },
                {false,true ,true ,true ,false,true ,false,false,false,true ,false,false},
                {false,false,false,true ,false,true ,false,true ,false,false,false,false},
                {false,false,true ,false,true ,false,true ,true ,false,true ,false,false},
                {true ,true ,true ,true ,false,true ,true ,false,false,true ,false,false},
                {false,false,false,true ,false,false,true ,true ,false,true ,true ,false},
                {false,false,true ,false,true ,false,true ,false,false,false,false,false},
                {true ,true ,true ,true ,true ,true ,true ,false,false,true ,false,false},
                {false,false,true ,false,false,true ,false,false,false,false,true ,false}
        };
        horizontalLines = new boolean[][]{
                {true ,false,false,true ,true ,false,false,false,true ,true ,true ,true ,false},
                {false,true ,true ,true ,true ,true ,true ,true ,true ,true ,false,false,false},
                {false,false,true ,true ,true ,false,false,true ,true ,true ,true ,false,false},
                {false,false,false,true ,true ,true ,false,false,false,true ,false,false,false},
                {false,false,false,false,true ,false,false,true ,true ,true ,false,false,false},
                {true ,true ,false,false,false,true ,true ,true ,true ,false,true ,true ,true },
                {false,true ,true ,true ,true ,true ,false,false,false,true ,true ,true ,false},
                {true ,false,false,false,true ,false,true ,false,true ,false,false,true ,true },
                {false,true ,false,false,false,true ,false,true ,true ,true ,true ,true ,false},
                {true ,true ,false,true ,false,true ,true ,false,false,true ,false,true ,false},
                {false,true ,true ,false,true ,false,false,true ,true ,false,true ,true ,true },
                {false,true ,false,false,true ,false,false,true ,true ,true ,false,false,true }
        };
    }

    // set the starting postion of the ball according to the maze number
    public void setStartPosition(int mazeNumber) {
        switch (mazeNumber) {
            case 1:
                ballX = context.getResources().getInteger(R.integer.maze1StartPosX);
                ballY = context.getResources().getInteger(R.integer.maze1StartPosY);
                break;
            case 2:
                ballX = context.getResources().getInteger(R.integer.maze2StartPosX);
                ballY = context.getResources().getInteger(R.integer.maze2StartPosY);
                break;
            case 3:
                ballX = context.getResources().getInteger(R.integer.maze3StartPosX);
                ballY = context.getResources().getInteger(R.integer.maze3StartPosY);
                break;
        }
    }

    // set the ending position of the maze according to the maze number
    public void setMazeEndPosition(int mazeNumber) {
        switch (mazeNumber) {
            case 1:
                mazeEndXPos = context.getResources().getInteger(R.integer.maze1EndPosX);
                mazeEndYPos = context.getResources().getInteger(R.integer.maze1EndPosY);
                break;
            case 2:
                mazeEndXPos = context.getResources().getInteger(R.integer.maze2EndPosX);
                mazeEndYPos = context.getResources().getInteger(R.integer.maze2EndPosY);
                break;
            case 3:
                mazeEndXPos = context.getResources().getInteger(R.integer.maze3EndPosX);
                mazeEndYPos = context.getResources().getInteger(R.integer.maze3EndPosY);
                break;
        }
    }

    // move the ball if it's not hitting a wall or the end of the screen
    public boolean move(int direction) {
        boolean moved = false;
        if(direction == UP) {
            if(ballY != 0 && !horizontalLines[ballY -1][ballX]) {
                ballY--;
                moved = true;
            }
        }
        if(direction == DOWN) {
            if(ballY != verticalLines.length -1 && !horizontalLines[ballY][ballX]) {
                ballY++;
                moved = true;
            }
        }
        if(direction == RIGHT) {
            if(ballX != horizontalLines[0].length -1 && !verticalLines[ballY][ballX]) {
                ballX++;
                moved = true;
            }
        }
        if(direction == LEFT) {
            if(ballX != 0 && !verticalLines[ballY][ballX -1]) {
                ballX--;
                moved = true;
            }
        }
        if(moved) {
            if(ballX == mazeEndXPos && ballY == mazeEndYPos) {
                gameComplete = true;
            }
        }
        return moved;
    }

    // set the directions number for easier use
    private void setDirections(Context context) {
        UP = context.getResources().getInteger(R.integer.UP);
        DOWN = context.getResources().getInteger(R.integer.DOWN);
        RIGHT = context.getResources().getInteger(R.integer.RIGHT);
        LEFT = context.getResources().getInteger(R.integer.LEFT);
    }
}
