package com.example.app2048;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import java.util.HashMap;
import java.util.Map;

public class CanvasView extends View {
    public static int BACKGROUND_START_X;
    public static int BACKGROUND_START_Y;
    public static int BACKGROUND_WIDTH;
    public static int PADDING;
    public static int CELL_WIDTH;
    private int atTouchStartX = 0;
    private int atTouchStartY = 0;
    private int atTouchFinishX = 0;
    private int atTouchFinishY = 0;
    private static int width;
    private static int height;
    private GameManager gameManager;
    private Cell[][] backgroundCells = new Cell[4][4];
    private Map<Integer, Integer> colorMapForCell = new HashMap<Integer, Integer>();
    private Map<Integer, Integer> colorMapForText = new HashMap<Integer, Integer>();
    private Context context;
    private ActingCell[][] viewCells  = new ActingCell[4][4];
    private Paint paint;
    private RectF backgroundRectF;

    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initWidthAndHeight(context);
        BACKGROUND_START_X = (int) (width * 0.025);
        BACKGROUND_START_Y = (int) (height * 0.28);
        PADDING = (int) (width * 0.02);
        BACKGROUND_WIDTH = (int) (width * 0.95);
        CELL_WIDTH = (int) (width * 0.2125);
        backgroundRectF = new RectF(BACKGROUND_START_X, BACKGROUND_START_Y, BACKGROUND_START_X + BACKGROUND_WIDTH, BACKGROUND_START_Y + BACKGROUND_WIDTH);
        gameManager = new GameManager(this,context);
        initPaint();
        initField();
        initColorMapForCells();
        initColorMapForText();
    }

    public void restart(){
        gameManager = new GameManager(this,getContext());
    }

    public ActingCell[][] getViewCells() {
        return viewCells;
    }

    public void setViewCells(ActingCell[][] viewCells) {
        this.viewCells = viewCells;
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
        colorMapForText.put(2, 0xFF5D544B);
        colorMapForText.put(4, 0xFF5D544B);
        colorMapForText.put(8, 0xFF5D544B);
        colorMapForText.put(16, 0xFF5D544B);
        colorMapForText.put(32, 0xFF5D544B);
        colorMapForText.put(64, 0xFF5D544B);
        colorMapForText.put(128, 0xFF5D544B);
        colorMapForText.put(256, 0xFF5D544B);
        colorMapForText.put(512, 0xFF5D544B);
        colorMapForText.put(1024, 0xFF5D544B);
        colorMapForText.put(2048, 0xFF5D544B);
        colorMapForText.put(4096, 0xFFFFFFFF);
        colorMapForText.put(8192, 0xFFFFFFFF);
        colorMapForText.put(16384, 0xFFFFFFFF);
        colorMapForText.put(32768, 0xFFFFFFFF);
        colorMapForText.put(65536, 0xFFFFFFFF);
        colorMapForText.put(131072, 0xFFFFFFFF);
        colorMapForText.put(262144, 0xFFFFFFFF);
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
        paint.setTextSize((int) (CELL_WIDTH * 0.3));
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRGB(250,248,239);
        paint.setColor(0xFF948B81);
        canvas.drawRoundRect(backgroundRectF, (int) (CELL_WIDTH * 0.244), (int) (CELL_WIDTH * 0.244), paint);
        paint.setColor(0xFFD6CDC4);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                canvas.drawRoundRect(backgroundCells[i][j].getRectF(), (int) (CELL_WIDTH * 0.122), (int) (CELL_WIDTH * 0.122), paint);
            }
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                paint.setColor(colorMapForCell.get(gameManager.getActingCells()[i][j].getValue()));
                canvas.drawRoundRect(gameManager.getActingCells()[i][j].getRectF(), (int) (CELL_WIDTH * 0.122), (int) (CELL_WIDTH * 0.122), paint);
                paint.setColor(colorMapForText.get(gameManager.getActingCells()[i][j].getValue()));
                canvas.drawText(gameManager.getActingCells()[i][j].getValue() + "", gameManager.getActingCells()[i][j].getX() + (int) (CELL_WIDTH * 0.5), gameManager.getActingCells()[i][j].getY() + (int) (CELL_WIDTH * 0.625), paint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            atTouchStartX = (int) event.getX();
            atTouchStartY = (int) event.getY();
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            atTouchFinishX = (int) event.getX();
            atTouchFinishY = (int) event.getY();
            gameManager.onTouchEvent(atTouchStartX, atTouchStartY, atTouchFinishX, atTouchFinishY);
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