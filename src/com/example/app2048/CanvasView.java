package com.example.app2048;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import java.util.HashMap;

public class CanvasView extends View {

    private Canvas canvas;
    public static int BACKGROUND_START_X;
    public static int BACKGROUND_START_Y;
    public static int BACKGROUND_WIDTH;
    public static int PADDING;
    public static int CELL_WIDTH;
    private int AtTouchStartX = 0;
    private int AtTouchStartY = 0;
    private int finishX = 0;
    private int finishY = 0;
    private static int width;
    private static int height;
    private GameManager gameManager;
    private Cell[][] backgroundCells = new Cell[4][4];
    private HashMap<Integer, Integer> colorMapForCell = new HashMap<Integer, Integer>();
    private HashMap<Integer, Integer> colorMapForText = new HashMap<Integer, Integer>();
    private Paint paint;
    private RectF backgroundRectF;


    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initWidthAndHeight(context);
        BACKGROUND_START_X = (int) (width * 0.025);
        BACKGROUND_START_Y = (int) (height * 0.25);
        PADDING = (int) (width * 0.02);
        BACKGROUND_WIDTH = (int) (width * 0.95);
        CELL_WIDTH = (int) (width * 0.2125);
        backgroundRectF = new RectF(BACKGROUND_START_X, BACKGROUND_START_Y, BACKGROUND_START_X + BACKGROUND_WIDTH, BACKGROUND_START_Y + BACKGROUND_WIDTH);

        gameManager = new GameManager(this, width, height);
        initPaint();
        initField();
        initColorMapForCells();
        initColorMapForText();
    }

    private void initColorMapForCells() {
        colorMapForCell.put(0, 0x00000000);
        colorMapForCell.put(2, Color.rgb(239, 154, 154));
        colorMapForCell.put(4, Color.rgb(179, 157, 219));
        colorMapForCell.put(8, Color.rgb(129, 212, 250));
        colorMapForCell.put(16, Color.rgb(165, 214, 167));
        colorMapForCell.put(32, Color.rgb(255, 245, 57));
        colorMapForCell.put(64, Color.rgb(255, 171, 145));
        colorMapForCell.put(128, Color.rgb(240, 98, 146));
        colorMapForCell.put(256, Color.rgb(121, 134, 203));
        colorMapForCell.put(512, Color.rgb(77, 208, 225));
        colorMapForCell.put(1024, Color.rgb(174, 213, 129));
        colorMapForCell.put(2048, Color.rgb(255, 213, 79));
        colorMapForCell.put(4096, Color.rgb(142, 36, 170));
        colorMapForCell.put(8192, Color.rgb(30, 136, 229));
        colorMapForCell.put(16384, Color.rgb(0, 137, 123));
        colorMapForCell.put(32768, Color.rgb(192, 202, 51));
        colorMapForCell.put(65536, Color.rgb(251, 140, 0));
        colorMapForCell.put(131072, Color.rgb(78, 52, 46));
        colorMapForCell.put(262144, Color.rgb(0, 0, 0));
    }

    private void initColorMapForText() {
        colorMapForText.put(0, 0x00000000);
        colorMapForText.put(2, Color.rgb(0, 0, 0));
        colorMapForText.put(4, Color.rgb(0, 0, 0));
        colorMapForText.put(8, Color.rgb(0, 0, 0));
        colorMapForText.put(16, Color.rgb(0, 0, 0));
        colorMapForText.put(32, Color.rgb(0, 0, 0));
        colorMapForText.put(64, Color.rgb(0, 0, 0));
        colorMapForText.put(128, Color.rgb(0, 0, 0));
        colorMapForText.put(256, Color.rgb(0, 0, 0));
        colorMapForText.put(512, Color.rgb(0, 0, 0));
        colorMapForText.put(1024, Color.rgb(0, 0, 0));
        colorMapForText.put(2048, Color.rgb(0, 0, 0));
        colorMapForText.put(4096, Color.rgb(255, 255, 255));
        colorMapForText.put(8192, Color.rgb(255, 255, 255));
        colorMapForText.put(16384, Color.rgb(255, 255, 255));
        colorMapForText.put(32768, Color.rgb(255, 255, 255));
        colorMapForText.put(65536, Color.rgb(255, 255, 255));
        colorMapForText.put(131072, Color.rgb(255, 255, 255));
        colorMapForText.put(262144, Color.rgb(255, 255, 255));
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
        paint.setTextSize(60);
        paint.setStyle(Paint.Style.FILL);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;
        canvas.drawRGB(210, 214, 219);
        paint.setColor(Color.rgb(147, 167, 175));
        canvas.drawRoundRect(backgroundRectF, 40, 40, paint);
        paint.setColor(Color.rgb(205, 214, 219));
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                canvas.drawRoundRect(backgroundCells[i][j].getRectF(), 20, 20, paint);
            }
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {

                paint.setColor(colorMapForCell.get(gameManager.getActingCells()[i][j].getValue()));
                canvas.drawRoundRect(gameManager.getActingCells()[i][j].getRectF(), 20, 20, paint);
                paint.setColor(colorMapForText.get(gameManager.getActingCells()[i][j].getValue()));
                canvas.drawText(gameManager.getActingCells()[i][j].getValue() + "", gameManager.getActingCells()[i][j].getX() + 90, gameManager.getActingCells()[i][j].getY() + 135, paint);

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
            invalidate();
        }

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