package com.example.app2048;

/**
 * Created by ChornyiUA on 11.11.2015.
 */
public class ActingCell extends Cell {
    private int value;
    private boolean moving;

    public boolean isMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public ActingCell(int x, int y) {
        super(x, y);
        value = 0;
    }

    public void doubleValue() {
        value = value * 2;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
