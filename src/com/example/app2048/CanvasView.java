package com.example.app2048;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class CanvasView extends View {

    private Canvas canvas;
    public static final int BACKGROUND_START_X = 30;
    public static final int BACKGROUND_START_Y = 300;
    public static final int BACKGROUND_WIDTH = 1020;
    public static final int PADDING = 20;
    public static final int CELL_WIDTH = 230;
    private int AtTouchStartX = 0;
    private int AtTouchStartY = 0;
    private int finishX = 0;
    private int finishY = 0;
    private static int width;
    private static int height;
    private GameManager gameManager;
    private Cell[][] backgroundCells = new Cell[4][4];
    private Paint paint;
    private RectF backgroundRectF = new RectF(BACKGROUND_START_X, BACKGROUND_START_Y, BACKGROUND_START_X + BACKGROUND_WIDTH, BACKGROUND_START_Y + BACKGROUND_WIDTH);


    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWidthAndHeight(context);
        gameManager = new GameManager(this, width, height);
        initPaint();
        initField();
    }

    private void initWidthAndHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        width = point.x;
        height = point.y;
    }

    private void initPaint() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRGB(210, 214, 219);
        paint.setColor(Color.rgb(147, 167, 175));
        canvas.drawRoundRect(backgroundRectF, 40, 40, paint);
        paint.setColor(Color.rgb(205, 214, 219));
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                canvas.drawRoundRect(backgroundCells[i][j].getRectF(), 20, 20, paint);
            }
        }
        gameManager.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            AtTouchStartX = (int) event.getX();
            AtTouchStartY = (int) event.getY();
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            finishX = (int) event.getX();
            finishY = (int) event.getY();
            gameManager.onTouchEvent(AtTouchStartX, AtTouchStartY, finishX, finishY);
        }
        invalidate();
        return true;
    }



    private void initField() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                backgroundCells[i][j] = new Cell(BACKGROUND_START_X + PADDING + (PADDING + CELL_WIDTH) * i, BACKGROUND_START_Y + PADDING + (PADDING + CELL_WIDTH) * j);
            }
        }
    }
}