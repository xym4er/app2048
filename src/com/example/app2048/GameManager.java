package com.example.app2048;

import android.graphics.Canvas;

/**
 * Created by ChornyiUA on 09.11.2015.
 */
public class GameManager {
    public enum direction {UP, DOWN, LEFT, RIGHT, STOP}
    private CanvasView canvasView;
    private int width;
    private int height;

    public GameManager(CanvasView canvasView, int width, int height) {
        this.canvasView=canvasView;
        this.width=width;
        this.height=height;
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
