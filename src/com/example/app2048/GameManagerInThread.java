package com.example.app2048;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceHolder;

public class GameManagerInThread extends Thread {
    private static final int FIELD_WIDTH = 300;
    private static final int FIELD_HEIGHT = 250;

    /** Область, на которой будем рисовать */
    private SurfaceHolder surfaceHolder;

    /** Состояние потока (выполняется или нет. Нужно, чтобы было удобнее прибивать поток, когда потребуется) */
    private boolean running;

    /** Стили рисования */
    private Paint paint;

    /** Прямоугольник игрового поля */
    private Rect field;

    /**
     * Конструктор
     * @param surfaceHolder Область рисования
     * @param context Контекст приложения
     */
    public GameManagerInThread(SurfaceHolder surfaceHolder, Context context)
    {
        this.surfaceHolder = surfaceHolder;
        running = false;

        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.STROKE);

        int left = 10;
        int top = 50;
        field = new Rect(left, top, left + FIELD_WIDTH, top + FIELD_HEIGHT);
    }

    /**
     * Задание состояния потока
     * @param running
     */
    public void setRunning(boolean running)
    {
        this.running = running;
    }

    @Override
    /** Действия, выполняемые в потоке */
    public void run()
    {
        while (running)
        {
            Canvas canvas = null;
            try
            {
                // подготовка Canvas-а
                canvas = surfaceHolder.lockCanvas();
                synchronized (surfaceHolder)
                {
                    // собственно рисование
                    canvas.drawRect(field, paint);
                }
            }
            catch (Exception e) { }
            finally
            {
                if (canvas != null)
                {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
}
