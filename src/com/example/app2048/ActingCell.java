package com.example.app2048;

/**
 * Created by ChornyiUA on 11.11.2015.
 */
public class ActingCell extends Cell {
    private int value;
    private boolean isVoid;

    public ActingCell(int x, int y) {
        super(x, y);
        value = 2;
        isVoid = true;
    }

    public void doubleValue() {
        value = value * 2;
    }

    public int getValue() {
        return value;
    }
}
