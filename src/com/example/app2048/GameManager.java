package com.example.app2048;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Random;

/**
 * Created by ChornyiUA on 09.11.2015.
 */
public class GameManager {
    public enum direction {UP, DOWN, LEFT, RIGHT, STOP}

    private ActingCell[][] actingCells;
    private final Random random = new Random();
    private boolean canMakeNewCell;
    private int randomI;
    private int randomJ;
    private Context context;
    private HashMap<ActingCell,ActingCell> cellsToMove = new HashMap<>();
    private HashMap<ActingCell,ActingCell> cellsToAdd = new HashMap<>();

    public GameManager(Context context) {
        this.context=context;
        initActingCells();
        canMakeNewCell = false;
    }

    public ActingCell[][] getActingCells() {
        return actingCells;
    }

    private void initActingCells() {
        actingCells = new ActingCell[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                actingCells[i][j] = new ActingCell(CanvasView.BACKGROUND_START_X + CanvasView.PADDING + (CanvasView.PADDING + CanvasView.CELL_WIDTH) * i, CanvasView.BACKGROUND_START_Y + CanvasView.PADDING + (CanvasView.PADDING + CanvasView.CELL_WIDTH) * j);
            }
        }
        makeRandomActingCell();
    }

    private void makeRandomActingCell() {
        while (true) {
            randomI = random.nextInt(4);
            randomJ = random.nextInt(4);
            if (actingCells[randomI][randomJ].getValue() == 0) {
                if (random.nextInt(10) > 8) {
                    actingCells[randomI][randomJ].setValue(4);
                    return;
                } else {
                    actingCells[randomI][randomJ].setValue(2);
                    return;
                }
            }
        }
    }

    public void onTouchEvent(int startX, int startY, int finishX, int finishY) {
        switch (checkDirection(startX, startY, finishX, finishY)) {
            case UP:
                swipeUp();
                if (haveFreeCell() & canMakeNewCell) {
                    makeRandomActingCell();
                    canMakeNewCell = false;
                }
                break;
            case DOWN:
                swipeDown();
                if (haveFreeCell() & canMakeNewCell) {
                    makeRandomActingCell();
                    canMakeNewCell = false;
                }
                break;
            case LEFT:
                swipeLeft();
                if (haveFreeCell() & canMakeNewCell) {
                    makeRandomActingCell();
                    canMakeNewCell = false;
                }
                break;
            case RIGHT:
                swipeRight();
                if (haveFreeCell() & canMakeNewCell) {
                    makeRandomActingCell();
                    canMakeNewCell = false;
                }
                break;
            case STOP:
                break;
        }
    }

    private boolean haveFreeCell() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (actingCells[i][j].getValue() == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private void swipeRight() {
        for (int col = 0; col < 4; col++) {
            // Проверяемая (опорная) и текущая ячейки
            int pivot = 3, row = 2;
            while (row >= 0) {
                // Текущая ячейка пуста, переходим на следующую
                if (actingCells[row][col].getValue() == 0) {
                    row--;
                    // Опорная ячейка пуста, переносим в нее значение текущей
                } else if (actingCells[pivot][col].getValue() == 0) {
                    cellsToMove.put(actingCells[row][col],actingCells[pivot][col]);
                    actingCells[pivot][col].setValue(actingCells[row][col].getValue());
                    actingCells[row--][col].setValue(0);
                    canMakeNewCell = true;
                }
                // Значения опорной и текущей ячеек совпадают — складываем их и переходим на следующую строчку
                else if (actingCells[pivot][col].getValue() == actingCells[row][col].getValue()) {
                    //adding(actingCells[row][col] to actingCells[pivot][col]);
                    actingCells[pivot][col].doubleValue();
                    if (actingCells[pivot][col].getValue() >= 64 ){
                        gameWon(actingCells[pivot][col].getValue());
                    }
                    pivot--;
                    actingCells[row--][col].setValue(0);
                    canMakeNewCell = true;
                }
                // Нечего двигать — едем дальше
                else if (--pivot == row) {
                    row--;
                }
            }
        }
    }

    private void gameWon(int value) {
        Toast toast = Toast.makeText(context,"Поздравляю! Вы набрали "+value, Toast.LENGTH_LONG);
        toast.show();
    }

    private void swipeLeft() {
        for (int col = 0; col < 4; col++) {
            // Проверяемая (опорная) и текущая ячейки
            int pivot = 0, row = 1;
            while (row < 4) {
                // Текущая ячейка пуста, переходим на следующую
                if (actingCells[row][col].getValue() == 0) {
                    row++;
                    // Опорная ячейка пуста, переносим в нее значение текущей
                } else if (actingCells[pivot][col].getValue() == 0) {
                    actingCells[pivot][col].setValue(actingCells[row][col].getValue());
                    actingCells[row++][col].setValue(0);
                    canMakeNewCell = true;
                }
                // Значения опорной и текущей ячеек совпадают — складываем их и переходим на следующую строчку
                else if (actingCells[pivot][col].getValue() == actingCells[row][col].getValue()) {
                    actingCells[pivot][col].doubleValue();
                    if (actingCells[pivot][col].getValue() >= 64 ){
                        gameWon(actingCells[pivot][col].getValue());
                    }
                    pivot++;
                    actingCells[row++][col].setValue(0);
                    canMakeNewCell = true;
                }
                // Нечего двигать — едем дальше
                else if (++pivot == row) {
                    row++;
                }
            }
        }
    }

    private void swipeDown() {
        int pivot;
        int row;
        for (int col = 0; col < 4; col++) {
            // Проверяемая (опорная) и текущая ячейки
            pivot = 3;
            row = 2;
            while (row >= 0) {
                // Текущая ячейка пуста, переходим на следующую
                if (actingCells[col][row].getValue() == 0) {
                    row--;
                    // Опорная ячейка пуста, переносим в нее значение текущей
                } else if (actingCells[col][pivot].getValue() == 0) {
                    actingCells[col][pivot].setValue(actingCells[col][row].getValue());
                    actingCells[col][row].setValue(0);
                    actingCells[col][row--].setMoving(true);
                    canMakeNewCell = true;
                }
                // Значения опорной и текущей ячеек совпадают — складываем их и переходим на следующую строчку
                else if (actingCells[col][pivot].getValue() == actingCells[col][row].getValue()) {
                    actingCells[col][pivot].doubleValue();
                    if (actingCells[pivot][col].getValue() >= 2048 ){
                        gameWon(actingCells[pivot][col].getValue());
                    }
                    pivot--;
                    actingCells[col][row].setValue(0);
                    actingCells[col][row--].setMoving(true);
                    canMakeNewCell = true;
                }
                // Нечего двигать — едем дальше
                else if (--pivot == row) {
                    row--;
                }
            }
        }
    }

    private void swipeUp() {
        int pivot;
        int row;
        for (int col = 0; col < 4; col++) {
            // Проверяемая (опорная) и текущая ячейки
            pivot = 0;
            row = 1;
            while (row < 4) {
                // Текущая ячейка пуста, переходим на следующую
                if (actingCells[col][row].getValue() == 0) {
                    row++;
                // Опорная ячейка пуста, переносим в нее значение текущей
                } else if (actingCells[col][pivot].getValue() == 0) {
                    actingCells[col][pivot].setValue(actingCells[col][row].getValue());
                    actingCells[col][row].setMoving(true);
                    actingCells[col][row++].setValue(0);
                    canMakeNewCell = true;
                }
                // Значения опорной и текущей ячеек совпадают — складываем их и переходим на следующую строчку
                else if (actingCells[col][pivot].getValue() == actingCells[col][row].getValue()) {
                    actingCells[col][pivot].doubleValue();
                    if (actingCells[pivot][col].getValue() >= 64 ){
                        gameWon(actingCells[pivot][col].getValue());
                    }
                    pivot++;
                    actingCells[col][row].setValue(0);
                    actingCells[col][row++].setMoving(true);
                    canMakeNewCell = true;
                }
                // Нечего двигать — едем дальше
                else if (++pivot == row) {
                    row++;
                }
            }
        }
    }

    public direction checkDirection(int startX, int startY, int finishX, int finishY) {
        if ((Math.abs(startY - finishY) > CanvasView.CELL_WIDTH*0.3) && (Math.abs(startY - finishY) > Math.abs(startX - finishX))) {
            if (startY > finishY) {
                return direction.UP;
            }
            if (startY < finishY) {
                return direction.DOWN;
            }
        }
        if ((Math.abs(startX - finishX) > CanvasView.CELL_WIDTH*0.3) && (Math.abs(startY - finishY) <= Math.abs(startX - finishX))) {
            if (startX > finishX) {
                return direction.LEFT;
            }
            if (startX < finishX) {
                return direction.RIGHT;
            }
        }
        return direction.STOP;
    }
}
