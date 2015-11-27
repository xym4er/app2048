package com.example.app2048;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.KeyEvent;
import android.view.SurfaceHolder;

public class GameManagerInThread extends Thread {
    private static final int FIELD_WIDTH = 800;
    private static final int FIELD_HEIGHT = 500;

    /** Область, на которой будем рисовать */
    private SurfaceHolder surfaceHolder;

    /** Состояние потока (выполняется или нет. Нужно, чтобы было удобнее прибивать поток, когда потребуется) */
    private boolean running;

    /** Стили рисования */
    private Paint paint;

    /** Прямоугольник игрового поля */
    private Rect field;

    /** Ресурсы приложения */
    private Resources res;

    /** Мячик */
    private Ball ball;

    /** Ракетка, управляемая игроком */
    private Racquet us;

    /** Ракетка, управляемая компьютером*/
    private Racquet them;

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

        field = new Rect();

            ball = new Ball(context.getDrawable(R.drawable.ball));
            us = new Racquet(context.getDrawable(R.drawable.us));
            them = new Racquet(context.getDrawable(R.drawable.them));

    }

    public void initPositions(int screenHeight, int screenWidth)
    {
        int left = (screenWidth - FIELD_WIDTH) / 2;
        int top = (screenHeight - FIELD_HEIGHT) / 2;

        field.set(left, top, left + FIELD_WIDTH, top + FIELD_HEIGHT);
        // мячик ставится в центр поля
        ball.setCenterX(field.centerX());
        ball.setCenterY(field.centerY());

        // ракетка игрока - снизу по центру
        us.setCenterX(field.centerX());
        us.setBottom(field.bottom);

        // ракетка компьютера - сверху по центру
        them.setCenterX(field.centerX());
        them.setTop(field.top);
    }

    /** Обновление объектов на экране */
    private void refreshCanvas(Canvas canvas)
    {
        canvas.drawRGB(255,255,255);
        // рисуем игровое поле
        canvas.drawRect(field, paint);

        // рисуем игровые объекты
        ball.draw(canvas);
        us.draw(canvas);
        them.draw(canvas);
    }

    /** Обновление состояния игровых объектов */
    private void updateObjects()
    {
        ball.update();
        us.update();
        them.update();

        // проверка столкновения мячика с вертикальными стенами
        if (ball.getLeft() <= field.left)
        {
            ball.setLeft(field.left + Math.abs(field.left - ball.getLeft()));
            ball.reflectVertical();
        }
        else if (ball.getRight() >= field.right)
        {
            ball.setRight(field.right - Math.abs(field.right - ball.getRight()));
            ball.reflectVertical();
        }

        // проверка столкновения мячика с горизонтальными стенами
        if (ball.getTop() <= field.top)
        {
            ball.setTop(field.top + Math.abs(field.top - ball.getTop()));
            ball.reflectHorizontal();
        }
        else if (ball.getBottom() >= field.bottom)
        {
            ball.setBottom(field.bottom - Math.abs(field.bottom - ball.getBottom()));
            ball.reflectHorizontal();
        }
    }

    /**
     * Задание состояния потока
     * @param running
     */
    public void setRunning(boolean running)
    {
        this.running = running;
    }

    /**
     * Обработка нажатия кнопки
     * @param keyCode Код нажатой кнопки
     * @return Было ли обработано нажатие
     */
    public boolean doKeyDown(int keyCode)
    {
        switch (keyCode)
        {
            case KeyEvent.KEYCODE_DPAD_LEFT:
                us.setDirection(GameObject.DIR_LEFT);
                return true;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                us.setDirection(GameObject.DIR_RIGHT);
                return true;
            default:
                return false;
        }
    }
    /**
     * Обработка отпускания кнопки
     * @param keyCode Код кнопки
     * @return Было ли обработано действие
     */
    public boolean doKeyUp(int keyCode)
    {
        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT ||
                keyCode == KeyEvent.KEYCODE_DPAD_RIGHT)
        {
            us.setDirection(GameObject.DIR_NONE);
            return true;
        }
        return false;
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
//                    canvas.drawRGB(255,255,255);
                    updateObjects(); // обновляем объекты
                    refreshCanvas(canvas); // обновляем экран
                    sleep(20);
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
