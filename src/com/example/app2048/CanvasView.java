package com.example.app2048;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;

/**
 * Created by ChornyiUA on 09.11.2015.
 */
public class CanvasView extends View {
    private static int width;
    private static int height;
    private GameManager gameManager;
    private Cell[][] cells=new Cell[4][4];
    private Paint paint;
    private RectF backgroundRectF= new RectF(30,300,30+1020,300+1020);


    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWidthAndHeight(context);
        gameManager=new GameManager(this, width, height);
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

        canvas.drawRGB(210,214,219);
        paint.setColor(Color.rgb(147,167,175));
        canvas.drawRoundRect(backgroundRectF,40,40,paint);
        paint.setColor(Color.rgb(205,214,219));
        for (int i = 0; i <4 ; i++) {
            for (int j = 0; j < 4; j++) {
                canvas.drawRoundRect(cells[i][j].getRectF(),20,20,paint);
            }
        }

        gameManager.onDraw(canvas);
    }

    @Override
    public void setOnTouchListener(OnTouchListener l) {
        super.setOnTouchListener(l);
        cells[0][0].setRectF(new RectF(200,200,430,430));
    }

    private void initField() {
        for (int i = 0; i <4 ; i++) {
            for (int j = 0; j < 4; j++) {
                cells[i][j]=new Cell(30+20+20*i+i*230,300+20+20*j+j*230);
            }
        }
    }
}
//,