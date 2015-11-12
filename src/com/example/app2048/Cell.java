package com.example.app2048;

import android.graphics.RectF;

/**
 * Created by ChornyiUA on 09.11.2015.
 */
public class Cell {
    protected int x;
    protected int y;
    protected RectF rectF;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        rectF = new RectF(x, y, x + CanvasView.CELL_WIDTH, y + CanvasView.CELL_WIDTH);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setRectF(RectF rectF) {
        this.rectF = rectF;
    }

    public RectF getRectF() {
        return rectF;
    }

}
