package com.example.app2048;

import android.graphics.Canvas;

import java.util.Random;

/**
 * Created by ChornyiUA on 09.11.2015.
 */
public class GameManager {
    public enum direction {UP, DOWN, LEFT, RIGHT, STOP}

    private CanvasView canvasView;
    private int width;
    private int height;
    private ActingCell[][] actingCells;
    final Random random = new Random();
    private int randomI;
    private int randomJ;

    public ActingCell[][] getActingCells() {
        return actingCells;
    }

    public GameManager(CanvasView canvasView, int width, int height) {
        this.canvasView = canvasView;
        this.width = width;
        this.height = height;

        initActingCells();
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
                if (haveFreeCell()) {
                    makeRandomActingCell();
                }
                break;
            case DOWN:
                swipeDown();
                if (haveFreeCell()) {
                    makeRandomActingCell();
                }
                break;
            case LEFT:
                swipeLeft();
                if (haveFreeCell()) {
                    makeRandomActingCell();
                }
                break;
            case RIGHT:
                swipeRight();
                if (haveFreeCell()) {
                    makeRandomActingCell();
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
            // ѕровер€ема€ (опорна€) и текуща€ €чейки
            int pivot = 3, row = 2;

            while (row >= 0) {
                // “екуща€ €чейка пуста, переходим на следующую
                if (actingCells[row][col].getValue() == 0) {
                    row--;
                    // ќпорна€ €чейка пуста, переносим в нее значение текущей
                } else if (actingCells[pivot][col].getValue() == 0) {
                    actingCells[pivot][col].setValue(actingCells[row][col].getValue());
                    actingCells[row--][col].setValue(0);
                }
                // «начени€ опорной и текущей €чеек совпадают Ч складываем их и переходим на следующую строчку
                else if (actingCells[pivot][col].getValue() == actingCells[row][col].getValue()) {
                    actingCells[pivot--][col].doubleValue();
                    actingCells[row--][col].setValue(0);
                }
                // Ќечего двигать Ч едем дальше
                else if (--pivot == row) {
                    row--;
                }

            }
        }
    }

    private void swipeLeft() {
        for (int col = 0; col < 4; col++) {
            // ѕровер€ема€ (опорна€) и текуща€ €чейки
            int pivot = 0, row = 1;

            while (row < 4) {
                // “екуща€ €чейка пуста, переходим на следующую
                if (actingCells[row][col].getValue() == 0) {
                    row++;
                    // ќпорна€ €чейка пуста, переносим в нее значение текущей
                } else if (actingCells[pivot][col].getValue() == 0) {
                    actingCells[pivot][col].setValue(actingCells[row][col].getValue());
                    actingCells[row++][col].setValue(0);
                }
                // «начени€ опорной и текущей €чеек совпадают Ч складываем их и переходим на следующую строчку
                else if (actingCells[pivot][col].getValue() == actingCells[row][col].getValue()) {
                    actingCells[pivot++][col].doubleValue();
                    actingCells[row++][col].setValue(0);
                }
                // Ќечего двигать Ч едем дальше
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
            // ѕровер€ема€ (опорна€) и текуща€ €чейки
            pivot = 3;
            row = 2;

            while (row >= 0) {
                // “екуща€ €чейка пуста, переходим на следующую
                if (actingCells[col][row].getValue() == 0) {
                    row--;
                    // ќпорна€ €чейка пуста, переносим в нее значение текущей
                } else if (actingCells[col][pivot].getValue() == 0) {
                    actingCells[col][pivot].setValue(actingCells[col][row].getValue());
                    actingCells[col][row].setValue(0);
                    actingCells[col][row--].setMoving(true);
                }
                // «начени€ опорной и текущей €чеек совпадают Ч складываем их и переходим на следующую строчку
                else if (actingCells[col][pivot].getValue() == actingCells[col][row].getValue()) {
                    actingCells[col][pivot--].doubleValue();
                    actingCells[col][row].setValue(0);
                    actingCells[col][row--].setMoving(true);
                }
                // Ќечего двигать Ч едем дальше
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
            // ѕровер€ема€ (опорна€) и текуща€ €чейки
            pivot = 0;
            row = 1;

            while (row < 4) {
                // “екуща€ €чейка пуста, переходим на следующую
                if (actingCells[col][row].getValue() == 0) {
                    row++;
                    // ќпорна€ €чейка пуста, переносим в нее значение текущей
                } else if (actingCells[col][pivot].getValue() == 0) {
                    actingCells[col][pivot].setValue(actingCells[col][row].getValue());
                    actingCells[col][row].setMoving(true);
                    actingCells[col][row++].setValue(0);

                }
                // «начени€ опорной и текущей €чеек совпадают Ч складываем их и переходим на следующую строчку
                else if (actingCells[col][pivot].getValue() == actingCells[col][row].getValue()) {
                    actingCells[col][pivot++].doubleValue();
                    actingCells[col][row].setValue(0);
                    actingCells[col][row++].setMoving(true);
                }
                // Ќечего двигать Ч едем дальше
                else if (++pivot == row) {
                    row++;
                }

            }
        }
    }

    public void onDraw(Canvas canvas) {

    }

    public direction checkDirection(int startX, int startY, int finishX, int finishY) {
        if ((Math.abs(startY - finishY) > 50) && (Math.abs(startY - finishY) > Math.abs(startX - finishX))) {
            if (startY > finishY) {
                return direction.UP;
            }
            if (startY < finishY) {
                return direction.DOWN;
            }
        }
        if ((Math.abs(startX - finishX) > 50) && (Math.abs(startY - finishY) < Math.abs(startX - finishX))) {
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
